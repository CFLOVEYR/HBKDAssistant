package com.tofirst.study.hbkdassistant.base.study;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.dao.ExamCasheDao;
import com.tofirst.study.hbkdassistant.domain.ExamAddExperience;
import com.tofirst.study.hbkdassistant.utils.common.StringUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 添加资料页面
 */
public class ExamAddPager extends BasePaper {

    private View examAddView;
    private EditText et_exam_add_subject;
    private EditText et_exam_add_experience;
    private Button bt_exam_add;

    public ExamAddPager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
        examAddView = View.inflate(mActivity, R.layout.study_exam_add, null);
        initView();
        fl_base_content.addView(examAddView);
    }

    private void initView() {
        et_exam_add_subject = (EditText) examAddView.findViewById(R.id.et_exam_add_subject);//科目
        et_exam_add_experience = (EditText) examAddView.findViewById(R.id.et_exam_add_experience);//经验
        bt_exam_add = (Button) examAddView.findViewById(R.id.bt_exam_add);



        //提交到服务器
        bt_exam_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String subject = et_exam_add_subject.getText().toString();
                 String experience = et_exam_add_experience.getText().toString();
                //判断输入内容是否为空
                if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(experience)) {
                    UIUtils.showToastSafe("科目或经验不能为空哟!!么么哒");
                } else {

                    ExamAddExperience exam = new ExamAddExperience();
                    BmobUser currentUser =  BmobUser.getCurrentUser(mActivity);//获取当前对象
                    //添加数据
                    Map<String, String> mExamData=new HashMap<>();
                    mExamData.put("name",currentUser.getUsername());
                    mExamData.put("subject",subject);
                    mExamData.put("experience",experience);
                    ExamCasheDao dao=new ExamCasheDao(mActivity);
                    dao.insertAdd(mExamData);

                    String userID = currentUser.getObjectId();
                    exam.setUserID(userID);
                    exam.setSubject(subject);
                    exam.setExperience(experience);

                    exam.save(mActivity, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            UIUtils.showToastSafe("添加成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            UIUtils.showToastSafe("添加失败");
                        }
                    });
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }
}
