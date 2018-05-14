package com.youpin.comic.mainpage.adapter;

import android.app.Activity;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youpin.comic.R;
import com.youpin.comic.base.KDBaseAdapter;
import com.youpin.comic.mainpage.bean.SearchBean;

/**
 * 搜索界面模糊搜索的Adapter
 * @author liuguoyan
 *
 */
public class SearchBean4BriefAdapter extends KDBaseAdapter<SearchBean> {
	
	public static final String TAG = "SearchBean4BriefAdapter" ;
	
	private String textFormat = "<font color='#666666'>%s</font><font color= '#579cf4'>%s</font>";
	
	public SearchBean4BriefAdapter(Activity mActivity) {
		super(mActivity);
	}
	
	public SearchBean4BriefAdapter(Activity mActivity, Handler handler) {
		super(mActivity, handler);
	}
	
	static class ViewHolder{
		TextView title;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final SearchBean model=getDaList().get(position);
		if(convertView == null || convertView.getTag() ==null ){
			convertView = View.inflate(getContext(), R.layout.item_keywordsearch_brief,null);
			holder = new ViewHolder();
			holder.title=(TextView) convertView.findViewById(R.id.txt_brief);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			String latest = getContext().getString(R.string.search_latest)+model.getLast_name();
			String formated = String.format(textFormat, model.getTitle(),latest);
			holder.title.setText(Html.fromHtml(formated));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

}
