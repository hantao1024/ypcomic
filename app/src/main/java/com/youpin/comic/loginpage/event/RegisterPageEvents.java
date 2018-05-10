package com.youpin.comic.loginpage.event;


import com.youpin.comic.loginpage.bean.RegisterBean;
import com.youpin.comic.publicevent.BaseEvent;

/**
 * Created by hantao on 2018/2/7.
 */
public class RegisterPageEvents extends BaseEvent {
    private final boolean isSuccess;
    private final String message;
    private final RegisterBean registerBean;
    public RegisterPageEvents(boolean isSuccess, String message, RegisterBean registerBean) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.registerBean=registerBean;
    }

    public static RegisterPageEvents pullSuccess(boolean isSuccess, String message, RegisterBean registerBean) {
        return new RegisterPageEvents(isSuccess, message,registerBean);
    }

    public static RegisterPageEvents pullFale(String message) {
        return new RegisterPageEvents(false, message,null);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public RegisterBean getRegisterBean() {
        return registerBean;
    }

}
