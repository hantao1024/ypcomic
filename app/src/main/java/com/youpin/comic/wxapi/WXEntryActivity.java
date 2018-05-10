package com.youpin.comic.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youpin.comic.R;
import com.youpin.comic.openapi.OpenApiTool;
import com.youpin.comic.openapi.WixinChatApi;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	public static final String APP_ID = "wxe62b4f74c0e08999" ;
	public static final String APP_SECRET = "0d1229419ebd9d9559d940621e6e4eae" ;
	
	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, false);
	}
	
	@Override
	public void onReq(BaseReq arg0) {
	}
	
	@Override
	public void onResp(BaseResp resp) {
		
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			
			result = R.string.errcode_success;
			
			if (resp instanceof SendAuth.Resp) {
				SendAuth.Resp rp = (SendAuth.Resp) resp ; 
				if (rp!=null  && rp.code!=null) {
					//获取code
					String code = rp.code ; 
					result = R.string.errcode_logging;
					sendBroadcast(WixinChatApi.getSendAuthCodeIntent(code)) ;
				}
			}
			
			OpenApiTool.sendSuccessBroadCast(WXEntryActivity.this) ;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			OpenApiTool.sendCancelBroadCast(WXEntryActivity.this); 
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			OpenApiTool.sendErrorBroadCast(WXEntryActivity.this) ; 
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		finish();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Toast.makeText(this, "onNewIntent in CallBack Activity", Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		api.handleIntent(getIntent(), this);
	}
	
	
	
}
