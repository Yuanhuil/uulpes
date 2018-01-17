package com.njpes.www.entity.baseinfo.enums;

public enum InterveneResult {

    level1(1, "取消预警"), level2(2, "继续跟进"), level3(3, "需要转介");

    private final int state;

    private final String name;

    private InterveneResult(int state, String info) {
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
        for (InterveneResult itemEnum : InterveneResult.values()) {
            if (value == itemEnum.state) {
                return itemEnum.name;
            }
        }
        return "";
    }
}
