package com.njpes.www.entity.workschedule;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class JobNotice implements Serializable {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.id
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.name
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.catalog
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private String catalog;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.attachment
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Boolean attachment;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.share
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private String share;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.author
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Long author;

    private String authorName;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.write_time
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Date writeTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.dep
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Long dep;

    private String depName;

    private String catname;// 分类名称
    private String staname;// 状态名称

    private String[] fileuuids;

    public String[] getFileuuids() {
        return fileuuids;
    }

    public void setFileuuids(String[] fileuuids) {
        this.fileuuids = fileuuids;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getStaname() {
        return staname;
    }

    public void setStaname(String staname) {
        this.staname = staname;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.author_role
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Long authorRole;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.modified
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Date modified;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.adult_time
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private Date adultTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.state
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private String state;

    private List<JobNoticeShare> jobNoticeShareList;

    public List<JobNoticeShare> getJobNoticeShareList() {
        return jobNoticeShareList;
    }

    public void setJobNoticeShareList(List<JobNoticeShare> jobNoticeShareList) {
        this.jobNoticeShareList = jobNoticeShareList;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.id
     *
     * @return the value of job_notice.id
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.id
     *
     * @param id
     *            the value for job_notice.id
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.name
     *
     * @return the value of job_notice.name
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.name
     *
     * @param name
     *            the value for job_notice.name
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.catalog
     *
     * @return the value of job_notice.catalog
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.catalog
     *
     * @param catalog
     *            the value for job_notice.catalog
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.attachment
     *
     * @return the value of job_notice.attachment
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Boolean getAttachment() {
        return attachment;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.attachment
     *
     * @param attachment
     *            the value for job_notice.attachment
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setAttachment(Boolean attachment) {
        this.attachment = attachment;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.share
     *
     * @return the value of job_notice.share
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public String getShare() {
        return share;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.share
     *
     * @param share
     *            the value for job_notice.share
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setShare(String share) {
        this.share = share;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.author
     *
     * @return the value of job_notice.author
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Long getAuthor() {
        return author;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.author
     *
     * @param author
     *            the value for job_notice.author
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setAuthor(Long author) {
        this.author = author;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.write_time
     *
     * @return the value of job_notice.write_time
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Date getWriteTime() {
        return writeTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.write_time
     *
     * @param writeTime
     *            the value for job_notice.write_time
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.dep
     *
     * @return the value of job_notice.dep
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Long getDep() {
        return dep;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.dep
     *
     * @param dep
     *            the value for job_notice.dep
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setDep(Long dep) {
        this.dep = dep;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.author_role
     *
     * @return the value of job_notice.author_role
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Long getAuthorRole() {
        return authorRole;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.author_role
     *
     * @param authorRole
     *            the value for job_notice.author_role
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setAuthorRole(Long authorRole) {
        this.authorRole = authorRole;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.modified
     *
     * @return the value of job_notice.modified
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Date getModified() {
        return modified;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.modified
     *
     * @param modified
     *            the value for job_notice.modified
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.adult_time
     *
     * @return the value of job_notice.adult_time
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public Date getAdultTime() {
        return adultTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.adult_time
     *
     * @param adultTime
     *            the value for job_notice.adult_time
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setAdultTime(Date adultTime) {
        this.adultTime = adultTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.state
     *
     * @return the value of job_notice.state
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.state
     *
     * @param state
     *            the value for job_notice.state
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setState(String state) {
        this.state = state;
    }
}