package com.youpin.comic.mainpage.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youpin.comic.R;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.mainpage.adapter.MainNewsAdapter;
import com.youpin.comic.mainpage.bean.User;
import com.youpin.comic.mainpage.dao.UserNameDao;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.mainpage.manager.MainPageManager;
import com.youpin.comic.publicevent.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by hantao on 2018/5/2.
 * 首页宅新闻
 */

public class MainNewsFragment extends StepFragment {
    MainNewsAdapter mainNewsAdapter;
    RefreshLayout refreshLayout;
    private List<User> userList = new LinkedList<>();

    ListView lv_content;
    private String mBaseUrl = "https://nbsdk-baichuan.alicdn.com/2.0.0/applink.htm?plat=android&appKey=23261993";

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
            v = inflater.inflate(R.layout.fragment_main_news, null);
        }
        return v;
    }

    @Override
    protected void findViews() {
        lv_content = (ListView) v.findViewById(R.id.lv_content);
        refreshLayout = (RefreshLayout) v.findViewById(R.id.refreshLayout);


    }

    @Override
    protected void initData() {
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        userList = UserNameDao.queryUser();
        mainNewsAdapter = new MainNewsAdapter(getActivity(), userList);
        mainNewsAdapter.setItemListner(itemListner);
        lv_content.setAdapter(mainNewsAdapter);
        MainPageManager.getInstance().doGet(false, mBaseUrl, null);
    }

    private void loadData(final boolean more) {

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
    public void onDestroyView() {
        super.onDestroyView();
        if (v != null) {
            ((ViewGroup) v.getParent()).removeView(v);
        }
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
        mainNewsAdapter.reLoad(list);
        mainNewsAdapter.notifyDataSetChanged();

    }


    private MainNewsAdapter.ItemListner itemListner = new MainNewsAdapter.ItemListner() {
        @Override
        public void Click(User user) {
//            UserNameDao.updateUser(user);
//            List<User> list = UserNameDao.queryUser();
//            shopListAdapter.reLoad(list);
//            shopListAdapter.notifyDataSetChanged();

//            Intent intent=new Intent(getActivity(),ThreeActivity.class);
//            startActivity(intent);
        }

        @Override
        public void Delete(User user) {
            UserNameDao.deleteUser(user.getId());
            List<User> list = UserNameDao.queryUser();
            mainNewsAdapter.reLoad(list);
            mainNewsAdapter.notifyDataSetChanged();
        }
    };

}
