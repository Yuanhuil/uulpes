package com.njpes.www.dao.baseinfo;

import com.njpes.www.entity.baseinfo.Job;

public interface JobMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job
     *
     * @mbggenerated Sat Mar 21 22:55:16 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job
     *
     * @mbggenerated Sat Mar 21 22:55:16 CST 2015
     */
    int insert(Job record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job
     *
     * @mbggenerated Sat Mar 21 22:55:16 CST 2015
     */
    int insertSelective(Job record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job
     *
     * @mbggenerated Sat Mar 21 22:55:16 CST 2015
     */
    Job selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job
     *
     * @mbggenerated Sat Mar 21 22:55:16 CST 2015
     */
    int updateByPrimaryKeySelective(Job record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job
     *
     * @mbggenerated Sat Mar 21 22:55:16 CST 2015
     */
    int updateByPrimaryKey(Job record);
}