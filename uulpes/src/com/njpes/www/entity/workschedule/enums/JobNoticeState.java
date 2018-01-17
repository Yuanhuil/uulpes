package com.njpes.www.entity.workschedule.enums;

import org.apache.commons.lang3.StringUtils;

public enum JobNoticeState {
    no_submit("1", "未提交"), no_check("2", "未审阅"), passed("3", "通过"), no_passed("4", "未通过"), issued("5", "已下发");

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

    private JobNoticeState(String id, String info) {
        this.info = info;
        this.id = id;
    }

    public static JobNoticeState valueOfKey(String value) {
        if (StringUtils.equals(value, "1")) {
            return no_submit;
        } else if (StringUtils.equals(value, "2")) {
            return no_check;
        } else if (StringUtils.equals(value, "3")) {
            return passed;
        } else if (StringUtils.equals(value, "4")) {
            return no_passed;
        } else if (StringUtils.equals(value, "5")) {
            return issued;
        } else
            return null;
    }

    public String value() {
        return this.id;
    }
}
