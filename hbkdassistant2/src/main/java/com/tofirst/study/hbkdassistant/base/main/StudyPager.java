package com.tofirst.study.hbkdassistant.base.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.acitvity.login.LoginActivity;
import com.tofirst.study.hbkdassistant.acitvity.main.ExamShareActivity;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.dao.ExamCasheDao;
import com.tofirst.study.hbkdassistant.domain.ExamAddExperience;
import com.tofirst.study.hbkdassistant.domain.ExamRequireExperience;
import com.tofirst.study.hbkdassistant.domain.Person;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * 学习
 */
public class StudyPager extends BasePaper {

    private View studyMainUI;
    private TextView tv_study_share;
    private Map<String, String> mExamData;
    private ExamCasheDao dao;

    public StudyPager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();

        studyMainUI = View.inflate(mActivity, R.layout.study_main, null);

        tv_study_share = (TextView) studyMainUI.findViewById(R.id.tv_study_share);

        tv_study_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(mActivity);
                if (bmobUser != null) {
                    // 允许用户使用应用,然后是自己的逻辑
                    UIUtils.startActivity(new Intent(mActivity, ExamShareActivity.class));

                } else {
                    //缓存用户对象为空时， 可打开用户登录界面…
                    UIUtils.showToastSafe("请您先登录");
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                }

            }
        });
        fl_base_content.addView(studyMainUI);
    }

    @Override
    public void initData() {
        super.initData();
        if (!SharePreUtils.getsPreBoolean(mActivity, "ExamCasheFlag", false)) {
            getDataFromServer();
        }

    }

    private void getDataFromServer() {
        dao = new ExamCasheDao(mActivity);
        querAddData();
        querRequireData();
    }

    /**
     * 查询用户添加数据的方法
     */
    private void querAddData() {
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

                            mExamData = new HashMap<>();
                            mExamData.put("name", person.getUsername());
                            mExamData.put("subject", exp.getSubject());
                            mExamData.put("experience", exp.getExperience());
                            boolean insert = dao.insertAdd(mExamData);
                            //如果添加成功
                            if (insert) {
                                SharePreUtils.putPreBoolean(mActivity, "ExamCasheFlag", true);
                            }

                        }

                        @Override
                        public void onFailure(int i, String s) {

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

    /**
     * 查询用户需求数据的方法
     */
    private void querRequireData() {
        final BmobQuery<ExamRequireExperience> query = new BmobQuery<ExamRequireExperience>();
        query.setLimit(10);
        query.findObjects(mActivity, new FindListener<ExamRequireExperience>() {
            @Override
            public void onSuccess(final List<ExamRequireExperience> list) {

                for (final ExamRequireExperience exp : list) {
                    BmobQuery<Person> query_1 = new BmobQuery<Person>();
                    query_1.getObject(mActivity, exp.getUserID(), new GetListener<Person>() {
                        @Override
                        public void onSuccess(Person person) {

                            mExamData = new HashMap<>();
                            mExamData.put("name", person.getUsername());
                            mExamData.put("subject", exp.getSubject());
                            mExamData.put("require", exp.getRequire());
                            boolean insert = dao.insertRequire(mExamData);
                            //如果添加成功
                            if (insert) {
                                SharePreUtils.putPreBoolean(mActivity, "ExamCasheFlag", true);
                            }

                        }

                        @Override
                        public void onFailure(int i, String s) {

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

}
