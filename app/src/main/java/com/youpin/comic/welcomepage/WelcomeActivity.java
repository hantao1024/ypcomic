package com.youpin.comic.welcomepage;

import android.os.Bundle;
import android.os.Message;

import com.youpin.comic.R;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.mainpage.bean.User;

import java.util.LinkedList;
import java.util.List;


public class WelcomeActivity extends StepActivity {
    private List<User> userList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void createContent() {

    }
    @Override
    protected void findViews() {
        setContentView(R.layout.activity_welcome);
    }
    @Override
    protected void initData() {

    }
    @Override
    protected void setListener() {

    }
    @Override
    public void free() {
    }

    @Override
    protected void onHandleMessage(Message msg) {
    }
}
