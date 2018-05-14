package com.youpin.comic.mainpage.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.youpin.comic.R;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.publicevent.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by hantao on 2018/5/14.
 */

public class DiscoverOrBookrackFragment extends StepFragment{

    /** 最新评论,热门评论 */
    private RadioButton rb_discover , rb_book_rack_title ;

    private ViewPager vp_discover_or_book_rack ;

    private MyAdapter mAdapter ;
    private ImageView iv_discover_or_book_rack_query ;

    @Override
    protected void onHandleMessage(Message msg) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }
    View v;
    @Override
    protected View createContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_discover_or_bookrack, null);
        }
        return v;
    }

    @Override
    protected void findViews() {
        rb_discover=(RadioButton) v.findViewById(R.id.rb_discover_title);
        rb_book_rack_title=(RadioButton)v.findViewById(R.id.rb_book_rack_title);
        vp_discover_or_book_rack=(ViewPager) v.findViewById(R.id.vp_discover_or_book_rack);
        iv_discover_or_book_rack_query=(ImageView) v.findViewById(R.id.iv_discover_or_book_rack_query);
    }

    @Override
    protected void initData() {
        mAdapter = new MyAdapter(getChildFragmentManager()) ;
        vp_discover_or_book_rack.setAdapter(mAdapter) ;
        vp_discover_or_book_rack.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0 ) {
                    rb_discover.setChecked(true);
                    rb_book_rack_title.setChecked(false);
                }else if (position == 1 ) {
                    rb_discover.setChecked(false);
                    rb_book_rack_title.setChecked(true);
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
        rb_discover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vp_discover_or_book_rack.setCurrentItem(0);
                }
            }
        });

        rb_book_rack_title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vp_discover_or_book_rack.setCurrentItem(1);
                }
            }
        });
        iv_discover_or_book_rack_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void free() {
        EventBusUtils.unregister(this);
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
                    fragment =  new MainCartoonStudyFragment();
                    fragment.setStepActivity(getStepActivity());
                    break;
                case 1:
                    fragment =  new MainCartoonStudyFragment();
                    fragment.setStepActivity(getStepActivity());
                    break;
            }
            return fragment ;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (v != null) {
            ((ViewGroup) v.getParent()).removeView(v);
        }
    }
}
