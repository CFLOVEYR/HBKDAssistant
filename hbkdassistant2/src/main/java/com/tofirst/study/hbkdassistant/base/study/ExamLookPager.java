package com.tofirst.study.hbkdassistant.base.study;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.dao.ExamCasheDao;
import com.tofirst.study.hbkdassistant.utils.common.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查看资料页面
 */
public class ExamLookPager extends BasePaper {

    private View examLookView;
    public List<String> mData = new ArrayList<>();
    private ListView lv_exam_look;
    private ExamLookAdapter adapter;
    private ExamDataObserver observer;
    private ContentResolver contentResolver;

    public ExamLookPager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
        examLookView = View.inflate(mActivity, R.layout.study_exam_look, null);
        lv_exam_look = (ListView) examLookView.findViewById(R.id.lv_exam_look);

        //注册一个广播观察者
        observer = new ExamDataObserver(new Handler());
        Uri parse = Uri.parse("content://com.tofirst.study.hbkdassistant.examchange");
        contentResolver = mActivity.getContentResolver();
        contentResolver.registerContentObserver(parse, true, observer);
    }


    @Override
    public void initData() {
        super.initData();
        upDateData();
        if (mData != null) {
//            UIUtils.showToastSafe(mData.toString());
            adapter = new ExamLookAdapter();
            lv_exam_look.setAdapter(adapter);
            ViewUtils.removeSelfFromParent(examLookView);
        }
        fl_base_content.addView(examLookView);
    }


    class ExamLookAdapter extends BaseAdapter {

        ViewHolder holder;

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.study_exam_look_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_exam_look_item);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.textView.setText(mData.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
    }

    /**
     * 更新数据
     */
    public void upDateData() {
        ExamCasheDao dao = new ExamCasheDao(mActivity);
        List<Map<String, String>> list_add = dao.queryAllAdd();
        List<Map<String, String>> list_require = dao.queryAllRequire();
        mData.clear();
        for (Map<String, String> mExamData : list_add) {
            String username = mExamData.get("name");
            String subject = mExamData.get("subject");
            String experience = mExamData.get("experience");
            mData.add(username + "分享了" + subject + "经验");
        }
        for (Map<String, String> mExamData : list_require) {
            String username = mExamData.get("name");
            String subject = mExamData.get("subject");
            String require = mExamData.get("require");
            mData.add(username + "需求" + subject +"经验");
        }
    }

    /**
     * 监听数据变化的观察者
     */
    class ExamDataObserver extends ContentObserver {

        public ExamDataObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            //更新数据
            upDateData();
//            UIUtils.showToastSafe("数据改变了" + mData.size());
            adapter.notifyDataSetChanged();
        }
    }

}
