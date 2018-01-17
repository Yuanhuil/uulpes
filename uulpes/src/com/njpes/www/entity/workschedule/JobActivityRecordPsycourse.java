package com.njpes.www.entity.workschedule;

import java.io.Serializable;
import java.util.Date;

public class JobActivityRecordPsycourse implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.id
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Long id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.author
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Long author;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.activitytype
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private String activitytype;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.dep
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Long dep;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.level
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private String level;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.speaker
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private String speaker;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.vip_event
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private String vipEvent;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.write_time
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Date writeTime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.audience
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private String audience;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.startactivitytime
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Date startactivitytime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.endactivitytime
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Date endactivitytime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.num
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Integer num;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_activityrecord_psycourse.share
     * 
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    private Boolean share;

    private String joboverview;

    private String typename;

    private String schoolyear;
    private String term;

    private String levelname;
    private String[] fileuuids;

    public String[] getFileuuids() {
        return fileuuids;
    }

    public void setFileuuids(String[] fileuuids) {
        this.fileuuids = fileuuids;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getJoboverview() {
        return joboverview;
    }

    public void setJoboverview(String joboverview) {
        this.joboverview = joboverview;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.id
     * 
     * @return the value of job_activityrecord_psycourse.id
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.id
     * 
     * @param id
     *            the value for job_activityrecord_psycourse.id
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.author
     * 
     * @return the value of job_activityrecord_psycourse.author
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Long getAuthor() {
        return author;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.author
     * 
     * @param author
     *            the value for job_activityrecord_psycourse.author
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setAuthor(Long author) {
        this.author = author;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.activitytype
     * 
     * @return the value of job_activityrecord_psycourse.activitytype
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public String getActivitytype() {
        return activitytype;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.activitytype
     * 
     * @param activitytype
     *            the value for job_activityrecord_psycourse.activitytype
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.dep
     * 
     * @return the value of job_activityrecord_psycourse.dep
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Long getDep() {
        return dep;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.dep
     * 
     * @param dep
     *            the value for job_activityrecord_psycourse.dep
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setDep(Long dep) {
        this.dep = dep;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.level
     * 
     * @return the value of job_activityrecord_psycourse.level
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public String getLevel() {
        return level;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.level
     * 
     * @param level
     *            the value for job_activityrecord_psycourse.level
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.speaker
     * 
     * @return the value of job_activityrecord_psycourse.speaker
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public String getSpeaker() {
        return speaker;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.speaker
     * 
     * @param speaker
     *            the value for job_activityrecord_psycourse.speaker
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.vip_event
     * 
     * @return the value of job_activityrecord_psycourse.vip_event
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public String getVipEvent() {
        return vipEvent;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.vip_event
     * 
     * @param vipEvent
     *            the value for job_activityrecord_psycourse.vip_event
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setVipEvent(String vipEvent) {
        this.vipEvent = vipEvent;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.write_time
     * 
     * @return the value of job_activityrecord_psycourse.write_time
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Date getWriteTime() {
        return writeTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.write_time
     * 
     * @param writeTime
     *            the value for job_activityrecord_psycourse.write_time
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.audience
     * 
     * @return the value of job_activityrecord_psycourse.audience
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public String getAudience() {
        return audience;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.audience
     * 
     * @param audience
     *            the value for job_activityrecord_psycourse.audience
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setAudience(String audience) {
        this.audience = audience;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column
     * job_activityrecord_psycourse.startactivitytime
     * 
     * @return the value of job_activityrecord_psycourse.startactivitytime
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Date getStartactivitytime() {
        return startactivitytime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column
     * job_activityrecord_psycourse.startactivitytime
     * 
     * @param startactivitytime
     *            the value for job_activityrecord_psycourse.startactivitytime
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setStartactivitytime(Date startactivitytime) {
        this.startactivitytime = startactivitytime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.endactivitytime
     * 
     * @return the value of job_activityrecord_psycourse.endactivitytime
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Date getEndactivitytime() {
        return endactivitytime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.endactivitytime
     * 
     * @param endactivitytime
     *            the value for job_activityrecord_psycourse.endactivitytime
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setEndactivitytime(Date endactivitytime) {
        this.endactivitytime = endactivitytime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.num
     * 
     * @return the value of job_activityrecord_psycourse.num
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Integer getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.num
     * 
     * @param num
     *            the value for job_activityrecord_psycourse.num
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_activityrecord_psycourse.share
     * 
     * @return the value of job_activityrecord_psycourse.share
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public Boolean getShare() {
        return share;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_activityrecord_psycourse.share
     * 
     * @param share
     *            the value for job_activityrecord_psycourse.share
     * @mbggenerated Mon Jul 13 21:48:25 CST 2015
     */
    public void setShare(Boolean share) {
        this.share = share;
    }
}