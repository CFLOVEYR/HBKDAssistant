package com.tofirst.study.hbkdassistant.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.tofirst.study.hbkdassistant.domain.EduBulletin;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;

/**
 * =========================================================
 * <p/>
 * 版权: 个人开发Mr.Jalen  版权所有(c) ${YEAR}
 * <p/>
 * 作者:${USER}
 * <p/>
 * 版本: 1.0
 * <p/>
 * <p/>
 * 创建日期 : ${DATE}  ${HOUR}:${MINUTE}
 * <p/>
 * <p/>
 * 邮箱：Studylifetime@sina.com
 * <p/>
 * 描述:
 * <p/>
 * <p/>
 * 修订历史:
 * <p/>
 * =========================================================
 */
public class NewsHolder extends BaseHolder<EduBulletin> {


    private TextView textView;

    @Override
    public View initView() {
        textView = new TextView(UIUtils.getContext());
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    @Override
    protected void refreshView() {
        final EduBulletin edu = getmData();
        textView.setText(edu.getTitle());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToastSafe(edu.getUrl());
            }
        });

    }
}
