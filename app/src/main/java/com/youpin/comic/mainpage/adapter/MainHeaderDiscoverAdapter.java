package com.youpin.comic.mainpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youpin.comic.R;
import com.youpin.comic.base.KDBaseAdapter;
import com.youpin.comic.mainpage.bean.User;

import java.util.List;


/**
 * Created by hantao on 2018/5/17.
 */
public class MainHeaderDiscoverAdapter extends KDBaseAdapter<User> {

    private ItemListner itemListner;
    public interface ItemListner {
        public void Click(User user);

        public void Delete(User user);
    }

    public void setItemListner(ItemListner itemListner) {
        this.itemListner = itemListner;
    }

    public MainHeaderDiscoverAdapter(Context context) {
        super(context);
    }

    public MainHeaderDiscoverAdapter(Context context, List<User> list) {
        super(context, list);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_main_discover_header_listview, null);
        }
        ViewHolder holder = getViewHolder(convertView);
        if (getDaList()==null) {
            return convertView;
        }
        final User user = getDaList().get(position);
//        holder.tv_user_name.setText(user.getName());
//        getBitmap(holder.iv_photo, user.getUrl());
        holder.tv_item_discover_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListner != null && user != null) {
                    user.setName("张三");
                    itemListner.Click(user);
                }
            }
        });
        return convertView;
    }

    /**
     * 获得控件管理对象
     *
     * @param view
     * @return
     */
    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    /**
     * 控件管理类
     */
    private class ViewHolder {
        private TextView   tv_item_discover_title;
        private ImageView iv_discover_img_show;

        ViewHolder(View view) {
            tv_item_discover_title = (TextView) view.findViewById(R.id.tv_item_discover_title);
        }
    }
}
