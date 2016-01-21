package com.tofirst.study.hbkdassistant.utils.common;
import android.content.Context;
import com.google.gson.Gson;
import com.tofirst.study.hbkdassistant.domain.FirstJson;

/**
 * 解析本地数据的内容
 */
public class parsesJsonDataUtils {
    /**
     * 获得新闻左菜单的数据
     */
    public static String[] getLeftMenuData(Context ctx, String url) {
        String result = SharePreUtils.getsPreString(ctx, url, null);
        Gson gson = new Gson();
        FirstJson firstJson = gson.fromJson(result, FirstJson.class);
        return firstJson.newsleftmenu.split(",");
    }
    /**
     * 获得版本下载地址
     */
    public static String getUpdateURL(Context ctx, String url) {
        String result = SharePreUtils.getsPreString(ctx, url, null);
        Gson gson = new Gson();
        FirstJson firstJson = gson.fromJson(result, FirstJson.class);
        return firstJson.updateURL;
    }
    /**
     * 获得版本号
     */
    public static int getVersionCode(Context ctx, String url) {
        String result = SharePreUtils.getsPreString(ctx, url, null);
        Gson gson = new Gson();
        FirstJson firstJson = gson.fromJson(result, FirstJson.class);
        return firstJson.versionCode;
    }
    /**
     * 获得版本名
     */
    public static String getVersionName(Context ctx, String url) {
        String result = SharePreUtils.getsPreString(ctx, url, null);
        Gson gson = new Gson();
        FirstJson firstJson = gson.fromJson(result, FirstJson.class);
        return firstJson.versionName;
    }
    /**
     * 获得新版本内容
     */
    public static String getVersionDescrpition(Context ctx, String url) {
        String result = SharePreUtils.getsPreString(ctx, url, null);
        Gson gson = new Gson();
        FirstJson firstJson = gson.fromJson(result, FirstJson.class);
        return firstJson.description;
    }
}
