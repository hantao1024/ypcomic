package com.youpin.comic.loginpage.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.youpin.comic.R;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.loginpage.fragment.ForgetPWDFragment;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.publicevent.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ForgetPWDActivity extends StepActivity {
    private static final String TAG = "ForgetPWDActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    @Override
    protected void createContent() {

    }
    @Override
    protected void findViews() {
        setContentView(R.layout.activity_forget_pwd);
    }

    @Override
    protected void initData() {
        setTitle(getActivity().getString(R.string.txt_forgot_pwd));
        setFragment();
    }

    @Override
    protected void setListener() {

    }


    @Override
    public void free() {
        EventBusUtils.unregister(this);
    }

    @Override
    protected void onHandleMessage(Message msg) {
    }

    /**
     * 接受传递过来的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HomePageEvents event) {
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }


    ForgetPWDFragment fragment;
    private void setFragment() {
        try {
            //步骤一：添加一个FragmentTransaction的实例
            FragmentManager fragmentManager =getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //步骤二：用add()方法加上Fragment的对象rightFragment
            fragment = new ForgetPWDFragment();
            fragment.setStepActivity(getActivity());
            transaction.add(R.id.ll_set_forget_fragment, fragment);

            //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
