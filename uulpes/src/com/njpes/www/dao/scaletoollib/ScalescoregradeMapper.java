package com.njpes.www.dao.scaletoollib;

import com.njpes.www.entity.scaletoollib.Scalescoregrade;

public interface ScalescoregradeMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_scoregrade
     *
     * @mbggenerated Sat Mar 21 23:15:05 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_scoregrade
     *
     * @mbggenerated Sat Mar 21 23:15:05 CST 2015
     */
    int insert(Scalescoregrade record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_scoregrade
     *
     * @mbggenerated Sat Mar 21 23:15:05 CST 2015
     */
    int insertSelective(Scalescoregrade record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_scoregrade
     *
     * @mbggenerated Sat Mar 21 23:15:05 CST 2015
     */
    Scalescoregrade selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_scoregrade
     *
     * @mbggenerated Sat Mar 21 23:15:05 CST 2015
     */
    int updateByPrimaryKeySelective(Scalescoregrade record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table scale_scoregrade
     *
     * @mbggenerated Sat Mar 21 23:15:05 CST 2015
     */
    int updateByPrimaryKey(Scalescoregrade record);
}