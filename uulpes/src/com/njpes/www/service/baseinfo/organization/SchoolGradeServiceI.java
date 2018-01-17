package com.njpes.www.service.baseinfo.organization;

import com.njpes.www.entity.baseinfo.organization.SchoolGrade;

public interface SchoolGradeServiceI {
    /**
     * 插入操作
     * 
     * @param 实体
     * @author 赵忠诚
     */
    public int insert(SchoolGrade e);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public SchoolGrade findById(int id);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int update(SchoolGrade e);

    /**
     * 根据id删除实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int del(SchoolGrade e);

    public int delAllGradesInSchool(long schoolid);
}
