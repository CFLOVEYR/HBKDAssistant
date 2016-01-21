package com.tofirst.study.hbkdassistant.domain;

import cn.bmob.v3.BmobObject;

/**
 * 反馈信息的实体类
 */
public class FeedBack extends BmobObject {
    public String title;
    public String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
