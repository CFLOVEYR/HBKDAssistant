package com.tofirst.study.hbkdassistant.utils.common;

import android.content.Context;
import android.widget.Toast;

/**
 * 弹出框的工具类
 */
public class ToastUtils {
    /**
     * 弹出框的方法
     * @param context
     * @param message
     */
    public static void showToast(Context context,String message){
        //弹出框的简单逻辑
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
