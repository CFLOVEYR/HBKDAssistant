package com.tofirst.study.hbkdassistant.base;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

/**
 * 内容的基类
 */
public class BasePaper {
    public AppCompatActivity mActivity;
    public View mRootView;
    public FrameLayout fl_base_content;
    public TextView textView;

    public BasePaper(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
        initViews();
    }


    /**
     * 初始化组件
     */
    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.layout_base_paper, null);
        fl_base_content = (FrameLayout) mRootView.findViewById(R.id.fl_base_content);//内容
        textView = new TextView(mActivity);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(UIUtils.px2dip(20));
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }


}
