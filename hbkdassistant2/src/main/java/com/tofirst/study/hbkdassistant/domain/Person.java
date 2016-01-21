package com.tofirst.study.hbkdassistant.domain;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 注册用户信息的实体类
 */
public class Person extends BmobUser {
    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    //    private String CustomName; //用户名
    private BmobFile pic;  //图片
    private  Integer level;//经验等级  2000 自己换算在哪个级别: 青铜  白银  黄金..
    private  Integer grade;//入学年份  11届..
    private  String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
