package com.njpes.www.entity.scaletoollib;

public class StatConfigWithBLOBs extends StatConfig {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column stat_param.independent_vars
     *
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    private String independentVars;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column stat_param.dependent_vars
     *
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    private String dependentVars;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column stat_param.step_pages
     *
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    private String stepPages;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column stat_param.independent_vars
     *
     * @return the value of stat_param.independent_vars
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    public String getIndependentVars() {
        return independentVars;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column stat_param.independent_vars
     *
     * @param independentVars
     *            the value for stat_param.independent_vars
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    public void setIndependentVars(String independentVars) {
        this.independentVars = independentVars;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column stat_param.dependent_vars
     *
     * @return the value of stat_param.dependent_vars
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    public String getDependentVars() {
        return dependentVars;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column stat_param.dependent_vars
     *
     * @param dependentVars
     *            the value for stat_param.dependent_vars
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    public void setDependentVars(String dependentVars) {
        this.dependentVars = dependentVars;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column stat_param.step_pages
     *
     * @return the value of stat_param.step_pages
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    public String getStepPages() {
        return stepPages;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column stat_param.step_pages
     *
     * @param stepPages
     *            the value for stat_param.step_pages
     * @mbggenerated Wed Sep 02 22:12:51 CST 2015
     */
    public void setStepPages(String stepPages) {
        this.stepPages = stepPages;
    }
}