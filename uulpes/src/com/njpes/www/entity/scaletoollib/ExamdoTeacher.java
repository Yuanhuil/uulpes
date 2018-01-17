package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ExamdoTeacher implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1206325802120735419L;
    private long taskid;
    private long userid;
    private long orgid;
    private long resultid;
    private String xxmc;
    private String xm;// 姓名
    private int roleid;
    private String rolename;
    private String xbm;
    private String csrq;
    private String sfzjh;
    private String xmpy;
    private int questionnum;
    private String scaleid;
    private String scalename;
    private String oktime;
    private String attrs;
    private int taskfrom;
    private int testlimit;
    private String countyid;
    private String cityid;
    private String starttime;
    private String endtime;
    private int normid;

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public long getResultid() {
        return resultid;
    }

    public void setResultid(long resultid) {
        this.resultid = resultid;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getXmpy() {
        return xmpy;
    }

    public void setXmpy(String xmpy) {
        this.xmpy = xmpy;
    }

    public int getQuestionnum() {
        return questionnum;
    }

    public void setQuestionnum(int questionnum) {
        this.questionnum = questionnum;
    }

    public String getScaleid() {
        return scaleid;
    }

    public void setScaleid(String scaleid) {
        this.scaleid = scaleid;
    }

    public String getScalename() {
        return scalename;
    }

    public void setScalename(String scalename) {
        this.scalename = scalename;
    }

    public String getOktime() {
        return oktime;
    }

    public void setOktime(String oktime) {
        this.oktime = oktime;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public int getTaskfrom() {
        return taskfrom;
    }

    public void setTaskfrom(int taskfrom) {
        this.taskfrom = taskfrom;
    }

    public String getCountyid() {
        return countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
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

    public int getTestlimit() {
        return testlimit;
    }

    public void setTestlimit(int testlimit) {
        this.testlimit = testlimit;
    }

    public int getNormid() {
        return normid;
    }

    public void setNormid(int normid) {
        this.normid = normid;
    }

}
