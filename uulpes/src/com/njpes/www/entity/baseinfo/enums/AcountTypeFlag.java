package com.njpes.www.entity.baseinfo.enums;

/**
 * 用户类型
 * 
 * @author 赵忠诚
 */
public enum AcountTypeFlag {
    student(1, "学生"), teacher(2, "教师"), parent(3, "家长"), ecuser(4, "教委人员");

    String info;
    int id;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {

        this.info = info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    private AcountTypeFlag(int id, String info) {
        this.info = info;
        this.id = id;
    }
}
