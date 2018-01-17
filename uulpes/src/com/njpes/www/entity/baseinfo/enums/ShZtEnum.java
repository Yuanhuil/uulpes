package com.njpes.www.entity.baseinfo.enums;

public enum ShZtEnum {
    PASS("1", "通过"), NOPASS("2", "未通过");

    private final String id;
    private final String info;

    private ShZtEnum(String value, String info) {
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
