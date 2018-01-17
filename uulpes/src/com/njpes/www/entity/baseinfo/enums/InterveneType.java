package com.njpes.www.entity.baseinfo.enums;

public enum InterveneType {

    level1(1, "电话疏导"), level2(2, "辅导室约见"), level3(3, "赶赴现场"), level4(4, "其他");

    private final int state;

    private final String name;

    private InterveneType(int state, String info) {
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
        for (InterveneType itemEnum : InterveneType.values()) {
            if (value == itemEnum.state) {
                return itemEnum.name;
            }
        }
        return "";
    }
}
