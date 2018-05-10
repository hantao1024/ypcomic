package com.youpin.comic.openapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youpin.comic.R;
import com.youpin.comic.wxapi.WXEntryActivity;
import com.youpin.comic.wxapi.WXUtil;

public class WixinChatApi {
	
	/** 获取到微信code的广播,在登录界面使用 */
	public static final String BOADCATST_WECHAT_GETCODE = "com.dmzj.manhua.api.openapi.wechat.getcode" ;
	/** 获取到微信code的key */
	public static final String INTENT_EXTRA_CODE= "intent_extra_code";
	
	/** 微信获取到了access_token */
	public static final int MSG_WHAT_WECHATTOKEN = 0x2101;
	
	private IWXAPI api;
	
	private Activity mActivity ; 
	
	private Handler mHandler ; 
	
	public WixinChatApi(Activity mActivity ,Handler mHandler ) {
		this.mActivity = mActivity ; 
		this.mHandler = mHandler ; 
		wechatInit() ; 
	}
	
	private void wechatInit(){
		 // 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(mActivity, WXEntryActivity.APP_ID, false);
		api.registerApp(WXEntryActivity.APP_ID);    	
	}
	
	public void login(){
		final SendAuth.Req req = new SendAuth.Req();
	    req.scope = "snsapi_userinfo";
	    req.state = "wechat_sdk_demo_test";
	    api.sendReq(req);
	}
	
	/**
	 * 微信登录时,获取到code时,我们使用广播把它传播出去
	 * @param code
	 * @return
	 */
	public static Intent getSendAuthCodeIntent(String code){
		Intent intent = new Intent(BOADCATST_WECHAT_GETCODE) ;
		intent.putExtra(INTENT_EXTRA_CODE, code) ; 
		return intent ; 
	}
	
	/**
	 * @param frienc_circle 是否朋友圈
	 * @param activity
	 * @param title
	 * @param img_url
	 * @param content_url
	 * @param share_text
	 * @param listener
	 * 
	 */
	public void share2Weixin(boolean firendCircle,Activity activity ,String title, String img_url , String content_url, String share_text , final MyShareListener listener){
		
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = content_url ;
		WXMediaMessage wcmsg = new WXMediaMessage(webpage);
		wcmsg.title = title;
		wcmsg.description = share_text;
		
		Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);
		wcmsg.thumbData = WXUtil.bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = wcmsg;
		req.scene = firendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
		
		
	}
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	
}
