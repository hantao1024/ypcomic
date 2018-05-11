package com.youpin.comic.mainpage.fragment;

import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youpin.comic.R;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.mainpage.adapter.GridviewRollAdapter;
import com.youpin.comic.mainpage.adapter.MainRecommendAdapter;
import com.youpin.comic.mainpage.bean.GridviewRollBean;
import com.youpin.comic.mainpage.bean.User;
import com.youpin.comic.mainpage.dao.UserNameDao;
import com.youpin.comic.mainpage.events.HomePageEvents;
import com.youpin.comic.mainpage.manager.MainPageManager;
import com.youpin.comic.publicevent.EventBusUtils;
import com.youpin.comic.publicviews.ImageCycleView;
import com.youpin.comic.upmarqueeview.UPMarqueeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by hantao on 2018/5/2.
 * 首页推荐
 */

public class MainRecommendFragment extends StepFragment {
    MainRecommendAdapter shopListAdapter;
    RefreshLayout refreshLayout;
    private List<User> userList = new LinkedList<>();

    ListView lv_content;
    private String mBaseUrl = "https://nbsdk-baichuan.alicdn.com/2.0.0/applink.htm?plat=android&appKey=23261993";
    private ImageCycleView mImageCycleView ;

    private View mHeaderView;
    GridView gridView;
    GridviewRollAdapter gridviewRollAdapter;
    List<GridviewRollBean> gridviewRollBeanList;
    List<String> textArray = new ArrayList<>();
    UPMarqueeView up_show_title;
    List<View> views = new ArrayList<>();
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

        mHeaderView = View.inflate(getActivity(), R.layout.main_recommend_header, null);
        gridView=(GridView) mHeaderView.findViewById(R.id.gv_roll_grid);
        up_show_title=(UPMarqueeView) mHeaderView.findViewById(R.id.up_show_title);
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
        shopListAdapter = new MainRecommendAdapter(getActivity(), userList);
        shopListAdapter.setItemListner(itemListner);
        lv_content.setAdapter(shopListAdapter);
        setData();
        MainPageManager.getInstance().doGet(false, mBaseUrl, null);
    }

    private void setTextData(){
        textArray.clear();
        textArray.add("测试000000000000001");
        textArray.add("测试000000000000002");
        textArray.add("测试000000000000003");

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
        gridviewRollBeanList.add(item);
        gridviewRollBeanList.add(item1);
        gridviewRollBeanList.add(item2);
        gridviewRollBeanList.add(item3);
        gridviewRollBeanList.add(item4);
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
        setView();
        processHeader();

        List<User> list = UserNameDao.queryUser();
        shopListAdapter.reLoad(list);
        shopListAdapter.notifyDataSetChanged();


    }


    private MainRecommendAdapter.ItemListner itemListner = new MainRecommendAdapter.ItemListner() {
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
        int size = gridviewRollBeanList.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        gridviewRollAdapter = new GridviewRollAdapter(getActivity(),
                gridviewRollBeanList);
        gridviewRollAdapter.setItemListner(gridviewRollItemListner);
        gridView.setAdapter(gridviewRollAdapter);
    }

    GridviewRollAdapter.ItemListner gridviewRollItemListner=new GridviewRollAdapter.ItemListner() {
        @Override
        public void Click(GridviewRollBean gridviewRollBean) {
            Toast.makeText(getActivity(),gridviewRollBean.getName(),Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     */
    private void setView() {
        setTextData();
        if (textArray==null) {
            return;
        }
        views.clear();
        for (int i = 0; i < textArray.size(); i ++) {
            final int position = i;
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_up_view, null);
            //初始化布局的控件
            TextView tv_up_title = (TextView) moreView.findViewById(R.id.tv_up_title);

            /**
             * 设置监听
             */
            moreView.findViewById(R.id.tv_up_title).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), position + "你点击了" + textArray.get(position).toString(), Toast.LENGTH_SHORT).show();
                }
            });
            //进行对控件赋值
            tv_up_title.setText(textArray.get(i).toString());

            //添加到循环滚动数组里面去
            views.add(moreView);
        }

        up_show_title.setViews(views);
        /**
         * 设置item_view的监听
         */
        up_show_title.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getActivity(), "你点击了第几个items" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
