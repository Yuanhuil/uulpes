package com.njpes.www.entity.baseinfo.enums;

public enum AppointmentState {

    appointment(0, "已预约"), finish(1, "完成"), undone(2, "未完成");

    private final int state;

    private final String name;

    private AppointmentState(int state, String info) {
        this.state = state;
        this.name = info;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }
}
