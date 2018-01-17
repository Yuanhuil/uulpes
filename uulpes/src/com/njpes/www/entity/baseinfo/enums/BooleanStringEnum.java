package com.njpes.www.entity.baseinfo.enums;

public enum BooleanStringEnum {
    TRUE("1", "是"), FALSE("2", "否");

    private final String id;
    private final String info;

    private BooleanStringEnum(String value, String info) {
        this.id = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getId() {
        return id;
    }
}
