package com.youpin.comic.base;

import android.app.Activity;
import android.os.Handler;

/**
 * 漫画类的Adapter使用此基Adapter,抽象出一些漫画常用的常量等,方便使用
 * @author liuguoyan
 * @param <D>
 */
public abstract class CommicBaseAdapter<D> extends KDBaseAdapter<D> {
	
	/** 当漫画被点击,要查看某漫画时**/
	public static final int MSG_WHAT_ONCOMMIC_CONFIRM= 0x01112;
	
	/** 当小说被点击,要查看某小说时 **/
	public static final int MSG_WHAT_ONNOVEL_CONFIRM= 0x01116;
	
	/** 当专题被点击,要查看某漫画时 **/
	public static final int MSG_WHAT_SPECIAL_CONFIRM= 0x01113;
	
	/** 漫画id */
	public static final String MSG_BUNDLE_KEY_COMMIC_ID= "msg_bundle_key_commic_id";
	
	/** 漫画title */
	public static final String MSG_BUNDLE_KEY_COMMIC_TITLE= "msg_bundle_key_commic_title";
	
	/** 专题id */
	public static final String MSG_BUNDLE_KEY_SPECIAL_ID= "msg_bundle_key_special_id";
	
	/** 专题title */
	public static final String MSG_BUNDLE_KEY_SPECIAL_TITLE= "msg_bundle_key_special_title";
	
	/** 小说id */
	public static final String MSG_BUNDLE_KEY_NOVEL_ID= "msg_bundle_key_novel_id";
	
	/** 小说title */
	public static final String MSG_BUNDLE_KEY_NOVEL_TITLE= "msg_bundle_key_novel_title";
	
	
	
	public CommicBaseAdapter(Activity mActivity) {
		super(mActivity);
	}
	
	public CommicBaseAdapter(Activity mActivity, Handler handler) {
		super(mActivity, handler);
	}
	

	
	
}
