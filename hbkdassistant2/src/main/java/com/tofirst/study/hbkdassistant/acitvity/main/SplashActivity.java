package com.tofirst.study.hbkdassistant.acitvity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.global.ServiceURL;
import com.tofirst.study.hbkdassistant.inteface.MyCallBack;
import com.tofirst.study.hbkdassistant.utils.common.LogUtils;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.tofirst.study.hbkdassistant.utils.common.VersionUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.XUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SplashActivity extends AppCompatActivity {

    private AnimationSet set;
    private boolean UpdateFlag = false;
    private boolean enterDialogFlag = false;
    private RelativeLayout rl_spalash;
    private TextView tv_splash_versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_spalash = (RelativeLayout) findViewById(R.id.rl_splash);
        tv_splash_versionName = (TextView) findViewById(R.id.tv_splash_versionName);
        initData();
        //初始化闪屏动画
        initAnimotion();
        initAnimotionListener();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        /**
         * 初始化校园新闻的数据,保存到数据库
         */
        new Thread() {
            @Override
            public void run() {
                super.run();
                //获取数据
                getDataFromServer();
            }
        }.start();
    }

    /**
     * 从服务器得到信息,并保存到本地缓存
     */
    private void getDataFromServer() {
        //get获取Json字符串
        XUtil.GetJson(ServiceURL.FirstJsonURL, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    SharePreUtils.putPreString(SplashActivity.this, ServiceURL.FirstJsonURL, result);
                    //获取版本名称
                    tv_splash_versionName.setText("版本号:  " + VersionUtils.getVersionName(SplashActivity.this));
                    //检查版本更新
//                    checkVersion();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("解析失败");
                Toast.makeText(SplashActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 初始化动画的监听
     */
    private void initAnimotionListener() {
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                initSDK();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initSDK() {

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int status, UpdateResponse updateResponse) {

                if (UmengUpdateAgent.isIgnore(SplashActivity.this, updateResponse)) {
                    enterNext();
                } else {
                    switch (status) {
                        case UpdateStatus.Yes: // has update

                            break;
                        case UpdateStatus.No: // has no update
                            enterNext();
                            break;
                        case UpdateStatus.NoneWifi: // none wifi
                            Toast.makeText(SplashActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            enterNext();
                            break;
                        case UpdateStatus.Timeout: // time out
                            Toast.makeText(SplashActivity.this, "链接超时", Toast.LENGTH_SHORT).show();
                            enterNext();
                            break;
                    }
                }


            }
        });
        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

            @Override
            public void onClick(int status) {
                switch (status) {
                    case UpdateStatus.Update:
                        break;
                    case UpdateStatus.Ignore:
                        enterNext();
                        break;
                    case UpdateStatus.NotNow:
                        enterNext();
                        break;
                }
            }
        });

        //  友盟自动更新
        if (SharePreUtils.getsPreBoolean(this, "AutoUpdateFlag", true)) {
            UmengUpdateAgent.update(this);
        }
        //静默下载更新
        if (SharePreUtils.getsPreBoolean(this, "SilentUpdateFlag", false)) {
            UIUtils.showToastSafe("静默下载更新");
            UmengUpdateAgent.silentUpdate(this);
        }

    }

    /**
     * 进入下一个页面
     */
    private void enterNext() {
        if (SharePreUtils.getsPreBoolean(SplashActivity.this, "GuideFlag", false)) {
            //已经进入过引导页面
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            //没有进入过引导页面
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        }
        finish();
    }

    /**
     * 初始化闪屏页的动画方法
     */
    private void initAnimotion() {
        //设置动画集合
        set = new AnimationSet(false);
        //旋转动画
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);//设置动画结束后保持原状
        //缩放动画
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(1000);
        scale.setFillAfter(true);//设置动画结束后保持原状
        //渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(3000);
        alpha.setFillAfter(true);//设置动画结束后保持原状
        //添加到动画集合中
        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);
        rl_spalash.startAnimation(set);
    }

    //如果不下载,也会跳转到主页面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            enterNext();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                enterNext();
                break;

            default:

                break;
        }
        return super.onKeyDown(keyCode, event);
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
