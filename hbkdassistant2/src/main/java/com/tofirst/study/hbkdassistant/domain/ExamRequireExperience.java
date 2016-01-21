package com.tofirst.study.hbkdassistant.domain;

import cn.bmob.v3.BmobObject;

/**
 * 分享经验的类
 */
public class ExamRequireExperience extends BmobObject {
    //角色ObjectID
    private String userID;
    private String subject;
    private String require;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }
}
