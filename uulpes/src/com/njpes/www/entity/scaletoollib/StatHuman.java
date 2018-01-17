package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class StatHuman implements Serializable {

    private Long id;
    // 学段
    private int xd;
    // 班级名称
    private String bjmch;
    // 学校代码
    private String xxdm;
    // 学校名称
    private String xxmc;
    // 年级名称
    private String njmc;
    // 年级代码id
    private int gradecodeid;
    // 组织机构id
    private long orgid;
    // 班级id
    private long bjid;

    private long accountId;

    private String bjxx;

    public String getBjxx() {
        return bjxx;
    }

    public void setBjxx(String bjxx) {
        this.bjxx = bjxx;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * 组织机构(学校)id
     */
    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    /** 班级id */
    public long getBjid() {
        return bjid;
    }

    public void setBjid(long bjid) {
        this.bjid = bjid;
    }

    public int getGradecodeid() {
        return gradecodeid;
    }

    public void setGradecodeid(int gradecodeid) {
        this.gradecodeid = gradecodeid;
    }

    public String getBjmch() {
        return bjmch;
    }

    public void setBjmch(String bjmc) {
        this.bjmch = bjmc;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public String getNjmc() {
        return njmc;
    }

    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getXd() {
        return xd;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }
}
