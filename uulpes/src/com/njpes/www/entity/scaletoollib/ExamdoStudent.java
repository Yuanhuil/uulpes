package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ExamdoStudent implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2411169826304710654L;
    private long id;
    private long taskid;
    private long userid;
    private long orgid;
    private long resultid;
    private String xxmc;
    private String xm;// 姓名
    private String xbm;
    private String csrq;
    private String sfzjh;
    private String xh;
    private String xmpy;
    private int xd;
    private int gradeid;
    private int realgradeid;
    private String njmc;
    private String bjmc;
    private long bj;
    private int nj;
    private int questionnum;
    private String scaleid;
    private String scalename;
    private String starttime;
    private String endtime;
    private String oktime;
    private int studentvisible;
    private int teachervisible;
    private int parentvisible;
    private int warnvisible;
    private int testlimit;
    private String attrs;
    private boolean ifmh;
    private int statusflag;
    private String countyid;
    private String cityid;
    private int taskfrom;

    private String bzrid;// 班主任id
    private String parentid;// 家长id

    private int normid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public long getResultid() {
        return resultid;
    }

    public void setResultid(long resultid) {
        this.resultid = resultid;
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

    public String getNjmc() {
        return njmc;
    }

    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }

    public long getBj() {
        return bj;
    }

    public void setBj(long bj) {
        this.bj = bj;
    }

    public int getNj() {
        return nj;
    }

    public void setNj(int nj) {
        this.nj = nj;
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

    public String getOktime() {
        return oktime;
    }

    public void setOktime(String oktime) {
        this.oktime = oktime;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXmpy() {
        return xmpy;
    }

    public void setXmpy(String xmpy) {
        this.xmpy = xmpy;
    }

    public int getXd() {
        return xd;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public int getGradeid() {
        return gradeid;
    }

    public void setGradeid(int grade) {
        this.gradeid = grade;
    }

    public int getRealgradeid() {
        return realgradeid;
    }

    public void setRealgradeid(int realgradeid) {
        this.realgradeid = realgradeid;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public int getStudentvisible() {
        return studentvisible;
    }

    public void setStudentvisible(int studentvisible) {
        this.studentvisible = studentvisible;
    }

    public int getTeachervisible() {
        return teachervisible;
    }

    public void setTeachervisible(int teachervisible) {
        this.teachervisible = teachervisible;
    }

    public int getParentvisible() {
        return parentvisible;
    }

    public void setParentvisible(int parentvisible) {
        this.parentvisible = parentvisible;
    }

    public int getWarnvisible() {
        return warnvisible;
    }

    public void setWarnvisible(int warnvisible) {
        this.warnvisible = warnvisible;
    }

    public int getTestlimit() {
        return testlimit;
    }

    public void setTestlimit(int testlimit) {
        this.testlimit = testlimit;
    }

    public boolean isIfmh() {
        return ifmh;
    }

    public void setIfmh(boolean ifmh) {
        this.ifmh = ifmh;
    }

    public int getStatusflag() {
        return statusflag;
    }

    public void setStatusflag(int statusflag) {
        this.statusflag = statusflag;
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

    public int getTaskfrom() {
        return taskfrom;
    }

    public void setTaskfrom(int taskfrom) {
        this.taskfrom = taskfrom;
    }

    public String getBzrid() {
        return bzrid;
    }

    public void setBzrid(String bzrid) {
        this.bzrid = bzrid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public int getNormid() {
        return normid;
    }

    public void setNormid(int normid) {
        this.normid = normid;
    }

}
