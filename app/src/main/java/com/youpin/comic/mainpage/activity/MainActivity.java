package com.youpin.comic.mainpage.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.youpin.comic.R;
import com.youpin.comic.base.BaseViewPager;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.mainpage.fragment.MainCartoonStudyFragment;
import com.youpin.comic.mainpage.fragment.MineFragment;
import com.youpin.comic.mainpage.fragment.OneFragment;
import com.youpin.comic.mainpage.fragment.ThreeFragment;
import com.youpin.comic.publicevent.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;


public class MainActivity extends StepActivity {


    private static final String TAG = "MainActivity";

    NavigationController navigationController;
    BaseViewPager mViewPager;
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
        setContentView(R.layout.activity_main);

        mViewPager = generateFindViewById(R.id.vp_main_bottom);
        mViewPager.setSlide(false);
        PageNavigationView tab = generateFindViewById(R.id.main_tab);


        final ArrayList<Fragment> fgLists = new ArrayList<>(3);
        fgLists.add(new MainCartoonStudyFragment());
        fgLists.add(new OneFragment());
        fgLists.add(new ThreeFragment());
        fgLists.add(new MineFragment());

        navigationController = tab.custom()
                .addItem(newItem(R.drawable.icon_main_one_no_checked,R.drawable.icon_main_one_checked,"漫研"))
                .addItem(newItem(R.drawable.icon_main_two_no_checked,R.drawable.icon_main_two_checked,"读漫"))
                .addItem(newItem(R.drawable.icon_main_three_no_checked,R.drawable.icon_main_three_checked,"聊漫"))
                .addItem(newItem(R.drawable.icon_main_four_no_checked,R.drawable.icon_main_four_checked,"我的"))
                .build();


        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }
        });

        navigationController.setupWithViewPager(mViewPager);

        //设置消息数
        navigationController.setMessageNumber(3,8);

//        //设置显示小圆点
//        navigationController.setHasMessage(0,true);
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text){
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable,checkedDrawable,text);
        normalItemView.setTextDefaultColor(getActivity().getResources().getColor(R.color.comm_gray_low));
        normalItemView.setTextCheckedColor(getActivity().getResources().getColor(R.color.login_red_mid));
        return normalItemView;
    }
    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {

        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                //选中时触发
                if (index==3) {
                    navigationController.setMessageNumber(3,0);
                }
            }

            @Override
            public void onRepeat(int index) {
                //重复选中时触发
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
}
