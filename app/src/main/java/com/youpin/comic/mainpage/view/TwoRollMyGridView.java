package com.youpin.comic.mainpage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by hantao on 2018/5/17.
 */

public class TwoRollMyGridView extends GridView {

    public TwoRollMyGridView(Context context) {
        super(context);
    }

    public TwoRollMyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoRollMyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO 自动生成的构造函数存根
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO 自动生成的方法存根
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
