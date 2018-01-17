package com.njpes.www.entity.baseinfo.enums;

/**
 * @Description:
 * @author zhangchao
 * @Date 2015-5-27 下午9:45:52
 */
public enum SwitchEnum {
    open(1, "开"), close(0, "关");

    private final int value;
    private final String info;

    private SwitchEnum(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public int getValue() {
        return value;
    }
}
