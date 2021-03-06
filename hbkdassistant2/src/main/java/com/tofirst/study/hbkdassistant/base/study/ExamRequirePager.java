package com.tofirst.study.hbkdassistant.base.study;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.base.BasePaper;
import com.tofirst.study.hbkdassistant.dao.ExamCasheDao;
import com.tofirst.study.hbkdassistant.domain.ExamRequireExperience;
import com.tofirst.study.hbkdassistant.utils.common.StringUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 添加资料页面
 */
public class ExamRequirePager extends BasePaper {

    private View examRequireView;
    private EditText et_exam_require_subject;
    private EditText et_exam_require_experience;
    private Button bt_exam_require;
    public ExamRequirePager(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initViews() {
        super.initViews();
        examRequireView = View.inflate(mActivity, R.layout.study_exam_require, null);
        initView();
        fl_base_content.addView(examRequireView);
    }

    private void initView() {
        et_exam_require_subject = (EditText) examRequireView.findViewById(R.id.et_exam_require_subject);//科目
        et_exam_require_experience = (EditText) examRequireView.findViewById(R.id.et_exam_require_experience);//经验
        bt_exam_require = (Button) examRequireView.findViewById(R.id.bt_exam_require);


        //提交到服务器
        bt_exam_require.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = et_exam_require_subject.getText().toString();
                String require = et_exam_require_experience.getText().toString();
                //判断输入内容是否为空
                if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(require)) {
                    UIUtils.showToastSafe("科目或需求不能为空哟!!么么哒");
                } else {
                    BmobUser currentUser = BmobUser.getCurrentUser(mActivity);//获取当前对象
                    String userID = currentUser.getObjectId();

                    //添加数据到本地数据库
                    Map<String, String> mExamData = new HashMap<>();
                    mExamData.put("name", currentUser.getUsername());
                    mExamData.put("subject", subject);
                    mExamData.put("require", require);
                    ExamCasheDao dao = new ExamCasheDao(mActivity);
                    dao.insertRequire(mExamData);

                    //需要上传服务器
                    ExamRequireExperience exam = new ExamRequireExperience();
                    exam.setUserID(userID);
                    exam.setSubject(subject);
                    exam.setRequire(require);

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
