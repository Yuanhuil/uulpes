package com.njpes.www.dao.assessmentcenter;

import com.njpes.www.entity.assessmentcenter.InvestReport;

public interface InvestReportMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_report
     *
     * @mbggenerated Sun Jun 07 14:36:58 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_report
     *
     * @mbggenerated Sun Jun 07 14:36:58 CST 2015
     */
    int insert(InvestReport record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_report
     *
     * @mbggenerated Sun Jun 07 14:36:58 CST 2015
     */
    int insertSelective(InvestReport record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_report
     *
     * @mbggenerated Sun Jun 07 14:36:58 CST 2015
     */
    InvestReport selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_report
     *
     * @mbggenerated Sun Jun 07 14:36:58 CST 2015
     */
    int updateByPrimaryKeySelective(InvestReport record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_report
     *
     * @mbggenerated Sun Jun 07 14:36:58 CST 2015
     */
    int updateByPrimaryKey(InvestReport record);
}