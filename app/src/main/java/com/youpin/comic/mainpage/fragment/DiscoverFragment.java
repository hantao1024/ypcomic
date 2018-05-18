package com.youpin.comic.mainpage.fragment;

import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youpin.comic.R;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.mainpage.adapter.GridviewRollAdapter;
import com.youpin.comic.mainpage.adapter.MainDiscoverAdapter;
import com.youpin.comic.mainpage.adapter.MainHeaderDiscoverAdapter;
import com.youpin.comic.mainpage.bean.GridviewRollBean;
import com.youpin.comic.mainpage.bean.User;
import com.youpin.comic.mainpage.dao.UserNameDao;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.mainpage.manager.MainPageManager;
import com.youpin.comic.mainpage.view.TwoRollMyGridView;
import com.youpin.comic.publicevent.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by hantao on 2018/5/2.
 * 发现
 */

public class DiscoverFragment extends StepFragment {
    MainDiscoverAdapter shopListAdapter;
    MainHeaderDiscoverAdapter mainHeaderDiscoverAdapter;
    RefreshLayout refreshLayout;
    private List<User> userList = new LinkedList<>();

    ListView lv_content;
    ListView lv_header;
    private String mBaseUrl = "https://nbsdk-baichuan.alicdn.com/2.0.0/applink.htm?plat=android&appKey=23261993";

    private View mHeaderView;
    TwoRollMyGridView gridView;
    GridView gridOneView;
    GridviewRollAdapter gridviewRollAdapter;
    GridviewRollAdapter gridviewOneRollAdapter;
    List<GridviewRollBean> gridviewRollBeanList;
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
            v = inflater.inflate(R.layout.fragment_main_recommend, null);
        }
        return v;
    }

    @Override
    protected void findViews() {
        lv_content = (ListView) v.findViewById(R.id.lv_content);
        refreshLayout = (RefreshLayout) v.findViewById(R.id.refreshLayout);

        mHeaderView = View.inflate(getActivity(), R.layout.main_discover_header, null);
        gridView=(TwoRollMyGridView) mHeaderView.findViewById(R.id.gv_discover_roll_grid);
        gridOneView=(GridView) mHeaderView.findViewById(R.id.gv_one_roll_grid);
        lv_header=(ListView) mHeaderView.findViewById(R.id.lv_discover_header);
        //禁止上拉加载：
        refreshLayout.setEnableLoadMore(false);
        //使上拉加载具有弹性效果：或者不自动加载
        refreshLayout.setEnableAutoLoadMore(false);
        //禁止越界拖动：
        refreshLayout.setEnableOverScrollDrag(false);
    }

    private void processHeader() {
//        refreshHeaders();
        try {
            lv_content.setAdapter(null);
            setView(lv_content,"headview", mHeaderView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lv_content.setAdapter(shopListAdapter);

    }

    @Override
    protected void initData() {
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        userList = UserNameDao.queryUser();
        shopListAdapter = new MainDiscoverAdapter(getActivity(), userList);
        shopListAdapter.setItemListner(itemListner);
        lv_content.setAdapter(shopListAdapter);
        setData();
        MainPageManager.getInstance().doGet(false, mBaseUrl, null);
    }


    /**设置数据*/
    private void setData() {
        gridviewRollBeanList = new ArrayList<GridviewRollBean>();
        GridviewRollBean item = new GridviewRollBean();
        item.setUrl("");
        item.setContnet("0755");
        item.setName("0755");

        GridviewRollBean item1 = new GridviewRollBean();
        item1.setUrl("");
        item1.setContnet("0756");
        item1.setName("0756");


        GridviewRollBean item2 = new GridviewRollBean();
        item2.setUrl("");
        item2.setContnet("0757");
        item2.setName("0757");


        GridviewRollBean item3 = new GridviewRollBean();
        item3.setUrl("");
        item3.setContnet("0758");
        item3.setName("0758");


        GridviewRollBean item4 = new GridviewRollBean();
        item4.setUrl("");
        item4.setContnet("0759");
        item4.setName("0759");


        GridviewRollBean item5 = new GridviewRollBean();
        item5.setUrl("");
        item5.setContnet("0760");
        item5.setName("0760");

        GridviewRollBean item6 = new GridviewRollBean();
        item6.setUrl("");
        item6.setContnet("0761");
        item6.setName("0761");
        gridviewRollBeanList.add(item);
        gridviewRollBeanList.add(item1);
        gridviewRollBeanList.add(item2);
        gridviewRollBeanList.add(item3);
        gridviewRollBeanList.add(item4);
        gridviewRollBeanList.add(item5);
        gridviewRollBeanList.add(item6);
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
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                MainPageManager.getInstance().doGet(true, mBaseUrl, null);
////                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
//            }
//        });


    }

    @Override
    public void onResume() {
        super.onResume();
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
        setGridView();
        setListView();
        setOneGridView();
        processHeader();

        List<User> list = UserNameDao.queryUser();
        shopListAdapter.reLoad(list);
        shopListAdapter.notifyDataSetChanged();


    }


    private MainDiscoverAdapter.ItemListner itemListner = new MainDiscoverAdapter.ItemListner() {
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
            shopListAdapter.reLoad(list);
            shopListAdapter.notifyDataSetChanged();
        }
    };

  private MainHeaderDiscoverAdapter.ItemListner itemHeaderListner = new MainHeaderDiscoverAdapter.ItemListner() {
        @Override
        public void Click(User user) {
        }

        @Override
        public void Delete(User user) {
            UserNameDao.deleteUser(user.getId());
            List<User> list = UserNameDao.queryUser();
            mainHeaderDiscoverAdapter.reLoad(list);
            mainHeaderDiscoverAdapter.notifyDataSetChanged();
        }
    };

    private void setView(ListView listView, String s, View view) {

        switch (s) {
            case "footview":
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(view);
                }
                break;
            case "headview":
                if (listView.getHeaderViewsCount() == 0) {
                    listView.addHeaderView(view);
                }
                break;
        }


    }


    /**设置GirdView参数，绑定数据*/
    private void setGridView() {
        gridviewRollAdapter = new GridviewRollAdapter(getActivity(),
                gridviewRollBeanList);
        gridviewRollAdapter.setItemListner(gridviewRollItemListner);
        gridView.setAdapter(gridviewRollAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int count = gridviewRollAdapter.getCount();
        int columns = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        int columnViewWidth = dm.widthPixels / 3;
        gridView.setAdapter(gridviewRollAdapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columns * columnViewWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(columnViewWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        if (count <= 3) {
            gridView.setNumColumns(count);
        } else {
            gridView.setNumColumns(columns);
        }
    }

    GridviewRollAdapter.ItemListner gridviewRollItemListner=new GridviewRollAdapter.ItemListner() {
        @Override
        public void Click(GridviewRollBean gridviewRollBean) {
            Toast.makeText(getActivity(),gridviewRollBean.getName(),Toast.LENGTH_SHORT).show();
        }
    };

    private void setListView(){

        mainHeaderDiscoverAdapter = new MainHeaderDiscoverAdapter(getActivity(), userList);
        mainHeaderDiscoverAdapter.setItemListner(itemHeaderListner);
        lv_header.setAdapter(mainHeaderDiscoverAdapter);
        setListViewHeightBasedOnChildren(lv_header);

    }

    /**设置GirdView参数，绑定数据*/
    private void setOneGridView() {
        int size = gridviewRollBeanList.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridOneView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridOneView.setColumnWidth(itemWidth); // 设置列表项宽
        gridOneView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridOneView.setStretchMode(GridView.NO_STRETCH);
        gridOneView.setNumColumns(size); // 设置列数量=列表集合数

        gridviewOneRollAdapter = new GridviewRollAdapter(getActivity(),
                gridviewRollBeanList);
        gridviewOneRollAdapter.setItemListner(gridviewOneRollItemListner);
        gridOneView.setAdapter(gridviewOneRollAdapter);
    }

    GridviewRollAdapter.ItemListner gridviewOneRollItemListner=new GridviewRollAdapter.ItemListner() {
        @Override
        public void Click(GridviewRollBean gridviewRollBean) {
            Toast.makeText(getActivity(),gridviewRollBean.getName(),Toast.LENGTH_SHORT).show();
        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
    }
}
