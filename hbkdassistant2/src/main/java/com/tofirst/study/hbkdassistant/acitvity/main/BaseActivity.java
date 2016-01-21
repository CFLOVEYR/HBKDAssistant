package com.tofirst.study.hbkdassistant.acitvity.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tofirst.study.hbkdassistant.R;

public abstract class BaseActivity extends AppCompatActivity {

    private static BaseActivity mForegroundActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.mForegroundActivity = this;
    }


    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }
}
