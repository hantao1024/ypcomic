package com.youpin.comic.loginpage.event;


import com.youpin.comic.publicevent.BaseEvent;

/**
 * Created by hantao on 2018/5/9.
 * 验证码
 */
public class CaptchaEvents extends BaseEvent {
    private final boolean isSuccess;
    private final String message;
    private final String data;
    private final String mark;

    public CaptchaEvents( boolean isSuccess, String message, String data,String mark) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.data = data;
        this.mark = mark;
    }

    public static CaptchaEvents pullSuccess( boolean isSuccess, String message, String data,String mark) {
        return new CaptchaEvents(isSuccess, message,data,mark);
    }

    public static CaptchaEvents pullFale( String message,String mark) {
        return new CaptchaEvents(false, message,"",mark);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public String getMark() {
        return mark;
    }
}
