package com.njpes.www.entity.workschedule;

public class JobAttachmentMapping {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_attachment_mapping.id
     *
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_attachment_mapping.sort
     *
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    private Integer sort;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_attachment_mapping.fid
     *
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    private Long fid;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_attachment_mapping.jobfileid
     *
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    private Long jobfileid;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_attachment_mapping.resource
     *
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    private String resource;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_attachment_mapping.id
     *
     * @return the value of job_attachment_mapping.id
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */

    private JobAttachment jobattachment;

    public JobAttachment getJobattachment() {
        return jobattachment;
    }

    public void setJobattachment(JobAttachment jobattachment) {
        this.jobattachment = jobattachment;
    }

    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_attachment_mapping.id
     *
     * @param id
     *            the value for job_attachment_mapping.id
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_attachment_mapping.sort
     *
     * @return the value of job_attachment_mapping.sort
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_attachment_mapping.sort
     *
     * @param sort
     *            the value for job_attachment_mapping.sort
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_attachment_mapping.fid
     *
     * @return the value of job_attachment_mapping.fid
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public Long getFid() {
        return fid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_attachment_mapping.fid
     *
     * @param fid
     *            the value for job_attachment_mapping.fid
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public void setFid(Long fid) {
        this.fid = fid;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_attachment_mapping.jobfileid
     *
     * @return the value of job_attachment_mapping.jobfileid
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public Long getJobfileid() {
        return jobfileid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_attachment_mapping.jobfileid
     *
     * @param jobfileid
     *            the value for job_attachment_mapping.jobfileid
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public void setJobfileid(Long jobfileid) {
        this.jobfileid = jobfileid;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_attachment_mapping.resource
     *
     * @return the value of job_attachment_mapping.resource
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public String getResource() {
        return resource;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_attachment_mapping.resource
     *
     * @param resource
     *            the value for job_attachment_mapping.resource
     * @mbggenerated Thu Dec 10 01:07:12 CST 2015
     */
    public void setResource(String resource) {
        this.resource = resource;
    }
}