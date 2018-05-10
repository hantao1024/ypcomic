package com.youpin.comic.loginpage.event;


import com.youpin.comic.loginpage.bean.LoginUserBean;
import com.youpin.comic.publicevent.BaseEvent;

/**
 * Created by hantao on 2018/5/9.
 * 登出
 */
public class LoginOutEvents extends BaseEvent {
    private final boolean isSuccess;
    private final String message;
    private final LoginUserBean loginUserBean;

    public LoginOutEvents(boolean isSuccess, String message,LoginUserBean loginUserBean) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.loginUserBean = loginUserBean;
    }

    public static LoginOutEvents pullSuccess(boolean isSuccess, String message,LoginUserBean loginUserBean) {
        return new LoginOutEvents(isSuccess, message,loginUserBean);
    }

    public static LoginOutEvents pullFale(String message) {
        return new LoginOutEvents(false, message,null);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public LoginUserBean getLoginUserBean() {
        return loginUserBean;
    }
}
