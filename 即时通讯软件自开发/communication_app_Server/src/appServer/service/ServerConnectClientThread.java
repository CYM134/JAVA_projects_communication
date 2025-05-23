package appServer.service;

import app_common.Message;
import app_common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类对应的对象和某个客户端保持通信
 * 接收客户端传过来的消息，以实现不同客户端的功能需求的正确实现
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;//连接到服务端的用户ID
    //创建用于存储聊天室的集合
    private static ConcurrentHashMap<String,String[]> ChatRoom
            =new ConcurrentHashMap<>();

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
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

                } else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES_JUDGE)){
                    //根据message获取getterId,然后得到对应线程
                    ServerConnectClientThread serverConnectClientThread
                            = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    //得到socket关联的对象输出流，好传送Message对象
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    //创建一个Message对象用于回复客户端
                    Message ms = new Message();
                    ms.setGetter(message.getGetter());
                    ms.setSender(message.getSender());
                    if(serverConnectClientThread==null){  //||message.getGetter().equals(message.getSender())
                        System.out.println(message.getGetter()+" 不在线！ ");
                        ms.setMesType(MessageType.MESSAGE_USER_ONLINE_FAIL);
                        oos.writeObject(ms);
                    }else{
                        System.out.println(message.getGetter()+" 在线！可以私聊 ");
                        ms.setMesType(MessageType.MESSAGE_USER_ONLINE_SUCCESS);
                        oos.writeObject(ms);
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES_SNED)){//转发消息给需要接收消息的用户
                    //根据message获取getterId,然后得到对应线程
                    ServerConnectClientThread serverConnectClientThread
                            = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    //得到对应线程的对象输出流,就是另一个线程
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    //根据getterId获取到对应的线程，然后将message对象转发
                    ObjectOutputStream oos =
                            new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);

                } else if (message.getMesType().equals(MessageType.CREATE_CHATROOM_ID)) {
                   String[] room = ChatRoom.get(message.getRoomId());
                    if(room==null&&!message.getRoomId().isEmpty()){
                        System.out.println("该聊天室ID没有被使用过！可以创建！");
                        ChatRoom.put(message.getRoomId(),new String[100]);
                        room = ChatRoom.get(message.getRoomId());
                        room[0]=message.getSender();
                    }else{
                        message.setMesType(MessageType.CREATE_CHATROOM_FAIL);
                    }
                    //根据message获取getterId,然后得到对应线程
                    ServerConnectClientThread serverConnectClientThread
                            = ManageClientThreads.getServerConnectClientThread(message.getSender());
                    //得到对应线程的对象输出流,就是另一个线程
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                }else if(message.getMesType().equals(MessageType.JOIN_CHATROOM_ID)){
                    String[] room = ChatRoom.get(message.getRoomId());  //获取集合中的ID
                    if(room==null){
                        System.out.println("没有找到想加入的聊天室ID "+message.getRoomId()+",返回错误提示！");
                        message.setMesType(MessageType.JOIN_CHATROOM_FAIL);
                    }else{
                        System.out.println("想加入的聊天室在集合里！可以加入！");
                        for (int i=0;i<room.length;i++){
                            if(room[i]==null){  //找到一个空位
                                room[i]=message.getSender();
                                break;
                            } else if(room[i].equals(message.getSender())){
                                System.out.println("用户已经在聊天室里了！");
                                break;
                            }
                        }
                    }
                    //根据message获取getterId,然后得到对应线程
                    ServerConnectClientThread serverConnectClientThread
                            = ManageClientThreads.getServerConnectClientThread(message.getSender());
                    //得到对应线程的对象输出流,就是另一个线程
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_ROOM_MES)) {
                    String[] room = ChatRoom.get(message.getRoomId());  //获取集合中的ID
                    for(String user:room){
                        if(user!=null&&!user.equals(message.getSender())){
                            String senderId =message.getSender();
                            System.out.println("发消息的是："+message.getSender()+"  给聊天室用户："+user+"发消息");  //检验用户
                            //根据循环获取每个用户的ID,然后得到对应线程
                            ServerConnectClientThread serverConnectClientThread
                                    = ManageClientThreads.getServerConnectClientThread(user);
                            //得到对应线程的对象输出流,就是另一个线程
                            ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                            message.setSender(user);
                            oos.writeObject(message);
                            message.setSender(senderId);
                        }
                    }
                } else{
                    System.out.println("其它类型的message,暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
