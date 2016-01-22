package com.tofirst.study.hbkdassistant.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.xutils.x;

import java.io.File;

import cn.bmob.v3.Bmob;

public class BaseApplication extends Application {
    //获取到主线程的上下文
    private static BaseApplication mContext;
    //获取到主线程的handler
    private static Handler mMainThreadHanler;
    //获取到主线程
    private static Thread mMainThread;
    //获取到主线程的id
    private static int mMainThreadId;


    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
        this.mContext = this;
        this.mMainThreadHanler = new Handler();
        this.mMainThread = Thread.currentThread();
        //获取到调用线程的id
        this.mMainThreadId = android.os.Process.myTid();
    }

    /**
     * 初始化第三方组件
     */
    private void initSDK() {

        // 初始化 Bmob SDK
        Bmob.initialize(this, "9fcd33170d268436fda52e4685c8ed7d");
        //初始化XUtils3
        x.Ext.init(this);
        //设置debug模式
        x.Ext.setDebug(true);
        initImageLoader(this);
    }

    /**
     * 获取Application
     */
    public static BaseApplication getApplication() {
        return mContext;
    }

    /**
     * 获取主线程的Handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHanler;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }
    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
/*        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir)).writeDebugLogs()
                .build();*/
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }

}
