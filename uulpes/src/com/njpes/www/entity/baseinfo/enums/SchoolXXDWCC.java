package com.njpes.www.entity.baseinfo.enums;

/**
 * 学校单位层次
 * 
 * @author 赵忠诚
 */
public enum SchoolXXDWCC {
    school("1", "校本部"), branch_school("2", "分校"), others("9", "其他");

    String info;
    String id;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {

        this.info = info;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    private SchoolXXDWCC(String id, String info) {
        this.info = info;
        this.id = id;
    }

    public String value() {
        return id;
    }
}
