package com.tofirst.study.hbkdassistant.utils.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取版本号和版本名字
 */
public class VersionUtils {

    /**
     * 获取版本名字的方法
     */
    public static String getVersionName(Context ctx) {
        String versionName = null;
        PackageManager packageManager = ctx.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    ctx.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本号的方法
     */
    public static int getVersionCode(Context ctx) {
        int versionCode = 0;
        PackageManager packageManager = ctx.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    ctx.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            // System.out.println("--------------"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }
}
