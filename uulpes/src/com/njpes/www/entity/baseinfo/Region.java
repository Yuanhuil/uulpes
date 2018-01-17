package com.njpes.www.entity.baseinfo;

import java.io.Serializable;

import com.njpes.www.entity.baseinfo.enums.OrgLevelEnum;

public class Region implements Serializable {
    private final RegionCode regionCode;
    private String name;
    private String provName;
    private String cityName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region(RegionCode regionCode) {
        this.regionCode = regionCode;
    }

    public String[] splitCode() {
        return regionCode.splitCode();
    }

    public String getCode() {
        return regionCode.getCode();
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public OrgLevelEnum getOrganizationLeve() {
        return regionCode.getOrganizationLeve();
    }
}
