package com.youpin.comic.mainpage.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youpin.comic.R;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.base.StepFragment;
import com.youpin.comic.mainpage.bean.ClassifyFilterBeans;
import com.youpin.comic.publicviews.BounceScrollView;
import com.youpin.comic.publicviews.ViewPagerIndicatorTwo;

import java.util.ArrayList;
import java.util.List;


/**
 * 漫研
 * @author hantao
 */
public class MainCartoonStudyFragment extends StepFragment implements View.OnClickListener {
	private List<Fragment> mTabContents = new ArrayList<>();
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private ViewPagerIndicatorTwo mIndicator;
	private BounceScrollView mScrollView;
	private ImageView iv_main_boy_or_girl;
	private ImageView iv_main_query;


	/** model集合 */
	private List<ClassifyFilterBeans.ClassifyFilterItem> classItems =new ArrayList<ClassifyFilterBeans.ClassifyFilterItem>();
	@Override
	public void onClick(View v) {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	View v;

	@Override
	protected View createContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment_cartoon_study, null);
		}
		return v;
	}


	@Override
	protected void findViews() {
		mScrollView = (BounceScrollView) v.findViewById(R.id.id_scrollview);
		mViewPager = (ViewPager) v.findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicatorTwo) v.findViewById(R.id.id_indicator);
		mViewPager.setOffscreenPageLimit(1);
		iv_main_query = (ImageView) v.findViewById(R.id.iv_main_query);
		iv_main_boy_or_girl = (ImageView) v.findViewById(R.id.iv_main_boy_or_girl);
//		mScrollView.setSlide(false);
	}

	@Override
	protected void initData() {
		setFirstItem();
	}

	private void setFirstItem() {
		classItems.clear();
		ClassifyFilterBeans.ClassifyFilterItem  classifyFilterItem=new ClassifyFilterBeans.ClassifyFilterItem();
		classifyFilterItem.setTag_id(0);
		classifyFilterItem.setTag_name("推荐");
		classItems.add(classifyFilterItem);


		ClassifyFilterBeans.ClassifyFilterItem  classifyFilterItem1=new ClassifyFilterBeans.ClassifyFilterItem();
		classifyFilterItem1.setTag_id(1);
		classifyFilterItem1.setTag_name("漫文");
		classItems.add(classifyFilterItem1);

		ClassifyFilterBeans.ClassifyFilterItem  classifyFilterItem2=new ClassifyFilterBeans.ClassifyFilterItem();
		classifyFilterItem2.setTag_id(2);
		classifyFilterItem2.setTag_name("宅新闻");
		classItems.add(classifyFilterItem2);

		ClassifyFilterBeans.ClassifyFilterItem  classifyFilterItem3=new ClassifyFilterBeans.ClassifyFilterItem();
		classifyFilterItem3.setTag_id(3);
		classifyFilterItem3.setTag_name("神评论");
		classItems.add(classifyFilterItem3);

		setAdapter(classItems);
	}

	private void setAdapter(List<ClassifyFilterBeans.ClassifyFilterItem> classItems ) {
		if (null==classItems||classItems.isEmpty()) {
			return;
		}
		for (int i=0;classItems.size()>i;i++) {
			if (i == 0) {
				MainRecommendFragment fragment = new MainRecommendFragment();
				Bundle bundle = new Bundle();
				bundle.putString("nameStr", classItems.get(i).getTag_name());
				bundle.putInt("ids", classItems.get(i).getTag_id());
				fragment.setArguments(bundle);
				fragment.setStepActivity((StepActivity) getActivity());
				mTabContents.add(fragment);
			} else if(i==1){
				MainCartoonArticleFragment fragment = new MainCartoonArticleFragment();
				Bundle bundle = new Bundle();
				bundle.putString("nameStr", classItems.get(i).getTag_name());
				bundle.putInt("ids", classItems.get(i).getTag_id());
				fragment.setArguments(bundle);
				fragment.setStepActivity((StepActivity) getActivity());
				mTabContents.add(fragment);
			} else if(i==2){
				MainNewsFragment fragment = new MainNewsFragment();
				Bundle bundle = new Bundle();
				bundle.putString("nameStr", classItems.get(i).getTag_name());
				bundle.putInt("ids", classItems.get(i).getTag_id());
				fragment.setArguments(bundle);
				fragment.setStepActivity((StepActivity) getActivity());
				mTabContents.add(fragment);
			} else if(i==3){
				MainCommentFragment fragment = new MainCommentFragment();
				Bundle bundle = new Bundle();
				bundle.putString("nameStr", classItems.get(i).getTag_name());
				bundle.putInt("ids", classItems.get(i).getTag_id());
				fragment.setArguments(bundle);
				fragment.setStepActivity((StepActivity) getActivity());
				mTabContents.add(fragment);
			}


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
		iv_main_boy_or_girl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		iv_main_query.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if (iv_main_boy_or_girl!=null) {
					iv_main_boy_or_girl.setVisibility(position==0?View.VISIBLE:View.GONE);
				}
			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	@Override
	public void free() {
	}

	@Override
	protected void onHandleMessage(Message msg) {

	}
}
