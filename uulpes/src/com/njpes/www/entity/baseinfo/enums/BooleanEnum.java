package com.njpes.www.entity.baseinfo.enums;

/**
 * boolean 值没去展示，在页面显示的value
 * 
 * @author 赵忠诚
 */
public enum BooleanEnum {
    TRUE(Boolean.TRUE, "是"), FALSE(Boolean.FALSE, "否");

    private final Boolean value;
    private final String info;

    private BooleanEnum(Boolean value, String info) {
        this.value = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public Boolean getValue() {
        return value;
    }
}
