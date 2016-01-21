package com.tofirst.study.hbkdassistant.holder;

import android.view.View;

/**
 * =========================================================
 *
 *  版权: 个人开发Mr.Jalen  版权所有(c) ${YEAR}

 *  作者:${USER}

 *  版本: 1.0
 *
 *
 *  创建日期 : ${DATE}  ${HOUR}:${MINUTE}
 *
 *
 *  邮箱：Studylifetime@sina.com
 *
 *  描述:
 *
 *
 *  修订历史:
 *
 * =========================================================
 */
public abstract class BaseHolder<T> {
    private T mData;

    private final View view;

    public BaseHolder() {
        view = initView();
        view.setTag(this);
    }

    public View getRootView() {
        return view;
    }

    public abstract View initView();

    public void setData(T mData) {
        this.mData = mData;
        refreshView();
    }

    protected abstract void refreshView();

    public T getmData() {
        return mData;
    }
}
