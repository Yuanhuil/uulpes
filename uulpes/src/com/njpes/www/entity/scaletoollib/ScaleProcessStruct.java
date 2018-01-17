package com.njpes.www.entity.scaletoollib;

public class ScaleProcessStruct {

    private String scaleid;
    private String scaleName;
    private long orgid;
    private String orgName;
    private String cityid;
    private String cityName;
    private String countyid;
    private String countyName;
    private int nj;
    private int gradeid;
    private String gradeName;
    private long bj;
    private String className;
    private int roleid;
    private String roleName;
    private int totalPerson;
    private int noTestPerson;
    private int testPerson;
    private String percentage;
    // 也需要根据taskId过滤
    private int taskId;
    private int taskfrom;

    public String getScaleid() {
        return scaleid;
    }

    public void setScaleid(String scaleid) {
        this.scaleid = scaleid;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTotalPerson() {
        return totalPerson;
    }

    public void setTotalPerson(int totalPerson) {
        this.totalPerson = totalPerson;
    }

    public int getNoTestPerson() {
        return noTestPerson;
    }

    public void setNoTestPerson(int noTestPerson) {
        this.noTestPerson = noTestPerson;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyid() {
        return countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getGradeid() {
        return gradeid;
    }

    public void setGradeid(int gradeid) {
        this.gradeid = gradeid;
    }

    public int getNj() {
        return nj;
    }

    public void setNj(int nj) {
        this.nj = nj;
    }

    public long getBj() {
        return bj;
    }

    public void setBj(long bj) {
        this.bj = bj;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getTestPerson() {
        return testPerson;
    }

    public void setTestPerson(int testPerson) {
        this.testPerson = testPerson;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskfrom() {
        return taskfrom;
    }

    public void setTaskfrom(int taskfrom) {
        this.taskfrom = taskfrom;
    }

}
