package com.tofirst.study.hbkdassistant.base.study;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.main.ExamShowDetailActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.ExamShowShareActivity;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.dao.ExamCasheDao;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
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
    private List<Map<String, String>> list_add;
    private List<Map<String, String>> list_require;

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
        private static final int TYPE1 = 0;
        private static final int TYPE2 = 1;
        ViewHolder1 holder1;
        ViewHolder2 holder2;

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
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE1;
            } else if (position > 0 && position < list_add.size()) {
                return TYPE2;
            } else if (position == list_add.size()) {
                return TYPE1;
            } else if (position > list_add.size()) {
                return TYPE2;
            }
            return super.getItemViewType(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //获得需要显示的类型
            int type = getItemViewType(position);
            //复用的处理
            if (convertView == null) {
                switch (type) {
                    case 0:
                        holder1 = new ViewHolder1();
                        convertView = View.inflate(mActivity, R.layout.exam_look_item1, null);
                        holder1.textView = (TextView) convertView.findViewById(R.id.iv_exam_item1);

                        convertView.setTag(holder1);
                        break;
                    case 1:
                        holder2 = new ViewHolder2();
                        convertView = View.inflate(mActivity, R.layout.exam_look_item2, null);
                        holder2.ivExamLookPic = (ImageView) convertView.findViewById(R.id.iv_exam_look_pic);
                        holder2.ivExamLookTitle = (TextView) convertView.findViewById(R.id.tv_exam_look_title);
                        holder2.ivExamLookContent = (TextView) convertView.findViewById(R.id.tv_exam_look_content);
                        convertView.setTag(holder2);
                        break;
                    default:
                        break;
                }
            } else {
                switch (type) {
                    case 0:
                        holder1 = (ViewHolder1) convertView.getTag();
                        break;
                    case 1:
                        holder2 = (ViewHolder2) convertView.getTag();
                        break;
                    default:
                        break;
                }
            }
            //资源的赋值
            switch (type) {
                case 0:
                    holder1 = (ViewHolder1) convertView.getTag();
                    if (position == 0) {
                        holder1.textView.setText("分享");
                    } else if (position == list_add.size()) {
                        holder1.textView.setText("需求");
                    }

                    break;
                case 1:
                    holder2 = (ViewHolder2) convertView.getTag();
                    holder2.ivExamLookPic.setImageResource(R.mipmap.pic_default);
                    if (position > 0 && position < list_add.size()) {
                        holder2.ivExamLookTitle.setText(mData.get(position));
                        holder2.ivExamLookContent.setText(mData.get(position));
                    } else if (position > list_add.size()) {
                        holder2.ivExamLookTitle.setText(mData.get(position));
                        holder2.ivExamLookContent.setText(mData.get(position));
                    }

                    break;
                default:
                    break;
            }
            return convertView;
        }
    }

    class ViewHolder1 {
        TextView textView;
    }

    class ViewHolder2 {
        ImageView ivExamLookPic;
        TextView ivExamLookTitle;
        TextView ivExamLookContent;
    }

    /**
     * 更新数据
     */
    public void upDateData() {
        ExamCasheDao dao = new ExamCasheDao(mActivity);
        final List<Map<String, String>> list_add = dao.queryAllAdd();
        List<Map<String, String>> list_require = dao.queryAllRequire();
        mData.clear();
        for (Map<String, String> mExamData : list_add) {
            this.list_add = list_add;
            String username = mExamData.get("name");
            String subject = mExamData.get("subject");
            String experience = mExamData.get("experience");
            mData.add(username + "," + subject + "," + experience);
        }
        for (Map<String, String> mExamData : list_require) {
            this.list_require = list_require;
            String username = mExamData.get("name");
            String subject = mExamData.get("subject");
            String require = mExamData.get("require");
//            mData.add(username + "需求" + subject + "经验");
            mData.add(username + "," + subject + "," + require);
        }
        lv_exam_look.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position < list_add.size()) {
                    Intent intent_add = new Intent(mActivity, ExamShowShareActivity.class);
                    intent_add.putExtra("add", mData.get(position));
                    UIUtils.startActivity(intent_add);
                } else if (position > list_add.size()) {
                    Intent intent_require = new Intent(mActivity, ExamShowDetailActivity.class);
                    intent_require.putExtra("require", mData.get(position));
                    UIUtils.startActivity(intent_require);
                }

            }
        });
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
