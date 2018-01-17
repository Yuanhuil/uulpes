package com.njpes.www.entity.baseinfo.enums;

public enum WarningLever {

    level1(1, "一般危险"), level2(2, "中度危险"), level3(3, " 重度危险");

    private final int state;

    private final String name;

    private WarningLever(int state, String info) {
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
        for (WarningLever itemEnum : WarningLever.values()) {
            if (value == itemEnum.state) {
                return itemEnum.name;
            }
        }
        return "";
    }
}
