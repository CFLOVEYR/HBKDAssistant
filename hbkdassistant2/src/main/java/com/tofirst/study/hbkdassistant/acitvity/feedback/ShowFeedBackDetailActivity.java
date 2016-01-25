package com.tofirst.study.hbkdassistant.acitvity.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.umeng.analytics.MobclickAgent;


public class ShowFeedBackDetailActivity extends AppCompatActivity {

    private TextView tv_feed_title;
    private TextView tv_feed_content;
    private boolean isLight;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feed_back_detail);
        setTitle("反馈信息详情");
        initView();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        tv_feed_title.setText(title);
        tv_feed_content.setText(content);
    }

    private void initView() {
        initToolBar();
        tv_feed_title = (TextView) findViewById(R.id.tv_feed_title);
        tv_feed_content = (TextView) findViewById(R.id.tv_feed_content);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
