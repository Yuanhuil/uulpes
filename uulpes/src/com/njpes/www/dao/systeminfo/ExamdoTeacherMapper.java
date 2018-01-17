package com.njpes.www.dao.systeminfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.systeminfo.ExamdoTeacher;
import com.njpes.www.utils.PageParameter;

public interface ExamdoTeacherMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_teacher
     *
     * @mbggenerated Sat Nov 21 17:07:15 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_teacher
     *
     * @mbggenerated Sat Nov 21 17:07:15 CST 2015
     */
    int insert(ExamdoTeacher record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_teacher
     *
     * @mbggenerated Sat Nov 21 17:07:15 CST 2015
     */
    int insertSelective(ExamdoTeacher record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_teacher
     *
     * @mbggenerated Sat Nov 21 17:07:15 CST 2015
     */
    ExamdoTeacher selectByPrimaryKey(Long id);

    List<Map> queryExamdoTeachersByRoleId(Map param);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_teacher
     *
     * @mbggenerated Sat Nov 21 17:07:15 CST 2015
     */
    int updateByPrimaryKeySelective(ExamdoTeacher record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_teacher
     *
     * @mbggenerated Sat Nov 21 17:07:15 CST 2015
     */
    int updateByPrimaryKey(ExamdoTeacher record);

    List<com.njpes.www.entity.systeminfo.ExamdoTeacher> selectEntityByPage(
            @Param("examdoTeacher") ExamdoTeacher examdoTeacher, @Param("page") PageParameter page,
            @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    int resetExamdo(long id);

    int resetExamdoBatch(String[] ids);
}