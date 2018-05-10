package com.youpin.comic.openapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.youpin.comic.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class TencentOpenApi {
	
	private static final String TAG = "TencentOpenApi";

	public static final String APP_ID = "101131188" ;

	private Tencent mTencent;


	private Activity mContext;


	public static final int MSG_WHAT_QQTOKEN = 0x21;

	private Handler mHandler;

	private IUiListener handleBaseUiListener  ;

	public TencentOpenApi(Activity mContext,Handler mHandler) {
		this.mContext = mContext;
		this.mHandler = mHandler;
	}

	public void getAccessToken(){
		login();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		Tencent.onActivityResultData(requestCode, resultCode, data, handleBaseUiListener) ;
	}

	private class BaseUiListener implements IUiListener {
		@Override
		public void onError(UiError e) {
			Log.d(TAG,"onError(UiError e)");
		}
		@Override
		public void onCancel() {
			Log.d(TAG," onCancel()");
		}
		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject)response , true);
		}
	}


	protected void doComplete(Object response ,boolean sendMsg) {
		if (null == response) {
            Log.d(TAG,"返回为空");
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            Log.d(TAG,"登录失败");
            return;
        }
        Log.d(TAG, response.toString());
        AccessTokenKeeper4Tencent.Oauth2Access4Tencent bean = new AccessTokenKeeper4Tencent.Oauth2Access4Tencent();
        bean.setPfkey(jsonResponse.optString("pfkey"));
        bean.setAccess_token(jsonResponse.optString("access_token"));
        bean.setOpenid(jsonResponse.optString("openid"));
        AccessTokenKeeper4Tencent.writeAccessToken(mContext, bean);
        if (sendMsg) {
        	mHandler.sendEmptyMessage(MSG_WHAT_QQTOKEN);
		}
	}
	/**
	 * 登录方法
	 */
	public void login()
	{
		BaseUiListener mIUiListener = new BaseUiListener();
		handleBaseUiListener = mIUiListener ;
		mTencent = Tencent.createInstance(APP_ID, mContext);
		if (mTencent != null&&!mTencent.isSessionValid()) {
			mTencent.login(mContext, "all", mIUiListener);
		}
	}


	/**
	 * 分享到QQ
	 */
	public void share2QQ(Activity activity ,String title, String img_url , String content_url, String share_text , final MyShareListener listener){
		 final Bundle params = new Bundle();
		 params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//分享的类型。图文分享(普通分享)填Tencent.SHARE_TO_QQ_TYPE_DEFAULT
		 params.putString(QQShare.SHARE_TO_QQ_TITLE, title);		//分享的标题, 最长30个字符。
		 params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  share_text);//分享的消息摘要，最长40个字。
		 params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  content_url);//这条分享消息被好友点击后的跳转URL。
		 params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,img_url);	//分享图片的URL或者本地路径
		 params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  activity.getString(R.string.app_name));//手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
		 IUiListener mIUiListener  = new IUiListener() {
			@Override
			public void onError(UiError arg0) {
				if (listener != null) {
					listener.onError(null) ;
				}
			}
			@Override
			public void onComplete(Object arg0) {
				if (listener != null) {
					listener.onSuccess(null) ;
				}
			}
			@Override
			public void onCancel() {
				if (listener != null) {
					listener.onCancel(null) ;
				}
			}
		};
		handleBaseUiListener = mIUiListener ;
		mTencent.shareToQQ(mContext, params, mIUiListener) ;
	}

	/**
	 * 分享到qq空间
	 */
	public void share2QQZone(Activity activity ,String title, String img_url , String content_url, String share_text , final MyShareListener listener){
		final Bundle params = new Bundle();
		ArrayList<String> imgs = new ArrayList<String>();
		imgs.add(img_url) ;
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//分享的标题，最多200个字符。
	    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, share_text);//摘要:分享的摘要，最多600字符。
	    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, content_url);//跳转URL: 需要跳转的链接，URL字符串。
	    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);//图片链接ArrayList:QZone接口暂不支持发送多张图片的能力，若传入多张图片，则会自动选入第一张图片作为预览图
	    IUiListener mIUiListener  = new IUiListener() {
			@Override
			public void onError(UiError arg0) {
				if (listener != null) {
					listener.onError(null) ;
				}
			}
			@Override
			public void onComplete(Object arg0) {
				if (listener != null) {
					listener.onSuccess(null) ;
				}
			}
			@Override
			public void onCancel() {
				if (listener != null) {
					listener.onCancel(null) ;
				}
			}
		};
		handleBaseUiListener = mIUiListener ;
	    mTencent.shareToQzone(activity, params, mIUiListener);
	}

	public void logout(){
		mTencent = Tencent.createInstance(APP_ID, mContext);
		if (mTencent!=null && mTencent.isSessionValid()) {
			mTencent.logout(mContext);
		}
	}

	/** 分享给好友 */
	public void share(Bundle bundle){
		mTencent = Tencent.createInstance(APP_ID, mContext);
		mTencent.shareToQQ(mContext, bundle, new IUiListener() {
			@Override
			public void onError(UiError arg0) {
			}
			@Override
			public void onComplete(Object arg0) {
				Toast.makeText(mContext, mContext.getString(R.string.errcode_success), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancel() {
				Toast.makeText(mContext, mContext.getString(R.string.errcode_cancel),Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void getUserInfo(){
		UserInfo mInfo = new UserInfo(mContext, mTencent.getQQToken());
		mInfo.getUserInfo(new UserInfoUiListener());
	}

	private class UserInfoUiListener implements IUiListener {
		protected void doComplete(Object response) {
			if (null == response) {
                Log.d(TAG,"返回为空");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Log.d(TAG,"获取失败");
                return;
            }
            Log.d(TAG, "用户信息："+jsonResponse.toString());
            /**
            {
            	  "is_yellow_year_vip": "0",
            	  "ret": 0,
            	  "figureurl_qq_1": "http://q.qlogo.cn/qqapp/101131188/06C68144C8C23302154EB63C3BFFFBFA/40",
            	  "figureurl_qq_2": "http://q.qlogo.cn/qqapp/101131188/06C68144C8C23302154EB63C3BFFFBFA/100",
            	  "nickname": "动漫之家",
            	  "yellow_vip_level": "0",
            	  "is_lost": 0,
            	  "msg": "",
            	  "city": "朝阳",
            	  "figureurl_1": "http://qzapp.qlogo.cn/qzapp/101131188/06C68144C8C23302154EB63C3BFFFBFA/50",
            	  "vip": "0",
            	  "level": "0",
            	  "figureurl_2": "http://qzapp.qlogo.cn/qzapp/101131188/06C68144C8C23302154EB63C3BFFFBFA/100",
            	  "province": "北京",
            	  "is_yellow_vip": "0",
            	  "gender": "男",
            	  "figureurl": "http://qzapp.qlogo.cn/qzapp/101131188/06C68144C8C23302154EB63C3BFFFBFA/30"
            	}
            **/
		}

		@Override
		public void onError(UiError e) {
			Log.d(TAG,"onError(UiError e)");
		}

		@Override
		public void onCancel() {
			Log.d(TAG," onCancel()");
		}

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject)response);
		}
	}

	public Tencent getmTencent() {
		return mTencent;
	}

	public void setmTencent(Tencent mTencent) {
		this.mTencent = mTencent;
	}


}
