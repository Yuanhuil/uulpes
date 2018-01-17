package com.njpes.www.entity.baseinfo.enums;

public enum XueDuan {
    prim_school(1, "小学"), j_school(2, "初中"), s_school(3, "高中"), adult(4, "成人");

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

    private XueDuan(int id, String info) {
        this.info = info;
        this.id = id;
    }

    public static XueDuan valueOf(int value) { // 手写的从int到enum的转换函数
        switch (value) {
        case 1:
            return prim_school;
        case 2:
            return j_school;
        case 3:
            return s_school;
        case 4:
            return adult;
        default:
            return null;
        }
    }

    public int value() {
        return this.id;
    }
}
