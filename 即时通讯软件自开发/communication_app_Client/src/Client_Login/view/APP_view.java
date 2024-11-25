package Client_Login.view;

import Client_Login.service.UserClientService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;



public class APP_view extends Application {


    private UserClientService userClientService =new UserClientService(); //用于登录服务器或者注册用户

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JAVA通信-Q我");
        primaryStage.getIcons().add(new Image("file:image/deer.png"));

        StackPane root = new StackPane();
        // 设置渐变背景色
        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.LIGHTCORAL),
                        new Stop(1, Color.DARKBLUE)),
                CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));
        //创建根布局,用于垂直排列子控件
        VBox content =new VBox();
        content.setPadding(new Insets(20));//设置内边距，在容器的上下左右设置20px的间距
        content.setSpacing(10);//设置子控件之间的垂直间距:20px
        content.setAlignment(Pos.CENTER);

        // 添加广告横幅（使用图片替代文字）
        Image bannerImage = new Image("file:D:image/background_1.png");
        ImageView bannerImageView = new ImageView(bannerImage);
        bannerImageView.setFitWidth(400); // 设置图片宽度
        bannerImageView.setPreserveRatio(true); // 保持图片比例
        root.getChildren().add(bannerImageView);

        // 设置标签和下拉框
        HBox accountBox = new HBox();
        accountBox.setSpacing(10);
        accountBox.setAlignment(Pos.CENTER);
        Label appLabel = new Label("账号");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("100", "200", "300"); // 设置常用账号
        comboBox.setEditable(true); // 允许用户手动输入
        accountBox.getChildren().addAll(appLabel, comboBox);

        // 设置密码标签和输入框
        HBox passwordBox = new HBox();
        passwordBox.setSpacing(10);
        passwordBox.setAlignment(Pos.CENTER);
        Label passwordLabel = new Label("密码");
        PasswordField passwordField = new PasswordField();
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // 定义按钮
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(20);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        Button loginButton = new Button("登录");
        Button cancelButton = new Button("取消");
        Button enrollButton = new Button("注册");
        //设置按钮样式，使用函数实现
        setButtonStyle(loginButton);
        setButtonStyle(cancelButton);
        setButtonStyle(enrollButton);
        buttonBox.getChildren().addAll(enrollButton,loginButton, cancelButton);


        // 点击登录按钮，先判断是否成功，成功进入菜单界面，失败则提示并返回
        loginButton.setOnAction(event -> {
            String userID = comboBox.getValue();
            String password = passwordField.getText();
            if(userClientService.checkUser(userID,password)){
                showUserMenu(primaryStage,userID);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,
                    "账号: " + userID + "\n密码: " + password, ButtonType.OK);
                alert.setHeaderText("登录失败，用户账号或密码错误！或用户未注册");
                alert.showAndWait();
            }
        });

        //点击注册按钮，给user添加标记，以*号为标记，传回服务端。若名字重复则提示并返回
        enrollButton.setOnAction(event -> {
            String userID='*'+comboBox.getValue();
            String password =passwordField.getText();
            if(userClientService.checkUser(userID,password)){
                String userId = userID.substring(1);
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "账号: " + userId + "\n密码: " + password, ButtonType.OK);
                alert.setHeaderText("注册成功！");
                alert.showAndWait();
            } else{
                String userId = userID.substring(1);
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "账号: " + userId + "\n密码: " + password, ButtonType.OK);
                alert.setHeaderText("注册失败，用户ID已被使用或者为空！");
                alert.showAndWait();
            }

        });

        cancelButton.setOnAction(event ->{
            primaryStage.close();  //按下取消按钮后退出当前页面
        } );


        // 设置布局
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(30);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(bannerImageView,accountBox,passwordBox, buttonBox);
        root.getChildren().addAll(mainLayout);

        // 设置场景
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showUserMenu(Stage primaryStage,String userID){
        VBox menuLayout=new VBox();
        menuLayout.setSpacing(20);
        menuLayout.setAlignment(Pos.CENTER);
        // 设置渐变背景色
        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.LIGHTCORAL),
                        new Stop(1, Color.DARKBLUE)),
                CornerRadii.EMPTY, Insets.EMPTY);
        menuLayout.setBackground(new Background(backgroundFill));

        //显示用户ID以查看是否登录正确
        Label label = new Label("欢迎用户："+userID+"进入操作菜单！");
        label.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");

        Button showOnlineUserButton = new Button("显示在线用户列表");
        Button MassMessageButton = new Button("群发消息");
        Button privateChatButton = new Button("私聊消息");
        Button sendFilesButton = new Button("发送文件");
        Button logoutButton = new Button("退出");
        setButtonStyle(showOnlineUserButton);
        setButtonStyle(MassMessageButton);
        setButtonStyle(privateChatButton);
        setButtonStyle(sendFilesButton);
        setButtonStyle(logoutButton);

        //点击按钮进入显示用户列表功能
        showOnlineUserButton.setOnAction(event->{
            userClientService.onlineFriendList();
        });
        //点击按钮进入私聊功能
        privateChatButton.setOnAction(event->{
            userClientService.getChatUserWindow();
        });
        //点击按钮进入群发消息功能
        MassMessageButton.setOnAction(event->{

        });
        //点击按钮进入发送文件功能
        sendFilesButton.setOnAction(event->{

        });
        //点击按钮进入退出功能，无异常退出系统
        logoutButton.setOnAction(event -> {
            //调用方法结束进程，无异常退出系统
            userClientService.logout();
            primaryStage.close();

        });
        //bug修复：点击右上角的关闭窗口键之后可以实现无异常退出，不报错
        primaryStage.setOnCloseRequest(event -> {
            userClientService.logout();
            primaryStage.close();
        });

        menuLayout.getChildren().addAll(label,showOnlineUserButton,privateChatButton,MassMessageButton,sendFilesButton,logoutButton);
        Scene scene = new Scene(menuLayout, 800, 600);
        primaryStage.setScene(scene);
    }

    private void setButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }

}

