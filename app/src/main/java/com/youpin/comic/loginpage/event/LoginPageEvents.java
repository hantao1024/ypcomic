package com.youpin.comic.loginpage.event;


import com.youpin.comic.loginpage.bean.LoginUserBean;
import com.youpin.comic.publicevent.BaseEvent;

/**
 * Created by hantao on 2018/2/7.
 */
public class LoginPageEvents extends BaseEvent {
    private final boolean isSuccess;
    private final String message;
    private final LoginUserBean loginUserBean;
    private final String mark;
    public LoginPageEvents(boolean isSuccess, String message,LoginUserBean loginUserBean,String mark) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.loginUserBean=loginUserBean;
        this.mark=mark;
    }

    public static LoginPageEvents pullSuccess( boolean isSuccess, String message,LoginUserBean loginUserBean,String mark) {
        return new LoginPageEvents(isSuccess, message,loginUserBean,mark);
    }

    public static LoginPageEvents pullFale(String message,String mark) {
        return new LoginPageEvents(false, message,null,mark);
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

    public String getMark() {
        return mark;
    }
}
