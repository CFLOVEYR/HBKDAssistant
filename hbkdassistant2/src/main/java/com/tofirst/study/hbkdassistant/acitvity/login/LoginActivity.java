package com.tofirst.study.hbkdassistant.acitvity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private EditText et_user, et_psd;
    private boolean isLight;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
        et_user = (EditText) findViewById(R.id.et_user);
        et_psd = (EditText) findViewById(R.id.et_psd);
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
    /**
     * 注册的方法
     *
     * @param view
     */
    public void register(View view) {
        //跳转到注册页面
        enterRegisterPager();
    }

    /**
     * 登录的方法
     *
     * @param view
     */
    public void login(View view) {
        String name = et_user.getText().toString();
        String psd=et_psd.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psd)) {
            Toast.makeText(LoginActivity.this, "账号或者密码不能为空!!么么哒", Toast.LENGTH_SHORT).show();
        } else {
            final BmobUser bu2 = new BmobUser();
            bu2.setUsername(name);
            bu2.setPassword(psd);
            bu2.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //统计登录
                    MobclickAgent.onProfileSignIn(bu2.getObjectId());
                    //跳入到主界面
                    enterHomePager();
                }

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(LoginActivity.this, "登录失败" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /**找回密码*/
    public void pickup(View view){
        enterPickUpPager();
    }
    /**
     * 跳入到主界面
     */
    private void enterHomePager() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
    /**
     * 跳入到注册界面
     */
    private void enterRegisterPager() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    /**
     * 跳入到主界面
     */
    private void enterPickUpPager() {
        startActivity(new Intent(LoginActivity.this, PickUpPSDActivity.class));
        finish();
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
