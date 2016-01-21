package com.tofirst.study.hbkdassistant.base.study;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.domain.ExamAddExperience;
import com.tofirst.study.hbkdassistant.domain.Person;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.tofirst.study.hbkdassistant.utils.common.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * 查看资料页面
 */
public class ExamLookPager extends BasePaper {

    private View examLookView;
    static final List<String> mData = new ArrayList<>();
    private ListView lv_exam_look;
    private ExamLookAdapter adapter;

    public ExamLookPager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
        examLookView = View.inflate(mActivity, R.layout.study_exam_look, null);
        lv_exam_look = (ListView) examLookView.findViewById(R.id.lv_exam_look);
    }


    @Override
    public void initData() {
        super.initData();
        //网络获取数据
        getDataFromServer();

        adapter = new ExamLookAdapter();
        lv_exam_look.setAdapter(adapter);
        ViewUtils.removeSelfFromParent(examLookView);
        fl_base_content.addView(examLookView);

    }

    private void getDataFromServer() {
        final BmobQuery<ExamAddExperience> query = new BmobQuery<ExamAddExperience>();
        query.setLimit(10);
        query.findObjects(mActivity, new FindListener<ExamAddExperience>() {
            @Override
            public void onSuccess(final List<ExamAddExperience> list) {

                for (final ExamAddExperience exp : list) {
                    BmobQuery<Person> query_1 = new BmobQuery<Person>();
                    query_1.getObject(mActivity, exp.getUserID(), new GetListener<Person>() {
                        @Override
                        public void onSuccess(Person person) {
                            if (mData.size() <= list.size()) {
                                mData.add(person.getUsername() + "科目  " + exp.getSubject() + " 经验" + exp.getExperience());
                            }
//                            UIUtils.showToastSafe(person.getUsername() + "科目  " + exp.getSubject() + " 经验" + exp.getExperience());
                        }

                        @Override
                        public void onFailure(int i, String s) {
//                            UIUtils.showToastSafe("查询失败" + s);
                        }


                    });
                }

            }

            @Override
            public void onError(int i, String s) {
                UIUtils.showToastSafe("网络失败" + s);
            }
        });
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


}
