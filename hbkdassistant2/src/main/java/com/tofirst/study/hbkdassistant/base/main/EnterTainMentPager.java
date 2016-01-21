package com.tofirst.study.hbkdassistant.base.main;

import android.support.v7.app.AppCompatActivity;

import com.tofirst.study.hbkdassistant.base.BasePaper;

/**
 * 娱乐
 */
public class EnterTainMentPager extends BasePaper {
    public EnterTainMentPager(AppCompatActivity mActivity) {
        super(mActivity);
    }
    @Override
    public void initViews() {
        super.initViews();
        textView.setText("娱乐");
        fl_base_content.addView(textView);
    }
    @Override
    public void initData() {
        super.initData();
    }
}
