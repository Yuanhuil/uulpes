package com.njpes.www.entity.baseinfo.enums;

public enum ThemeEnum {
    theme1("theme1", "魅力蓝"), theme2("theme2", "自然绿"), theme3("theme3", "魔力灰");

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

    private ThemeEnum(String id, String info) {
        this.info = info;
        this.id = id;
    }

}
