package com.tofirst.study.hbkdassistant.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 请求父控件不要拦截的viewpager
 */
public class NoHorizontalNesDetailViewPager extends ViewPager {
    public NoHorizontalNesDetailViewPager(Context context) {
        super(context);
    }

    public NoHorizontalNesDetailViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 第一页需要拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);//请求不拦截
        return super.dispatchTouchEvent(ev);
    }
}
