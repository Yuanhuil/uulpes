package com.njpes.www.dao.assessmentcenter;

import com.njpes.www.entity.assessmentcenter.InvestResult;

public interface InvestResultMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_result
     *
     * @mbggenerated Thu Jan 07 23:32:33 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_result
     *
     * @mbggenerated Thu Jan 07 23:32:33 CST 2016
     */
    int insert(InvestResult record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_result
     *
     * @mbggenerated Thu Jan 07 23:32:33 CST 2016
     */
    int insertSelective(InvestResult record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_result
     *
     * @mbggenerated Thu Jan 07 23:32:33 CST 2016
     */
    InvestResult selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_result
     *
     * @mbggenerated Thu Jan 07 23:32:33 CST 2016
     */
    int updateByPrimaryKeySelective(InvestResult record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_result
     *
     * @mbggenerated Thu Jan 07 23:32:33 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(InvestResult record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table invest_result
     *
     * @mbggenerated Thu Jan 07 23:32:33 CST 2016
     */
    int updateByPrimaryKey(InvestResult record);
}