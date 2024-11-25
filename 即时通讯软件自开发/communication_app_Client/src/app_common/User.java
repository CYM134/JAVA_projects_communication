package app_common;

import java.io.Serial;
import java.io.Serializable;

/**
 * 该类表示一个用户信息
 * Serializable:序列化，使对象能通过object流被读写
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;  //增强兼容性
    private String userId;//用户ID/用户名
    private String passwd;//用户密码

    public User(){}

    public User(String passwd, String userId) {
        this.passwd = passwd;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
