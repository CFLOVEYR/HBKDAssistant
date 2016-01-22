package com.tofirst.study.hbkdassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * =========================================================
 * <p/>
 * 版权: 个人开发Mr.Jalen  版权所有(c) ${YEAR}
 * <p/>
 * 作者:${USER}
 * <p/>
 * 版本: 1.0
 * <p/>
 * <p/>
 * 创建日期 : ${DATE}  ${HOUR}:${MINUTE}
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
public class ExamShareCacheDbHelper extends SQLiteOpenHelper {


    public ExamShareCacheDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    String add = "create table if not exists ExamAddCache (id INTEGER primary key autoincrement,name text,subject text,experience text)";
    String require = "create table if not exists ExamRequireCache (id INTEGER primary key autoincrement,name text,subject text,require text)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(add);
        db.execSQL(require);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion){
            case 1:
            db.execSQL(require);
            break;
            default:

            break;

        }
    }
}
