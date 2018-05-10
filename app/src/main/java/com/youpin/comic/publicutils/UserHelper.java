package com.youpin.comic.publicutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.youpin.comic.loginpage.activity.LoginActivity;
import com.youpin.comic.loginpage.bean.LoginUserBean;
import com.youpin.comic.loginpage.dao.LoginDao;

public final class UserHelper {
	

	public interface OnUserOnLineListener{
		/**
		 * 如果执行到此方法,user肯定不为null,即user肯定在线
		 * @param user
		 */
		public void onUserOnline(LoginUserBean user);
	}
	
	/**
	 * 当用户不在线时,自动跳转到登录界面时的回调
	 * @author hantao
	 *
	 */
	public interface OnUserOfflineGoLoginActivity{
		
		public void onGoingLoginActivity() ; 
		
	}
	
	
	public interface OnCheckUserListener{
		/**
		 * @param user
		 * 用户在线,返回user对象
		 */
		public void onUserOnline(LoginUserBean user);
		/**
		 * 用户不在线,回调此方法
		 */
		public void onUserOffline();
	}
	
	/**
	 * 检测用户是否在线,不在线就去登录
	 * @param activity
	 * @param listener
	 */
	public static void checkIfUserOnLineOtherWiseGoLogin(Activity activity, OnUserOnLineListener listener){
		checkIfUserOnLineOtherWiseGoLogin(activity, -1, listener);
	}
	
	public static void checkIfUserOnLineOtherWiseGoLogin(Activity activity, int requestCode , OnUserOnLineListener listener){
		checkIfUserOnLineOtherWiseGoLogin(activity, requestCode, listener, null) ; 
	}
	
	public static void checkIfUserOnLineOtherWiseGoLogin(Activity activity, int requestCode , OnUserOnLineListener listener , OnUserOfflineGoLoginActivity offlineGoLoginActivity){
		checkIfUserOnLineOtherWiseGoLogin(activity, requestCode, listener, offlineGoLoginActivity, null) ; 
	}
	
	public static void checkIfUserOnLineOtherWiseGoLogin(Activity activity, int requestCode , OnUserOnLineListener listener , OnUserOfflineGoLoginActivity offlineGoLoginActivity , Bundle bundle){
		LoginUserBean user = LoginDao.queryUserOne();
		boolean online = user == null ? false : true ; 
		if (online) {
			if (listener !=null) {
				listener.onUserOnline(user) ; 
			}
		} else {
			if (offlineGoLoginActivity != null) {
				offlineGoLoginActivity.onGoingLoginActivity() ; 
			}
			
			Intent intent = new Intent(activity, LoginActivity.class) ;
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			
			if (bundle != null) {
				intent.putExtras(bundle) ; 
			}
			
			if (requestCode == -1) {
				activity.startActivity(intent) ; 
			}else {
				activity.startActivityForResult(intent, requestCode) ; 
			}
		}
	}
	
	
	/**
	 * 检测用户是否在线
	 */
	public static void checkIfUserOnLine(Context activity, OnCheckUserListener listener){
		LoginUserBean user = LoginDao.queryUserOne();
		if (user==null) {
			listener.onUserOffline() ;
		}else {
			listener.onUserOnline(user) ; 
		}
	}
}
