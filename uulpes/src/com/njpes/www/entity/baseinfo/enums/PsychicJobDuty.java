package com.njpes.www.entity.baseinfo.enums;

public enum PsychicJobDuty {
    chief("1", "负责人"), normal("2", "工作人员");

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

    private PsychicJobDuty(String id, String info) {
        this.info = info;
        this.id = id;
    }
}
