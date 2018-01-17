package com.njpes.www.entity.util;

public enum ExcelDefaultValue {

    CURRENT_USER("current_user", "当前用户"), CURRENT_DEP("current_dep", "当前组织机构"), CURRENT_DATE("current_date", "当前时间");

    private ExcelDefaultValue(String id, String info) {
        this.id = id;
        this.info = info;
    }

    String id;
    String info;

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

}
