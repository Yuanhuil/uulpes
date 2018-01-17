package com.njpes.www.entity.scaletoollib;

public class ReportLookStudentFilterParam {

    private String xzxs;// 判断机构层级：区县或者直属学校
    private long orgid;//
    private String nj;// 年级名称
    private String xzqh;// 行政区划
    private String xxmc;// 学校名称
    private String scaleName;// 量表名称
    private String scaleId;
    private String scaleSourceId;
    private String scaleTypeId;
    private String username;// 姓名
    private String gender;// 性别
    private String identiyId;// 身份证
    private String testTime;// 测试用时
    private int warningLevel;// 预警级别
    private int validVal= -1;// 无效问卷
    private String starttime; // 测评开始时间
    private String endtime;// 测评结束时间
    private int xd = -1;
    private String xdmc;
    private int bj = -1;
    private String bjmc;
    private String[] bjarray;
    private String[] xxname;
    private String[] qxarray;
    private String[] zsxxarray;
    private String qxorxxarray;
    private String bjidarray;

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
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

    public int getXd() {
        return xd;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }

    public String getXdmc() {
        return xdmc;
    }

    public void setXdmc(String xdmc) {
        this.xdmc = xdmc;
    }

    public int getBj() {
        return bj;
    }

    public void setBj(int bj) {
        this.bj = bj;
    }

    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }

    public String[] getBjarray() {
        return bjarray;
    }

    public void setBjarray(String[] bjarray) {
        this.bjarray = bjarray;
    }

    public String[] getXxname() {
        return xxname;
    }

    public void setXxname(String[] xxname) {
        this.xxname = xxname;
    }

    public String[] getQxarray() {
        return qxarray;
    }

    public void setQxarray(String[] qxarray) {
        this.qxarray = qxarray;
    }

    public String[] getZsxxarray() {
        return zsxxarray;
    }

    public void setZsxxarray(String[] zsxxarray) {
        this.zsxxarray = zsxxarray;
    }

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

    public String getQxorxxarray() {
        return qxorxxarray;
    }

    public void setQxorxxarray(String qxorxxarray) {
        this.qxorxxarray = qxorxxarray;
    }

    public String getXzxs() {
        return xzxs;
    }

    public void setXzxs(String xzxs) {
        this.xzxs = xzxs;
    }

    public String getBjidarray() {
        return bjidarray;
    }

    public void setBjidarray(String bjidarray) {
        this.bjidarray = bjidarray;
    }

    public int getValidVal() {
        return validVal;
    }

    public void setValidVal(int validVal) {
        this.validVal = validVal;
    }

}
