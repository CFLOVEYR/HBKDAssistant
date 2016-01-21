package com.tofirst.study.hbkdassistant.acitvity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.tofirst.study.hbkdassistant.inteface.MyProgressCallBack;
import com.tofirst.study.hbkdassistant.utils.common.LogUtils;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.tofirst.study.hbkdassistant.utils.common.VersionUtils;
import com.tofirst.study.hbkdassistant.utils.common.parsesJsonDataUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.XUtil;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private AnimationSet set;
    private boolean UpdateFlag = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    break;
                default:

                    break;
            }
        }
    };
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
     * 检查版本更新
     */
    private void checkVersion() {
        /**
         * 是否设置不自动检查更新
         */
        if (!SharePreUtils.getsPreBoolean(this, "UpdateFlag", false)) {
            int versionCode_local = VersionUtils.getVersionCode(this);
            int versionCode_server = parsesJsonDataUtils.getVersionCode(this, ServiceURL.FirstJsonURL);
            //如果本地版本小于服务器版本的话
            if (versionCode_local < versionCode_server) {
                UpdateFlag = true;
            }
        }
    }

    /**
     * 展示是否更新的对话框
     */
    private void showUpdateDialog() {
        enterDialogFlag = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本: " + parsesJsonDataUtils.getVersionName(this, ServiceURL.FirstJsonURL));
        String message = parsesJsonDataUtils.getVersionDescrpition(this, ServiceURL.FirstJsonURL);
        builder.setMessage(message);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterNext();
            }
        });
        /**
         * 取消对话框的操作
         */
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterNext();
            }
        });
        builder.show();
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
        XUtil.GetJson(ServiceURL.FirstJsonURL,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    SharePreUtils.putPreString(SplashActivity.this, ServiceURL.FirstJsonURL, result);
                    //获取版本名称
                    tv_splash_versionName.setText("版本号:  " + VersionUtils.getVersionName(SplashActivity.this));
                    //检查版本更新
                    checkVersion();
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
                if (UpdateFlag) {
                    showUpdateDialog();
                } else {
                    enterNext();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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


    /**
     * 利用xUtils自带的方法,来实现下载App通用方法
     */
    public void downLoadApk() {
        //获取下载的地址
        String mDownLoadURL = parsesJsonDataUtils.getUpdateURL(this, ServiceURL.FirstJsonURL);
        //文件保存在本地的路径
        String target = Environment.getExternalStorageDirectory()
                + "/hbkdzs.apk";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            XUtil.DownLoadFile(mDownLoadURL, target, new MyProgressCallBack<File>() {

                        @Override
                        public void onSuccess(File result) {
                            UIUtils.showToastSafe("下载成功"+result.getAbsolutePath());
                            // 跳转到系统的下载页面
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setDataAndType(Uri.fromFile(result),
                                    "application/vnd.android.package-archive");
                            startActivityForResult(intent, 0);// 跳转后的监听事件
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            UIUtils.showToastSafe("下载失败");
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isDownloading) {
                            Message msg = handler.obtainMessage();
                            msg.obj = "下载进度" + current * 100 / total + "%";
                            handler.sendMessage(msg);
                            System.out.println("下载进度" + current * 100 / total + "%");
                        }
                    }

            );
        } else {
            Toast.makeText(SplashActivity.this, "Sdcard异常或者不存在",
                    Toast.LENGTH_SHORT).show();
        }

    }

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
}
