package appServer.service;

import app_common.Message;
import app_common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 该类对应的对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;//连接到服务端的用户ID

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {  //这里可以让线程处于run的状态，从而可以发送/接收消息
        while(true){
            try {
                System.out.println("服务端和客户端"+userId+"保持通信，读取数据....");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //根据得到的message类型来做相应的业务处理
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //表明客户端要在线用户列表
                    //在线用户列表形式 100 200 紫霞仙子
                    System.out.println(message.getSender()+"需要显示在线用户列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    //构建Message对象，返回给客户端
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());
                    //返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    //表明执行客户端退出功能，无异常退出系统
                    System.out.println(message.getSender()+"退出系统");
                    //将需要退出的客户端对应线程从集合中移除
                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    socket.close();//关闭连接
                    break;  //退出线程

                } else{
                    System.out.println("其它类型的message,暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
