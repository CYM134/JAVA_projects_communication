package appServer.service;

import app_common.Message;
import app_common.MessageType;
import app_common.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端类，在监听9999端口，等待客户端的连接，并保持通信
 */
public class appServer {
    private ServerSocket ss=null;

    //创建一个集合，存放多个用户，如果是这些用户登录，就认为是合法滴
    //HashMap在多线程情况下线程不安全，ConcurrentHashMap则是线程同步处理，在多线程下是安全的,因此采用ConcurrentHashMap
    private static ConcurrentHashMap<String,User> validUsers=new ConcurrentHashMap<>();
    private static final String USER_DATA_FILE = "users.dat"; // 用户数据存储文件
    static { //静态代码块，只会初始化一次，用于初始化validUsers
//        validUsers.put("100",new User("123456","100"));
//        validUsers.put("200",new User("123456","200"));
//        validUsers.put("300",new User("123456","300"));
//        validUsers.put("陆正达",new User("123456","陆正达"));
//        validUsers.put("小狗",new User("123456","小狗"));
//        validUsers.put("杨羊羊",new User("123456","杨羊羊"));
            // bug:每次重启服务端，用户数据就会重置会初始状态，现修改为从文件加载用户数据，确保用户数据的长期有效性
            loadUsersFromFile();
    }

    //函数作用：验证用户是否合法
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
    //函数作用：检验用户注册是否合法
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


    public appServer(){
        try {
            System.out.println("服务端在9999端口监听....");
            ss = new ServerSocket(9999);
            printUserFromFile();
            //当和某个客户端连接之后，会继续监听，因此使用循环
            while (true){
                //如果没有客户端连接就会进入阻塞阶段
                Socket socket = ss.accept();
                //得到socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //得到socket关联的对象输出流，好传送Message对象
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();  //读取客户端发送的User对象
                //创建一个Message对象用于回复客户端
                Message message = new Message();
                //验证用户是否合法
                //表明用户需要注册
                if(u.getUserId().charAt(0)=='*'){
                    if(checkEnroll(u.getUserId(), u.getPasswd())){//注册成功
                        message.setMesType(MessageType.MESSAGE_ENROLL_SUCCESS);
                        oos.writeObject(message);
                    }else{
                        message.setMesType(MessageType.MESSAGE_ENROLL_FAIL);
                        oos.writeObject(message);
                    }
                }else { //表明用户执行登录功能
                    if (checkUser(u.getUserId(), u.getPasswd())) { //登录成功
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS); //设置Message对象的值
                        //将message对象回复给客户端
                        oos.writeObject(message);
                        //创建一个线程和客户端保持通信，该线程需要持有socket对象
                        ServerConnectClientThread serverConnectClientThread =
                                new ServerConnectClientThread(socket, u.getUserId());
                        //启动该线程
                        serverConnectClientThread.start();
                        //将线程对象放入集合中进行管理
                        ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);
                    } else {  //登录失败
                        System.out.println("账号 ID = " + u.getUserId() + "   password = " + u.getPasswd() + " 验证失败！");
                        message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                        oos.writeObject(message);
                        socket.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //当服务端退出了监听之后，需要关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修复bug:每次关闭服务端后新注册用户的消息会丢失的问题
     * 将hashmap里面的数据保存到json文件中，这样以后每次启动服务端都可以从中得到以前的所有合法数据
     * 保证每次关闭服务端后数据不会丢失
     */
    //保存用户数据到文件
    private static void saveUsersToFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(validUsers);
            System.out.println("用户数据已保存到文件！");
        } catch (IOException e) {
            System.err.println("保存用户数据时出错：" + e.getMessage());
        }
    }

    //从文件中加载用户数据
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

    //打印用户数据用于debug
    private static void printUserFromFile(){
        File file = new File(USER_DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                ConcurrentHashMap<String, User> users = (ConcurrentHashMap<String, User>) ois.readObject();
                System.out.println("文件中的用户数据：");
                for (String userId : users.keySet()) {
                    User user = users.get(userId);
                    System.out.println("用户ID: " + user.getUserId() + ", 密码: " + user.getPasswd());
                }
            } catch (Exception e) {
                System.err.println("读取用户数据文件时出错：" + e.getMessage());
            }
        } else {
            System.out.println("用户数据文件不存在！");
        }
    }

}
