package com.tofirst.study.hbkdassistant.base.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.login.LoginActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.SettingActivity;
import com.tofirst.study.hbkdassistant.acitvity.login.UserInfoActivity;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的模块
 */
public class MinePager extends BasePaper implements View.OnClickListener {

    private View mineView;
    private CircleImageView iv_circle_mine_pic;
    private LinearLayout ll_mine_personal_info;
    private LinearLayout ll_mine_personal_collect;
    private LinearLayout ll_mine_personal_remark;
    private LinearLayout ll_mine_setting;
    private BmobUser bmobUser;

    public MinePager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
        mineView = View.inflate(mActivity, R.layout.mine_main, null);
        initView();
        fl_base_content.addView(mineView);
    }


    //初始化组件
    private void initView() {
        iv_circle_mine_pic = (CircleImageView) mineView.findViewById(R.id.iv_circle_mine_pic);
        ll_mine_personal_info = (LinearLayout) mineView.findViewById(R.id.ll_mine_personal_info);
        ll_mine_personal_collect = (LinearLayout) mineView.findViewById(R.id.ll_mine_personal_collect);
        ll_mine_personal_remark = (LinearLayout) mineView.findViewById(R.id.ll_mine_personal_remark);
        ll_mine_setting = (LinearLayout) mineView.findViewById(R.id.ll_mine_setting);

        iv_circle_mine_pic.setOnClickListener(this);
        ll_mine_personal_info.setOnClickListener(this);
        ll_mine_personal_collect.setOnClickListener(this);
        ll_mine_personal_remark.setOnClickListener(this);
        ll_mine_setting.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_circle_mine_pic:
                UIUtils.showToastSafe("即将进入图片选择页面");
                break;
            case R.id.ll_mine_personal_info:

                bmobUser = BmobUser.getCurrentUser(mActivity);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
//                    UIUtils.showToastSafe("即将进入个人信息页面");
                    UIUtils.startActivity(new Intent(mActivity, UserInfoActivity.class));

                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    UIUtils.startActivity(new Intent(mActivity, LoginActivity.class));
                }
                break;
            case R.id.ll_mine_personal_collect:
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    UIUtils.showToastSafe("即将进入个人分享页面");
                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                }
                break;
            case R.id.ll_mine_personal_remark:
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    UIUtils.showToastSafe("即将进入个人评论页面");

                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                }
                break;
            case R.id.ll_mine_setting:
                UIUtils.startActivity(new Intent(mActivity, SettingActivity.class));
                break;

            default:

                break;

        }
    }
}
