package com.njpes.www.entity.baseinfo.enums;

public enum SexEnum {
    w("0", "女"), m("1", "男");

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

    private SexEnum(String id, String info) {
        this.info = info;
        this.id = id;
    }

}
