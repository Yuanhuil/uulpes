package com.njpes.www.entity.baseinfo.enums;

public enum DianXingGeAn {

    level1(1, "一级典型个案"), level2(2, "二级典型个案"), level3(3, "三级典型个案");

    private final int state;

    private final String name;

    private DianXingGeAn(int state, String info) {
        this.state = state;
        this.name = info;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public static String getName(int value) {
        for (DianXingGeAn itemEnum : DianXingGeAn.values()) {
            if (value == itemEnum.state) {
                return itemEnum.name;
            }
        }
        return "";
    }
}
