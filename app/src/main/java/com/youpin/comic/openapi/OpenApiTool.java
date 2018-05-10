package com.youpin.comic.openapi;

import android.content.Context;
import android.content.Intent;

public class OpenApiTool {
	
	/***
	 * 分享成功
	 */
	public static final String BROADCAST_SHARED_SUCCESS = "com.dmzj.manhua.share.success" ;
	/**
	 * 分享错误
	 */
	public static final String BROADCAST_SHARED_ERROR = "com.dmzj.manhua.share.error" ;
	/**
	 * 分享取消
	 **/
	public static final String BROADCAST_SHARED_CANCEL = "com.dmzj.manhua.share.cancle" ;
	
	public static void sendSuccessBroadCast(Context context){
		Intent intent = new Intent(BROADCAST_SHARED_SUCCESS) ;
		context.sendBroadcast(intent) ; 
	}
	
	public static void sendErrorBroadCast(Context context){
		Intent intent = new Intent(BROADCAST_SHARED_ERROR) ;
		context.sendBroadcast(intent) ; 
	}
	
	public static void sendCancelBroadCast(Context context){
		Intent intent = new Intent(BROADCAST_SHARED_CANCEL) ;
		context.sendBroadcast(intent) ; 
	}
	
	
	
}
