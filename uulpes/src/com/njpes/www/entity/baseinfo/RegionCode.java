package com.njpes.www.entity.baseinfo;

import org.apache.commons.lang3.StringUtils;

import com.njpes.www.entity.baseinfo.enums.OrgLevelEnum;

public class RegionCode {
    private final String code;

    public String getCode() {
        return code;
    }

    public RegionCode(String code) {
        super();
        this.code = StringUtils.rightPad(code, 6, '0');
    }

    public OrgLevelEnum getOrganizationLeve() {
        if (getCode().endsWith("0000")) {
            return OrgLevelEnum.PROV_LEV;
        } else if (getCode().endsWith("00")) {
            return OrgLevelEnum.CITY_LEV;
        } else {
            return OrgLevelEnum.DIST_LEV;
        }
    }

    public String[] splitCode() {
        String[] result = new String[3];
        result[0] = getCode().substring(0, 2);// province; // 省码
        result[1] = getCode().substring(2, 4);// city // 地区码
        result[2] = getCode().substring(4); // DISTRICT 区县码
        return result;
    }

    public String getCompleteProvCode() {
        return getCode().substring(0, 2) + "0000";
    }

    public String getCompleteCityCode() {
        return getCode().substring(0, 4) + "00";
    }

    public String getCompleteDistCode() {
        return getCode();
    }
}
