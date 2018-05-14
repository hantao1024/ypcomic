package com.youpin.comic.mainpage.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youpin.comic.R;
import com.youpin.comic.base.CommicBaseAdapter;
import com.youpin.comic.mainpage.bean.SearchBean;


/**
 * 搜索界面全部搜索的Adapter
 * @author liuguoyan
 */
public class SearchBean4InfoAdapter extends CommicBaseAdapter<SearchBean> {


	/** 圆角图片的默认圆角大小 */
	public static final int DEF_CORNER_RAIDUS = 5 ;
	public static final String TAG = "SearchBean4InfoAdapter" ;
	
	public static final int MSG_WHAT_ONITEM_CHECKED= 0x31;
	public static final String MSG_BUNDEL_KEY_ITEM_ID= "msg_bundel_key_item_id";
	public static final String MSG_BUNDEL_KEY_ITEM_TITLE= "msg_bundel_key_item_title";
	
	public SearchBean4InfoAdapter(Activity mActivity) {
		super(mActivity);
	}
	
	public SearchBean4InfoAdapter(Activity mActivity, Handler handler) {
		super(mActivity, handler);
	}
	
	public static class ViewHolder{
		public ImageView imageview;
		public TextView charpter;
		
		public TextView title;
		public TextView author;
		public TextView classify;
		public TextView latest;
		
		public RelativeLayout layout_main;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null || convertView.getTag() ==null ){
			convertView = View.inflate(getContext(), R.layout.item_searchinfo_list_item,null);
			holder = new ViewHolder();
			
			holder.imageview = (ImageView) convertView.findViewById(R.id.img_main_pic);
			holder.charpter=(TextView) convertView.findViewById(R.id.txt_chapter);
			
			holder.title=(TextView) convertView.findViewById(R.id.txt_title);
			holder.author=(TextView) convertView.findViewById(R.id.txt_author);
			holder.classify=(TextView) convertView.findViewById(R.id.txt_class);
			holder.latest=(TextView) convertView.findViewById(R.id.txt_latests);
			holder.layout_main = (RelativeLayout) convertView.findViewById(R.id.layout_main);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if (getDaList()==null || position >= getDaList().size() ) return convertView ; 
		final SearchBean model=getDaList().get(position);

		getBitmap(holder.imageview, model.getCover(),DEF_CORNER_RAIDUS);
		
		holder.charpter.setText(model.getLast_name());
		holder.title.setText(model.getTitle());
		holder.author.setText(model.getAuthors());
		holder.classify.setText(model.getTypes());
		holder.latest.setText(model.getLast_name());
		
		holder.layout_main.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = Message.obtain();
				msg.what = MSG_WHAT_ONITEM_CHECKED;
				Bundle data = new Bundle();
				data.putString(MSG_BUNDEL_KEY_ITEM_ID, model.getId());
				data.putString(MSG_BUNDEL_KEY_ITEM_TITLE, model.getTitle());
				msg.setData(data);
				getHandler().sendMessage(msg);
			}
		});
		
		return convertView;
	}
}
