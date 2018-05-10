package com.youpin.comic.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
	
	/** 内置handler */
	private Handler mDefaultHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onHandleMessage(msg);
		}
	};

	/**
	 * 内置handler处理到某个消息时，该方法被回调。子类实现该方法以定义对消息的处理。
	 * 
	 * @param msg
	 *            消息
	 */
	protected abstract void onHandleMessage(Message msg);

	/**
	 * 获取内置handler
	 * 
	 * @return 内置handler
	 */
	public Handler getDefaultHandler() {
		return mDefaultHandler;
	}
}
