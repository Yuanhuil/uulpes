package com.njpes.www.entity.baseinfo;

import java.io.Serializable;

public class Parent implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String gxm;// 成员关系
    private String cyxm;// 成员姓名
    private String xbm;// 性别
    private String csny;// 出生年月
    private String mzm;// 民族
    private String gjdqm;// 国籍地区
    private String jkzkm;// 健康状况
    private String cygzdw;// 成员工作单位
    private String cyem;// 从业
    private String zyjszwm;// 专业技术职务
    private String zwjbm;// 职务级别
    private String dh;// 电话
    private String dzxx;// 电子信箱
    private String sfjhr;// 是否监护人
    private String xlm;// 学历
    private String lxdz;// 联系地址
    private String sjhm;// 手机号码

    private Long accountId;
    private Long studentAccountId;
    private String bjxx;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGxm() {
        return gxm;
    }

    public void setGxm(String gxm) {
        this.gxm = gxm;
    }

    public String getCyxm() {
        return cyxm;
    }

    public void setCyxm(String cyxm) {
        this.cyxm = cyxm;
    }

    public String getCsny() {
        return csny;
    }

    public void setCsny(String csny) {
        this.csny = csny;
    }

    public String getMzm() {
        return mzm;
    }

    public void setMzm(String mzm) {
        this.mzm = mzm;
    }

    public String getGjdqm() {
        return gjdqm;
    }

    public void setGjdqm(String gjdqm) {
        this.gjdqm = gjdqm;
    }

    public String getJkzkm() {
        return jkzkm;
    }

    public void setJkzkm(String jkzkm) {
        this.jkzkm = jkzkm;
    }

    public String getCygzdw() {
        return cygzdw;
    }

    public void setCygzdw(String cygzdw) {
        this.cygzdw = cygzdw;
    }

    public String getCyem() {
        return cyem;
    }

    public void setCyem(String cyem) {
        this.cyem = cyem;
    }

    public String getZyjszwm() {
        return zyjszwm;
    }

    public void setZyjszwm(String zyjszwm) {
        this.zyjszwm = zyjszwm;
    }

    public String getZwjbm() {
        return zwjbm;
    }

    public void setZwjbm(String zwjbm) {
        this.zwjbm = zwjbm;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getDzxx() {
        return dzxx;
    }

    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }

    public String getSfjhr() {
        return sfjhr;
    }

    public void setSfjhr(String sfjhr) {
        this.sfjhr = sfjhr;
    }

    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getXlm() {
        return xlm;
    }

    public void setXlm(String xlm) {
        this.xlm = xlm;
    }

    public String getLxdz() {
        return lxdz;
    }

    public void setLxdz(String lxdz) {
        this.lxdz = lxdz;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getStudentAccountId() {
        return studentAccountId;
    }

    public void setStudentAccountId(Long studentAccountId) {
        this.studentAccountId = studentAccountId;
    }

    public String getBjxx() {
        return bjxx;
    }

    public void setBjxx(String bjxx) {
        this.bjxx = bjxx;
    }
}