package com.njpes.www.entity.baseinfo.organization;

import java.io.Serializable;
import java.util.List;

public class Educommission implements Serializable {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.id
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.jwdm
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String jwdm;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.jwmc
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String jwmc;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.jwdz
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String jwdz;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.jwfzr
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String jwfzr;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.lxdh
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String lxdh;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.czdh
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String czdh;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.dzxx
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String dzxx;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.zydz
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String zydz;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column educommission.attribate
     *
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    private String attribate;

    private Long orgid;

    private List<Ecpsychicteam> psychicyTeams;

    private List<Ecpsychicjob> psychicyJobs;

    private Organization org;

    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    public Long getOrgid() {
        return orgid;
    }

    public void setOrgid(Long orgid) {
        this.orgid = orgid;
    }

    public List<Ecpsychicteam> getPsychicyTeams() {
        return psychicyTeams;
    }

    public void setPsychicyTeams(List<Ecpsychicteam> psychicyTeams) {
        this.psychicyTeams = psychicyTeams;
    }

    public List<Ecpsychicjob> getPsychicyJobs() {
        return psychicyJobs;
    }

    public void setPsychicyJobs(List<Ecpsychicjob> psychicyJobs) {
        this.psychicyJobs = psychicyJobs;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.id
     *
     * @return the value of educommission.id
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.id
     *
     * @param id
     *            the value for educommission.id
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.jwdm
     *
     * @return the value of educommission.jwdm
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getJwdm() {
        return jwdm;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.jwdm
     *
     * @param jwdm
     *            the value for educommission.jwdm
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setJwdm(String jwdm) {
        this.jwdm = jwdm;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.jwmc
     *
     * @return the value of educommission.jwmc
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getJwmc() {
        return jwmc;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.jwmc
     *
     * @param jwmc
     *            the value for educommission.jwmc
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setJwmc(String jwmc) {
        this.jwmc = jwmc;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.jwdz
     *
     * @return the value of educommission.jwdz
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getJwdz() {
        return jwdz;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.jwdz
     *
     * @param jwdz
     *            the value for educommission.jwdz
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setJwdz(String jwdz) {
        this.jwdz = jwdz;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.jwfzr
     *
     * @return the value of educommission.jwfzr
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getJwfzr() {
        return jwfzr;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.jwfzr
     *
     * @param jwfzr
     *            the value for educommission.jwfzr
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setJwfzr(String jwfzr) {
        this.jwfzr = jwfzr;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.lxdh
     *
     * @return the value of educommission.lxdh
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getLxdh() {
        return lxdh;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.lxdh
     *
     * @param lxdh
     *            the value for educommission.lxdh
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.czdh
     *
     * @return the value of educommission.czdh
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getCzdh() {
        return czdh;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.czdh
     *
     * @param czdh
     *            the value for educommission.czdh
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setCzdh(String czdh) {
        this.czdh = czdh;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.dzxx
     *
     * @return the value of educommission.dzxx
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getDzxx() {
        return dzxx;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.dzxx
     *
     * @param dzxx
     *            the value for educommission.dzxx
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.zydz
     *
     * @return the value of educommission.zydz
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getZydz() {
        return zydz;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.zydz
     *
     * @param zydz
     *            the value for educommission.zydz
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setZydz(String zydz) {
        this.zydz = zydz;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column educommission.attribate
     *
     * @return the value of educommission.attribate
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public String getAttribate() {
        return attribate;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column educommission.attribate
     *
     * @param attribate
     *            the value for educommission.attribate
     * @mbggenerated Sat May 09 22:26:21 CST 2015
     */
    public void setAttribate(String attribate) {
        this.attribate = attribate;
    }
}