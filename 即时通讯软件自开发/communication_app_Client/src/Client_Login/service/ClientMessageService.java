package Client_Login.service;

import app_common.Message;
import app_common.MessageType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * 该类用于处理消息的服务功能
 */
public class ClientMessageService {

    // 使用 HashMap 来管理每个用户的聊天窗口
    private ConcurrentHashMap<String, Stage> chatStageMap = new ConcurrentHashMap<>();

    /**
     * 判断发送方和接收方，向服务端发送消息的验证
     * @param sendId  发送消息的用户 ID
     * @param getterId 接收消息的用户 ID
     */
    public void judgeSenderAndGetter(String sendId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES_JUDGE);
        message.setSender(sendId);
        message.setGetter(getterId);
        System.out.println(sendId + " 希望给 " + getterId + " 发送消息");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread(sendId).getSocket().getOutputStream()
            );
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送私聊消息
     * @param sendId  发送者 ID
     * @param getterId 接收者 ID
     * @param content 消息内容
     */
    public void sendMessageToOne(String sendId, String getterId, String content) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES_SNED);
        message.setSender(sendId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        System.out.println(sendId + " 正在给 " + getterId + " 发送消息");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread(sendId).getSocket().getOutputStream()
            );
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 打开或聚焦用户私聊窗口
     * @param message 消息对象
     */
    public void openChatWindow(Message message) {
        String chatKey = generateChatKey(message.getSender(), message.getGetter());
        Platform.runLater(() -> {
            Stage chatStage = chatStageMap.get(chatKey);
            if (chatStage != null) {
                // 如果窗口已存在，直接聚焦
                chatStage.requestFocus();
                return;
            }

            // 创建新的私聊窗口
            Stage stage = new Stage();
            stage.setTitle("你正在和 " + message.getGetter() + " 聊天中...");
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

            // 按钮点击事件
            sendButton.setOnAction(e -> {
                String content = inputField.getText().trim();
                if (!content.isEmpty()) {
                    String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String messageDisplay = String.format("[%s] %s 对 %s 说: %s",
                            timestamp, message.getSender(), message.getGetter(), content);
                    chatArea.appendText(messageDisplay + "\n");
                    inputField.clear();
                    sendMessageToOne(message.getSender(), message.getGetter(), content);
                }
            });

            inputField.setOnAction(e -> sendButton.fire());

            // 窗口布局
            HBox inputBox = new HBox(10, inputField, sendButton);
            HBox.setHgrow(inputField, Priority.ALWAYS);
            VBox root = new VBox(10, chatArea, inputBox);
            root.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");

            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);

            // 将聊天区域存储到 Stage 的 UserData 中
            stage.setUserData(chatArea);

            // 关闭窗口时移除映射
            stage.setOnCloseRequest(e -> chatStageMap.remove(message.getGetter()));

            // 将窗口加入映射
            chatStageMap.put(chatKey, stage);
            stage.show();
        });
    }

    /**
     * 显示私聊消息
     * @param message 消息对象
     */
    public void showPrivateChat(Message message) {
        String chatKey = generateChatKey(message.getSender(), message.getGetter());
        Platform.runLater(() -> {
            Stage stage = chatStageMap.get(chatKey);
            if (stage == null) {
                // 如果窗口不存在，创建一个新的窗口
                stage = createPrivateChatStage(message);
            }
            TextArea chatArea = (TextArea) stage.getUserData();
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String messageDisplay = String.format("[%s] %s 对 %s 说: %s",
                    timestamp, message.getSender(), message.getGetter(), message.getContent());
            chatArea.appendText(messageDisplay + "\n");
        });
    }

    /**
     * 创建私聊窗口
     * @param message 消息对象
     * @return Stage 私聊窗口
     */
    private Stage createPrivateChatStage(Message message) {
        String chatKey = generateChatKey(message.getSender(), message.getGetter());
        Stage stage = new Stage();
        stage.setTitle("你正在和 " + message.getSender() + " 聊天中...");
        stage.getIcons().add(new Image("file:image/deer.png"));

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        TextField inputField = new TextField();
        inputField.setPromptText("请输入消息...");
        Button sendButton = new Button("发送");
        setButtonStyle(sendButton);

        sendButton.setOnAction(e -> {
            String content = inputField.getText().trim();
            if (!content.isEmpty()) {
                String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                String messageDisplay = String.format("[%s] %s 对 %s 说: %s",
                        timestamp, message.getGetter(), message.getSender(), content);
                chatArea.appendText(messageDisplay + "\n");
                inputField.clear();
                sendMessageToOne(message.getGetter(), message.getSender(), content);
            }
        });

        inputField.setOnAction(e -> sendButton.fire());

        HBox inputBox = new HBox(10, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        VBox root = new VBox(10, chatArea, inputBox);
        root.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setUserData(chatArea);

        stage.setOnCloseRequest(e -> chatStageMap.remove(message.getGetter()));
        chatStageMap.put(chatKey, stage);
        stage.show();
        return stage;
    }

    /**
     * 私聊功能存在问题，两个用户会出现三个窗口，应该是message的改变致使键发生改变导致
     * 现在修改键标识为："发送方-接收方" 的组合来作为键，这样可以唯一标识聊天窗口，从而修复bug
     */
    private String generateChatKey(String user1, String user2) {
        // 保证两个用户的键顺序一致（比如100-200和200-100为同一个键）,这样就可以避免100和200之间的互连会产生2个窗口的问题
        return user1.compareTo(user2) < 0 ? user1 + "-" + user2 : user2 + "-" + user1;
    }

    /**
     * 设置按钮样式
     * @param button 按钮对象
     */
    private void setButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }
}
