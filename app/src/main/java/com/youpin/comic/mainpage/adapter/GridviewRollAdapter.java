package com.youpin.comic.mainpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youpin.comic.R;
import com.youpin.comic.base.KDBaseAdapter;
import com.youpin.comic.mainpage.bean.GridviewRollBean;

import java.util.List;


/**
 * Created by hantao on 2018/2/18.
 */
public class GridviewRollAdapter extends KDBaseAdapter<GridviewRollBean> {

    private ItemListner itemListner;
    public interface ItemListner {
        public void Click(GridviewRollBean gridviewRollBean);
    }

    public void setItemListner(ItemListner itemListner) {
        this.itemListner = itemListner;
    }

    public GridviewRollAdapter(Context context) {
        super(context);
    }

    public GridviewRollAdapter(Context context, List<GridviewRollBean> list) {
        super(context, list);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_main_roll_grid_view, null);
        }
        ViewHolder holder = getViewHolder(convertView);
        if (getDaList()==null) {
            return convertView;
        }
        final GridviewRollBean gridviewRollBean = getDaList().get(position);
        holder.tv_name.setText(gridviewRollBean.getName());
        holder.tv_content.setText(gridviewRollBean.getContnet());
        getBitmap(holder.iv_img, gridviewRollBean.getUrl());
        holder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListner != null && gridviewRollBean != null) {
                    itemListner.Click(gridviewRollBean);
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
        private TextView tv_name, tv_content;
        private ImageView iv_img;

        ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
        }
    }
}
