package com.youpin.comic.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youpin.comic.constant.AppConfig;


/**
 * 所有的 Fragment 都要继承于此 StepFragment,
 * 已经封装好了生生命周期方法,基本按照StepActivity的方式使用
 * 注意:在Activity中我们使用Intent传值
 * 在外部用      Fragment.setArguments(Bundle) 传入值
 * 在内部用     Fragment.getArguments().getInt[getString...]("key")的方式接收值
 *
 *
public class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position) {
    	//这里需要注意的是,当 position的Fragment已经被new过了之后,当界面重新回来的时候,
    	//就不会再调用此方法了,于是我们可以大但地在这里使用匿名实例,在FragmentPagerAdapter中会帮我们做好保存工作
    	//简单方式
    	if(position == 0 ) return new FirstFragment();
    	if(position == 1 ) return new SecondFragent();
    	//参数方式
        if(position == 0 ) {
        	/////////////////////////////////////////方式一
        	FirstFragment firstF = new FirstFragment() ;
        	Bundle bundle = new Bundle();
        	bundle.putString("key","value");
        	firstF.setArguments(bundle)  ;
        	return firstF ;
        	/////////////////////////////////////////方式二(明显第二种要简化第一种方式,我们推荐使用第二种方式)
        	FirstFragment firstF = new FirstFragment() ;
        	firstF.getArgumentsExtra().putString("key","value");
        	return firstF ;
        }
    }
}

//使用
MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
ViewPager mPager  = (ViewPager)findViewById(R.id.pager);
mPager.setAdapter(mAdapter);

 *
 * @author hantao
 */
public abstract class StepFragment extends BaseFragment {

//	onCreate(Bundle savedInstanceState)
//	onCreateView(LayoutInflater inflater
//	onActivityCreated(Bundle savedInstanceState) 
//	onStart()
//	onResume()

	private StepActivity stepActivity ;

	public StepActivity getStepActivity(){
		return stepActivity ;
	}
	public void setStepActivity(StepActivity activity){
		this.stepActivity = activity ;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState!=null && getArguments()!=null) {
			getArguments().putAll(savedInstanceState) ;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		return createContent(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViews() ;
		initData() ;
		setListener() ;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		free() ;
	}

	public Bundle getArgumentsExtra(){
		if (getArguments() == null) {
			Bundle info = new Bundle() ;
			setArguments(info)  ;
		}
		return getArguments() ;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (getArguments()!=null) {
			outState.putAll(getArguments()) ;
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (AppConfig.RELEASE) {
		}
	}

	@Override
	public void onPause() {
	    super.onPause();
	}

	/***
	 * set layout
	 */
	protected abstract View createContent(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState);
	/***
	 * find views
	 */
	protected abstract void findViews();
	/***
	 * initial data
	 */
	protected abstract void initData();
	/***
	 * set views listener
	 */
	protected abstract void setListener();
	/**
	 * free useless resource
	 */
	public abstract void free();


	public void showOprationDialogFragment(final String message){
		if (getStepActivity()!=null) {
			getStepActivity().showOprationDialog(message);
		}
	}
	public void cancelOprationDialogFragment(){
		if (getStepActivity()!=null) {
			getStepActivity().cancelOprationDialog();
		}
	}

}