package com.njpes.www.entity.scaletoollib;

public class ExamresultStudentWithBLOBs extends ExamresultStudent {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_student.individual_score
     *
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    private String individualScore;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_student.question_score
     *
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    private String questionScore;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_student.dim_score
     *
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    private String dimScore;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_student.individual_score
     *
     * @return the value of examresult_student.individual_score
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    public String getIndividualScore() {
        return individualScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_student.individual_score
     *
     * @param individualScore
     *            the value for examresult_student.individual_score
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    public void setIndividualScore(String individualScore) {
        this.individualScore = individualScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_student.question_score
     *
     * @return the value of examresult_student.question_score
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    public String getQuestionScore() {
        return questionScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_student.question_score
     *
     * @param questionScore
     *            the value for examresult_student.question_score
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    public void setQuestionScore(String questionScore) {
        this.questionScore = questionScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_student.dim_score
     *
     * @return the value of examresult_student.dim_score
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    public String getDimScore() {
        return dimScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_student.dim_score
     *
     * @param dimScore
     *            the value for examresult_student.dim_score
     * @mbggenerated Sat Mar 21 23:18:25 CST 2015
     */
    public void setDimScore(String dimScore) {
        this.dimScore = dimScore;
    }
}