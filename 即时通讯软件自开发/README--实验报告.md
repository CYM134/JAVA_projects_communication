# JAVA_projects_communication
## ——JAVA课设,开发通信软件-Q我

## 1.目的声明
### 主要要求功能
1.完成类似QQ或微信的即使通信功能，要求能单发消息、群发消息.

2.支持文本消息、文件的发送.

3.需要有界面。

4.支持多用户

## 2.项目概述

## 3.安装指南

**软件客户端：在`communication_app_Client`文件夹内**

**软件服务端：在`communication_app_Server`文件夹内**

安装JavaFX库(本机安装jdk21及以上)，JavaFX 是一个用于构建富客户端应用程序的 Java 库。
此处主要运用了用户界面构建相关的内容。注：需要在IDEA的编辑配置窗口的虚拟机配置中添加运行时选项，
将下载好的JavaFX的lib文件夹包输入，同时要将当前项目文档对JavaFX文件夹中的lib文件夹进行依赖，才可正常运行。

导文JAVAFX库的流程可以参考：

[]:https://blog.csdn.net/qq_33697094/article/details/126429278?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522e99c47ee6c9858a70c89c7811d87e905%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=e99c47ee6c9858a70c89c7811d87e905&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-126429278-null-null.142

**JAVAFX库安装完成之后，需要在客户端的`src -> Client_Login -> service -> UserClientService` 类中修改连接到的服务端的IP地址。将IP地址修改为服务端正在运行的终端计算机IP地址。如下：**

![](C:/Users/30548/Pictures/联想截图/联想截图_20241216092211.png)

客户端运行：`communication_app_Client`  ->  `src`  -> `Client_Login`  ->`view` ->  `APP_view`    :需配置好JAVAFX库

服务端运行：`communication_app_Server`  -> `src`  ->  `app_frame`  ->  `appFrame`

同时运行多个窗口功能：在应用程序**运行配置**中**选择修改选项  – > 勾选允许多个实例**即可。

![](C:/Users/30548/AppData/Roaming/Typora/typora-user-images/image-20241216103254448.png)

对导入的相关JavaFX库进行大概功能讲解:

| 导入类                                                | 主要功能                                                                      |
|----------------------------------------------------|---------------------------------------------------------------------------|
| Application                                        | 是 JavaFX 应用程序的基类，所有 JavaFX 应用都需要继承自这个类来启动应用并定义应用的生命周期和基本行为                |
| Geometry 相关类（Insets、Pos）                           | 用于设置界面元素的间距和布局位置等属性。例如，Insets 可用于设置容器四周的内边距，Pos 用于指定节点在布局容器中的对齐方式         |
| Scene、Stage                                        | 用于容纳和组织图形界面元素的场景对象，代表了一个可视化的场景；Stage 则是 JavaFX 应用程序的顶级窗口容器，类似于传统桌面应用中的主窗口 |
| Control 相关类（Button、Label、TextField、PasswordField等） | 常用的用户界面控件类，用于创建按钮、标签、文本输入框、密码输入框等各种交互元素                                   |
| ImageView 和 Image                                  | 用于在界面中显示图片。Image 用于加载图片资源，ImageView 则是将加载的图片展示在场景中的视图对象                   |
| Color、LinearGradient、Stop、CycleMethod              | 用于设置界面元素的颜色以及创建渐变效果，比如设置背景颜色为渐变效果等                                        |

构建app_common库，用于作为客户端和服务端直接消息传输的`Object`类型：

| 类名 | 主要功能                         |
|-----|------------------------------|
|   Message  | 封装各种类型的消息，主要用于在客户端和服务端之间传递消息 |
|   MessageType  | 用于定义消息的类型，用于规范消息的分类，使得服务端和客户端能够根据统一的消息类型标准来进行通信和处理                   |
|   User  | 一个数据载体类，用于表示用户相关的信息          |

还有其他散碎的导入包 (` JDK `自带的) ：

| 导入包 | 主要功能 |
|-----|------|
| java.io包    |   提供了用于进行输入 / 输出操作的类和接口，可用于处理文件、流以及其他与数据输入输出相关的任务   |
|   java.net包  |   提供了用于网络编程的类和接口，可实现网络通信相关的各种功能，如创建服务器、客户端，进行网络数据传输等   |
|   java.util.HashMap包  |   提供了HashMap类，它是一种常用的键值对集合实现，用于存储和检索具有唯一键的元素   |
|   java.util.concurrent.ConcurrentHashMap包  |  提供了ConcurrentHashMap类，它是一种支持多线程并发访问的键值对集合实现，在多线程环境下能够安全地进行插入、删除和检索操作    |

## 4.操作流程（运行效果）
登入界面：

![](../程序运行截图/1、初始登录界面.png)

在服务端会展现合法的用户的数据：

![](../程序运行截图/2、服务端显示合法用户数据.png)

登录账户失败则会进行提示：

![](../程序运行截图/3、登录失败提示.png)

用户注册，若用户名已被使用则会弹出注册失败的提示：

![](../程序运行截图/4、注册失败提示.png)

成功登入后展现二级操作菜单：

![](../程序运行截图/5、二级操作菜单.png)

多个用户同时在线：

![](../程序运行截图/6、多用户在线.png)

显示在线用户：

![](../程序运行截图/7、显示在线用户列表.png)

私聊其他用户（含发送文件功能）：

首先系统会弹出窗口询问需要私聊的对象(没有使用数据库，所以只能和在线用户私聊)：

![](../程序运行截图/8、询问私聊对象窗口.png)

输出错误则会弹出错误窗口提示错误原因：

![](../程序运行截图/9、私聊输入错误弹出提示.png)

验证通过后即可进入私聊界面：

![](../程序运行截图/10、私聊界面.png)

用户发送消息及回复：

![](../程序运行截图/11、私聊.png)

点击文件发送按钮，可以进行选择：

![](../程序运行截图/12、发送文件.png)

收到文件的用户则会弹出提示窗口：

![](../程序运行截图/13、收到文件弹出保存提示.png)

用户点击确定可以选择保存的地址：

![](../程序运行截图/14、用户选择保存的地方.png)

可以看见文件已经被正确保存：

![](../程序运行截图/15、文件正确保存到相应地方.png)

**使用聊天室按钮创建类似群聊的功能**：

创建聊天室及加入：

![](../程序运行截图/16、获取聊天室消息窗口.png)

创建聊天室的窗口：

![](../程序运行截图/17、创建聊天室窗口.png)

若创建的聊天室ID重复则会弹出窗口警告：

![](../程序运行截图/18、创建聊天室ID重复报错提示.png)

加入聊天室窗口：

![](../程序运行截图/19、加入聊天室窗口.png)

若加入的聊天室ID不存在则弹出错误提示：

![](../程序运行截图/20、加入聊天室ID不存在报错窗口.png)

进入聊天室后可以多人同时在线聊天：

![](../程序运行截图/21、聊天室窗口.png)

服务端则会在后台展现各个操作的相应提示：

![](../程序运行截图/22、服务端会在相应窗口发出操作提示.png)

## 5.功能详细介绍
### 服务端

创建服务端套接字（ServerSocket）对象并绑定到端口 9999，同时打印出服务端正在监听的信息。

### 1）用户登录
#### 1.定义了一个服务端套接字（ServerSocket）对象，用于在指定端口（9999）监听客户端的连接请求。

    private ServerSocket ss=null;
#### 2.处理客户端的登录和注册操作

读取客户端发送的 User 类对象，根据该对象的用户 ID 的第一个字符判断客户端是执行登录操作还是注册操作：

（1）如果用户 ID 的第一个字符是 *，表示客户端要进行注册操作。调用 checkEnroll 方法验证注册信息
    
    private boolean checkEnroll(String userID,String passwd){
        String userId=userID.substring(1);
        User user =validUsers.get(userId);
        //若userID不在validUsers中,则可以注册
        if(user ==null&&!userId.equals("")){
            validUsers.put(userId,new User(passwd,userId));
            saveUsersToFile(); // 注册成功后保存到文件
            return true;
        }else { //说明用户ID已经存在
            return false;
        }
    }

（2）如果用户 ID 的第一个字符不是 *，表示客户端要进行登录操作。调用 checkUser 方法验证登录信息，

    private boolean checkUser(String userID,String passwd){
        User user =validUsers.get(userID);
        //若userID不在validUsers中，则为空
        if(user==null){
            System.out.println("user为空！");
            return false;
        }
        //若userID存在，但是密码错误
        if(!user.getPasswd().equals(passwd)){
            System.out.println("password错误！"+passwd);
            return false;
        }
        return true;
    }
#### 3.与客户端进行通信以及管理
如果注册成功：
创建一个 Message 类对象并设置其消息类型为 MessageType.MESSAGE_ENROLL_SUCCESS，然后通过对象输出流发送给客户端；如果注册失败，设置消息类型为 MessageType.MESSAGE_ENROLL_FAIL 并发送给客户端。

    if(checkEnroll(u.getUserId(), u.getPasswd())){//注册成功
                        message.setMesType(MessageType.MESSAGE_ENROLL_SUCCESS);
                        oos.writeObject(message);
                    }else{
                        message.setMesType(MessageType.MESSAGE_ENROLL_FAIL);
                        oos.writeObject(message);
                    }
如果登录成功：
设置 Message 类对象的消息类型为 MessageType.MESSAGE_LOGIN_SUCCESS，并发送给客户端。

    if (checkUser(u.getUserId(), u.getPasswd())) { //登录成功
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS); //设置Message对象的值
                        //将message对象回复给客户端
                        oos.writeObject(message);
                      
                        //将线程对象放入集合中进行管理
                        ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);
如果登录失败：
抛出登录失败的提示，再设置消息类型为 MessageType.MESSAGE_LOGIN_FAIL，发送给客户端后关闭套接字连接。

    System.out.println("账号 ID = " + u.getUserId() + "   password = " + u.getPasswd() + " 验证失败！");
                        message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                        oos.writeObject(message);
                        socket.close();
#### 4.客户端连接的线程
在登录成功后，需要创建一个 ServerConnectClientThread 类的线程（这里看不到其具体实现，但推测是用于与客户端保持通信的线程），启动该线程，并将该线程添加到 ManageClientThreads（同样不清楚其具体实现，可能是用于管理与客户端连接线程的集合）中
    
    //创建一个线程和客户端保持通信，该线程需要持有socket对象
                        ServerConnectClientThread serverConnectClientThread =
                                new ServerConnectClientThread(socket, u.getUserId());
                        //启动该线程
                        serverConnectClientThread.start();
### 5.用户信息的存储和读取

将hashmap里面的数据保存到json文件中，这样以后每次启动服务端都可以从中得到以前的所有合法数据，保证每次关闭服务端后数据不会丢失。

    private static void saveUsersToFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(validUsers);
            System.out.println("用户数据已保存到文件！");
        } catch (IOException e) {
            System.err.println("保存用户数据时出错：" + e.getMessage());
        }
    }
使用了try-with-resources语句来确保资源（这里是ObjectOutputStream和与之关联的FileOutputStream）能够被正确地打开和关闭，即使在出现异常的情况下也能保证资源释放。

（1）首先创建了一个ObjectOutputStream，它包装了一个指向指定文件（由常量USER_DATA_FILE指定）的FileOutputStream。

（2）然后通过调用oos.writeObject(validUsers)将validUsers对象写入到输出流中，这里假设validUsers是一个可序列化的对象（比如实现了Serializable接口的集合等，从加载方法推测可能是ConcurrentHashMap<String, User>类型）。

    private static void loadUsersFromFile(){
        File file = new File(USER_DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                validUsers = (ConcurrentHashMap<String, User>) ois.readObject();
                System.out.println("用户数据已从文件加载！");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("加载用户数据时出错：" + e.getMessage());
            }
        } else {
            System.out.println("用户数据文件不存在，初始化空的用户数据！");
        }
    }
同样使用try-with-resources语句创建一个ObjectInputStream，它包装了指向该文件的FileInputStream。通过ois.readObject()从输入流中读取对象，并将其强制转换为ConcurrentHashMap<String, User>类型赋值给validUsers。这里要求User类以及ConcurrentHashMap中存储的键值对相关的类都必须实现Serializable接口，否则会抛出ClassNotFoundException。
### 客户端

定义了一个UserClientService类型的私有最终成员变量userClientService用于处理登录服务器或者注册用户的相关业务逻辑

    private final UserClientService userClientService = new UserClientService(); //用于登录服务器或者注册用户
再根据avaFX 应用程序的launch方法作为JavaFX 应用程序的标准入口点它会初始化 JavaFX 运行时环境，并根据提供的参数启动应用程序

    public static void main(String[] args) {
    launch(args);
    }
这里直接将传入的args参数传递给launch方法来启动应用程序。

### 1）图形用户界面的实现
#### （1）设置舞台（Stage）相关属性：
首先设置了主舞台（primaryStage）的标题为 "JAVA通信-Q我"，这将显示在应用程序窗口的标题栏上，并在左上角添加一个图标，图标的路径为"file:image/deer.png"。
    
    primaryStage.setTitle("JAVA通信-Q我");
    primaryStage.getIcons().add(new Image("file:image/deer.png"));

#### （2）设置根布局（StackPane）及背景色：
创建了一个StackPane作为根布局容器，接着为根布局设置了渐变背景色，通过创建一个BackgroundFill对象，其中包含一个LinearGradient对象来定义从Color.LIGHTCORAL到Color.DARKBLUE的渐变效果，再将这个BackgroundFill对象设置为根布局的背景。

    StackPane root = new StackPane();
    // 设置渐变背景色
    BackgroundFill backgroundFill = new BackgroundFill(
    new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
    new Stop(0, Color.LIGHTCORAL),
    new Stop(1, Color.DARKBLUE)),
    CornerRadii.EMPTY, Insets.EMPTY);
    root.setBackground(new Background(backgroundFill));

#### （3）创建垂直布局（VBox）并设置属性：
创建了一个VBox布局容器，用于在其中垂直排列其他子控件，并设置其内边距，使得按钮之间各有20px的间距，还设置了按钮之间的垂直间距为10px，且设置为居中对齐。

    //创建根布局,用于垂直排列子控件
    VBox content = new VBox();
    content.setPadding(new Insets(20));//设置内边距，在容器的上下左右设置20px的的间距
    content.setSpacing(10);//设置子控件之间的垂直间距:20px
    content.setAlignment(Pos.CENTER);

#### （4）添加广告横幅：
创建了一个Image对象来加载广告横幅图片，其路径指定为 "file:D:image/background_1.png"

    // 添加广告横幅（使用图片替代文字）
    Image bannerImage = new Image("file:D:image/background_1.png");
    ImageView bannerImageView = new ImageView(bannerImage);
    bannerImageView.setFitWidth(400); // 设置图片宽度
    bannerImageView.setPreserveRatio(true); // 保持图片比例
    root.getChildren().add(bannerImageView);
#### （5）设置账号相关的标签和下拉框：
创建了一个HBox布局容器用于水平排列账号相关的组件，为HBox设置了间距为10px，并且将其内部组件的对齐方式设置为居中对齐。

并且创建了一个Label标注为 "账号"，以及一个ComboBox下拉框，在下拉框中添加了"100"、"200"、"300"这几个常用账号选项，并且设置该下拉框允许用户手动输入其他账号。最后将标签和下拉框添加到HBox容器中

    // 设置标签和下拉框
    HBox accountBox = new HBox();
    accountBox.setSpacing(10);
    accountBox.setAlignment(Pos.CENTER);
    Label appLabel = new Label("账号");
    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.getItems().addAll("100", "200", "300"); // 设置常用账号
    comboBox.setEditable(true); // 允许用户手动输入
    accountBox.getChildren().addAll(appLabel, comboBox);
#### （6）设置密码相关的标签和输入框：
同样是创建一个HBox布局容器用于水平排列密码相关的组件，再创建一个label为“密码”的标注和一个用于输入密码的PasswordField。

    // 设置密码标签和输入框
    HBox passwordBox = new HBox();
    passwordBox.setSpacing(10);
    passwordBox.setAlignment(Pos.CENTER);
    Label passwordLabel = new Label("密码");
    PasswordField passwordField = new PasswordField();
    passwordBox.getChildren().addAll(passwordLabel, passwordField);
#### （7）设置按钮相关操作：
创建了一个HBox布局容器，后续定义三个按钮：“登录”、“取消”、“注册”，通过调用setButtonStyle函数来为每个按钮设置样式，最后将这三个按钮添加到HBox容器中。

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

如果登录成功后进入下一级图像用户界面（见下一的用户登录功能），定义了“显示在线用户列表”、“群发消息”、“私聊消息”、“发送文件”、“退出”五个按钮

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

#### （8）创建展示在线用户列表界面的方法：
创建一个新的 JavaFX 舞台（Stage）和场景（Scene）来展示在线用户列表，通过Platform.runLater()确保在 JavaFX 应用程序的主线程中进行界面相关的操作。

建一个新的舞台，设置其标题为 “在线用户列表”，并添加一个图标（通过指定的图片路径file:image/deer.png，这里路径可能需要根据实际情况调整）。然后创建一个标签用于标注在线用户列表，设置其字体大小和文本颜色。接着创建一个VBox布局容器，将标签和onlineUserListView添加到其中。再创建一个场景，设置其大小为300x400像素，并将背景颜色设置为Color.DARKSLATEBLUE。最后将场景设置到舞台上并显示舞台，从而展示出在线用户列表的界面。

    private void createUI() {
    Platform.runLater(() -> {
        Stage stage = new Stage();
        stage.setTitle("在线用户列表");
        stage.getIcons().add(new Image("file:image/deer.png"));
        Label label = new Label("在线用户有：");
        label.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");
        VBox root = new VBox();
        root.getChildren().addAll(new Node[]{label, this.onlineUserListView});
        Scene scene = new Scene(root, (double)300.0F, (double)400.0F);
        scene.setFill(Color.DARKSLATEBLUE);
        stage.setScene(scene);
        stage.show();
    });
    }
#### （9）创建聊天窗口：

创建一个新的 JavaFX Stage，它代表一个独立的窗口，将标题设置为 “你正在和 [接收者 ID] 聊天中...”，同时通过 stage.getIcons().add(new Image("file:image/deer.png")) 添加一个图标到窗口上，这里通过指定的文件路径 file:image/deer.png 来加载图标

    Stage stage = new Stage();
    stage.setTitle("你正在和 " + message.getGetter() + " 聊天中...");或者为（stage.setTitle("你正在和 " + message.getSender() + " 聊天中...")，根据发送者和接收者决定）
    stage.getIcons().add(new Image("file:image/deer.png"));

**紧接着创建聊天区域和输入区域的组件：**
* 创建一个 TextArea 组件用于显示聊天记录，设置其为不可编辑（setEditable(false)），这样用户就不能修改聊天记录，并且设置自动换行（setWrapText（true））。

      TextArea chatArea = new TextArea();
      chatArea.setEditable(false);
      chatArea.setWrapText(true);

* 再创建 TextField（消息输入框）：

      TextField inputField = new TextField();
      inputField.setPromptText("请输入消息...");

* 然后创建按钮“发送”并对其进行一定的样式设计：

      Button sendButton = new Button("发送");
      this.setButtonStyle(sendButton);

* 紧接着还创建了文件路径输入框：

      TextField inputSrcText = new TextField();
      inputSrcText.setPromptText("输入发送文件的地址，如:D:\\xxx.jpg");

* 发送文件按钮及样式设置：

      Button sendFileButton = new Button("发送文件");
      this.setButtonStyle(sendFileButton);

* 浏览文件按钮及图标设置：
  首先创建一个 Image 对象，从指定路径加载图像，作为浏览文件按钮的图表显示，创建ImageView用来展示Image，并设置图标视图的高度和宽度为固定的24像素，
  最后创建带有图标的浏览文件按钮，按钮上除了显示文字 “浏览文件” 外，还会展示之前设置好的文件夹图标，方便用户直观地识别并点击该按钮来选择要发送的文件。

      Image iconImage = new Image("file:image/文件夹.png");
      ImageView iconView = new ImageView(iconImage);
      iconView.setFitWidth((double)24.0F);
      iconView.setFitHeight((double)24.0F);
      Button iconButton = new Button("浏览文件", iconView);

* 然后同时对布局容器进行一个设计：

      HBox inputBox = new HBox((double)10.0F, new Node[]{inputField, sendButton});
      HBox.setHgrow(inputField, Priority.ALWAYS);
      HBox sendFileBox = new HBox((double)10.0F, new Node[]{inputSrcText, iconButton, sendFileButton}); 
      HBox.setHgrow(inputSrcText, Priority.ALWAYS);
      VBox root = new VBox((double)10.0F, new Node[]{chatArea, inputBox, sendFileBox});
      root.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");

* 最后创建场景（Scene）并关联到 Stage 展示窗口：
  
      Scene scene = new Scene(root, (double)500.0F, (double)300.0F);
      stage.setScene(scene);
      stage.setUserData(chatArea);
      stage.setOnCloseRequest((e) -> this.chatStageMap.remove(chatKey));
      this.chatStageMap.put(chatKey, stage);
      stage.show();
#### （10）创建聊天室窗口：
设置标题，添加小图标

      stage.setTitle("获取聊天室....")
      stage.getIcons().add(new Image("file:image/deer.png"))

创建了三个按钮，分别是用于创建聊天室的 "创建聊天室" 按钮、加入聊天室的 "加入聊天室" 按钮以及返回的 "返回" 按钮

      Button createRoomButton = new Button("创建聊天室");
      Button joinRoomButton = new Button("加入聊天室");
      Button exitButton = new Button("返回");

再是相似的为其垂直布局容器进行设置，再关联场景到窗口并展示：
stage.setScene(getRoomIdScene); 将创建好的 Scene 关联到之前创建的 Stage 窗口上，使得窗口能够展示出这个场景所包含的界面内容

### 2）用户登录
根据上述定义好的图像用户界面
#### （1）点击登录：
首先通过comboBox.getValue()获取用户在下拉框中选择或手动输入的账号（userID），并通过passwordField.getText()获取用户在密码输入框中输入的密码（password）
再对用户输入的账号和密码进行验证是否有效（checkUser方法，在服务端定义好的）

如果验证通过（checkUser返回true），则调用showUserMenu进入用户菜单界面，否则创建一个Akert对话框，在上面显示用户输入的账号和密码信息和“登录失败，用户账户或密码错误！或用户未注册”的字样

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
#### （2）点击注册：
首先构造一个带有特殊标记（以*号开头）的账号字符串，通过'*' + comboBox.getValue()获取，同时通过passwordField.getText()获取用户输入的密码（password）。
接着同样调用checkUser方法进行验证

如果验证通过，先通过userID.substring(1)去掉账号字符串开头的*号，得到实际的用户账号，然后创建一个Alert对话框，在对话框的内容文本中显示账号和密码信息，并在对话框的头部设置文本为 “注册成功！”。
否则，也是先通过uesrID。substring（1）得到实际账户，然后创建一个Alert对话框，并在对话框的头部设置文本为 “注册失败，用户 ID 已被使用或者为空！”。

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
#### （3）点击取消：
当用户点击取消按钮时，直接调用primaryStage.close()方法关闭当前的主舞台，也就是退出当前的应用程序页面。

     cancelButton.setOnAction(event ->{
            primaryStage.close();  //按下取消按钮后退出当前页面
        } );
### 3）在线人数获取
#### （1）先建立客户端与服务端之间的连接：
socket：用于保存与服务器端建立连接的套接字对象，通过这个套接字可以进行客户端与服务器之间的输入输出流操作，从而实现数据的传输

onlineUserListView：一个ListView对象，用于在界面上展示在线用户的列表信息，它将在接收到服务器发送的在线用户列表消息时被更新并显示出来。

clientMessageService：用于处理客户端消息相关的服务。

    private Socket socket;
    private ListView<String> onlineUserListView;
    private ClientMessageService clientMessageService = new ClientMessageService();
先确保服务端正在运行，再在线程的执行体中，通过一个无限循环不断地尝试读取服务器发送过来的消息。首先创建一个ObjectInputStream对象，它从与服务器连接的套接字的输入流（this.socket.getInputStream()）中读取数据，以便能够反序列化接收到的对象。然后通过ois.readObject()读取一个对象，并将其强制转换为Message类型

    public void run() {
    while (true) {
        try {
            System.out.println("客户端线程运行中，等待读取从服务端发送而来的信息");
            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            Message message = (Message) ois.readObject();
#### （2）接收服务端发来的Messages类型并进行打印显示在线人数：
**当接收到的消息类型（通过message.getMesType()获取）为"5"时：**

表示收到了服务器发送的在线用户列表信息。首先将消息内容（通过message.getContent()获取）按照空格进行分割，得到一个包含所有在线用户的字符串数组onlineUsers。
然后在控制台打印出当前在线用户列表的标题以及每个用户的信息。接着调用createUI方法创建一个用于展示在线用户列表的界面，最后通过Platform.runLater()在 JavaFX 应用程序的主线程中更新onlineUserListView的内容，将在线用户列表设置为刚刚获取到的数组中的所有元素。

     if (message.getMesType().equals("5")) {
                    String[] onlineUsers = message.getContent().split(" ");//按照空格进行分割
                    System.out.println("======当前在线用户列表======");
    
                    for(int i = 0; i < onlineUsers.length; ++i) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }//在控制台打印数据
    
                    this.createUI();
                    Platform.runLater(() -> this.onlineUserListView.getItems().setAll(Arrays.asList(onlineUsers)));
                    //调用JavaFx应用程序将数据传到在线用户列表中
                }
**当接收到的消息类型（通过message.getMesType()获取）为"333"时：**

通过Platform.runLater()在主线程中创建一个Alert对话框，并在对话框内容中显示输入的用户信息，在对话框的头部设置文本“你输入的用户目前不在线！”。

    else if (message.getMesType().equals("333")) {
    Platform.runLater(() -> {
        Alert alert = new Alert(AlertType.ERROR, "用户: " + message.getGetter(), new ButtonType[]{ButtonType.OK});
        alert.setHeaderText("你输入的用户目前不在线！");
        alert.showAndWait();
    });

**当接收到的消息类型（通过message.getMesType()获取）为"444"时：**

调用clientMessageService对象的openChatWindow方法，并将接收到的消息作为参数传递进去，推测是用于打开一个聊天窗口。

    else if (message.getMesType().equals("444")) {
    this.clientMessageService.openChatWindow(message);

**当接收到的消息类型（通过message.getMesType()获取）为"31"时：**

调用clientMessageService对象的showPrivateChat方法，并将接收到的消息作为参数传递进去，显示存在的私聊信息。

    else if (message.getMesType().equals("31")) {
    this.clientMessageService.showPrivateChat(message);

**当接收到的消息类型（通过message.getMesType()获取）为其他类型时：**

打印“暂时不处理该类message”

    else {
    System.out.println("是其它类型的message，暂时不处理");

### 4） 群发消息
* 定义了一个 ConcurrentHashMap

  用于确定聊天对象和群对象的位置。
  采用了键值对的方法将用户ID和聊天窗口Stage绑定在一起。
* 创建Message对象（MessageType为“10”）

  接收三个参数，分别为sender（消息的发送者），RoomID（聊天室的ID，即群的位置），content（要发送的文本内容）

  然后然后通过与服务器连接的套接字输出流将消息发送出去，将以getClientConnectServerThread 方法获取与发送者对应的客户端连接线程对象，
  再从该线程关联的 Socket（套接字，用于网络通信）对象中获取输出流，最后基于这个输出流创建了能进行对象序列化输出的 ObjectOutputStream，目的是为了将 Message 对象通过网络发送出去。不成功将抛出异常

      Message message = new Message();
      message.setMesType("10");
      message.setRoomId(RoomId);
      message.setSender(sender);
      message.setContent(content);
      message.setSendTime((new SimpleDateFormat("HH:mm:ss")).format(new Date()));
      System.out.println(sender + " 正在聊天室： " + RoomId + " 中发送消息");
      
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
* 对聊天室的一些细节设置

  * 创建提示标签：
    创建了一个 Label 组件，用于在界面上向用户显示提示文本，告知用户在此处需要输入想要创建的聊天室 ID，起到引导用户操作的作用。

        Label label = new Label("请输入要创建的聊天室ID：");
  * 创建文本输入框：
    创建了一个文本输入框，供用户输入具体的聊天室 ID 内容。并且设置了输入框的提示文本，当输入框为空时，会显示这个提示内容，进一步提示用户操作，提升用户体验。

        TextField userIdField = new TextField();        
        userIdField.setPromptText("请输入要创建的聊天室ID:");
  * “确认”按钮：
    首先获取用户在文本输入框中输入的内容，并去除前后空白字符，再通过userIdField.getText().trim()获取到聊天室ID
    再创建Message对象（MessageType为“8”），用于存储消息，然后根据前者的RoomID的信息，然后试图创建聊天室，并会打印出提示信息，告知用户当前正在进行创建特定聊天室的操作，便于调试和用户了解操作情况。
  
          String roomId = userIdField.getText().trim();
            Message message = new Message();
            message.setMesType("8");
            message.setSender(userId);
            message.setRoomId(roomId);
            System.out.println(userId + " 希望创建聊天室： " + roomId);
    最后进行消息发送操作，通过网络将创建聊天室的消息发送出去
        
        try {
        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream());
        oos.writeObject(message);
        } catch (IOException e) {
        throw new RuntimeException(e);
        }
  
  * “取消”按钮：
    调用 stage.close() 关闭当前传入的 Stage 所代表的窗口
        
    
        returnButton.setOnAction((actionEvent) -> stage.close());
* 在实际聊天室窗口中
  会先确保多线程的运行成功

      Platform.runLater(() -> {... });
  此时聊天窗口的标题会据传入的 Message 对象获取发送者信息，并设置窗口标题，标题明确显示了当前是哪位用户正在哪个聊天室进行群聊，方便用户直观区分不同的聊天窗口。

      String var10001 = message.getSender();
      stage.setTitle("用户" + var10001 + "正在聊天室 " + message.getRoomId() + " 中进行群聊...");
      stage.getIcons().add(new Image("file:image/deer.png"));

  创建聊天内容显示区域（TextArea），创建了一个文本区域用于展示聊天消息内容，并且将文本区域设置为不可编辑状态充当聊天记录。

      TextArea chatArea = new TextArea();
      chatArea.setEditable(false);
      chatArea.setWrapText(true);
  消息输入时首先通过 inputField.getText().trim() 获取输入框中的内容并去除首尾空格，然后判断内容是否为空。如果不为空，通过 (new SimpleDateFormat("HH:mm:ss")).format(new Date()) 获取当前时间，并按照指定格式 HH:mm:ss 格式化得到 timestamp
  再使用 String.format 方法将发送者、接收者、当前时间和输入的内容组合成一个格式化的聊天记录字符串 messageDisplay。将 messageDisplay 添加到 chatArea 中，用于在聊天记录区域显示刚刚发送的消息。
  通过 inputField.clear() 清空输入框内容，以便用户输入下一条消息。
  最后通过 this.sendMessageToOne(message.getSender(), message.getGetter(), content) 将用户输入的消息发送给接收者，这里 sendMessageToOne 方法已经实现了正确的消息发送功能。


* 聊天室如何收到消息

  首先会尝试从存储聊天窗口的映射表（chatStageMap）中获取相应发送者对应的聊天窗口（Stage），如果窗口不存在则先调用 openChatRoomWindow 方法创建窗口，

      Stage stage = (Stage)this.chatStageMap.get(message.getSender());
      if (stage == null) {
      this.openChatRoomWindow(message);
      stage = (Stage)this.chatStageMap.get(message.getSender());
      }
  然后获取窗口中的聊天内容显示区域（TextArea），并将消息内容追加到该区域进行展示，确保消息能正确呈现在对应的聊天界面上。且会使用换行操作使得新消息能够在聊天窗口内分行显示

      TextArea chatArea = (TextArea)stage.getUserData();
      String messageDisplay = message.getContent();
      chatArea.appendText(messageDisplay + "\n");
  注意也要使用Platform.runLater(() -> {... });确保后续代码块在 JavaFX 应用线程中执行


* 加入聊天室的操作
  点击“加入聊天室”按钮后会出现新的窗口，对此窗口进行了一些设置：
  
  又标题，提示标签，和文本输入框（填要加入的聊天室ID）
  
      stage.setTitle("加入聊天室ID....");
        Label label = new Label("请输入要加入的聊天室ID：");
        TextField userIdField = new TextField();
        userIdField.setPromptText("请输入要加入的聊天室ID:");
  
  点击确定按钮，触发加入聊天室的请求发送，点击取消按钮，则关闭当前界面窗口。
  
      Button confirmButton = new Button("确定");
        this.setButtonStyle(confirmButton);
        Button returnButton = new Button("取消");
        this.setButtonStyle(returnButton);

  接上点击确定后，触发了监听器confirmButton.setOnAction((actionEvent) ->{......}
    * 获取聊天室ID

          String roomId = userIdField.getText().trim();
    * 构造消息对象
      MessageType为“9”.内含发送者的用户ID和聊天室的ID，用以和客户端数据传输。
  
           Message message = new Message();
            message.setMesType("9");
            message.setSender(userId);
            message.setRoomId(roomId);
    * 输出提示消息
    
          System.out.println(userId + " 希望加入聊天室： " + roomId);
    * 消息发送操作和异常处理
      
      同样是获取一个能够将对象序列化并写入网络输出流的 ObjectOutputStream 对象，然后获取到与当前用户相关的线程对象，然后从该线程对象关联的 Socket
      对象中获取输出流，最后基于这个输出流创建 ObjectOutputStream 对象，以便后续将 Message 对象以序列化的形式写入输出流并发送出去。完成 ObjectOutputStream 对象的创建后，使用 oos.writeObject(message); 
      将之前构造好的 Message 对象写入到输出流中，实现消息的网络发送操作，从而将用户加入聊天室的请求传递到服务器端或者其他相应的接收端。
  
          try {
                ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
### 5） 私聊
* 先定义了一个 ConcurrentHashMap，用于存储聊天窗口的 Stage 对象
  
    private ConcurrentHashMap<String, Stage> chatStageMap = new ConcurrentHashMap<>();

  创建一个Message类型对象（MessageType为“3”），用以设置发送者和接收者的ID，然后通过与服务器连接的套接字输出流将消息发送出去

 ManageClientConnectServerThread 类提供了获取与指定用户 ID 对应的客户端与服务器连接线程的方法，通过该线程获取套接字，并利用 ObjectOutputStream 将消息序列化后发送给服务器。
  如果在发送过程中出现 IOException，则直接抛出一个运行时异常。

    public void judgeSenderAndGetter(String sendId, String getterId) {
    Message message = new Message();
    message.setMesType("3");
    message.setSender(sendId);
    message.setGetter(getterId);
    System.out.println(sendId + " 希望给 " + getterId + " 发送消息");
    
    try {
        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sendId).getSocket().getOutputStream());
        oos.writeObject(message);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }

再创建一个Message类型对象（MessageType为“31”），再设置发送者和接收者 ID、消息内容以及发送时间（格式化为 HH:mm:ss）。
然后同样通过获取与发送者 ID 对应的客户端与服务器连接线程的套接字输出流，使用 ObjectOutputStream 将消息发送出去。若出现 IOException，则抛出运行时异常。

    public void sendMessageToOne(String sendId, String getterId, String content) {
    Message message = new Message();
    message.setMesType("31");
    message.setSender(sendId);
    message.setGetter(getterId);
    message.setContent(content);
    message.setSendTime((new SimpleDateFormat("HH:mm:ss")).format(new Date()));
    System.out.println(sendId + " 正在给 " + getterId + " 发送消息");
    
    try {
        ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sendId).getSocket().getOutputStream());
        oos.writeObject(message);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }

* 然后判断所指定的聊天窗口存在与否：

  * 如果聊天窗口存在
  调用 chatStage.requestFocus() 方法将该聊天窗口设置为焦点，使其显示在所有窗口的最前面。


      Stage chatStage = (Stage)this.chatStageMap.get(chatKey);
        if (chatStage!= null) {
        chatStage.requestFocus();
        }
  * 如果聊天窗口不存在：

      创建新的聊天窗口（详见用户图形界面的（9）） 
成功构建起俩者的聊天窗口后，根据两个用户 ID（user1 和 user2）生成一个唯一的聊天会话标识 chatKey，确保对于任意两个用户之间的聊天会话，都能生成一个唯一且固定格式的标识，方便在 chatStageMap 中进行管理和查找。

* 对发送按钮的处理

      private String generateChatKey(String user1, String user2) {
      return user1.compareTo(user2) < 0? user1 + "-" + user2 : user2 + "-" + user1;
        }
        sendButton.setOnAction((e) -> {
        String content = inputField.getText().trim();
        if (!content.isEmpty()) {
            String timestamp = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
            String messageDisplay = String.format("[%s] %s 对 %s 说: %s", timestamp, message.getSender(), message.getGetter(), content);
            chatArea.appendText(messageDisplay + "\n");
            inputField.clear();
            this.sendMessageToOne(message.getSender(), message.getGetter(), content);
        }
        
        });

首先通过 inputField.getText().trim() 获取输入框中的内容并去除首尾空格，然后判断内容是否为空。如果不为空，通过 (new SimpleDateFormat("HH:mm:ss")).format(new Date()) 获取当前时间，并按照指定格式 HH:mm:ss 格式化得到 timestamp
再使用 String.format 方法将发送者、接收者、当前时间和输入的内容组合成一个格式化的聊天记录字符串 messageDisplay。将 messageDisplay 添加到 chatArea 中，用于在聊天记录区域显示刚刚发送的消息。
通过 inputField.clear() 清空输入框内容，以便用户输入下一条消息。
最后通过 this.sendMessageToOne(message.getSender(), message.getGetter(), content) 将用户输入的消息发送给接收者，这里 sendMessageToOne 方法已经实现了正确的消息发送功能。

* 聊天记录处理


    TextArea chatArea = new TextArea();
    chatArea.setEditable(false);
    chatArea.setWrapText(true);
创建一个文本区域组件，用于显示聊天消息，将文本区域设置为不可编辑状态，这样就可以只显示聊天记录而不能修改，同时设置文本自动换行，确保长消息能够在窗口内合理显示。


### 6）发送文件
在原有私聊功能上新添发送文件的功能，所以用户图形界面是在原有的私聊窗口上增加发送文件，浏览文件的按钮。

* 点击浏览文件（iconButton.setOnAction）；

  先创建一个FileChooser对象（JavaFX中提供弹出文件选择对话框的类），以此方便从本地文件系统中选择文件

      FileChooser fileChooser = new FileChooser();

  为文件选择对话框设置标题

      fileChooser.setTitle("选择文件");

  调用 showOpenDialog 方法弹出文件选择对话框，并传入当前的 Stage（聊天窗口）作为父窗口，然后进行文件选择（进行一次判断，是否选择了文件，没选择返回null）：
      
      fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
      File selectedFile = fileChooser.showOpenDialog(stage);
      if (selectedFile != null) {
      inputSrcText.setText(selectedFile.getAbsolutePath());
      }
* 点击发送文件（sendFileButton.setOnAction）：
  先获取文件路径输入框中的文本内容并去除空白字符，获取到有效的文件路径信息。

      String content = inputSrcText.getText().trim(); 
  紧接着判断文件是否有效，若有效则进行一些固定提示音的打印，以及像私聊功能中，将之保存在聊天记录中，且清空输入框

      String text = "我给你发送了一个文件！";
      String timestamp = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
       String messageDisplay = String.format("[%s] %s 对 %s 说: %s", timestamp, message.getSender(), message.getGetter(), text);
      chatArea.appendText(messageDisplay + "\n");
      inputSrcText.clear();
  最后调用sendFileToOne（）函数，实现服务端和客户端对文件发送、接收的相关服务，如下；

**发送文件：**

  构建一个Message对象，设置其类型为 "7"，且设置消息的发送者和接收者标识，明确该文件消息是从哪个人端发送到哪个人处，再将本地文件的路径设置到消息对象中。

      Message message = new Message();
      message.setMesType("7");
      message.setSender(senderId);
      message.setGetter(getterId);
      message.setSrc(src);
  将本地文件的内容读取到字节数组中，并在finally中关闭文件，注：文件太大会抛出异常，文件输入流不为null也抛出异常。

      byte[] fileBytes = new byte[(int)(new File(src)).length()];
      try {
              fileInputStream = new FileInputStream(src);
              fileInputStream.read(fileBytes);
              message.setFileBytes(fileBytes);
          } catch (Exception e) {
              throw new RuntimeException(e);
      } finally {
              if (fileInputStream != null) {
                  try {
                      fileInputStream.close();
                  } catch (IOException e) {
                      throw new RuntimeException(e);
                  }
              }
      
          }
  再通过网络进行Message对象发送

      try {
      ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
      oos.writeObject(message);
      } catch (IOException e) {
      throw new RuntimeException(e);
      }

**接收文件：**

  先在控制台打印文件发送消息提醒

    PrintStream var10000 = System.out;
    String var10001 = message.getSender();
    var10000.println("\n" + var10001 + "给" + message.getGetter() + "发送文件：" + message.getSrc());

  为确保在JavaFX应用线程能正常安全使用，使用 Platform.runLater 将后续对话框相关操作代码包裹起来，确保能正确运行在合适的线程环境中

  然后使用JavaFX 的 Alert（弹出提示对话框的组件）建立对话框，并设定一些初始属性，还创建一个“OK”按钮

    Alert alert = new Alert(AlertType.INFORMATION, "用户: " + message.getSender(), new ButtonType[]{ButtonType.OK});
    alert.setHeaderText("用户" + message.getSender() + "给你发来了一个文件！");
    ButtonType okButtonType = ButtonType.OK;
    Button okButton = (Button)alert.getDialogPane().lookupButton(okButtonType);
    if (okButton != null) {
    okButton.setText("确认接收并保存");
    }

  “OK”按钮会获取用户选择的保存路径，然后根据服务端发来的Message（通过 message.getSrc()），俩者进行拼接形成完整的文件保存路径 filePath，最后再将这个完整路径重新设置到消息对象的 Dest 属性中，更新了文件最终要保存的准确位置信息。

    message.setDest(selectedDirectory.getAbsolutePath());
    System.out.println("选中的目录: " + message.getDest());
    String fileName = message.getSrc().substring(message.getSrc().lastIndexOf(File.separator) + 1);
    String var10000 = message.getDest();
    String filePath = var10000 + File.separator + fileName;
    message.setDest(filePath);
    System.out.println("最终的路径: " + message.getDest());
    alert.close();

最后还会在控制台打印最终确定的文件保存路径信息，告知用户保存成功与保存的具体位置

文件保存相关的核心操作：

创建一个 FileOutputStream 对象，最终确定的文件保存路径作为参数，这个流对象用于将数据写入到文件中，准备将接收到的文件内容写入到磁盘文件中
调用fileOutputStream.write(message.getFileBytes())使用 write 方法将消息对象中包含的文件字节数组写入到通过 FileOutputStream 关联的文件中，在文件数据成功写入后，关闭 FileOutputStream，打印“文件保存成功！”提示。

    try {
          FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
          fileOutputStream.write(message.getFileBytes());
          fileOutputStream.close();
          } catch (IOException e) {
          throw new RuntimeException(e);
          }
## （6）实验总结和体会

我们使用了网络编程（`Socke`）、多线程（借用了`ConcurrentHashMap`）、IO输入输出流来实现多用户同时在线的通信功能，消息及数据的传输和保存利用了Message来实现（采用`HashMap`的方式），

创建了客户端与服务端两个端口，Server端服务端口搭建、实现管理和客户端通信的线程、覆写Thread中的run方法确保长时间即时通信。Client实现覆写Thread中的run方法，保持服务端与客户端的长时间联通、实现管理客户端连接到服务端的线程的类。

为改善用户体验的UI图形用户界面主要采用了JavaFX来完成，通过其自带的一些软件包，实现窗口的搭建，按钮的设置，窗口之间的跳转，
当然还有对登录，聊天窗口的美化操作。聊天窗口中，还运用了AreaText，来实现聊天记录的创建与保存，使之更合理与方便。聊天时文件的发送则主要是通过文件的输入输出流（`FileInputStream`）
以及Message，在`MessageType`中增加字节数组，采用二进制形式传输文件，再取出message中的文件字节数组，并通过文件输出流写出到磁盘上。群聊功能则是通过聊天室来实现，
采用了思维导图方式。

由于编写代码的时间太短，所以有很多功能并没有实现，只是完成了课上要求实现的功能，如果后期还有时间，我们自己会添加相应的功能去尝试完善代码并放置到GitHub上开源作为成果。

## 作者信息：

### 代码编写：崔文伙、杨育培、吴承臻

### 文档编写：崔文伙、陆达正
