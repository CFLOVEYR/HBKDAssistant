package com.tofirst.study.hbkdassistant.acitvity.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.domain.FeedBack;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class ShowFeedBackActivity extends AppCompatActivity {
    private ListView listView;//展示信息的ListView
    private TextView emptyView;//如果没信息,或者为错误的显示的界面
    private FeedbackAdapter adapter;
    private List<FeedBack> list_feedback;
    private boolean isLight;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feed_back);
        setTitle("历史反馈");
        initToolBar();
        listView = (ListView) findViewById(R.id.lv_show_feed);
        emptyView = new TextView(this);
        emptyView.setText("没有反馈记录");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(25); //设置字体大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        addContentView(emptyView, params);
        listView.setEmptyView(emptyView);//如果为空显示这个界面
        /**
         * 查询服务器的信息
         */
        BmobQuery<FeedBack> query = new BmobQuery<FeedBack>();
        query.order("-createdAt");//按照时间排序
        query.findObjects(this, new FindListener<FeedBack>() {
            @Override
            public void onSuccess(List<FeedBack> list) {
                list_feedback = new ArrayList<FeedBack>();
                list_feedback=list;
                adapter = new FeedbackAdapter(ShowFeedBackActivity.this, list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String str) {
                emptyView.setText(str);
            }
        });

        //点击事件查看详情
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowFeedBackActivity.this, ShowFeedBackDetailActivity.class);
                intent.putExtra("title",list_feedback.get(position).getTitle());
                intent.putExtra("content",list_feedback.get(position).getContent());
                startActivity(intent);
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
    static class FeedbackAdapter extends BaseAdapter {

        List<FeedBack> fbs;
        LayoutInflater inflater;

        //数据的添加
        public FeedbackAdapter(Context context, List<FeedBack> feedbacks) {
            this.fbs = feedbacks;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return fbs.size();
        }

        @Override
        public Object getItem(int position) {
            return fbs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_feedback, null);
                holder.content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            FeedBack feedback = fbs.get(position);

            holder.content.setText(feedback.getContent());
            holder.time.setText(feedback.getCreatedAt());

            return convertView;
        }

        static class ViewHolder {
            TextView content;
            TextView time;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
