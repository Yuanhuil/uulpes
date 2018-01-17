package com.njpes.www.service.baseinfo.organization;

import com.njpes.www.entity.baseinfo.organization.SchoolPsychicteam;

public interface SchoolpsychicteamServiceI {

    /**
     * 插入操作
     * 
     * @param 实体
     * @author 赵忠诚
     */
    public int insert(SchoolPsychicteam e);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public SchoolPsychicteam findById(int id);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int update(SchoolPsychicteam e);

    /**
     * 根据id删除实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int del(SchoolPsychicteam e);
}
