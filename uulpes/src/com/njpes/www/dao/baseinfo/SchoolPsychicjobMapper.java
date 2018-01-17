package com.njpes.www.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.organization.SchoolPsychicjob;

public interface SchoolPsychicjobMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table school_psychicjob
     *
     * @mbggenerated Wed Aug 12 22:44:41 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table school_psychicjob
     *
     * @mbggenerated Wed Aug 12 22:44:41 CST 2015
     */
    int insert(SchoolPsychicjob record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table school_psychicjob
     *
     * @mbggenerated Wed Aug 12 22:44:41 CST 2015
     */
    int insertSelective(SchoolPsychicjob record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table school_psychicjob
     *
     * @mbggenerated Wed Aug 12 22:44:41 CST 2015
     */
    SchoolPsychicjob selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table school_psychicjob
     *
     * @mbggenerated Wed Aug 12 22:44:41 CST 2015
     */
    int updateByPrimaryKeySelective(SchoolPsychicjob record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table school_psychicjob
     *
     * @mbggenerated Wed Aug 12 22:44:41 CST 2015
     */
    int updateByPrimaryKeyWithBLOBs(SchoolPsychicjob record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table school_psychicjob
     *
     * @mbggenerated Wed Aug 12 22:44:41 CST 2015
     */
    int updateByPrimaryKey(SchoolPsychicjob record);

    /**
     * 根据组织机构id获取心理工作信息
     * 
     * @param 教委id
     * @return 列表
     * @author 赵忠诚
     */
    public List<SchoolPsychicjob> getJobsByOrgid(@Param("schoolid") Long schoolid);
}