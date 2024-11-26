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
    String MESSAGE_USER_ONLINE_FAIL="333";  //用户在线
    String MESSAGE_USER_ONLINE_SUCCESS="444";//用户不在线
}
