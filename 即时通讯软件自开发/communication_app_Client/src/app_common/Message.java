package app_common;

import java.io.Serial;
import java.io.Serializable;

/**
 * 表示客户端和服务端通信时的消息对象
 * Serializable:序列化，使对象能通过object流被读写
 */
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;  //增强兼容性
    private String sender;//确定发送者是谁
    private String getter;//确定接收者是谁
    private String content;//确定发送的消息内容是什么
    private String sendTime;//确定消息的发送时间是什么时候
    private String mesType;//确定发送的消息类型
    private String roomId;//聊天室ID

    //和发送文件相关的东西
    private byte[] fileBytes; //文件内容以字节方式进行传输
    private int fileLen = 0;//获取文件的长度
    private String dest; //将文件传输到哪里
    private String src; //源文件的路径

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
