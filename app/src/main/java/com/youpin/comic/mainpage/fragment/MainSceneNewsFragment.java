package com.youpin.comic.mainpage.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youpin.comic.R;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.constant.URLPathMaker;
import com.youpin.comic.mainpage.bean.ClassifyFilterBeans;
import com.youpin.comic.mainpage.events.NewPageEvents;
import com.youpin.comic.mainpage.manager.MainPageManager;
import com.youpin.comic.publicevent.EventBusUtils;
import com.youpin.comic.constant.AppConfig;
import com.youpin.comic.publicutils.ObjectMaker;
import com.youpin.comic.publicviews.BounceScrollView;
import com.youpin.comic.publicviews.ViewPagerIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;



/**
 * 新闻入口
 * @author hantao
 */
public class MainSceneNewsFragment extends StepFragment implements View.OnClickListener {
	private List<Fragment> mTabContents = new ArrayList<>();
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;
	private BounceScrollView mScrollView;

	/** 分类model集合 */
	private List<ClassifyFilterBeans.ClassifyFilterItem> classItems =new ArrayList<ClassifyFilterBeans.ClassifyFilterItem>();
	@Override
	public void onClick(View v) {

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
			v = inflater.inflate(R.layout.activity_news_main, null);
		}
		return v;
	}


	@Override
	protected void findViews() {
		mScrollView = (BounceScrollView) v.findViewById(R.id.id_scrollview);
		mViewPager = (ViewPager) v.findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator) v.findViewById(R.id.id_indicator);
		mViewPager.setOffscreenPageLimit(1);

	}

	@Override
	protected void initData() {
		setFirstItem();
		MainPageManager.getInstance().doGetThree(false, URLPathMaker.get_url(AppConfig.API_NEW), null);
	}

	private void setFirstItem() {
		ClassifyFilterBeans.ClassifyFilterItem  classifyFilterItem=new ClassifyFilterBeans.ClassifyFilterItem();
		classifyFilterItem.setTag_id(-1);
		classifyFilterItem.setTag_name("推荐");
		classItems.add(0,classifyFilterItem);
	}

	private void setAdapter(List<ClassifyFilterBeans.ClassifyFilterItem> classItems ) {
		if (null==classItems||classItems.isEmpty()) {
			return;
		}
		for (int i=0;classItems.size()>i;i++) {
			OneFragment fragment = new OneFragment();
			Bundle bundle = new Bundle();
			bundle.putString("nameStr", classItems.get(i).getTag_name());
			bundle.putInt("ids", classItems.get(i).getTag_id());
			fragment.setArguments(bundle);
			fragment.setStepActivity((StepActivity) getActivity());
			mTabContents.add(fragment);
		}

		mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
			@Override
			public int getCount() {
				return mTabContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mTabContents.get(position);
			}
		};
		//设置Tab上的标题
		mIndicator.setTabItemTitles(getListNameStr(classItems));
		mViewPager.setAdapter(mAdapter);
		//设置关联的ViewPager
		mIndicator.setViewPager(mViewPager, mScrollView, 0);
	}
	private List<String> getListNameStr(List<ClassifyFilterBeans.ClassifyFilterItem> classItems ){
		if (classItems==null&&classItems.isEmpty()) {
			return null;
		}
		List<String> datas=new ArrayList<String>();
		for (int i=0;i<classItems.size();i++) {
			datas.add(classItems.get(i).getTag_name());
		}
		return datas;
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
	public void onEventMainThread(NewPageEvents event) {

		if (event.isSuccess()&&!TextUtils.isEmpty(event.getMessage())) {
			try {
				JSONArray arr = new JSONArray(event.getMessage()) ;
				classItems = ObjectMaker.convert2List(arr, ClassifyFilterBeans.ClassifyFilterItem.class) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (classItems!=null&&!classItems.isEmpty()) {
			setAdapter(classItems);
		}

	}
}
