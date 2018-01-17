package com.njpes.www.service.baseinfo.organization;

import com.njpes.www.entity.baseinfo.organization.SchoolPsychicjob;

public interface SchoolpsychicJobServiceI {
    /**
     * 插入操作
     * 
     * @param 实体
     * @author 赵忠诚
     */
    public int insert(SchoolPsychicjob e);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public SchoolPsychicjob findById(int id);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int update(SchoolPsychicjob e);

    /**
     * 根据id删除实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int del(SchoolPsychicjob e);
}
