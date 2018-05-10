package com.youpin.comic.publicholder;

import android.widget.TextView;

import com.youpin.comic.publicviews.EventImageView;
import com.youpin.comic.publicviews.LoadImageView;
import com.youpin.comic.publicviews.MyViewPager;
import com.youpin.comic.publicviews.PhotoView;


/**
 * 用来预加载图片信息,非系统的图片加载机制
 * @author liuguoyan
 */
public class ViewPackerHelper {
	
	public static class ViewHolder extends CartoonReadHolder{
		
		public EventImageView imageView;
		
		public TextView txt_terminal;
		
		@Override
		public LoadImageView getLoadImageView() {
			return imageView;
		} 
		
	}
	
	public static class PagerViewHolder extends CartoonReadHolder{
		
		public PhotoView imageView  ;
		
		public MyViewPager viewpager  ;
		
		@Override
		public LoadImageView getLoadImageView() {
			return imageView;
		}
		
	}
	
	
}
