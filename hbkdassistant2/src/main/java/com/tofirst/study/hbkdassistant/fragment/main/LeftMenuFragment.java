package com.tofirst.study.hbkdassistant.fragment.main;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.SettingActivity;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;

/**
 * 侧边栏的FragMent
 */
public class LeftMenuFragment extends MainBaseFragment implements View.OnClickListener {

    private View leftMenu;
    private TextView mTv_nightmode;
    private TextView mTv_setting;
    private LinearLayout ll_left_menu;
    private boolean isLight;

    @Override
    public View initViews() {
        leftMenu = View.inflate(mActivity, R.layout.item_left_menu, null);
        mTv_setting = (TextView) leftMenu.findViewById(R.id.tv_setting);
        mTv_nightmode = (TextView) leftMenu.findViewById(R.id.tv_nightmode);
        ll_left_menu = (LinearLayout) leftMenu.findViewById(R.id.ll_left_menu);
        mTv_nightmode.setOnClickListener(this);
        mTv_setting.setOnClickListener(this);
        return leftMenu;
    }

    @Override
    public void initData() {
        super.initData();
        //设置主题样式
        isLight = SharePreUtils.getsPreBoolean(mActivity, "isLight", true);
        mTv_nightmode.setText(isLight ? "日间模式":"夜间模式");
        setLeftMenuBackGround(isLight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:
                mActivity.startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.tv_nightmode:
                isLight=!isLight;
                mTv_nightmode.setText(isLight?"日间模式":"夜间模式");
                SharePreUtils.putPreBoolean(mActivity,"isLight",true);
                setLeftMenuBackGround(isLight);
                MainActivity MainUI= (MainActivity) mActivity;
                MainUI.setLightorNight(isLight);
                break;
            default:

                break;
        }
    }
    /**设置底部导航栏的夜间和日间模式*/
    public  void setLeftMenuBackGround(boolean isLight){
        ll_left_menu.setBackgroundResource(isLight ? R.color.light_toolbar : R.color.dark_toolbar);
    }

}
