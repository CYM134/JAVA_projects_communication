package appServer.service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThreads {
    private static ConcurrentHashMap<String,ServerConnectClientThread> hm =new ConcurrentHashMap<>();

    //添加线程对象到hm集合
    public static void addClientThread(String userID,ServerConnectClientThread serverConnectClientThread){
        hm.put(userID,serverConnectClientThread);
    }

    //根据获得的用户ID返回ServerConnectClientThread线程
    public static ServerConnectClientThread getServerConnectClientThread(String userID){
        return hm.get(userID);
    }

    //从集合中移除线程
    public static void removeServerConnectClientThread(String userId){
        hm.remove(userId);
    }

    //在此编写方法返回在线用户列表
    public static String getOnlineUser(){
        //集合遍历，遍历hashmap中的key
        Iterator<String> iterator =hm.keySet().iterator();
        StringBuilder onlineUserList= new StringBuilder();
        while(iterator.hasNext()){
            onlineUserList.append(iterator.next()).append(" ");
        }
        return onlineUserList.toString();
    }


}
