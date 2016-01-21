package com.tofirst.study.hbkdassistant.base.newsdetail;

import android.app.Activity;
import android.view.View;

/**
 * 侧边栏控制的菜单详情页的内容的基类
 */
public abstract class BaseLeftMenuPaper {
    public Activity mActivity;
    public final View mRootView;

    public BaseLeftMenuPaper(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initViews();
    }

    /**
     * 初始化组件
     */
    public abstract View initViews();

    /**
     * 初始化数据
     */
    public void initData() {

    }

    public abstract void updateTheme();

}
