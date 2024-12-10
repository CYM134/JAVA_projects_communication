package Client_Login.service;

import app_common.Message;
import app_common.MessageType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类用于处理消息的服务功能，包括判断是否在线、消息的发送与接收过程窗口的创建与调用
 */
public class ClientMessageService {

    // 使用 HashMap 来管理每个用户的聊天窗口，键值为聊天双方的唯一标识，不然会出现弹出多个窗口的bug
    private ConcurrentHashMap<String, Stage> chatStageMap = new ConcurrentHashMap<>();
    private FileClientService fileClientService =new FileClientService();

    /**
     * 判断发送方和接收方，向服务端发送消息的验证
     * @param sendId  发送消息的用户 ID
     * @param getterId 接收消息的用户 ID
     */
    public void judgeSenderAndGetter(String sendId, String getterId) {
        // 构建消息对象，类型为判断用户通信
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES_JUDGE);
        message.setSender(sendId);
        message.setGetter(getterId);
        System.out.println(sendId + " 希望给 " + getterId + " 发送消息");

        try {// 通过用户的连接线程发送消息
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
        // 构建消息对象，类型为私聊消息
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES_SEND);
        message.setSender(sendId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        System.out.println(sendId + " 正在给 " + getterId + " 发送消息");

        try {// 通过用户的连接线程发送消息
//                System.out.println(sendId);
//                System.out.println(getterId);
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
        // 生成聊天窗口的唯一键值
        String chatKey = generateChatKey(message.getSender(), message.getGetter());
        Platform.runLater(() -> {
            Stage chatStage = chatStageMap.get(chatKey);
            if (chatStage != null) {
                // 如果窗口已存在，直接聚焦，不再重复生成，同时反复更新
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

            //添加一个发送文件的输入框和按钮
            TextField inputSrcText = new TextField();
            inputSrcText.setPromptText("输入发送文件的地址，如:D:\\xxx.jpg");
            Button sendFileButton = new Button("发送文件");
            setButtonStyle(sendFileButton);
            //使用IconImage为Button添加图标
            Image iconImage = new Image("file:image/文件夹.png");
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(24);
            iconView.setFitHeight(24);
            Button iconButton = new Button("浏览文件", iconView);

            // 发送按钮点击事件
            sendButton.setOnAction(e -> {
                String content = inputField.getText().trim();
                if (!content.isEmpty()) {
                    // 添加时间戳并显示消息
                    String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String messageDisplay = String.format("[%s] %s 对 %s 说: %s",
                            timestamp, message.getSender(), message.getGetter(), content);
                    chatArea.appendText(messageDisplay + "\n");
                    inputField.clear();// 清空输入框
                    sendMessageToOne(message.getSender(), message.getGetter(), content);
                }
            });
            // 按下 Enter 键时触发发送
            inputField.setOnAction(e -> sendButton.fire());

            //点击浏览文件按钮触发文件浏览事件
            //通过JavaFX中的 FileChooser 方法来实现浏览电脑文件资源管理器并选择文件的功能，并把地址显示在文本框中
            iconButton.setOnAction(event->{
                FileChooser fileChooser = new FileChooser();  //创建文本选择器
                fileChooser.setTitle("选择文件");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); //初始界面定为个人文件夹

                // 打开文件选择对话框并获取选中的文件
                File selectedFile = fileChooser.showOpenDialog(stage);
                // 如果选择了文件，则将文件路径设置到文本框中
                if (selectedFile != null) {
                    inputSrcText.setText(selectedFile.getAbsolutePath());
                }
            });
            //点击发送文件按钮触发发送文件事件
            sendFileButton.setOnAction(event->{
                String content = inputSrcText.getText().trim();
                if(!content.isEmpty()){
                    String text ="我给你发送了一个文件！";
                    // 添加时间戳并显示消息
                    String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String messageDisplay = String.format("[%s] %s 对 %s 说: %s",
                            timestamp, message.getSender(), message.getGetter(), text);
                    chatArea.appendText(messageDisplay + "\n");
                    inputSrcText.clear();// 清空输入框
                    sendMessageToOne(message.getSender(), message.getGetter(), text);
                    fileClientService.sendFileToOne(content,message.getSender(),message.getGetter());
                }
            });

            // 设置窗口布局
            HBox inputBox = new HBox(10, inputField, sendButton);
            HBox.setHgrow(inputField, Priority.ALWAYS);
            HBox sendFileBox = new HBox(10, inputSrcText,iconButton,sendFileButton);
            HBox.setHgrow(inputSrcText, Priority.ALWAYS);
            VBox root = new VBox(10, chatArea, inputBox,sendFileBox);
            root.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");


            Scene scene = new Scene(root, 500, 300);
            stage.setScene(scene);

            // 将聊天区域存储到 Stage 的 UserData 中
            stage.setUserData(chatArea);

            // 关闭窗口时移除HashMap中的窗口
            //BUG调试:修复了窗口只能显示一次的问题，也就是窗口不能正常退出HashMap的问题
            stage.setOnCloseRequest(e -> chatStageMap.remove(chatKey));

            // 将窗口加入HashMap
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
            //获取聊天区域
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
        // 聊天记录显示区域
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        // 输入框和发送按钮
        TextField inputField = new TextField();
        inputField.setPromptText("请输入消息...");
        Button sendButton = new Button("发送");
        setButtonStyle(sendButton);
        //添加一个发送文件的输入框和按钮
        TextField inputSrcText = new TextField();
        inputSrcText.setPromptText("输入发送文件的地址，如:D:\\xxx.jpg");
        Button sendFileButton = new Button("发送文件");
        setButtonStyle(sendFileButton);
        //使用IconImage为Button添加图标
        Image iconImage = new Image("file:image/文件夹.png");
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitWidth(24);
        iconView.setFitHeight(24);
        Button iconButton = new Button("浏览文件", iconView);

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

        //点击浏览文件按钮触发文件浏览事件
        //通过JavaFX中的 FileChooser 方法来实现浏览电脑文件资源管理器并选择文件的功能，并把地址显示在文本框中
        iconButton.setOnAction(event->{
            FileChooser fileChooser = new FileChooser();  //创建文本选择器
            fileChooser.setTitle("选择文件");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); //初始界面定为个人文件夹

            // 打开文件选择对话框并获取选中的文件
            File selectedFile = fileChooser.showOpenDialog(stage);
            // 如果选择了文件，则将文件路径设置到文本框中
            if (selectedFile != null) {
                inputSrcText.setText(selectedFile.getAbsolutePath());
            }
        });
        //点击发送文件按钮触发发送文件事件
        sendFileButton.setOnAction(event->{
            String content = inputSrcText.getText().trim();
            if(!content.isEmpty()){
                String text ="我给你发送了一个文件！";
                // 添加时间戳并显示消息
                String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                String messageDisplay = String.format("[%s] %s 对 %s 说: %s",
                        timestamp, message.getSender(), message.getGetter(), text);
                chatArea.appendText(messageDisplay + "\n");
                inputSrcText.clear();// 清空输入框
                sendMessageToOne(message.getGetter(),message.getSender(), text);
                fileClientService.sendFileToOne(content,message.getGetter(),message.getSender());
            }
        });

        // 设置窗口布局
        HBox inputBox = new HBox(10, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        HBox sendFileBox = new HBox(10, inputSrcText,iconButton,sendFileButton);
        HBox.setHgrow(inputSrcText, Priority.ALWAYS);
        VBox root = new VBox(10, chatArea, inputBox,sendFileBox);
        root.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");

        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.setUserData(chatArea);
        //BUG调试：修复了窗口只能显示一次的问题，也就是窗口不能正常退出HashMap的问题
        stage.setOnCloseRequest(e -> chatStageMap.remove(chatKey));
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
