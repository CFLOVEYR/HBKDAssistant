package com.tofirst.study.hbkdassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * =========================================================
 *
 *  版权: 个人开发Mr.Jalen  版权所有(c) ${YEAR}
 *
 *  作者:${USER}
 *
 *  版本: 1.0
 *
 *
 *  创建日期 : ${DATE}  ${HOUR}:${MINUTE}
 *
 *
 * 邮箱：Studylifetime@sina.com
 *
 * 描述:
 *      用于缓存新闻数据的类
 *
 * 修订历史:
 *
 * =========================================================
 */
public class NewsCacheDbHelper extends SQLiteOpenHelper {
    public NewsCacheDbHelper(Context context, int version) {
        super(context, "cache.db", null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists CacheList (id INTEGER primary key autoincrement,date INTEGER unique,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
