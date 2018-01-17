package com.njpes.www.dao.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.organization.GradeCode;

public interface GradeCodeMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table gradecode
     *
     * @mbggenerated Sun Oct 11 16:40:59 CST 2015
     */
    int deleteByPrimaryKey(Byte id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table gradecode
     *
     * @mbggenerated Sun Oct 11 16:40:59 CST 2015
     */
    int insert(GradeCode record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table gradecode
     *
     * @mbggenerated Sun Oct 11 16:40:59 CST 2015
     */
    int insertSelective(GradeCode record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table gradecode
     *
     * @mbggenerated Sun Oct 11 16:40:59 CST 2015
     */
    GradeCode selectByPrimaryKey(Byte id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table gradecode
     *
     * @mbggenerated Sun Oct 11 16:40:59 CST 2015
     */
    int updateByPrimaryKeySelective(GradeCode record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table gradecode
     *
     * @mbggenerated Sun Oct 11 16:40:59 CST 2015
     */
    int updateByPrimaryKey(GradeCode record);

    List<GradeCode> getAllGrades();

    int getIdByName(String name);
}