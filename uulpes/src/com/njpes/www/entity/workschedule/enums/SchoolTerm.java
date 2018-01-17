package com.njpes.www.entity.workschedule.enums;

public enum SchoolTerm {
    first("1", "第一学期"), second("2", "第二学期");

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

    private SchoolTerm(String id, String info) {
        this.info = info;
        this.id = id;
    }

    static public SchoolTerm valueById(String id) {
        if (first.id.equals(id))
            return first;
        else if (second.id.equals(id))
            return second;
        else
            return null;
    }
}
