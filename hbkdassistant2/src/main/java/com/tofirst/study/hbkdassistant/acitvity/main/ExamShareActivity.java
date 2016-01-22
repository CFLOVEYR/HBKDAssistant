package com.tofirst.study.hbkdassistant.acitvity.main;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.base.study.ExamAddPager;
import com.tofirst.study.hbkdassistant.base.study.ExamLookPager;
import com.tofirst.study.hbkdassistant.base.study.ExamRequirePager;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class ExamShareActivity extends BaseActivity {


    private List<BasePaper> basePaperList;
    private PagerSlidingTabStrip indicator;
    private ViewPager pager;
    private ExamShareAdapter adapter;
    private boolean isLight;
    private Toolbar toolbar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_exam_share);
        setTitle("经验分享");
        pager = (ViewPager) findViewById(R.id.pager_exam_share);
        indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator_exam_share);
        initToolBar();
    }

    @Override
    protected void initData() {
        basePaperList = new ArrayList<>();
        basePaperList.add(new ExamLookPager(this));
        basePaperList.add(new ExamAddPager(this));
        basePaperList.add(new ExamRequirePager(this));

        adapter = new ExamShareAdapter();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        //设置选中后标签文字的颜色和没选中后的颜色
        indicator.setTextColor(UIUtils.getColor(R.color.tab_text_normal), UIUtils.getColor(R.color.tab_text_selected));
        //设置是显示三角指示器
//        indicator.setIsHasTtriangle(true);
        indicator.setCurrentItem(0);
        basePaperList.get(0).initData();
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                basePaperList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initToolBar() {
        isLight = SharePreUtils.getsPreBoolean(this, "isLight", true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    class ExamShareAdapter extends PagerAdapter {

        private final String[] mEaxmData;

        public ExamShareAdapter() {
            mEaxmData = UIUtils.getStringArray(R.array.exam_share);
        }

        @Override
        public int getCount() {
            return mEaxmData.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mEaxmData[position];
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePaper paper = basePaperList.get(position);
            container.addView(paper.mRootView);
            return paper.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
