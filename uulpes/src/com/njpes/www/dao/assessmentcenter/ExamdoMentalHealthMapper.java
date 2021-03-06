package com.njpes.www.dao.assessmentcenter;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.assessmentcenter.DataManageFilterParam;
import com.njpes.www.entity.assessmentcenter.ExamdoMentalHealth;
import com.njpes.www.utils.PageParameter;

public interface ExamdoMentalHealthMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_mental_health
     * 
     * @mbggenerated Mon Feb 01 18:19:06 CST 2016
     */
    int deleteByPrimaryKey(Long taskid);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_mental_health
     * 
     * @mbggenerated Mon Feb 01 18:19:06 CST 2016
     */
    int insert(ExamdoMentalHealth record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_mental_health
     * 
     * @mbggenerated Mon Feb 01 18:19:06 CST 2016
     */
    int insertSelective(ExamdoMentalHealth record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_mental_health
     * 
     * @mbggenerated Mon Feb 01 18:19:06 CST 2016
     */
    ExamdoMentalHealth selectByPrimaryKey(Long resultId);

    List queryExamdoMhForTeacherByPage(@Param("page") PageParameter page,
            @Param("dmgFilterParam") DataManageFilterParam dmgFilterParam);

    List queryExamdoMhForParentByPage(@Param("page") PageParameter page,
            @Param("dmgFilterParam") DataManageFilterParam dmgFilterParam);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_mental_health
     * 
     * @mbggenerated Mon Feb 01 18:19:06 CST 2016
     */
    int updateByPrimaryKeySelective(ExamdoMentalHealth record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table examdo_mental_health
     * 
     * @mbggenerated Mon Feb 01 18:19:06 CST 2016
     */
    int updateByPrimaryKey(ExamdoMentalHealth record);

    int updateOkTimeForStudent(ExamdoMentalHealth record);

    int updateOkTimeForTeacher(ExamdoMentalHealth record);

    int updateOkTimeForParent(ExamdoMentalHealth record);
}