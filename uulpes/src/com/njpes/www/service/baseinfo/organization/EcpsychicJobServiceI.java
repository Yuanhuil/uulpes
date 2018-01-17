package com.njpes.www.service.baseinfo.organization;

import com.njpes.www.entity.baseinfo.organization.Ecpsychicjob;

public interface EcpsychicJobServiceI {

    /**
     * 插入操作
     * 
     * @param 实体
     * @author 赵忠诚
     */
    public int insert(Ecpsychicjob e);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public Ecpsychicjob findById(int id);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int update(Ecpsychicjob e);

    /**
     * 根据id删除实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int del(Ecpsychicjob e);
}
