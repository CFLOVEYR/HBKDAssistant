package com.tofirst.study.hbkdassistant.acitvity.login;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.BaseActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.domain.Person;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;

import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {
    public EditText username, psd, psd_confirm, email, phone;
    private boolean isLight;
    private Toolbar toolbar;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        setTitle("注册");
        initViews();
        initToolBar();
    }

    private void initViews() {
        username = (EditText) findViewById(R.id.et_register_user);
        psd = (EditText) findViewById(R.id.et_register_psd);
        psd_confirm = (EditText) findViewById(R.id.et_register_psd_confirm);
        email = (EditText) findViewById(R.id.et_register_email);
        phone = (EditText) findViewById(R.id.et_register_phone_number);
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
     * 注册的界面方法
     *
     * @param view
     */
    public void register(View view) {
        String sname = username.getText().toString();
        String spsd = psd.getText().toString();
        String spsd_confirm = psd_confirm.getText().toString();
        String semail = email.getText().toString();
        String sphone = phone.getText().toString();
        if (TextUtils.isEmpty(sname) || TextUtils.isEmpty(spsd) || TextUtils.isEmpty(spsd_confirm)) {
            Toast.makeText(RegisterActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (!spsd.equals(spsd_confirm)) {
                Toast.makeText(RegisterActivity.this, "两次输入密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
            } else {
                //开始注册
                Person bomb=new Person();
                bomb.setUsername(sname);
                bomb.setPassword(spsd);
                bomb.setLevel(1000);
                bomb.setGrade(11);
                if (!TextUtils.isEmpty(semail)) {
                    bomb.setEmail(semail);
                }
                if (!TextUtils.isEmpty(sphone)) {
                    bomb.setMobilePhoneNumber(sphone);
                }
                //注意：不能用save方法进行注册
                bomb.signUp(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        //跳入到主界面
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(RegisterActivity.this, "注册失败" + msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}
