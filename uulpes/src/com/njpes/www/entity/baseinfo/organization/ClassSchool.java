package com.njpes.www.entity.baseinfo.organization;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClassSchool implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7643523056754018682L;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.id
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.bh
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String bh;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.bjmc
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String bjmc;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.jbny
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String jbny;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.bzrgh
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String bzrgh;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.bzxh
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String bzxh;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.bjrych
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String bjrych;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.xz
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private BigDecimal xz;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.bjlxm
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String bjlxm;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.wllx
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String wllx;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.byrq
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String byrq;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.sfssmzsyjxb
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String sfssmzsyjxb;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.syjxmsm
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String syjxmsm;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column class.nj
     * 
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    private String nj;

    private String xxdm;// 学校代码

    private String xxmc;

    private String njmc;

    private Integer gradeid;// gradecode 表id

    private long xxorgid;

    private long bzraccountid;
    
    private long zyid; //专业类型
    
    public long getZyid() {
        return zyid;
    }

    public void setZyid(long zyid) {
        this.zyid = zyid;
    }

    private int floze_flag;
    private String flozen_date;

    public long getBzraccountid() {
        return bzraccountid;
    }

    public void setBzraccountid(long bzraccountid) {
        this.bzraccountid = bzraccountid;
    }

    public long getXxorgid() {
        return xxorgid;
    }

    public void setXxorgid(long xxorgid) {
        this.xxorgid = xxorgid;
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

    public Integer getGradeid() {
        return gradeid;
    }

    public void setGradeid(Integer gradeid) {
        this.gradeid = gradeid;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.id
     * 
     * @return the value of class.id
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.id
     * 
     * @param id
     *            the value for class.id
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.bh
     * 
     * @return the value of class.bh
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getBh() {
        return bh;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.bh
     * 
     * @param bh
     *            the value for class.bh
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setBh(String bh) {
        this.bh = bh;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.bjmc
     * 
     * @return the value of class.bjmc
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getBjmc() {
        return bjmc;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.bjmc
     * 
     * @param bjmc
     *            the value for class.bjmc
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.jbny
     * 
     * @return the value of class.jbny
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getJbny() {
        return jbny;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.jbny
     * 
     * @param jbny
     *            the value for class.jbny
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setJbny(String jbny) {
        this.jbny = jbny;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.bzrgh
     * 
     * @return the value of class.bzrgh
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getBzrgh() {
        return bzrgh;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.bzrgh
     * 
     * @param bzrgh
     *            the value for class.bzrgh
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setBzrgh(String bzrgh) {
        this.bzrgh = bzrgh;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.bzxh
     * 
     * @return the value of class.bzxh
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getBzxh() {
        return bzxh;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.bzxh
     * 
     * @param bzxh
     *            the value for class.bzxh
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setBzxh(String bzxh) {
        this.bzxh = bzxh;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.bjrych
     * 
     * @return the value of class.bjrych
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getBjrych() {
        return bjrych;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.bjrych
     * 
     * @param bjrych
     *            the value for class.bjrych
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setBjrych(String bjrych) {
        this.bjrych = bjrych;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.xz
     * 
     * @return the value of class.xz
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public BigDecimal getXz() {
        return xz;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.xz
     * 
     * @param xz
     *            the value for class.xz
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setXz(BigDecimal xz) {
        this.xz = xz;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.bjlxm
     * 
     * @return the value of class.bjlxm
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getBjlxm() {
        return bjlxm;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.bjlxm
     * 
     * @param bjlxm
     *            the value for class.bjlxm
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setBjlxm(String bjlxm) {
        this.bjlxm = bjlxm;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.wllx
     * 
     * @return the value of class.wllx
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getWllx() {
        return wllx;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.wllx
     * 
     * @param wllx
     *            the value for class.wllx
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setWllx(String wllx) {
        this.wllx = wllx;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.byrq
     * 
     * @return the value of class.byrq
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getByrq() {
        return byrq;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.byrq
     * 
     * @param byrq
     *            the value for class.byrq
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setByrq(String byrq) {
        this.byrq = byrq;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.sfssmzsyjxb
     * 
     * @return the value of class.sfssmzsyjxb
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getSfssmzsyjxb() {
        return sfssmzsyjxb;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.sfssmzsyjxb
     * 
     * @param sfssmzsyjxb
     *            the value for class.sfssmzsyjxb
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setSfssmzsyjxb(String sfssmzsyjxb) {
        this.sfssmzsyjxb = sfssmzsyjxb;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.syjxmsm
     * 
     * @return the value of class.syjxmsm
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getSyjxmsm() {
        return syjxmsm;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.syjxmsm
     * 
     * @param syjxmsm
     *            the value for class.syjxmsm
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setSyjxmsm(String syjxmsm) {
        this.syjxmsm = syjxmsm;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column class.nj
     * 
     * @return the value of class.nj
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public String getNj() {
        return nj;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column class.nj
     * 
     * @param nj
     *            the value for class.nj
     * @mbggenerated Tue May 26 20:30:44 CST 2015
     */
    public void setNj(String nj) {
        this.nj = nj;
    }

    public int getFloze_flag() {
        return floze_flag;
    }

    public void setFloze_flag(int floze_flag) {
        this.floze_flag = floze_flag;
    }

    public String getFlozen_date() {
        return flozen_date;
    }

    public void setFlozen_date(String flozen_date) {
        this.flozen_date = flozen_date;
    }

}