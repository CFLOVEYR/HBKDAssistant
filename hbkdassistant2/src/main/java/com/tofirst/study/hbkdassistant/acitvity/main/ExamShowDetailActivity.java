package com.tofirst.study.hbkdassistant.acitvity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;

public class ExamShowDetailActivity extends AppCompatActivity {
    private boolean isLight;
    private Toolbar toolbar;
    private EditText et_exam_require_subject;
    private EditText et_exam_require_experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_show_detail);
        initView();
        initToolBar();
    }

    private void initView() {
        et_exam_require_subject = (EditText) findViewById(R.id.et_exam_require_show_subject);
        et_exam_require_experience = (EditText) findViewById(R.id.et_exam_require_show_experience);
        initToolBar();
        Intent intent = getIntent();
        String add = intent.getStringExtra("require");
        String[] split = add.split(",");
        String name = split[0];
        String subject = split[1];
        String require = split[2];
        et_exam_require_subject.setText(subject);
        et_exam_require_experience.setText(require);
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
