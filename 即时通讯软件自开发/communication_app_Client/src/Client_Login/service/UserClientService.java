package Client_Login.service;

import app_common.Message;
import app_common.MessageType;
import app_common.User;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 该类完成用户登录验证和用户注册等功能,需要服务端的IP地址
 */
public class UserClientService {


    private ClientMessageService clientMessageService =new ClientMessageService();
    //在其它地方可能也需要用到这个user信息，故而在外面创建
    private User u =new User();
    //socket在多线程阶段需要被获取到，故而在外面创建
    private Socket socket;
    //根据userID和pwd到服务器端验证用户是否合法
    public boolean checkUser(String userId,String pwd)  {
        boolean b =false;
        //创建user对象
        u.setUserId(userId);
        u.setPasswd(pwd);

        //连接到服务端，发送user对象
        //服务端目前在本机，故而使用本机的IP地址，在9999端口监听
        try {
            socket= new Socket(InetAddress.getByName("10.252.160.22"),9999);
            //发送User对象,发送对象类使用objectOutputStream流
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            //读取从服务端回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms=(Message) ois.readObject();
            //得到Message对象后进行判断
            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)){
                //创建客户端的线程并启动之
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                //将客户端线程放入集合中进行管理以方便实现后期的群发功能
                ManageClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);

                b=true;
            }else{
                //表示登录失败，那么关闭线程就可以了
                socket.close();
            }
            if(ms.getMesType().equals(MessageType.MESSAGE_ENROLL_SUCCESS)){
                b = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return b;
    }

    //向服务端请求在线用户列表
    public void onlineFriendList(){
        //发送一个Message，类型MESSAFE_GET_ONLINE_FRIEND
        Message message=new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        //发送给服务器
        //因为是多线程，那么应该先得到当前线程的socket的ObjectOutputStream对象
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);//向服务端要求显示在线用户列表
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    //方法作用：退出客户端，并给客户端发送一个退出系统的message对象
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//一定要指定是哪个客户端ID
        //发送message
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+"退出系统");

            System.exit(0); //结束进程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getChatUserWindow(){
        Stage stage = new Stage();
        stage.setTitle("私聊....");
        stage.getIcons().add(new Image("file:image/deer.png"));
        //填加label提示
        Label label = new Label("请输入要私聊用户ID(在线)：");
        // 用户ID输入框
        TextField userIdField = new TextField();
        userIdField.setPromptText("请输入要私聊用户ID(在线):");
        // 确定按钮
        Button confirmButton = new Button("确定");
        setButtonStyle(confirmButton);
        // 登录界面的布局
        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        loginLayout.getChildren().addAll(label,userIdField, confirmButton);

        //点击确定按钮之后,获取文本框里的用户ID，编写成message对象，发送给客户端
        //验证通过则跳入聊天界面，失败则提示
        confirmButton.setOnAction(event ->{
            String userId = userIdField.getText().trim();
           clientMessageService.judgeSenderAndGetter(u.getUserId(),userId);
        } );


        Scene getIdScene = new Scene(loginLayout, 300, 200);
        stage.setScene(getIdScene);
        stage.show();

    }


    //设置按钮样式
    private void setButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }


}
