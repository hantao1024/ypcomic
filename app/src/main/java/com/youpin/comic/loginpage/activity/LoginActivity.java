package com.youpin.comic.loginpage.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.youpin.comic.R;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.loginpage.fragment.LoginFragment;
import com.youpin.comic.loginpage.fragment.RegisterFragment;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.publicevent.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class LoginActivity extends StepActivity {
    private static final String TAG = "LoginActivity";

    /** 最新评论,热门评论 */
    private RadioButton rb_login , rb_register ;

    private ViewPager mPager ;

    private MyAdapter mAdapter ;
    private TextView tv_login_close ;

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
        setContentView(R.layout.activity_login);
        rb_login=generateFindViewById(R.id.rb_login_title);
        rb_register=generateFindViewById(R.id.rb_register_title);
        mPager=generateFindViewById(R.id.vp_login);
        tv_login_close=generateFindViewById(R.id.tv_login_close);
    }

    @Override
    protected void initData() {
        mAdapter = new MyAdapter(getSupportFragmentManager()) ;
        mPager.setAdapter(mAdapter) ;
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0 ) {
                    rb_login.setChecked(true);
                    rb_register.setChecked(false);
                }else if (position == 1 ) {
                    rb_login.setChecked(false);
                    rb_register.setChecked(true);
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    protected void setListener() {
        rb_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPager.setCurrentItem(0);
                }
            }
        });

        rb_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPager.setCurrentItem(1);
                }
            }
        });
        tv_login_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeOpration();
            }
        });
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


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Fragment getItem(int position) {
            StepFragment fragment = null ;
            switch (position) {
                case 0:
                    fragment =  new LoginFragment();
                    fragment.setStepActivity(getActivity());
                    break;
                case 1:
                    fragment =  new RegisterFragment();
                    fragment.setStepActivity(getActivity());
                    break;
            }
            return fragment ;
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }
}
