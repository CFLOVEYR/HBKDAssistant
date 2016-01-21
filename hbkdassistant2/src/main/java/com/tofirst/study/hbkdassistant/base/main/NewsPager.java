package com.tofirst.study.hbkdassistant.base.main;

import android.support.v7.app.AppCompatActivity;

import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.base.newsdetail.BaseLeftMenuPaper;
import com.tofirst.study.hbkdassistant.base.newsdetail.MainDetailPager;
import com.tofirst.study.hbkdassistant.base.newsdetail.OthersDetailPager;
import com.tofirst.study.hbkdassistant.domain.zhnews.NewsListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻的主页面的实现
 */
public class NewsPager extends BasePaper {
    public List<BaseLeftMenuPaper> leftDetalis;
    public List<NewsListItem> items;
    public int CurrentPosition = 0;
    private MainActivity mMainUI;

    @Override
    public void initViews() {
        super.initViews();
        mMainUI = (MainActivity) mActivity;
        leftDetalis = new ArrayList<>();
        leftDetalis.add(new MainDetailPager(mActivity));
        fl_base_content.addView(leftDetalis.get(CurrentPosition).mRootView);
    }

    @Override
    public void initData() {
        super.initData();
        leftDetalis.get(CurrentPosition).initData();
    }

    //设置新闻详情页
    public void setLeftMenuDetailPager(int position) {
        CurrentPosition = position;
        BaseLeftMenuPaper leftpager = leftDetalis.get(position);
        fl_base_content.removeAllViews();//赋值的时候清空以前的组件
        //添加组件
        fl_base_content.addView(leftpager.mRootView);
        //初始化数据
        leftpager.initData();
        mMainUI.setTitle(items.get(CurrentPosition).getTitle());
    }

    public NewsPager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    public void setData(List<NewsListItem> items) {
        this.items = items;
        //为了保证数据传递过来之后,在加载页面
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                leftDetalis.add(new OthersDetailPager(mActivity, items.get(i).getId(), items.get(i).getTitle()));
            }
        }
//        UIUtils.showToastSafe("传递过来数据了" + items);
    }
}
