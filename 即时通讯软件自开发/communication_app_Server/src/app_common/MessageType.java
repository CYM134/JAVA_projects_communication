package app_common;

/**
 * 定义消息类型
 */
public interface MessageType {
    String MESSAGE_ENROLL_SUCCESS="111";//表示注册成功
    String MESSAGE_ENROLL_FAIL ="222";//表示注册失败
    String MESSAGE_LOGIN_SUCCESS="1";//表示登录成功
    String MESSAGE_LOGIN_FAIL = "2";//表示登录失败
    String MESSAGE_COMM_MES_JUDGE="3";//普通信息包
    String MESSAGE_COMM_MES_SNED="31";//传输消息
    String MESSAGE_COMM_MES_GET="32";//接收消息
    String MESSAGE_GET_ONLINE_FRIEND ="4";//要求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT="6";//客户端请求退出
    String MESSAGE_FILE_MES="7"; //发送文件类型
    String MESSAGE_USER_ONLINE_FAIL="333";  //用户在线
    String MESSAGE_USER_ONLINE_SUCCESS="444";
    String CREATE_CHATROOM_ID="8"; //创建聊天室信号
    String CREATE_CHATROOM_FAIL="888"; //创建的聊天室ID已经存在了
    String JOIN_CHATROOM_ID ="9"; //加入聊天室信号
    String JOIN_CHATROOM_FAIL ="999"; //加入聊天室的ID不存在//用户不在线
    String MESSAGE_ROOM_MES="10"; //群发消息
}
