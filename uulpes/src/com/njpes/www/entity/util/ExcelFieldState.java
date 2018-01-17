package com.njpes.www.entity.util;

public enum ExcelFieldState {

    A("A", "导入和导出字段"), I("I", "仅导入字段"), E("E", "仅导出字段");

    private ExcelFieldState(String id, String info) {
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
