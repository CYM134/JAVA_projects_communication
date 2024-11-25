package Client_Login.service;

import java.util.HashMap;

/**
 * 该类功能是管理客户端连接到服务端的线程的类
 */
public class ManageClientConnectServerThread {
    //将多个线程放入一个HashMap集合，key就是用户ID，value就是线程
    private static HashMap<String,ClientConnectServerThread> hm=new HashMap<>();

    //将某个线程加入到集合中
    public static void addClientConnectServerThread(String userID,ClientConnectServerThread clientConnectServerThread){
        hm.put(userID,clientConnectServerThread);
    }

    //传入UserID,可以得到对应的线程
    public static ClientConnectServerThread getClientConnectServerThread(String userID){
        return hm.get(userID);
    }


}
