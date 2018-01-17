package com.njpes.www.entity.scaletoollib;

public class ReportLookTeacherFilterParam {

    private long orgid;
    private String roleid = "-1";
    private String roleName;
    private String scaleName;
    private String scaleId;
    private String scaleSourceId;
    private String scaleTypeId;
    private String xzqh;// 行政区划
    private String xxmc;// 学校名称
    private String username;// 姓名
    private String gender;// 性别
    private String identiyId;// 身份证
    private String testTime;// 测试用时
    private String okTime;
    private int warningLevel;// 预警级别
    private int validVal = -1;// 无效问卷
    private String starttime; // 测评开始时间
    private String endtime;// 测评结束时间

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public String getScaleSourceId() {
        return scaleSourceId;
    }

    public void setScaleSourceId(String scaleSourceId) {
        this.scaleSourceId = scaleSourceId;
    }

    public String getScaleTypeId() {
        return scaleTypeId;
    }

    public void setScaleTypeId(String scaleTypeId) {
        this.scaleTypeId = scaleTypeId;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentiyId() {
        return identiyId;
    }

    public void setIdentiyId(String identiyId) {
        this.identiyId = identiyId;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public int getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(int warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getOkTime() {
        return okTime;
    }

    public void setOkTime(String okTime) {
        this.okTime = okTime;
    }

    public String getXzqh() {
        return xzqh;
    }

    public void setXzqh(String xzqh) {
        this.xzqh = xzqh;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public int getValidVal() {
        return validVal;
    }

    public void setValidVal(int validVal) {
        this.validVal = validVal;
    }

}
