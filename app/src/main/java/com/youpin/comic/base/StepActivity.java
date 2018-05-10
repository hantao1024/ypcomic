package com.youpin.comic.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youpin.comic.R;
import com.youpin.comic.constant.AppConfig;
import com.youpin.comic.publicutils.ProtocolDectorDialog;
import com.youpin.comic.publicviews.RoundTransformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class StepActivity extends BaseActivity {
	
	public static final String TAG = "StepActivity" ;
	
	/** 费时操作完成 */
	public static final int MSG_WHAT_TIMECONSUMING_COMPLETE= -0x099;
	
	public static final int MSG_WHAT_TIMECONSUMING_PREPARED= -0x098;
	
	private StepActivity mActivity;
	private boolean isFinished=false;
	/** 使用默认的KeyEvent处理事件,如:返回键默认关闭当前Activity */
	private boolean enabledefault_keyevent = true ; 
	
	/** 抽象操作Handler */
	private Handler mAbstractOprationHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onAbstractOprationMessage(msg);
		}
	};
	
	/**
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mActivity = this;
		invalidSAvedInstanceState(savedInstanceState) ;
		createContent();
		findViews();
		initData();
		setListener();
	}
	
	/**
	 * 查看是不是Intent中的数据为空,而 savedInstanceState 不空,则说明可能是系统导致界面销毁,我们把存储的数据还原
	 * @param savedInstanceState
	 */
	private void invalidSAvedInstanceState(Bundle savedInstanceState){
		if (savedInstanceState!=null && getIntent().getExtras()!=null) {
			getIntent().getExtras().putAll(savedInstanceState) ; 
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (getIntent().getExtras()!=null) {
			outState.putAll(getIntent().getExtras()) ; 
		}
		super.onSaveInstanceState(outState);
	}

	public Context getUseContent(Activity activity){
		//activity内部用最好不要用getActivity和getcontext,这个一般用在fragment里面，而且还有可能为空。
		//getContext()：这个是View类中提供的方法，在继承了View的类中才可以调用，返回的是当前View运行在哪个Activity Context中。
		//基本不会走getApplicationContext，除非个别手机个别情况可能出现,添加防止崩溃。
		Context context=null;
		if (activity!=null) {
			context= activity;
		} else if (getActivity() != null) {
			//基类自定义了，但最好传入当前activity。
			context= getActivity();
		}
		return context==null?getApplicationContext():context;
	}
	/***
	 * set layout
	 */
	protected abstract void createContent();
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

	@Override
	protected void onDestroy() {
		isFinished=true;
		super.onDestroy();
		free();
	}
	
	protected void onAbstractOprationMessage(Message msg){
		switch (msg.what) {
		case MSG_WHAT_TIMECONSUMING_COMPLETE:
			onTimeConsumingComplete(msg) ; 
			break;
		case MSG_WHAT_TIMECONSUMING_PREPARED : 
			OnTimeConsumingListener listener = (OnTimeConsumingListener) msg.obj ; 
			boolean showloading = msg.arg1 == 1 ? true : false ; 
			doConsumeTimeOprations(listener , showloading) ; 
			break ; 
		}
	}
	
	private void doConsumeTimeOprations(final OnTimeConsumingListener listener , final boolean showdialog){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Long flag = System.currentTimeMillis() ;
				mOnTimeConsumingListeners.put(flag, listener) ;
				listener.timeConsumingOpration() ; 
				Message msg = Message.obtain() ;
				msg.what = MSG_WHAT_TIMECONSUMING_COMPLETE ; 
				msg.obj = flag ; 
				msg.arg1 = showdialog ? 1 : 0 ; 
				mAbstractOprationHandler.sendMessage(msg) ; 
			}
		};
		Thread thread = new Thread(runnable) ;
		thread.start() ; 
	}
	
	/************************ 自用dialog start **************************/
	
	private ProtocolDectorDialog progressDialog  ;
	
	/***
	 * 显示操作dialog
	 */
	protected void showOprationDialog(final String message){
		if (Process.myTid() == getUIThreadId()) {
			// 调用在UI线程
			priShowOprationDialog(message) ; 
		} else {
			// 调用在非UI线程
			post(new Runnable() {
				@Override
				public void run() {
					priShowOprationDialog(message) ; 
				}
			});
		}
	}
	
	private void priShowOprationDialog(String text){
		ProtocolDectorDialog.STYLE style = (text != null && text.length() > 0 ) ? ProtocolDectorDialog.STYLE.NO_CLOSE : ProtocolDectorDialog.STYLE.NO_CLOSE_TXT  ;
		progressDialog = ProtocolDectorDialog.getDector(getActivity(), style , text, null) ;
		progressDialog.show() ; 
	}
	
	/**
	 * 关闭操作dialog
	 */
	protected void cancelOprationDialog(){
		if (Process.myTid() == getUIThreadId()) {
			// 调用在UI线程
			priCancelOprationDialog() ; 
		} else {
			// 调用在非UI线程
			post(new Runnable() {
				@Override
				public void run() {
					priCancelOprationDialog() ; 
				}
			});
		}
	}
	
	private void priCancelOprationDialog(){
		try {
			if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.cancel() ;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/************************ 自用dialog end.. **************************/
	
	/**
	 * 费时操作完成
	 * @param msg
	 */
	private void onTimeConsumingComplete(Message msg){
		Long flag = (Long) msg.obj ;
		OnTimeConsumingListener listener = mOnTimeConsumingListeners.get(flag) ; 
		if (listener!=null) {
			listener.onComplete() ;
			if (msg.arg1 == 1) {
				cancelOprationDialog()  ;
			}
			mOnTimeConsumingListeners.remove(flag) ; 
		}
	}
	
	/**
	 * 显示功能键
	 * @return
	 */
	public TextView showActionButton(){
		TextView v = (TextView) findViewById(R.id.action_all);
		if (v!=null) {
			v.setVisibility(View.VISIBLE);
		}
		return v;
	}
	
	/**
	 * 隐藏返回键
	 */
	public void hideBack(){
		View v = findViewById(R.id.back) ;
		if (v!=null) {
			v.setVisibility(View.INVISIBLE) ;
		}
	}

	public StepActivity getActivity() {
		return mActivity;
	}
	
	public void onBack(View v){
		closeOpration();
	}
	
	public void onAction(View v){
	}
	
	/**
	 * 设置标题，前提条件是本界面有id为title的Textview
	 * @param title 标题
	 */
	public void setTitle(String title) {
		try {
			TextView tit=(TextView) findViewById(R.id.title);
			if (tit!=null) {
                tit.setText(title);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置标题，前提条件是本界面有id为title的Textview
	 * @param strResId	文本资源id
	 */
	public void setTitle(int strResId){
		TextView tit=(TextView) findViewById(R.id.title);
		if (tit!=null) {
			tit.setText(getActivity().getString(strResId));
		}
	}
	
	@Override
	public void onBackPressed() {
		try {
			super.onBackPressed();
			closeOpration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 我们把我们关闭按钮的操作都放在这里,以便统一管理
	 */
	public void closeOpration() {
		free();
		getActivity().finish();
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode== KeyEvent.KEYCODE_BACK && enabledefault_keyevent){
			closeOpration();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public void onResume() {
		Log.d(TAG, "onResume() before super - "+ this.getClass().getSimpleName()) ;
    	super.onResume();
    	if (AppConfig.RELEASE) {
			//友盟的
//    		MobclickAgent.onResume(this);
		}
    	Log.d(TAG, "onResume() - "+ this.getClass().getSimpleName()) ;
    }
	
    public void onPause() {
    	super.onPause();
    	if (AppConfig.RELEASE) {
			//友盟的
//    		MobclickAgent.onPause(this);
		}
    	
    	Log.d(TAG, "onPause() - "+ this.getClass().getSimpleName()) ;
    }
    
	public boolean isEnabledefault_keyevent() {
		return enabledefault_keyevent;
	}
	
	/**
	 * 设置是否使用默认的keycode处理逻辑 ,默认值true
	 * @param enabledefault_keyevent
	 */
	public void setEnabledefault_keyevent(boolean enabledefault_keyevent) {
		this.enabledefault_keyevent = enabledefault_keyevent;
	}
	
	/*************************界面内部Listener************************/
	
	private Map<Long, OnTimeConsumingListener> mOnTimeConsumingListeners = new HashMap<Long, OnTimeConsumingListener>();
	
	/**
	 * @param listener
	 * @param text
	 * @param showdialog
	 */
	public void addTimeConsumingListener(final OnTimeConsumingListener listener , final String text, final boolean showdialog){
		if (showdialog) {
			showOprationDialog(text) ; 
		}
		
		Message msg = Message.obtain() ;
		msg.what = MSG_WHAT_TIMECONSUMING_PREPARED  ; 
		msg.obj = listener ; 
		msg.arg1 = showdialog ? 1 : 0 ; 
		mAbstractOprationHandler.sendMessageDelayed(msg, 300) ; 
		
	}
	
	public void addTimeConsumingListener(final OnTimeConsumingListener listener , final boolean showdialog){
		addTimeConsumingListener(listener, null ,showdialog) ; 
	}
	
	public void addTimeConsumingListener(final OnTimeConsumingListener listener){
		addTimeConsumingListener(listener, false) ; 
	}
	
	/**
	 * 费时操作监听接口
	 */
	public interface OnTimeConsumingListener{
		
		/**
		 * 费时操作,这是在子线程中进行的,注意如果有UI操作,就是要在此方法里进行了
		 */
		public void timeConsumingOpration();
		
		/**
		 * 费时操作完成,在主线程中完成,可以把更新UI的操作放在此回调中来进行
		 */
		public void onComplete();
		
	}

	public boolean isFinished() {
		return isFinished;
	}
	public  int IMG_CIRCULAR_BEAD = 20;
	public void getBitmap(ImageView imageView, String url){
		getBitmap(imageView,url,IMG_CIRCULAR_BEAD);
	}
	public void getBitmap(ImageView imageView, String url, int circularBead) {
		try {
			Glide.with(getActivity())
					.load(url)
					//                        .override(width,height)//这里的单位是px,可以设置显示大小

					/**
					 * DiskCacheStrategy 的枚举意义：

					 DiskCacheStrategy.NONE 什么都不缓存
					 DiskCacheStrategy.SOURCE 只缓存全尺寸图
					 DiskCacheStrategy.RESULT 只缓存最终的加载图
					 DiskCacheStrategy.ALL 缓存所有版本图（默认行为）
					 **/
					//                        .skipMemoryCache(true)
					//                        .diskCacheStrategy( DiskCacheStrategy.NONE )


					//                        .priority( Priority.LOW )//显示优先级
					.transform(new RoundTransformation(getActivity() , circularBead))
					.crossFade()//或者使用 dontAnimate() 关闭动画
					.placeholder(R.drawable.ic_launcher)//图片加载出来前，显示的图片
					.error(R.drawable.ic_launcher)//图片加载失败后，显示的图片
					.into(imageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		FragmentManager fragmentManager = getSupportFragmentManager();
		for (int indext = 0; indext < fragmentManager.getFragments().size(); indext++) {
			Fragment fragment = fragmentManager.getFragments().get(indext); //找到第一层Fragment
			if (fragment == null)
				Log.w(TAG, "Activity result no fragment exists for index: 0x"
						+ Integer.toHexString(requestCode));
			else
				handleResult(fragment, requestCode, resultCode, data);
		}
	}
	/**
	 * 递归调用，对所有的子Fragment生效
	 * @param fragment
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) {
		fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
		List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
		if (childFragment != null)
			for (Fragment f : childFragment)
				if (f != null) {
					handleResult(f, requestCode, resultCode, data);
				}
//		if(childFragment==null){
//
//		}

	}
}
