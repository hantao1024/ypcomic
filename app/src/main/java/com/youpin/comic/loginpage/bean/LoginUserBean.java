package com.youpin.comic.loginpage.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hantao on 2018/5/7.
 */
@Entity
public class LoginUserBean {
    public static final String STATUS_ONLINE= "";

    public static final String STATUS_OFFLINE= "";
    @Id(autoincrement = true) // id自增长
    private Long userId; // id

    @Index(unique = true) // 唯一性
    private String id; // 用户编号

    private String nickname; // 用户名称

    private String phone; // 用户性别
    private String email; // 用户性别
    private String created_at;
    private String api_token;
    private String status;
    @Generated(hash = 398403331)
    public LoginUserBean(Long userId, String id, String nickname, String phone,
            String email, String created_at, String api_token, String status) {
        this.userId = userId;
        this.id = id;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.created_at = created_at;
        this.api_token = api_token;
        this.status = status;
    }
    @Generated(hash = 1232875992)
    public LoginUserBean() {
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCreated_at() {
        return this.created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getApi_token() {
        return this.api_token;
    }
    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
   


}
