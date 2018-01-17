package com.njpes.www.entity.baseinfo.organization;

import java.io.Serializable;

public class Grade implements Serializable, Comparable<Grade> {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column grade.id
     * 
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column grade.nj
     * 
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    private String nj;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column grade.njmc
     * 
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    private String njmc;

    private String xz;

    private String gradeid;

    private String rxnf;// 入学年份

    public String getRxnf() {
        return rxnf;
    }

    public void setRxnf(String rxnf) {
        this.rxnf = rxnf;
    }

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }

    public String getXz() {
        return xz;
    }

    public void setXz(String xz) {
        this.xz = xz;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column grade.id
     * 
     * @return the value of grade.id
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column grade.id
     * 
     * @param id
     *            the value for grade.id
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column grade.nj
     * 
     * @return the value of grade.nj
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    public String getNj() {
        return nj;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column grade.nj
     * 
     * @param nj
     *            the value for grade.nj
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    public void setNj(String nj) {
        this.nj = nj;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column grade.njmc
     * 
     * @return the value of grade.njmc
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    public String getNjmc() {
        return njmc;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column grade.njmc
     * 
     * @param njmc
     *            the value for grade.njmc
     * @mbggenerated Mon May 25 23:46:40 CST 2015
     */
    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    @Override
    public int compareTo(Grade o) {
        return this.getGradeid().compareTo(o.getGradeid());
    }
}