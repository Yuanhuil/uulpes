package com.njpes.www.entity.workschedule;

public class JobActivityRecordWithBLOBsQuery extends JobActivityRecordWithBLOBs {

    private String provinceid;

    private String cityid;

    private String countyid;

    private String townid;

    private Integer orgLevel;

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getTownid() {
        return townid;
    }

    public void setTownid(String townid) {
        this.townid = townid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCountyid() {
        return countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid;
    }

}
