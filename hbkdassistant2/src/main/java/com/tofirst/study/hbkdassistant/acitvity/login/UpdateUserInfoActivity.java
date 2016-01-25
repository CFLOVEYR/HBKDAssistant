package com.tofirst.study.hbkdassistant.acitvity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.domain.Person;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.view.SlideSwitch;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private EditText et_user_name;
    private EditText et_user_grade;
    //    private EditText et_user_level;
    private EditText et_user_description;
    private EditText et_user_pnumber;
    private EditText et_user_email;
    private SlideSwitch tb_user_pnumber_bd_et;
    private SlideSwitch tb_user_email_bd_et;
    private boolean isLight;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        setTitle("更新个人信息");
        initView();
        initData();
    }


    private void initView() {
        initToolBar();
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_user_grade = (EditText) findViewById(R.id.et_user_grade);
//        et_user_level = (EditText) findViewById(R.id.et_user_level);
        et_user_description = (EditText) findViewById(R.id.et_user_description);
        et_user_pnumber = (EditText) findViewById(R.id.et_user_pnumber);
        et_user_email = (EditText) findViewById(R.id.et_user_email);
        tb_user_pnumber_bd_et = (SlideSwitch) findViewById(R.id.tb_user_pnumber_bd_et);
        tb_user_email_bd_et = (SlideSwitch) findViewById(R.id.tb_user_email_bd_et);

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

    private void initData() {
        //初始化数据
        Person user = BmobUser.getCurrentUser(this, Person.class);
        if (user != null) {
            //判断是否为空
            if (user.getGrade() != null) {
                et_user_grade.setText(user.getGrade() + "");
            }
            //判断是否为空
            if (user.getLevel() != null) {
                Integer level = user.getLevel();
                user.setLevel(level);

            }
            //判断是否为空
            if (user.getDescription() != null) {
                et_user_description.setText(user.getDescription());
            }
            //判断是否为空
            if (user.getUsername() != null) {
                et_user_name.setText(user.getUsername());
            }
            //判断是否为空
            if (user.getEmail() != null) {
                et_user_email.setText(user.getEmail());
            }
            //判断是否为空
            if (user.getMobilePhoneNumber() != null) {
                et_user_pnumber.setText(user.getMobilePhoneNumber());
            }
            //判断是否绑定手机
            if (null!=user.getMobilePhoneNumberVerified()) {
                tb_user_pnumber_bd_et.setState(user.getMobilePhoneNumberVerified());
            }
            //判断是否绑定邮箱
            if (null!=user.getEmailVerified()) {
                tb_user_email_bd_et.setState(user.getEmailVerified());
            }

        }
    }

    /**
     * 更新数据
     *
     * @param view
     */
    public void updateUserInfo(View view) {
        String name = et_user_name.getText().toString();
        String grade = et_user_grade.getText().toString();
//        String level = et_user_level.getText().toString();
        String desc = et_user_description.getText().toString();
        String pnumber = et_user_pnumber.getText().toString();
        String email = et_user_email.getText().toString();
        Person newUser = new Person();
        Person oldUser = BmobUser.getCurrentUser(this, Person.class);
        //更细的信息
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(grade) ||
                TextUtils.isEmpty(desc) || TextUtils.isEmpty(pnumber) || TextUtils.isEmpty(email)) {
            Toast.makeText(UpdateUserInfoActivity.this, "输入内容不能为空,请仔细检查", Toast.LENGTH_SHORT).show();
        } else {
            newUser.setUsername(name);
//            newUser.setLevel(Integer.parseInt(level));
            newUser.setGrade(Integer.parseInt(grade));
            newUser.setMobilePhoneNumber(pnumber);
            newUser.setEmail(email);
            if (tb_user_pnumber_bd_et.isChecked()) {
                newUser.setMobilePhoneNumberVerified(true);
            } else {
                newUser.setMobilePhoneNumberVerified(false);
            }
            if (tb_user_email_bd_et.isChecked()) {
                newUser.setEmailVerified(true);
            } else {
                newUser.setEmailVerified(false);
            }
            newUser.update(this, oldUser.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(UpdateUserInfoActivity.this, "更新数据成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(UpdateUserInfoActivity.this, "更新数据失败" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        }


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
