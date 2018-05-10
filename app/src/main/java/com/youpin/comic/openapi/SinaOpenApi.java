package com.youpin.comic.openapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.Utility;
import com.youpin.comic.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SinaOpenApi {
	
	private Activity mContext;

	public static final int MSG_WHAT_SINATOKEN= 0x31;

	private Handler mHandler;

	public SinaOpenApi(Activity mContext,Handler mHandler) {
		this.mContext = mContext;
		this.mHandler = mHandler;
	}

	private SsoHandler mSsoHandler;

	private AuthListener mAuthListener;

	/** 微博分享的接口实例 */
    private IWeiboShareAPI mWeiboShareAPI;

    public IWeiboShareAPI getIWeiboShareAPI(){
    	return mWeiboShareAPI ;
    }

	public void getAccessToken(){
		// 创建授权认证信息
        WeiboAuth.AuthInfo authInfo = new WeiboAuth.AuthInfo(mContext, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mAuthListener = new AuthListener();
        WeiboAuth weiboAuth = new WeiboAuth(mContext, authInfo);
        mSsoHandler = new SsoHandler((Activity)mContext, weiboAuth);
        mSsoHandler.authorize(mAuthListener);
	}

	/**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        private static final String TAG = "AuthListener";

		@Override
        public void onComplete(Bundle values) {
			Log.d(TAG, "认证成功,还没把token持久化。。。");
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext, accessToken);
            }
            mHandler.sendEmptyMessage(MSG_WHAT_SINATOKEN);
            Log.d(TAG, "认证成功。。。");
        }

        @Override
        public void onWeiboException(WeiboException e) {
        	Log.d(TAG, "认证失败。。。"+ e.toString());
        }

        @Override
        public void onCancel() {
        	Log.d(TAG, "认证取消。。。");
        }
    }

    public void prepareShare(){
    	mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, Constants.APP_KEY);
    	boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
    	/** 如果装了客户端,就 */
    	if (isInstalledWeibo) {
    		mWeiboShareAPI.registerApp();
		}
    	mWeiboShareAPI.registerApp();
    }

    public void share(){
    	// 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, Constants.APP_KEY);
     // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
        boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
        int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();

        // 如果未安装微博客户端，设置下载微博对应的回调
        if (!isInstalledWeibo) {
        	Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(mContext);
        	if (token.getToken()!=null && token.getToken().length()>0) {
        		noClientShare(token);
			} else {
				AuthInfo authInfo = new AuthInfo(mContext, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		        WeiboAuth weiboAuth = new WeiboAuth(mContext, authInfo);
		        mSsoHandler = new SsoHandler((Activity)mContext, weiboAuth);
		        mSsoHandler.authorize(new WeiboAuthListener() {
					@Override
					public void onWeiboException(WeiboException arg0) {
					}
					@Override
					public void onComplete(Bundle values) {
						Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
			            if (accessToken != null && accessToken.isSessionValid()) {
			                AccessTokenKeeper.writeAccessToken(mContext, accessToken);
			            }
			            noClientShare(accessToken);
					}
					@Override
					public void onCancel() {
					}
				});
			}
        } else {
        	if (supportApiLevel == -1) {
        		Toast.makeText(mContext, "当前版本不支持分享!", 0).show();
            	return;
    		}
        	WeiboMessage weiboMessage = new WeiboMessage();
        	weiboMessage.mediaObject = getWebpageObj();

        	// 2. 初始化从第三方到微博的消息请求
        	SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        	// 用transaction唯一标识一个请求
        	request.transaction = String.valueOf(System.currentTimeMillis());
        	request.message = weiboMessage;

        	// 3. 发送请求消息到微博，唤起微博分享界面
        	mWeiboShareAPI.sendRequest(request);
		}
    }

    public void share2Weibo(Activity activity ,String title,  String img_url , String content_url, String share_text , final MyShareListener listener){

    	// 1. 初始化微博的分享消息
    	WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = share_text;

        // 设置 Bitmap 类型的图片到视频对象里
        mediaObject.setThumbImage(drawableToBitmap(activity.getResources().getDrawable(R.drawable.ic_launcher)));
        mediaObject.actionUrl = content_url;
        mediaObject.defaultText = share_text;

        weiboMessage.mediaObject = mediaObject ;

        TextObject textObject = new TextObject() ;
        textObject.text = share_text ;
        weiboMessage.textObject = textObject ;

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);



    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
    	Drawable d= drawable; //xxx根据自己的情况获取drawable
    	BitmapDrawable bd = (BitmapDrawable) d;
    	Bitmap bm = bd.getBitmap();
        return bm;
     }



    private void noClientShare(Oauth2AccessToken token){
    	StatusesAPI statusesAPI = new StatusesAPI(token);
    	statusesAPI.update("我正在使用动漫之家手机客户端看漫画，最全的漫画内容、最及时的漫画更新，你也下载一个看看吧！http://www.dmzj.com","0.0","0.0", new RequestListener() {
			@Override
			public void onWeiboException(WeiboException arg0) {
				try {
					JSONObject object = new JSONObject(arg0.getMessage().toString());
					if (object.opt("error_code").equals("20019")) {
						Toast.makeText(mContext, "不能重复发送喔!", 0).show();
					}else {
						Toast.makeText(mContext, "发送失败!", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onComplete(String arg0) {
				Toast.makeText(mContext, mContext.getString(R.string.errcode_success), 0).show();
			}
		});
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(mContext);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

		@Override
		public void onWeiboException(WeiboException e) {
		}
    }

    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    public void logout(){
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = mContext.getString(R.string.more_abut_share_title);
        mediaObject.description =mContext.getString(R.string.more_abut_share_text);

        // 设置 Bitmap 类型的图片到视频对象里
        mediaObject.setThumbImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher));
        mediaObject.actionUrl = "http://www.dmzj.com";
        mediaObject.defaultText = mContext.getString(R.string.more_abut_share_text);

        return mediaObject;
    }

	public SsoHandler getmSsoHandler() {
		return mSsoHandler;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	public void setmSsoHandler(SsoHandler mSsoHandler) {
		this.mSsoHandler = mSsoHandler;
	}

}
