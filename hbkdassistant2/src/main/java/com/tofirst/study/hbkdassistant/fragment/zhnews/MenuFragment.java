package com.tofirst.study.hbkdassistant.fragment.zhnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.login.LoginActivity;
import com.tofirst.study.hbkdassistant.acitvity.login.UserInfoActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.MainActivity;
import com.tofirst.study.hbkdassistant.domain.zhnews.NewsListItem;
import com.tofirst.study.hbkdassistant.global.Constant;
import com.tofirst.study.hbkdassistant.inteface.MyCallBack;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.HttpUtils;
import com.tofirst.study.hbkdassistant.utils.xutils3.XUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MenuFragment extends BaseFragment implements OnClickListener {
    private ListView lv_item;
    private TextView tv_download, tv_main, tv_backup, tv_login;
    private LinearLayout ll_menu;
    // private static String[] ITEMS = { "日常心理学", "用户推荐日报", "电影日报", "不许无聊",
    // "设计日报", "大公司日报", "财经日报", "互联网安全", "开始游戏", "音乐日报", "动漫日报", "体育日报" };
    private List<NewsListItem> items;
    private Handler handler = new Handler();
    private boolean isLight;
    private NewsTypeAdapter mAdapter;

    private int mCurrentPosition;
    private MainActivity mainUI;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);
        ll_menu = (LinearLayout) view.findViewById(R.id.ll_menu);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_backup = (TextView) view.findViewById(R.id.tv_backup);
        tv_download = (TextView) view.findViewById(R.id.tv_download);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        lv_item = (ListView) view.findViewById(R.id.lv_item);
        tv_main.setOnClickListener(this);
        tv_download.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MainActivity MainUI = (MainActivity) mActivity;
                MainUI.getContentFragment().getNewsPager().setLeftMenuDetailPager(position);
                //隐藏侧滑菜单
                MainUI.closeMenu();
                mCurrentPosition = position;
                mainUI.setCurId(items.get(position).getId());
//                adapter.notifyDataSetChanged();//通知做菜单选中的状态发生改变

//                getFragmentManager()
//                        .beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
//                        .replace(
//                                R.id.fl_content,
//                                new NewsFragment(items.get(position)
//                                        .getId(), items.get(position).getTitle()), "news").commit();
//                ((MainActivity) mActivity).setCurId(items.get(position).getId());
//                ((MainActivity) mActivity).closeMenu();
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        mainUI = (MainActivity) mActivity;
        isLight = ((MainActivity) mActivity).isLight();
        items = new ArrayList<NewsListItem>();
        if (HttpUtils.isNetworkConnected(mActivity)) {
            XUtil.getJsonObject(Constant.BASEURL + Constant.THEMES, new MyCallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    String json = result.toString();
                    SharePreUtils.putPreString(mActivity, Constant.THEMES, json);
                    parseJson(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }
            });
        } else {
            String json = SharePreUtils.getsPreString(mActivity, Constant.THEMES, "");
            try {
                JSONObject jsonObject = new JSONObject(json);
                parseJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseJson(JSONObject response) {
        try {
            JSONArray itemsArray = response.getJSONArray("others");
            for (int i = 0; i < itemsArray.length(); i++) {
                NewsListItem newsListItem = new NewsListItem();
                JSONObject itemObject = itemsArray.getJSONObject(i);
                newsListItem.setTitle(itemObject.getString("name"));
                newsListItem.setId(itemObject.getString("id"));
                items.add(newsListItem);
            }
            //为主页添加数据
            mainUI.getContentFragment().getNewsPager().setData(items);
            mAdapter = new NewsTypeAdapter();
            lv_item.setAdapter(mAdapter);
            updateTheme();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class NewsTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.menu_item, parent, false);
            }
            TextView tv_item = (TextView) convertView
                    .findViewById(R.id.tv_item);
            tv_item.setTextColor(getResources().getColor(isLight ? R.color.light_menu_listview_textcolor : R.color.dark_menu_listview_textcolor));
            tv_item.setText(items.get(position).getTitle());
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main:
                ((MainActivity) mActivity).loadLatest();
                mainUI.setTitle(R.string.home);
                ((MainActivity) mActivity).closeMenu();
                break;
            case R.id.tv_login:
                BmobUser  bmobUser = BmobUser.getCurrentUser(mActivity);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
//                    UIUtils.showToastSafe("即将进入个人信息页面");
                    UIUtils.startActivity(new Intent(mActivity, UserInfoActivity.class));

                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    UIUtils.startActivity(new Intent(mActivity, LoginActivity.class));
                }
                break;
        }
    }

    public void updateTheme() {
        isLight = ((MainActivity) mActivity).isLight();
        ll_menu.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_header : R.color.dark_menu_header));
        tv_login.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_backup.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_download.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_main.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_index_background : R.color.dark_menu_index_background));
        lv_item.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_listview_background : R.color.dark_menu_listview_background));
        mAdapter.notifyDataSetChanged();
    }
}
