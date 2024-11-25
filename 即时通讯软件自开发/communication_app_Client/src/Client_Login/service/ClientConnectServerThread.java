package Client_Login.service;

import app_common.Message;
import app_common.MessageType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * 该类目的是让线程可以持有Socket，以保持服务端与客户端的长时间联通
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;
    private ListView<String> onlineUserListView; // 用于显示在线用户的 ListView
    public boolean running =true;
    //构建构造函数，让其可以接受一个Socket对象
    public ClientConnectServerThread(Socket socket){
        this.socket=socket;
        this.onlineUserListView = new ListView<>();
    }

    @Override
    public void run(){
        //保持Thread在后台和服务器长时间稳定通信，因此可以使用循环保持通信
        while(running){
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

                }else {
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
