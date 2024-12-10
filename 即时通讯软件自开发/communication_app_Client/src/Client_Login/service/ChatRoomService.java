package Client_Login.service;

import app_common.Message;
import app_common.MessageType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类用于实现聊天室群聊功能
 */
public class ChatRoomService {

    // 使用 HashMap 来管理每个用户的聊天窗口，键值为聊天双方的唯一标识，不然会出现弹出多个窗口的bug
    private ConcurrentHashMap<String, Stage> chatStageMap = new ConcurrentHashMap<>();

    //用于发送聊天室的消息
    private void sendMessageToRoom(String sender ,String RoomId,String content){
        //构建消息类型对象，类型为群聊消息
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_ROOM_MES);
        message.setRoomId(RoomId);
        message.setSender(sender);
        message.setContent(content);
        message.setSendTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        System.out.println(sender + " 正在聊天室： " + RoomId + " 中发送消息");
        //发送给服务端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void getChatRoomWindow(String userId){
        Stage stage = new Stage();
        stage.setTitle("获取聊天室....");
        stage.getIcons().add(new Image("file:image/deer.png"));
        //选项按钮
        Button createRoomButton = new Button("创建聊天室");
        Button joinRoomButton = new Button("加入聊天室");
        Button exitButton = new Button("返回");
        setButtonStyle(createRoomButton);
        setButtonStyle(joinRoomButton);
        setButtonStyle(exitButton);

        //点击创建聊天室，输入想要创建聊天室的ID
        createRoomButton.setOnAction(actionEvent -> {
            getCreateChatRoomID(stage,userId);
        });
        //点击加入聊天室，输入想要加入聊天室的ID
        joinRoomButton.setOnAction(actionEvent -> {
            getJoinChatRoomID(stage,userId);
        });
        //点击返回按钮，退出该界面
        exitButton.setOnAction(event->{
            stage.close();
        });

        // 登录界面的布局
        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        loginLayout.getChildren().addAll(createRoomButton,joinRoomButton, exitButton);

        Scene getRoomIdScene = new Scene(loginLayout, 300, 200);
        stage.setScene(getRoomIdScene);
        stage.show();
    }


    //获取用户想要创建聊天室的ID
    private void getCreateChatRoomID(Stage stage,String userId){
        stage.setTitle("创建聊天室ID....");
        //填加label提示
        Label label = new Label("请输入要创建的聊天室ID：");
        // 用户ID输入框
        TextField userIdField = new TextField();
        userIdField.setPromptText("请输入要创建的聊天室ID:");
        // 按钮
        Button confirmButton = new Button("确定");
        setButtonStyle(confirmButton);
        Button returnButton = new Button("取消");
        setButtonStyle(returnButton);

        //实现按钮功能
        confirmButton.setOnAction(actionEvent -> {
            //如果ID名字重复，那么就发出警告！不然进入聊天室界面
            String roomId = userIdField.getText().trim();
            // 构建消息对象，类型为判断聊天室ID
            Message message = new Message();
            message.setMesType(MessageType.CREATE_CHATROOM_ID);
            message.setSender(userId);
            message.setRoomId(roomId);
            System.out.println(userId + " 希望创建聊天室： " + roomId);
            try {// 通过用户的连接线程发送消息
                ObjectOutputStream oos = new ObjectOutputStream(
                        ManageClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream()
                );
                oos.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        returnButton.setOnAction(actionEvent -> {
            stage.close();
        });

        // 界面布局
        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        loginLayout.getChildren().addAll(label,userIdField, confirmButton,returnButton);
        Scene getIdScene = new Scene(loginLayout, 300, 200);
        stage.setScene(getIdScene);
        stage.show();
    }


    public void openChatRoomWindow(Message message){
        Platform.runLater(()->{
            // 创建聊天室窗口
            Stage stage = new Stage();
            stage.setTitle("用户"+message.getSender()+"正在聊天室 " + message.getRoomId() + " 中进行群聊...");
            stage.getIcons().add(new Image("file:image/deer.png"));
            // 聊天记录显示区域
            TextArea chatArea = new TextArea();
            chatArea.setEditable(false);
            chatArea.setWrapText(true);
            // 输入框和按钮
            TextField inputField = new TextField();
            inputField.setPromptText("请输入消息...");
            Button sendButton = new Button("发送");
            setButtonStyle(sendButton);
            // 按下 Enter 键时触发发送
            inputField.setOnAction(e -> sendButton.fire());

            sendButton.setOnAction(event->{
                String content = inputField.getText().trim();
                if(!content.isEmpty()){
                    String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String messageDisplay = String.format("[%s] %s 说: %s",
                            timestamp, message.getSender(), content);
                    chatArea.appendText(messageDisplay + "\n");
                    inputField.clear();
                    sendMessageToRoom(message.getSender(),message.getRoomId(),messageDisplay);
                }
            });

            // 设置窗口布局
            HBox inputBox = new HBox(10, inputField, sendButton);
            HBox.setHgrow(inputField, Priority.ALWAYS);
            VBox layout = new VBox(10, chatArea, inputBox); // 垂直排列聊天区域和输入区域
            layout.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");
            // 创建场景并显示
            Scene scene = new Scene(layout, 500, 300);
            stage.setScene(scene);
            // 将聊天区域存储到 Stage 的 UserData 中
            stage.setUserData(chatArea);
            // 关闭窗口时移除HashMap中的窗口
            //BUG调试:修复了窗口只能显示一次的问题，也就是窗口不能正常退出HashMap的问题
            stage.setOnCloseRequest(e -> chatStageMap.remove(message.getSender()));

            // 将窗口加入HashMap
            chatStageMap.put(message.getSender(), stage);
            stage.show();
        });
    }

    public void showMessageOnChatRoomWindow(Message message){
        Platform.runLater(() -> {
            Stage stage = chatStageMap.get(message.getSender());
            if (stage == null) {
                // 如果窗口不存在，创建一个新的窗口
               openChatRoomWindow(message);
                stage = chatStageMap.get(message.getSender());
            }
            //获取聊天区域
            TextArea chatArea = (TextArea) stage.getUserData();
            String messageDisplay = message.getContent();
            chatArea.appendText(messageDisplay + "\n");
        });
    }

    //获取用户想要加入的聊天室ID
    private  void getJoinChatRoomID(Stage stage,String userId){
        stage.setTitle("加入聊天室ID....");
        //填加label提示
        Label label = new Label("请输入要加入的聊天室ID：");
        // 用户ID输入框
        TextField userIdField = new TextField();
        userIdField.setPromptText("请输入要加入的聊天室ID:");
        // 按钮
        Button confirmButton = new Button("确定");
        setButtonStyle(confirmButton);
        Button returnButton = new Button("取消");
        setButtonStyle(returnButton);

        //实现按钮功能
        confirmButton.setOnAction(actionEvent -> {
            //如果ID名字不在已创建的聊天室集合中，则发出警告，否则进入。
            String roomId = userIdField.getText().trim();
            // 构建消息对象，类型为判断聊天室ID
            Message message = new Message();
            message.setMesType(MessageType.JOIN_CHATROOM_ID);
            message.setSender(userId);
            message.setRoomId(roomId);
            System.out.println(userId + " 希望加入聊天室： " + roomId);
            try {// 通过用户的连接线程发送消息
                ObjectOutputStream oos = new ObjectOutputStream(
                        ManageClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream()
                );
                oos.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        returnButton.setOnAction(actionEvent -> {
            stage.close();
        });

        // 界面布局
        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        loginLayout.getChildren().addAll(label,userIdField, confirmButton,returnButton);
        Scene getIdScene = new Scene(loginLayout, 300, 200);
        stage.setScene(getIdScene);
        stage.show();
    }

    //设置按钮样式
    private void setButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }
}
