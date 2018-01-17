package com.njpes.www.entity.baseinfo.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 组织机构类型 分为教委和学校
 * 
 * @author 赵忠诚
 */
public enum OrganizationType {

    ec("1", "教育管理机构"), school("2", "学校"), taoststion("3", "陶老师工作站");

    String info;
    String id;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {

        this.info = info;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    private OrganizationType(String id, String info) {
        this.info = info;
        this.id = id;
    }

    public String value() {
        return id;
    }

    public static OrganizationType valueOfStr(String id) {
        if (StringUtils.isBlank(id))
            return null;
        if (StringUtils.equals(id, ec.id)) {
            return ec;
        } else if (StringUtils.equals(id, school.id)) {
            return school;
        } else {
            return taoststion;
        }
    }
}
