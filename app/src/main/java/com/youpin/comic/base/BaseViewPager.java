package com.youpin.comic.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hantao on 2018/5/2.
 */

public class BaseViewPager extends ViewPager {
    //是否可以进行滑动
    private boolean isSlide = false;
    private float mPointX;
    private float mPointY;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPointX = ev.getX();
//                mPointY = ev.getY();
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (Math.abs(ev.getX() - mPointX) > Math.abs(ev.getY() - mPointY)) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//
//            default:
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//
//    }
}
