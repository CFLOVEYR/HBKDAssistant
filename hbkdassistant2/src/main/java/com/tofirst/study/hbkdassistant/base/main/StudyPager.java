package com.tofirst.study.hbkdassistant.base.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.ExamShareActivity;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

/**
 * 学习
 */
public class StudyPager extends BasePaper {

    private View studyMainUI;
    private TextView tv_study_share;

    public StudyPager(AppCompatActivity mActivity) {
        super(mActivity);
    }
    @Override
    public void initViews() {
        super.initViews();

        studyMainUI = View.inflate(mActivity, R.layout.study_main, null);

        tv_study_share = (TextView) studyMainUI.findViewById(R.id.tv_study_share);

        tv_study_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              UIUtils.startActivity(new Intent(mActivity, ExamShareActivity.class));
            }
        });
        fl_base_content.addView(studyMainUI);
    }
    @Override
    public void initData() {
        super.initData();
    }
}
