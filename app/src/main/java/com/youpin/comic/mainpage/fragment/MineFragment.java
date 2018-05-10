package com.youpin.comic.mainpage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youpin.comic.R;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.loginpage.activity.LoginActivity;
import com.youpin.comic.loginpage.bean.LoginUserBean;
import com.youpin.comic.loginpage.dao.LoginDao;
import com.youpin.comic.loginpage.event.LoginOutEvents;
import com.youpin.comic.loginpage.manager.LoginPageManager;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.publicevent.EventBusUtils;
import com.youpin.comic.publicutils.UserHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by hantao on 2018/5/2.
 */

public class MineFragment extends StepFragment {
    RefreshLayout refreshLayout;
    ImageView iv_login;
    ImageView iv_login_out;
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
            v = inflater.inflate(R.layout.fragment_mine, null);
        }
        return v;
    }

    @Override
    protected void findViews() {
        refreshLayout = (RefreshLayout) v.findViewById(R.id.refreshLayout);
        iv_login = (ImageView) v.findViewById(R.id.iv_login);
        iv_login_out = (ImageView) v.findViewById(R.id.iv_login_out);

        //禁止上拉加载：
        refreshLayout.setEnableLoadMore(false);
        //使上拉加载具有弹性效果：或者不自动加载
        refreshLayout.setEnableAutoLoadMore(false);
        //禁止越界拖动：
        refreshLayout.setEnableOverScrollDrag(false);

    }

    @Override
    protected void initData() {
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
    }

    private void loadData(final boolean more) {

    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshLayout.finishLoadmore();
            }
        });
        iv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        iv_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserHelper.checkIfUserOnLine(getActivity(), new UserHelper.OnCheckUserListener() {
                    @Override
                    public void onUserOnline(LoginUserBean user) {
                        LoginPageManager.getInstance().doLoginOut(user);
                    }

                    @Override
                    public void onUserOffline() {

                    }
                });
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

    }
    /**
     * 接受传递过来的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginOutEvents event) {
        if (event!=null) {
            Toast.makeText(getStepActivity(), event.getMessage(), Toast.LENGTH_SHORT).show();
            LoginDao.deleteUser(event.getLoginUserBean());
        }

    }

}
