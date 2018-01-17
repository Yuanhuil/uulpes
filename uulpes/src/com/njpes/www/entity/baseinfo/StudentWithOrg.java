package com.njpes.www.entity.baseinfo;

public class StudentWithOrg extends StudentWithBLOBs {

    /**
     * 单位码
     */
    private String dwm;

    private String dwmc;

    private String njmc;

    private int bjmc;

    public int getBjmc() {
        return bjmc;
    }

    public void setBjmc(int bjmc) {
        this.bjmc = bjmc;
    }

    public String getDwm() {
        return dwm;
    }

    public void setDwm(String dwm) {
        this.dwm = dwm;
    }

    public String getNjmc() {
        return njmc;
    }

    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

}
