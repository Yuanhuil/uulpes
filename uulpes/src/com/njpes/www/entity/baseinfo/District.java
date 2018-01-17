package com.njpes.www.entity.baseinfo;

import java.io.Serializable;

public class District implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8205086255179245413L;
    private String code;// 行政区编码
    private String parentcode;// 上级行政区编码
    private String name;// 行政区名称
    private int level;// 行政区级别
    private boolean islast;// 是否是最后一级（即是否乡镇、街道）

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentcode() {
        return parentcode;
    }

    public void setParentcode(String parentcode) {
        this.parentcode = parentcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isIslast() {
        return islast;
    }

    public void setIslast(boolean islast) {
        this.islast = islast;
    }

}
