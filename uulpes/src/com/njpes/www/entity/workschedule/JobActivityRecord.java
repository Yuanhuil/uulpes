package com.njpes.www.entity.workschedule;

import java.util.Date;

public class JobActivityRecord {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord.id
     * 
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    private Long id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord.tablename
     * 
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    private String tablename;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord.starttime
     * 
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    private Date starttime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord.endtime
     * 
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    private Date endtime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord.activitycatalog
     * 
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    private String activitycatalog;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord.activitytype
     * 
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    private String activitytype;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord.dep
     * 
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    private Long dep;

    private String vipEvent;

    private String joboverview;

    private Long author;
    private String schoolyear;
    private String term;

    private String depname;

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }

    public String getSchoolyear() {
        return schoolyear;
    }

    public void setSchoolyear(String schoolyear) {
        this.schoolyear = schoolyear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public String getVipEvent() {
        return vipEvent;
    }

    public void setVipEvent(String vipEvent) {
        this.vipEvent = vipEvent;
    }

    public String getJoboverview() {
        return joboverview;
    }

    public void setJoboverview(String joboverview) {
        this.joboverview = joboverview;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord.id
     * 
     * @return the value of job_activityrecord.id
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord.id
     * 
     * @param id
     *            the value for job_activityrecord.id
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord.tablename
     * 
     * @return the value of job_activityrecord.tablename
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public String getTablename() {
        return tablename;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord.tablename
     * 
     * @param tablename
     *            the value for job_activityrecord.tablename
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord.starttime
     * 
     * @return the value of job_activityrecord.starttime
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord.starttime
     * 
     * @param starttime
     *            the value for job_activityrecord.starttime
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord.endtime
     * 
     * @return the value of job_activityrecord.endtime
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord.endtime
     * 
     * @param endtime
     *            the value for job_activityrecord.endtime
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord.activitycatalog
     * 
     * @return the value of job_activityrecord.activitycatalog
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public String getActivitycatalog() {
        return activitycatalog;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord.activitycatalog
     * 
     * @param activitycatalog
     *            the value for job_activityrecord.activitycatalog
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public void setActivitycatalog(String activitycatalog) {
        this.activitycatalog = activitycatalog;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord.activitytype
     * 
     * @return the value of job_activityrecord.activitytype
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public String getActivitytype() {
        return activitytype;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord.activitytype
     * 
     * @param activitytype
     *            the value for job_activityrecord.activitytype
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord.dep
     * 
     * @return the value of job_activityrecord.dep
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public Long getDep() {
        return dep;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord.dep
     * 
     * @param dep
     *            the value for job_activityrecord.dep
     * @mbggenerated Wed Jul 22 00:29:25 CST 2015
     */
    public void setDep(Long dep) {
        this.dep = dep;
    }
}