package com.njpes.www.entity.scaletoollib;

public class ExamresultIndividualWithBLOBs extends ExamresultIndividual {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_individual.individual_score
     *
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    private String individualScore;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_individual.question_score
     *
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    private String questionScore;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examresult_individual.dim_score
     *
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    private String dimScore;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_individual.individual_score
     *
     * @return the value of examresult_individual.individual_score
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    public String getIndividualScore() {
        return individualScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_individual.individual_score
     *
     * @param individualScore
     *            the value for examresult_individual.individual_score
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    public void setIndividualScore(String individualScore) {
        this.individualScore = individualScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_individual.question_score
     *
     * @return the value of examresult_individual.question_score
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    public String getQuestionScore() {
        return questionScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_individual.question_score
     *
     * @param questionScore
     *            the value for examresult_individual.question_score
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    public void setQuestionScore(String questionScore) {
        this.questionScore = questionScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examresult_individual.dim_score
     *
     * @return the value of examresult_individual.dim_score
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    public String getDimScore() {
        return dimScore;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examresult_individual.dim_score
     *
     * @param dimScore
     *            the value for examresult_individual.dim_score
     * @mbggenerated Sat Mar 21 23:18:02 CST 2015
     */
    public void setDimScore(String dimScore) {
        this.dimScore = dimScore;
    }
}