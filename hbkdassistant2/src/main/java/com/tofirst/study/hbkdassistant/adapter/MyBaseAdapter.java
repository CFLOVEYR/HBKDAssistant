package com.tofirst.study.hbkdassistant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tofirst.study.hbkdassistant.holder.BaseHolder;

import java.util.List;

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
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    ListView mListView;
    List<T> mData;
    private BaseHolder holder;

    public MyBaseAdapter(ListView mListView, List<T> mData) {
        this.mListView = mListView;
        setData(mData);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null != convertView) {
            holder = (BaseHolder) convertView.getTag();
        } else {
            holder = getHolder();
        }
        holder.setData(mData.get(position));
        return holder.getRootView();
    }


    public void setData(List<T> mData) {
        this.mData = mData;
    }

    public List<T> getData() {
        return mData;
    }

    protected abstract BaseHolder getHolder();
}
