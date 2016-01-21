package com.tofirst.study.hbkdassistant.base.main;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.feedback.FeedActivity;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

/**
 * 主页
 */
public class HomePager extends BasePaper {

    private View homeMainView;
    private ViewPager vp_home_main;
    private TextView tv_home_main_faceback;
    private PageIndicator mIndicator;
    private int[] mImagesArray;
    private CirclePageIndicator indicator_home_main;
    private  static  int totalCount=100;

    public HomePager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
        homeMainView = View.inflate(mActivity, R.layout.home_main, null);
        vp_home_main = (ViewPager) homeMainView.findViewById(R.id.vp_home_main);
        indicator_home_main = (CirclePageIndicator) homeMainView.findViewById(R.id.indicator_home_main);
        tv_home_main_faceback = (TextView) homeMainView.findViewById(R.id.tv_home_main_faceback);

        tv_home_main_faceback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UIUtils.showToastSafe("反馈信息功能正在开发中...");
                UIUtils.startActivity(new Intent(mActivity, FeedActivity.class));
            }
        });
        fl_base_content.addView(homeMainView);
    }
    @Override
    public void initData() {
        super.initData();
        mImagesArray = new int[]{R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3};
        HomeMainAdapter adapter=new HomeMainAdapter();
        vp_home_main.setAdapter(adapter);
        mIndicator=indicator_home_main;
        indicator_home_main.setViewPager(vp_home_main);
        indicator_home_main.setSnap(true);
        indicator_home_main.setCurrentItem(0);//设置第一页

    }


    class  HomeMainAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImagesArray.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv=new ImageView(mActivity);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//充满图片充满容器
            iv.setImageResource(mImagesArray[position%mImagesArray.length]);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    }
