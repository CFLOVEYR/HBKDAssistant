package com.tofirst.study.hbkdassistant.base.newsdetail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.NewsContentActivity;
import com.tofirst.study.hbkdassistant.adapter.NewsItemAdapter;
import com.tofirst.study.hbkdassistant.domain.zhnews.News;
import com.tofirst.study.hbkdassistant.domain.zhnews.StoriesEntity;
import com.tofirst.study.hbkdassistant.global.Constant;
import com.tofirst.study.hbkdassistant.inteface.MyCallBack;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.HttpUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.XUtil;

/**
 * 菜单详情页--组图
 */
public class OthersDetailPager extends BaseLeftMenuPaper {

    private ImageLoader mImageLoader;
    private ListView lv_news;
    private ImageView iv_title;
    private TextView tv_title;
    private String urlId;
    private News news;
    private NewsItemAdapter mAdapter;
    private String title;
    private View otherViews;

    public OthersDetailPager(Activity mActivity, String id, String title) {
        super(mActivity);
        urlId = id;
        this.title = title;
    }

    @Override
    public View initViews() {
        ((MainActivity) mActivity).setToolbarTitle(title);
        otherViews = View.inflate(mActivity, R.layout.news_layout, null);
        mImageLoader = ImageLoader.getInstance();
        lv_news = (ListView) otherViews.findViewById(R.id.lv_news);
        View header = View.inflate(mActivity,
                R.layout.news_header, null);
        iv_title = (ImageView) header.findViewById(R.id.iv_title);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        lv_news.addHeaderView(header);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;
                StoriesEntity entity = (StoriesEntity) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, NewsContentActivity.class);
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
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                }
            }
        });
        return otherViews;
    }

    @Override
    public void initData() {
        super.initData();
        if (HttpUtils.isNetworkConnected(mActivity)) {
            XUtil.GetJson(Constant.BASEURL + Constant.THEMENEWS + urlId, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + (Constant.BASE_COLUMN + Integer.parseInt(urlId)) + ",' " + result + "')");
                    db.close();
                    parseJson(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }
            });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + (Constant.BASE_COLUMN + Integer.parseInt(urlId)), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            db.close();
        }


    }

    @Override
    public void updateTheme() {
        mAdapter.updateTheme();
    }

    private void parseJson(String responseString) {
        Gson gson = new Gson();
        news = gson.fromJson(responseString, News.class);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        tv_title.setText(news.getDescription());
        mImageLoader.displayImage(news.getImage(), iv_title, options);
        mAdapter = new NewsItemAdapter(mActivity, news.getStories());
        lv_news.setAdapter(mAdapter);
    }


}
