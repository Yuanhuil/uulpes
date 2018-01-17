package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class DataManagerExamdoStudent implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8829659431681390425L;
    private long taskid;
    private long userid;
    private long orgid;
    private long resultid;
    private String xxmc;
    private String xm;// 姓名
    private String xbm;
    private String sfzjh;
    private String xh;
    private String xmpy;
    private int xd;
    private int gradeid;
    private String njmc;
    private String bjmc;
    private long bj;
    private int nj;
    private int qustionnum;
    private String scaleid;
    private String scalename;
    private String oktime;
    private String attrs;

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

    public int getQustionnum() {
        return qustionnum;
    }

    public void setQustionnum(int qustionnum) {
        this.qustionnum = qustionnum;
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

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

}
