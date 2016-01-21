package com.tofirst.study.hbkdassistant.acitvity.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.domain.FeedBack;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;

public class FeedActivity extends AppCompatActivity {

    private EditText et_feed_title;
    private EditText et_feed_content;
    private String msg;
    private boolean isLight;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("意见反馈");//设置页面标题
        initToolBar();
        et_feed_title = (EditText) findViewById(R.id.et_feed_title);
        et_feed_content = (EditText) findViewById(R.id.et_feed_content);
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
    /**
     * 查看更多反馈信息的跳转
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_feed_look_more://查看反馈信息
                startActivity(new Intent(FeedActivity.this, ShowFeedBackActivity.class));
                break;
            case R.id.bt_feed_save://反馈信息
                String content = et_feed_content.getText().toString();
                String title = et_feed_title.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    if (content.equals(msg)) {
                        Toast.makeText(this, "请勿重复提交反馈", Toast.LENGTH_SHORT).show();
                    } else {
                        msg = title;
                        // 发送反馈信息
                        Bundle bundle = new Bundle();
                        bundle.putString("title", title);
                        bundle.putString("content", content);
                        sendMessage(bundle);
                        Toast.makeText(this, "您的反馈信息已发送", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请填写反馈内容", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }

    }


    /**
     * 发送反馈信息给开发者
     *
     * @param msg 反馈信息
     */
    private void sendMessage(Bundle msg) {
        BmobPushManager bmobPush = new BmobPushManager(this);
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("isDeveloper", true);
        bmobPush.setQuery(query);
        String title = msg.getString("title");
        String content = msg.getString("content");
        bmobPush.pushMessage("标题" + title + "\n 内容为:" + content);
        saveFeedbackMsg(msg);
    }

    /**
     * 保存反馈信息到服务器
     *
     * @param msg 反馈信息
     */
    private void saveFeedbackMsg(Bundle msg) {
        FeedBack feedback = new FeedBack();
        String title = msg.getString("title");
        String content = msg.getString("content");
        feedback.setTitle(title);
        feedback.setContent(content);
        feedback.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.i("bmob", "反馈信息已保存到服务器");
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                Log.e("bmob", "保存反馈信息失败：" + arg0);
            }
        });
    }

}
