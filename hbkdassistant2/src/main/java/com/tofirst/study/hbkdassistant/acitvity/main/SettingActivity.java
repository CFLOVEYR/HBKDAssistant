package com.tofirst.study.hbkdassistant.acitvity.main;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.feedback.FeedActivity;
import com.tofirst.study.hbkdassistant.acitvity.login.LoginActivity;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.ToastUtils;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity {
    private LinearLayout ll_personal_info;
    private LinearLayout ll_personal_collect;
    private LinearLayout ll_personal_remark;
    private Button bt_setting_sign_out;
    private Button bt_setting_sign_out1;
    private LinearLayout ll_about_author;
    private LinearLayout ll_share_friend;
    private LinearLayout ll_check_version;
    private LinearLayout ll_feedback;
    private LinearLayout ll_app_recommond;
    private LinearLayout ll_message_setting;
    private LinearLayout ll_photo_setting;
    private LinearLayout ll_auto_setting;
    private boolean isLight;
    private Toolbar toolbar;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_main);
        setTitle("设置");
        initViews();
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

    private void initViews() {
        ll_personal_info = (LinearLayout) findViewById(R.id.ll_personal_info);//个人信息
        ll_personal_collect = (LinearLayout) findViewById(R.id.ll_personal_collect);//个人收藏
        ll_personal_remark = (LinearLayout) findViewById(R.id.ll_personal_remark);//个人评论
        ll_auto_setting = (LinearLayout) findViewById(R.id.ll_auto_setting);//自动跟新设置
        ll_photo_setting = (LinearLayout) findViewById(R.id.ll_photo_setting);//图片加载
        ll_message_setting = (LinearLayout) findViewById(R.id.ll_message_setting);//消息提醒设置
        ll_app_recommond = (LinearLayout) findViewById(R.id.ll_app_recommond);//应用推荐
        ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);//意见反馈
        ll_check_version = (LinearLayout) findViewById(R.id.ll_check_version);//检查版本更新
        ll_share_friend = (LinearLayout) findViewById(R.id.ll_share_friend);//分享给朋友
        ll_about_author = (LinearLayout) findViewById(R.id.ll_about_author);//关于作者
        bt_setting_sign_out1 = (Button) findViewById(R.id.bt_setting_sign_out);//销毁退出
/**
 *
 * 不需要判断是否要进入登录界面的模块
 */
        //自动更新设置
        ll_auto_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(SettingActivity.this, "自动更新设置");
            }
        });
        //应用推荐
        ll_app_recommond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(SettingActivity.this, "应用推荐");
            }
        });
        //检查版本更新
        ll_check_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(SettingActivity.this, "检查版本更新");
            }
        });
        //关于作者
        ll_about_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(SettingActivity.this, "关于作者");
            }
        });
        /**
         *  判读是否要进入登录页面的逻辑的模块
         */
        //个人信息
        ll_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    ToastUtils.showToast(SettingActivity.this, "即将进入个人信息");


                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
        //个人评论
        ll_personal_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    ToastUtils.showToast(SettingActivity.this, "即将进入个人评论");
                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
        //个人收藏
        ll_personal_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    ToastUtils.showToast(SettingActivity.this, "即将进入个人收藏");
                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
        //图片加载设置
        ll_photo_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    ToastUtils.showToast(SettingActivity.this, "即将进入图片加载设置");
                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
        //消息提醒设置
        ll_message_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    ToastUtils.showToast(SettingActivity.this, "即将进入消息提醒设置");
                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
        //反馈意见
        ll_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
//                    ToastUtils.showToast(SettingActivity.this, "即将进入反馈意见");
                   startActivity(new Intent(SettingActivity.this, FeedActivity.class));
                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
        //分享给朋友
        ll_share_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    ToastUtils.showToast(SettingActivity.this, "即将进入分享给朋友");
                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
        /**
         * 退出登录,注销的方法
         */
        bt_setting_sign_out1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut(SettingActivity.this);   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(SettingActivity.this);
            }
        });
    }
}