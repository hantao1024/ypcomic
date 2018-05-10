package com.youpin.comic.publicviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youpin.comic.R;

import java.util.List;



public class ViewPagerIndicator extends LinearLayout {
    // tab数量
    private int mTabVisibleCount;
    // tab上的内容
    private List<String> mTabTitles;
    // 与之绑定的ViewPager
    public ViewPager mViewPager;
    // 标题正常时的颜色
    private static final int COLOR_TEXT_NORMAL = 0xFF080808;
    //标题选中时的颜色
    private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFF579cf4;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    private int padding = 30;//显示的padding
    Context context;

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mTabVisibleCount = 4;
    }

    //设置可见的tab的数量
    public void setVisibleTabCount(int count) {
        this.mTabVisibleCount = count;
    }

    //设置tab的标题内容 可选，生成textview加入布局，灵活处理
    public void setTabItemTitles(List<String> datas) {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (datas != null && datas.size() > 0) {
            this.removeAllViews();
            this.mTabTitles = datas;
            for (int i = 0; mTabTitles.size() > i; i++) {
                addView(generateTextView(mTabTitles.get(i)));
            }
            // 设置item的click事件
            setItemClickEvent();
        }

    }

    //根据标题生成我们的TextView
    private View generateTextView(String text) {
        RelativeLayout view = new RelativeLayout(context);
        TextView textView = new TextView(context);
        textView.setTextColor(COLOR_TEXT_NORMAL);
        textView.setTextSize(17);
        textView.setId(R.id.id01);
//        textView.setMinWidth(30);
        textView.setPadding(padding, 0, padding, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.addView(textView, params);

        ImageView cursor = new ImageView(context);
        cursor.setScaleType(ImageView.ScaleType.CENTER);
        cursor.setId(R.id.id03);
        cursor.setPadding(0,0,0,12);
        RelativeLayout.LayoutParams cursorparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        cursorparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        cursorparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        cursorparams.setMargins(0, 0, 0, 0);
        view.addView(cursor, cursorparams);
        return view;
    }
    /**
     * 获取资源像素值
     *
     * @param resId
     *            资源id
     * @return 像素值
     */
    public int getDimensionPixel(int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }
    // 设置关联的ViewPager,以及传入 BounceScrollView，进行设置滚动
    public void setViewPager(ViewPager mViewPager, final BounceScrollView scrollView, int pos) {
        this.mViewPager = mViewPager;

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 设置字体颜色高亮
                resetTextViewColor();
                highLightTextView(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // 滚动
                scroll(scrollView, position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 设置当前页
        mViewPager.setCurrentItem(pos);
        // 高亮
        highLightTextView(pos);
    }

    //高亮文本
    protected void highLightTextView(int position) {
        View view = getChildAt(position);
        if (null != view) {
            TextView t = (TextView) view.findViewById(R.id.id01);
            t.setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
            ImageView c = (ImageView) view.findViewById(R.id.id03);
            c.setImageResource(R.drawable.img_triangle);
        }
    }

    //重置文本颜色
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (null != view) {
                TextView t = (TextView) view.findViewById(R.id.id01);
                t.setTextColor(COLOR_TEXT_NORMAL);
                ImageView c = (ImageView) view.findViewById(R.id.id03);
                c.setImageResource(0);
            }
        }
    }

    // 设置点击事件
    public void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    //指示器跟随手指滚动，以及容器滚动
    public void scroll(BounceScrollView scrollView, int position, float offset) {
        int tabWidth = getScreenWidth() / mTabVisibleCount;

        // 容器滚动，当移动到倒数最后一个的时候，开始滚动
        if (position >= (mTabVisibleCount - 1) && getChildCount() > mTabVisibleCount) {

            if (mTabVisibleCount != 1) {
                scrollView.setScrolledTo((position - 1) * tabWidth + (int) (tabWidth * offset), 0);
            } else
            // 为count为1时 的特殊处理
            {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        //处理特殊情况
        else if (position <= mTabVisibleCount - 1) {
            scrollView.setScrolledTo(0, 0);
        }

    }

    //设置布局中view的一些必要属性；如果设置了setTabTitles，布局中view则无效
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();

        if (cCount == 0)
            return;

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view
                    .getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(lp);
        }
        // 设置点击事件
        setItemClickEvent();

    }

    //获得屏幕的宽度
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        //获取是整个屏幕的宽度，我试过自定义宽度测量宽度，不行。因为本身还没内容，是后面添加的。所以需要后面加东西
        //需要这里减去宽度就行。其中这里减去120就是60dp的宽度
        return outMetrics.widthPixels - 120;
    }

}
