package com.njpes.www.entity.baseinfo.enums;

public enum UserState {

    normal(1, "正常状态"), blocked(2, "锁定状态"), deleted(3, "删除"), forbid(4, "禁止访问");

    private final int identify;

    private final String name;

    private UserState(int identify, String info) {
        this.identify = identify;
        this.name = info;
    }

    public int getIdentify() {
        return identify;
    }

    public String getName() {
        return name;
    }
}
