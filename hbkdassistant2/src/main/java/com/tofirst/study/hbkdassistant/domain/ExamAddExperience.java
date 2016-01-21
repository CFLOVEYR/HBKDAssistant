package com.tofirst.study.hbkdassistant.domain;

import cn.bmob.v3.BmobObject;

/**
 * 分享经验的类
 */
public class ExamAddExperience extends BmobObject {
    //角色ObjectID
    private String userID;
    private String subject;
    private String experience;

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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
