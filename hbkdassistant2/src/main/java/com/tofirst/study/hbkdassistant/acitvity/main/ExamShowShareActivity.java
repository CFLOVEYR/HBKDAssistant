package com.tofirst.study.hbkdassistant.acitvity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;

public class ExamShowShareActivity extends BaseActivity {
    private boolean isLight;
    private Toolbar toolbar;
    private EditText et_exam_add_show_experience;
    private EditText et_exam_add_show_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_show_share);
        initViews();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    private void initViews() {
        et_exam_add_show_subject = (EditText) findViewById(R.id.et_exam_add_show_subject);
        et_exam_add_show_experience = (EditText) findViewById(R.id.et_exam_add_show_experience);
        Intent intent = getIntent();
        String add = intent.getStringExtra("add");
        String[] split = add.split(",");
        String name = split[0];
        String subject = split[1];
        String experience = split[2];
        et_exam_add_show_subject.setText(subject);
        et_exam_add_show_experience.setText(experience);
        initToolBar();
    }

    private void initToolBar() {
        isLight = SharePreUtils.getsPreBoolean(this, "isLight", true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
