package com.njpes.www.dao.scaletoollib;

import com.njpes.www.entity.scaletoollib.ExamresultDimTeacher;

public interface ExamresultDimTeacherMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examresult_dim_teacher
     *
     * @mbggenerated Sat Mar 21 23:17:35 CST 2015
     */
    int deleteByPrimaryKey(Long resultId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examresult_dim_teacher
     *
     * @mbggenerated Sat Mar 21 23:17:35 CST 2015
     */
    int insert(ExamresultDimTeacher record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examresult_dim_teacher
     *
     * @mbggenerated Sat Mar 21 23:17:35 CST 2015
     */
    int insertSelective(ExamresultDimTeacher record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examresult_dim_teacher
     *
     * @mbggenerated Sat Mar 21 23:17:35 CST 2015
     */
    ExamresultDimTeacher selectByPrimaryKey(Long resultId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examresult_dim_teacher
     *
     * @mbggenerated Sat Mar 21 23:17:35 CST 2015
     */
    int updateByPrimaryKeySelective(ExamresultDimTeacher record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examresult_dim_teacher
     *
     * @mbggenerated Sat Mar 21 23:17:35 CST 2015
     */
    int updateByPrimaryKey(ExamresultDimTeacher record);
}