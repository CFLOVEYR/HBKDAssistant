package com.tofirst.study.hbkdassistant.base.newsdetail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.LatestContentActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.adapter.MainNewsItemAdapter;
import com.tofirst.study.hbkdassistant.domain.zhnews.Before;
import com.tofirst.study.hbkdassistant.domain.zhnews.Latest;
import com.tofirst.study.hbkdassistant.domain.zhnews.StoriesEntity;
import com.tofirst.study.hbkdassistant.global.Constant;
import com.tofirst.study.hbkdassistant.inteface.MyCallBack;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.HttpUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.XUtil;
import com.tofirst.study.hbkdassistant.view.Kanner;

import java.util.List;

/**
 *
 *  菜单详情页--互动
 *
 */
public class MainDetailPager extends BaseLeftMenuPaper {
    private Before before;
    private Kanner kanner;
    private ListView lv_news;
    private String date;
    private Handler handler = new Handler();
    private boolean isLoading = false;
    private MainNewsItemAdapter mAdapter;
    private Latest latest;
    private View mNewsView;
    private View header;
    public MainDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initViews() {
        mNewsView = View.inflate(mActivity, R.layout.main_news_layout, null);
        lv_news = (ListView) mNewsView.findViewById(R.id.lv_news);
        //头布局
        header = View.inflate(mActivity, R.layout.kanner, null);
        kanner = (Kanner) header.findViewById(R.id.kanner);
        lv_news.addHeaderView(header);
        //适配器
        mAdapter = new MainNewsItemAdapter(mActivity);
        lv_news.setAdapter(mAdapter);
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                UIUtils.showToastSafe("加载更多..."+totalItemCount);
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    //如果第一行可见,而且第一个为0,设置为true,可以下拉刷新,否则不能下拉刷新,防止下拉的时候出现下拉刷新
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                    //加载更多的判断-->>如果 可见条目+第一个可见的条目==总共的条目,而且不在下载中,就加载更多
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
//                        UIUtils.showToastSafe("加载更多...");
                        loadMore(Constant.BEFORE + date);
                    }
                }

            }
        });
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;
                StoriesEntity entity = (StoriesEntity) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, LatestContentActivity.class);
                intent.putExtra(Constant.START_LOCATION, startingLocation);
                intent.putExtra("entity", entity);
                intent.putExtra("isLight", ((MainActivity) mActivity).isLight());
                String readSequence = SharePreUtils.getsPreString(mActivity, "read", "");
                String[] splits = readSequence.split(",");
                StringBuffer sb = new StringBuffer();
                if (splits.length >= 200) {
                    for (int i = 100; i < splits.length; i++) {
                        sb.append(splits[i] + ",");
                    }
                    readSequence = sb.toString();
                }

                if (!readSequence.contains(entity.getId() + "")) {
                    readSequence = readSequence + entity.getId() + ",";
                }
                SharePreUtils.putPreString(mActivity, "read", readSequence);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setTextColor(mActivity.getResources().getColor(R.color.clicked_tv_textcolor));
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });

        return mNewsView;
    }

    private void loadFirst() {
        isLoading = true;
        //判断手机是否联网
        if (HttpUtils.isNetworkConnected(mActivity)) {
            XUtil.GetJson(Constant.BASEURL + Constant.LATESTNEWS, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
//                    UIUtils.showToastSafe("解析到的数据为: " + result);
                    //保存到本地数据库,做缓存
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    //更新本地数据
                    db.execSQL("replace into CacheList(date,json) values(" + Constant.LATEST_COLUMN + ",' " + result + "')");
                    db.close();//关闭数据库
                    //解析数据
                    parseLatestJson(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
//                    UIUtils.showToastSafe("解析失败: "+ex.toString());
                }
            });
        }
        //如果没有联网,从本地数据读取数据
        else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Constant.LATEST_COLUMN, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            } else {
                isLoading = false;
            }
            cursor.close();
            db.close();
        }

    }
    //加载更多
    private void loadMore(final String url) {
        isLoading = true;//正在加载
        if (HttpUtils.isNetworkConnected(mActivity)) {
            XUtil.GetJson(url, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
//                    PreUtils.putStringTo(Constant.CACHE, mActivity, url, responseString);
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + date + ",' " + result + "')");
                    db.close();
                    parseBeforeJson(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
//                    UIUtils.showToastSafe("解析失败: "+ex.toString());
                }
            });

        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date < " + date, null);
                isLoading = false;
                Snackbar sb = Snackbar.make(lv_news, "没有更多的离线内容了~", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(mActivity.getResources().getColor(((MainActivity) mActivity).isLight() ? android.R.color.holo_blue_dark : android.R.color.black));
                sb.show();
            }
            cursor.close();
            db.close();
        }
    }
    //解析最新的数据
    private void parseLatestJson(String responseString) {
        //Gson解析数据
        Gson gson = new Gson();
        latest = gson.fromJson(responseString, Latest.class);
        //数据赋值
        date = latest.getDate();
        kanner.setTopEntities(latest.getTop_stories());
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = latest.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle("今日热闻");
                storiesEntities.add(0, topic);
                //添加数据,并且刷新界面
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });


    }

    @Override
    public void initData() {
        super.initData();
        loadFirst();//加载数据
    }

    @Override
    public void updateTheme() {
        mAdapter.updateTheme();
    }

    //解析以前的数据
    private void parseBeforeJson(String responseString) {
        Gson gson = new Gson();
        before = gson.fromJson(responseString, Before.class);
        if (before == null) {
            isLoading = false;
            return;
        }
        date = before.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = before.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle(convertDate(date));
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }
    //格式化时间
    private String convertDate(String date) {
        String result = date.substring(0, 4);
        result += "年";
        result += date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }



}
