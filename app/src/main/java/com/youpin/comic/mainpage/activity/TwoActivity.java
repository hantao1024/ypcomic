package com.youpin.comic.mainpage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youpin.comic.R;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.mainpage.adapter.ShopListAdapter;
import com.youpin.comic.mainpage.bean.User;
import com.youpin.comic.mainpage.dao.UserNameDao;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.mainpage.manager.MainPageManager;
import com.youpin.comic.publicevent.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;



public class TwoActivity extends StepActivity {
    ListView lv_content;

    private static final String TAG = "TwoActivity";
    private String mBaseUrl = "https://nbsdk-baichuan.alicdn.com/2.0.0/applink.htm?plat=android&appKey=23261993";
    ShopListAdapter shopListAdapter;
    RefreshLayout refreshLayout;
    private List<User> userList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    @Override
    protected void createContent() {

    }
    @Override
    public void onAction(View v) {
        super.onAction(v);
        Intent intent=new Intent(TwoActivity.this,ThreeActivity.class);
        startActivity(intent);
    }
    @Override
    protected void findViews() {
        setContentView(R.layout.activity_two);
        showActionButton().setText("下一页");
        lv_content = generateFindViewById(R.id.lv_content);
        refreshLayout = generateFindViewById(R.id.refreshLayout);

//        //禁止上拉加载：
//        refreshLayout.setEnableLoadMore(false);
//        //使上拉加载具有弹性效果：或者不自动加载
//        refreshLayout.setEnableAutoLoadMore(false);
//        //禁止越界拖动：
//        refreshLayout.setEnableOverScrollDrag(false);

    }

    @Override
    protected void initData() {
        setTitle("第二页");
        //设置 Header 为 BezierRadar 样式
//        refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        userList = UserNameDao.queryUser();
        shopListAdapter = new ShopListAdapter(TwoActivity.this, userList);
        shopListAdapter.setItemListner(itemListner);
        lv_content.setAdapter(shopListAdapter);

    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                MainPageManager.getInstance().doGet(false, mBaseUrl, null);
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                MainPageManager.getInstance().doGet(true, mBaseUrl, null);
//                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
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
        if (event.isLoadmore()) {
            refreshLayout.finishLoadmore();//传入false表示加载失败
        } else {
            refreshLayout.finishRefresh();//传入false表示刷新失败
        }
        List<User> list = UserNameDao.queryUser();
        shopListAdapter.reLoad(list);
        shopListAdapter.notifyDataSetChanged();
    }

    private ShopListAdapter.ItemListner itemListner = new ShopListAdapter.ItemListner() {
        @Override
        public void Click(User user) {
            UserNameDao.updateUser(user);
            List<User> list = UserNameDao.queryUser();
            shopListAdapter.reLoad(list);
            shopListAdapter.notifyDataSetChanged();
        }

        @Override
        public void Delete(User user) {
            UserNameDao.deleteUser(user.getId());
            List<User> list = UserNameDao.queryUser();
            shopListAdapter.reLoad(list);
            shopListAdapter.notifyDataSetChanged();
        }
    };
}
