package com.youpin.comic.base;

import android.os.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有实体bean的基类,所有的实体bean都要继承于此类
 * @author hantao
 */
public class BaseBean {
	
	protected Object mTag;
	
	public void setTag(final Object tag) {
		mTag = tag;
	}
	
	public Object getTag(){
		return mTag;
	}
	
	protected Map<Integer, Object> mTags;
	
	public void setTag(int key,Object tag){
		if (mTags==null) {
			mTags = new HashMap<Integer, Object>();
		}
		mTags.put(Integer.valueOf(key), tag);
	}
	
	public Object getTag(int key){
		if (mTags==null) {
			return null;
		}
		if (!mTags.containsKey(Integer.valueOf(key))) {
			return null;
		}
		return mTags.get(Integer.valueOf(key));
	}
	
	public static void superCreateFromParcel(BaseBean bean , Parcel in){
		bean.mTag = in.readValue(Object.class.getClassLoader());
		bean.mTags = in.readHashMap(HashMap.class.getClassLoader());
	}
	
	public void superWriteToParcel(Parcel dest, int flags){
		dest.writeValue(mTag);
		dest.writeMap(mTags);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
