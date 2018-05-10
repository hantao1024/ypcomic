package com.youpin.comic.openapi;


import android.app.Activity;
import android.os.Handler;

public class OpenApiHelper {
	
	private static final String TAG = "OpenApiHelper";
	
	/** QQ平台 */
	public static final int PLAT_QQ = 0x1;
	/** 新浪平台 */
	public static final int PLAT_SINA = 0x2;
	
	public static final int MSG_WHAT_PLAT_QQ = 0x10;
	
	public static final int MSG_WHAT_PLAT_SINA = 0x11;
	
	private  Activity mActivity;
	
	private Handler mHandler;
	
	public OpenApiHelper(Activity mActivity,Handler mHandler) {
		this.mActivity = mActivity;
		this.mHandler = mHandler;
	}
	
	/**
	 * native 调用此方法发出登陆请求
	 * @param platform
	 */
	public void onLogin(int platform){
		switch (platform) {
		case PLAT_QQ:
			mHandler.sendEmptyMessage(MSG_WHAT_PLAT_QQ);
			break;
		case PLAT_SINA:
			mHandler.sendEmptyMessage(MSG_WHAT_PLAT_SINA);
			break;
		default:
			break;
		}
	}
	
	
	public native static void onTokenReturn(int plat , String token ,String uid);
	
	
}
