package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ExamTempAnswer implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column exam_answer_0.id
     *
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column exam_answer_0.qid
     *
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    private String qid;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column exam_answer_0.answer
     *
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    private String answer;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column exam_answer_0.id
     *
     * @return the value of exam_answer_0.id
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */

    // private String table;
    private Integer qindex;
    private Integer scaleid;
    private Integer countdown;

    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column exam_answer_0.id
     *
     * @param id
     *            the value for exam_answer_0.id
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column exam_answer_0.qid
     *
     * @return the value of exam_answer_0.qid
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    public String getQid() {
        return qid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column exam_answer_0.qid
     *
     * @param qid
     *            the value for exam_answer_0.qid
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    public void setQid(String qid) {
        this.qid = qid;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column exam_answer_0.answer
     *
     * @return the value of exam_answer_0.answer
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column exam_answer_0.answer
     *
     * @param answer
     *            the value for exam_answer_0.answer
     * @mbggenerated Wed Jun 29 18:31:58 CST 2016
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getQindex() {
        return qindex;
    }

    public void setQindex(Integer qindex) {
        this.qindex = qindex;
    }

    public Integer getScaleid() {
        return scaleid;
    }

    public void setScaleid(Integer scaleid) {
        this.scaleid = scaleid;
    }

    public Integer getCountdown() {
        return countdown;
    }

    public void setCountdown(Integer countdown) {
        this.countdown = countdown;
    }

    /*
     * public String getTable() { return table; }
     * 
     * public void setTable(String table) { this.table = table; }
     */

}