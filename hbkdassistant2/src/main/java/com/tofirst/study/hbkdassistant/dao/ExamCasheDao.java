package com.tofirst.study.hbkdassistant.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tofirst.study.hbkdassistant.db.ExamShareCacheDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * =========================================================
 * <p/>
 * 版权: 个人开发Mr.Jalen  版权所有(c) 2016
 * <p/>
 * 作者:Jalen
 * <p/>
 * 版本: 1.0
 * <p/>
 * <p/>
 * 创建日期 : 2016/1/22  10:59
 * <p/>
 * <p/>
 * 邮箱：Studylifetime@sina.com
 * <p/>
 * 描述:
 * <p/>
 * <p/>
 * 修订历史:
 * <p/>
 * =========================================================
 */
public class ExamCasheDao {
    private final ExamShareCacheDbHelper cacheDbHelper;
    Context context;
    private final Uri uri;

    public ExamCasheDao(Context context) {
        this.context = context;
        cacheDbHelper = new ExamShareCacheDbHelper(context, "examcashe.db", null, 1);
        uri = Uri.parse("content://com.tofirst.study.hbkdassistant.examchange");
    }

    /**
     * 添加数据
     *
     * @param mExamData
     * @return
     */
    public boolean insertAdd(Map<String, String> mExamData) {
        context.getContentResolver().notifyChange(uri, null);
//        UIUtils.showToastSafe("数据为" + mExamData.get("name"));
        SQLiteDatabase db = cacheDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", mExamData.get("name"));
        values.put("subject", mExamData.get("subject"));
        values.put("experience", mExamData.get("experience"));
        long insert = db.insert("ExamAddCache", null, values);
        //关闭流
        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }
    /**
     * 添加数据
     *
     * @param mExamData
     * @return
     */
    public boolean insertRequire(Map<String, String> mExamData) {
        context.getContentResolver().notifyChange(uri, null);
//        UIUtils.showToastSafe("数据为" + mExamData.get("name"));
        SQLiteDatabase db = cacheDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", mExamData.get("name"));
        values.put("subject", mExamData.get("subject"));
        values.put("require", mExamData.get("require"));
        long insert = db.insert("ExamRequireCache", null, values);
        //关闭流
        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }
    /**
     * 查询所有分享经验的数据
     *
     * @return
     */
    public List<Map<String, String>> queryAllAdd() {
//        context.getContentResolver().notifyChange(uri, null);
        List<Map<String, String>> listData = new ArrayList<>();
        SQLiteDatabase db = cacheDbHelper.getWritableDatabase();
        Cursor cursor = db.query("ExamAddCache", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String subject = cursor.getString(cursor.getColumnIndex("subject"));
            String experience = cursor.getString(cursor.getColumnIndex("experience"));
            Map<String, String> mExamData = new HashMap<>();
            mExamData.put("name", name);
            mExamData.put("subject", subject);
            mExamData.put("experience", experience);
            listData.add(mExamData);
        }
//        UIUtils.showToastSafe("查询到的数据为"+listData.toString());
        cursor.close();
        db.close();
        return listData;
    }

    /**
     * 查询所有分享经验的数据
     *
     * @return
     */
    public List<Map<String, String>> queryAllRequire() {
//        context.getContentResolver().notifyChange(uri, null);
        List<Map<String, String>> listData = new ArrayList<>();
        SQLiteDatabase db = cacheDbHelper.getWritableDatabase();
        Cursor cursor = db.query("ExamRequireCache", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String subject = cursor.getString(cursor.getColumnIndex("subject"));
            String experience = cursor.getString(cursor.getColumnIndex("require"));
            Map<String, String> mExamData = new HashMap<>();
            mExamData.put("name", name);
            mExamData.put("subject", subject);
            mExamData.put("require", experience);
            listData.add(mExamData);
        }
//        UIUtils.showToastSafe("查询到的数据为"+listData.toString());
        cursor.close();
        db.close();
        return listData;
    }

}
