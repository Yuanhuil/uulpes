package com.njpes.www.entity.baseinfo.enums;

/**
 * @author 赵忠诚
 */
public enum OrgLevel {
    china(1, "全国"), province(2, "省级"), city(3, "地市级"), county(4, "区县级"), town(5, "学区、乡镇级"), school(6, "学校");

    private final int identify;

    private final String name;

    private OrgLevel(int identify, String info) {
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
