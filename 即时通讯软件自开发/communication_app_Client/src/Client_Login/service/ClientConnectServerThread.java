package Client_Login.service;

import app_common.Message;
import app_common.MessageType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * 该类目的是让线程可以持有Socket，以保持服务端与客户端的长时间联通
 * 说白了就是，用于接收服务端传回的消息以实现不同按钮的功能
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;
    private ListView<String> onlineUserListView; // 用于显示在线用户的 ListView
    //用于消息聊天的clientMessageService对象
    private ClientMessageService clientMessageService =new ClientMessageService();
    //用于文件保存的FileClientService对象
    private FileClientService fileClientService =new FileClientService();
    //用于聊天室的chatRoomService对象
    private ChatRoomService chatRoomService=new ChatRoomService();
    //构建构造函数，让其可以接受一个Socket对象
    public ClientConnectServerThread(Socket socket){
        this.socket=socket;
        this.onlineUserListView = new ListView<>();
    }

    @Override
    public void run(){
        //保持Thread在后台和服务器长时间稳定通信，因此可以使用循环保持通信
        while(true){
            try {
                //设置若服务器没有发送message对象，线程会阻塞在这里
                System.out.println("客户端线程运行中，等待读取从服务端发送而来的信息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                //判断message类型，然后做相应的处理
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    //取出在线用户列表的信息，并显示
                    //规定以空格作为信息分割标志
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("======当前在线用户列表======");
                    for(int i = 0;i<onlineUsers.length;i++){
                        System.out.println("用户："+onlineUsers[i]);
                    }
                    createUI(); //创建获取在线用户列表窗口
                    //更新显示在线用户界面
                    //Platform.runLater()：这是一个用于在 JavaFX 应用的主线程中执行代码的方法
                    //因为JavaFX的 UI 更新必须在主线程中进行，runLater 使得我们能够在后台线程中（run()方法）安全地更新界面。
                    Platform.runLater(()->{
                        onlineUserListView.getItems().setAll(Arrays.asList(onlineUsers));
                    });

                }else if(message.getMesType().equals(MessageType.MESSAGE_USER_ONLINE_FAIL)){
                    // 将弹窗操作放到 Platform.runLater 中
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "用户: " + message.getGetter(), ButtonType.OK);
                        alert.setHeaderText("你输入的用户目前不在线！");
                        alert.showAndWait();
                    });
                } else if (message.getMesType().equals(MessageType.MESSAGE_USER_ONLINE_SUCCESS)) {
                    //用户上线，打开聊天窗口
                    clientMessageService.openChatWindow(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES_SEND)) {
                    //收到消息，显示在聊天窗口
                    clientMessageService.showPrivateChat(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    fileClientService.saveFileOnGetter(message);
                }else if(message.getMesType().equals(MessageType.CREATE_CHATROOM_ID)){
                    chatRoomService.openChatRoomWindow(message);
                } else if(message.getMesType().equals(MessageType.CREATE_CHATROOM_FAIL)){
                    //说明ID已经被使用了，报错提示 将弹窗操作放到 Platform.runLater 中
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "创建的聊天室ID: " + message.getRoomId(), ButtonType.OK);
                        alert.setHeaderText("你用于创建的聊天室ID已被使用，请重新输入！");
                        alert.showAndWait();
                    });
                } else if (message.getMesType().equals(MessageType.JOIN_CHATROOM_ID)) {
                    chatRoomService.openChatRoomWindow(message);
                } else if (message.getMesType().equals(MessageType.JOIN_CHATROOM_FAIL)) {
                    //说明聊天室ID没有创建不能加入，报错提示 将弹窗操作放到 Platform.runLater 中
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "加入的聊天室ID: " + message.getRoomId(), ButtonType.OK);
                        alert.setHeaderText("你想要的聊天室没有被创建，不能加入！");
                        alert.showAndWait();
                    });
                } else if (message.getMesType().equals(MessageType.MESSAGE_ROOM_MES)) {
                    chatRoomService.showMessageOnChatRoomWindow(message);
                } else {
                    System.out.println("是其它类型的message，暂时不处理");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
    //创建点击按钮显示在线用户列表界面
    private void createUI(){
        Platform.runLater(()->{
            Stage stage =new Stage();
            stage.setTitle("在线用户列表");
            stage.getIcons().add(new Image("file:image/deer.png"));

            Label label = new Label("在线用户有：");
            label.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");


            //VBox：一个垂直布局的容器，用来将控件（例如ListView）垂直排列
            VBox root = new VBox();
            root.getChildren().addAll(label, onlineUserListView);

            Scene scene = new Scene(root, 300, 400);
            scene.setFill(javafx.scene.paint.Color.DARKSLATEBLUE); // 设置背景颜色
            stage.setScene(scene);
            stage.show();

        });
    }


}
