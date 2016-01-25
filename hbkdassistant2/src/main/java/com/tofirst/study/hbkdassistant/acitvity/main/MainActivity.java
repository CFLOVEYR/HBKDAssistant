package com.tofirst.study.hbkdassistant.acitvity.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.db.ExamShareCacheDbHelper;
import com.tofirst.study.hbkdassistant.db.NewsCacheDbHelper;
import com.tofirst.study.hbkdassistant.fragment.main.ContentFragment;
import com.tofirst.study.hbkdassistant.fragment.zhnews.MenuFragment;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

public class MainActivity extends BaseActivity {
    private static final String CONTENFRAGMENT = "contenfragment";
    private static final String LEFTMENUFRAGMENT = "leftmenufragment";
    private FrameLayout fl_content;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SwipeRefreshLayout sr;
    private long firstTime;
    private Toolbar toolbar;
    private boolean isLight;
    private NewsCacheDbHelper dbHelper;
    private String curId;
    private ExamShareCacheDbHelper examdbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLight = SharePreUtils.getsPreBoolean(this, "isLight", true);//主题模式

        initCache();
        initViews();
    }


    private void initCache() {
        dbHelper = new NewsCacheDbHelper(this, 1);//创建缓存数据库
        examdbHelper = new ExamShareCacheDbHelper(this, "examcashe.db", null, 1);//创建数据库缓存
    }

    public void loadLatest() {
        initFragment();
        curId = "latest";
    }

    public void setCurId(String id) {
        curId = id;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    /**
     * 设置状态栏的主题
     */
    @TargetApi(21)
    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        initActionBar();
        initPurefResh();
        loadLatest();//最新,捎带初始化Fragment
    }


    private void initPurefResh() {
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UIUtils.showToastSafe("刷新了");
                sr.setRefreshing(false);
            }
        });
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_content, new ContentFragment(), CONTENFRAGMENT);
        transaction.replace(R.id.fl_menu, new MenuFragment(), LEFTMENUFRAGMENT);
        transaction.commit();
    }

    /**
     * 设置夜间模式的方法
     */
    public void setLightorNight(boolean isLight) {
        this.isLight = isLight;
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        //改变底部导航栏的背景
        getContentFragment().setRadioBackGround(isLight);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                mAnimationDrawable.stop();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                mAnimationDrawable.start();
            }
        };
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }

    /**
     * 设置是否能下拉刷新的方法
     */
    public void setSwipeRefreshEnable(boolean enable) {
        sr.setEnabled(enable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // 将布局挂载到menu上
        inflater.inflate(R.menu.main, menu);
        //改变日间和夜间的模式
        menu.getItem(0).setTitle(SharePreUtils.getsPreBoolean(MainActivity.this, "isLight", true) ? "夜间模式" : "日间模式");
        return true;//已经处理好了
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {  //AcionButton的点击事件
            case R.id.action_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;

            case R.id.action_mode:
                isLight = !isLight;
                // TODO: 15-8-29 现在只有这个activity有夜间模式，打开日报详情还不是啊
                item.setTitle(isLight ? "夜间模式" : "日间模式");
                //设置Toolbar的颜色
                toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
                setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
                //改变底部导航栏的背景
                getContentFragment().setRadioBackGround(isLight);
                //改变侧滑栏的背景
                getLeftMenuFragment().updateTheme();
                //改变内容的背景
                int position = getContentFragment().getNewsPager().CurrentPosition;
                getContentFragment().getNewsPager().leftDetalis.get(position).updateTheme();
                //保存数据
                SharePreUtils.putPreBoolean(MainActivity.this, "isLight", isLight);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 获得LeftMenuFragment
     *
     * @return
     */
    public MenuFragment getLeftMenuFragment() {
        FragmentManager manager = getSupportFragmentManager();
        MenuFragment left = (MenuFragment) manager.findFragmentByTag(LEFTMENUFRAGMENT);
        return left;
    }

    /**
     * 获得ContentFragment
     *
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        ContentFragment right = (ContentFragment) manager.findFragmentByTag(CONTENFRAGMENT);
        return right;
    }

    //关闭侧边栏
    public void closeMenu() {
        mDrawerLayout.closeDrawers();
    }

    /**
     * 判断二次退出的功能实现
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeMenu();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Snackbar sb = Snackbar.make(fl_content, "再按一次退出", Snackbar.LENGTH_SHORT);
                //设置提示框的主题模式--->>日间or夜间模式
                sb.getView().setBackgroundColor(getResources().getColor(isLight ? android.R.color.holo_blue_dark : android.R.color.black));
                sb.show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }

    }

    public boolean isLight() {
        return isLight;
    }

    public SQLiteOpenHelper getCacheDbHelper() {
        return dbHelper;
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

}
