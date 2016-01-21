package com.tofirst.study.hbkdassistant.fragment.main;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.base.main.HomePager;
import com.tofirst.study.hbkdassistant.base.main.MinePager;
import com.tofirst.study.hbkdassistant.base.main.NewsPager;
import com.tofirst.study.hbkdassistant.base.main.StudyPager;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页的内容的Fragment
 */
public class ContentFragment extends MainBaseFragment {
    private ViewPager vp_content_fragment;
    private RadioGroup rg_bottom_tag;
    private ContentVPAdatapter adatapter;
    private int mCurrentPage;//当前页数
    private List<BasePaper> paperList;
    private boolean isLight;
    private MainActivity mainUI;

    @Override
    public View initViews() {
        mainUI = (MainActivity) mActivity;
        View contentView = View.inflate(mActivity, R.layout.fragment_content, null);
        vp_content_fragment = (ViewPager) contentView.findViewById(R.id.vp_content_fragment);
        rg_bottom_tag = (RadioGroup) contentView.findViewById(R.id.rg_bottom_tag);
        return contentView;
    }

    @Override
    public void initData() {
        paperList = new ArrayList<BasePaper>();
        paperList.add(new HomePager((AppCompatActivity) mActivity));
        paperList.add(new NewsPager((AppCompatActivity) mActivity));//新闻模块
        paperList.add(new StudyPager((AppCompatActivity) mActivity));//学习模块
//        paperList.add(new EnterTainMentPager((AppCompatActivity) mActivity));//娱乐模块
        paperList.add(new MinePager((AppCompatActivity) mActivity));//娱乐模块

        initViewPager();
        initRadioGroup();
    }

    /**
     * 获得新闻的类
     *
     * @return
     */
    public NewsPager getNewsPager() {
        return (NewsPager) paperList.get(1);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        adatapter = new ContentVPAdatapter();
        vp_content_fragment.setAdapter(adatapter);
        //设置选中的是为主页,初始化不能滑动
        paperList.get(0).initData();
        mainUI.setTitle(R.string.home);
        vp_content_fragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                paperList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    /**设置底部导航栏的夜间和日间模式*/
    public  void setRadioBackGround(boolean isLight){
        if (isLight) {
            rg_bottom_tag.setBackgroundResource(R.drawable.bg_tab);
        }else {
            rg_bottom_tag.setBackgroundResource( R.color.dark_toolbar);
        }

    }
    /**
     * 初始化RadioGroup
     */
    private void initRadioGroup() {
        isLight = SharePreUtils.getsPreBoolean(mActivity, "isLight", true);
        setRadioBackGround(isLight);
        //默认选择的是第一个为首页
        rg_bottom_tag.check(R.id.rb_home);
        /**
         * 初始化RadioGroup的监听事件
         */
        rg_bottom_tag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mainUI.setTitle(R.string.home);
                        vp_content_fragment.setCurrentItem(0, false);//去除了动画效果
                        break;
                    case R.id.rb_news:
                        mainUI.setTitle(R.string.news);
                        vp_content_fragment.setCurrentItem(1, false);//去除了动画效果
                        break;

//                    case R.id.rb_setting:
//                        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//                        View view = View.inflate(mActivity, R.layout.activity_center_more, null);
//                        builder.setView(view);
//                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                builder.create().dismiss();
//                            }
//                        });
//                        builder.show();
//                        break;
                    case R.id.rb_study:
                        mainUI.setTitle(R.string.study);
                        vp_content_fragment.setCurrentItem(2, false);//去除了动画效果
                        break;
//                    case R.id.rb_entertainment:
//                        vp_content_fragment.setCurrentItem(3, false);//去除了动画效果
//                        break;

                    case R.id.rb_mine:
                        mainUI.setTitle(R.string.mine);
                        vp_content_fragment.setCurrentItem(3, false);//去除了动画效果
                        break;
                    default:

                        break;
                }
                adatapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * ViewPager的适配器
     */
    class ContentVPAdatapter extends PagerAdapter {

        @Override
        public int getCount() {
            return paperList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePaper basePaper = paperList.get(position);
            container.addView(basePaper.mRootView);
            return basePaper.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
