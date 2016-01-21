package com.tofirst.study.hbkdassistant.fragment.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.tofirst.study.hbkdassistant.widget.LoadingFrameLayout;

import java.util.List;

/**
 * =========================================================
 * <p/>
 * 版权: 个人开发Mr.Jalen  版权所有(c) 2015
 * <p/>
 * 作者: Jalen
 * <p/>
 * 版本: 1.0
 * <p/>
 * <p/>
 * 创建日期 : 2015/12/23  20:31
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
public abstract class BaseNewsFragment extends Fragment {

    private LoadingFrameLayout mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = new LoadingFrameLayout(UIUtils.getContext()) {
            @Override
            public View createSuccessView() {
                return BaseNewsFragment.this.createSuccessView();
            }

            @Override
            protected LoadingFrameLayout.LoadResult load() {
                return BaseNewsFragment.this.loadData();
            }
        };
        return mContentView;
    }

    protected abstract LoadingFrameLayout.LoadResult loadData();
    protected abstract View createSuccessView();
    /**
     * 检查数据的类型
     * @param object
     * @return 返回是成功,空,还是错误
     */
    protected LoadingFrameLayout.LoadResult check(Object object) {
        //返回为NULL
        if (object == null) {
            return LoadingFrameLayout.LoadResult.ERROR;
        }
        //如果数据长度为0,没有数据
        if (object instanceof List) {
            List list = (List) object;
            if (list.size() == 0) {
                return LoadingFrameLayout.LoadResult.EMPTY;
            }
        }
        return LoadingFrameLayout.LoadResult.SUCCESS;
    }
    /**
     * 展现的方法
     */
    public void show() {
        if (null != mContentView) {
            mContentView.show();
        }
    }
}
