package com.njpes.www.entity.baseinfo.enums;

/**
 * 学校属性
 * 
 * @author 赵忠诚
 */
public enum SchoolXXSX {
    college_school("1", "高等学校直属学校"), province_school("2", "省直属学校"), city_school("3", "地市直属学校"), county_school("4",
            "区县直属学校"), town_school("5", "乡镇学校");

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

    private SchoolXXSX(String id, String info) {
        this.info = info;
        this.id = id;
    }

    public String value() {
        return id;
    }
}
