package com.njpes.www.dao.systeminfo;

import com.njpes.www.entity.systeminfo.DogInfo;

public interface DogInfoMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table DogInfo
     * 
     * @mbggenerated Sun May 22 14:59:45 CST 2016
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table DogInfo
     * 
     * @mbggenerated Sun May 22 14:59:45 CST 2016
     */
    int insert(DogInfo record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table DogInfo
     * 
     * @mbggenerated Sun May 22 14:59:45 CST 2016
     */
    int insertSelective(DogInfo record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table DogInfo
     * 
     * @mbggenerated Sun May 22 14:59:45 CST 2016
     */
    DogInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table DogInfo
     * 
     * @mbggenerated Sun May 22 14:59:45 CST 2016
     */
    int updateByPrimaryKeySelective(DogInfo record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table DogInfo
     * 
     * @mbggenerated Sun May 22 14:59:45 CST 2016
     */
    int updateByPrimaryKey(DogInfo record);
}