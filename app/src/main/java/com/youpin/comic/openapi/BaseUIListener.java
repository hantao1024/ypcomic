package com.youpin.comic.openapi;


public class BaseUIListener {
//		implements IUiListener {
//	private Context mContext;
//	private String mScope;
//	private boolean mIsCaneled;
//	private static final int ON_COMPLETE = 0;
//	private static final int ON_ERROR = 1;
//	private static final int ON_CANCEL = 2;
//	private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//            case ON_COMPLETE:
//                JSONObject response = (JSONObject)msg.obj;
//                break;
//            case ON_ERROR:
//                UiError e = (UiError)msg.obj;
//                break;
//            case ON_CANCEL:
//                break;
//            }
//        }
//	};
//
//	public BaseUIListener(Context mContext) {
//		super();
//		this.mContext = mContext;
//	}
//
//
//	public BaseUIListener(Context mContext, String mScope) {
//		super();
//		this.mContext = mContext;
//		this.mScope = mScope;
//	}
//
//	public void cancel() {
//		mIsCaneled = true;
//	}
//
//
//	@Override
//	public void onComplete(Object response) {
//		if (mIsCaneled) return;
//	    Message msg = mHandler.obtainMessage();
//	    msg.what = ON_COMPLETE;
//	    msg.obj = response;
//	    mHandler.sendMessage(msg);
//	}
//
//	@Override
//	public void onError(UiError e) {
//		if (mIsCaneled) return;
//	    Message msg = mHandler.obtainMessage();
//        msg.what = ON_ERROR;
//        msg.obj = e;
//        mHandler.sendMessage(msg);
//	}
//
//	@Override
//	public void onCancel() {
//		if (mIsCaneled) return;
//	    Message msg = mHandler.obtainMessage();
//        msg.what = ON_CANCEL;
//        mHandler.sendMessage(msg);
//	}
//
//	public Context getmContext() {
//		return mContext;
//	}
//
//	public void setmContext(Context mContext) {
//		this.mContext = mContext;
//	}

}
