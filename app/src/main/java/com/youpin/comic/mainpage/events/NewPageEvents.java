package com.youpin.comic.mainpage.events;


import com.youpin.comic.publicevent.BaseEvent;

/**
 * Created by hantao on 2018/2/7.
 */
public class NewPageEvents extends BaseEvent {
    private final boolean isSuccess;
    private final String message;

    private final boolean isLoadmore;
    public NewPageEvents(boolean isLoadmore, boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.isLoadmore=isLoadmore;
    }

    public static NewPageEvents pullSuccess(boolean isLoadmore, boolean isSuccess, String message) {
        return new NewPageEvents(isLoadmore,isSuccess, message);
    }

    public static NewPageEvents pullFale(boolean isLoadmore, String message) {
        return new NewPageEvents(isLoadmore,false, message);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public boolean isLoadmore() {
        return isLoadmore;
    }
}
