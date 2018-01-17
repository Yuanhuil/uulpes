package com.njpes.www.service.baseinfo.organization;

import com.njpes.www.entity.baseinfo.organization.ClassSchool;

public interface SchoolClassServiceI {
    /**
     * 插入操作
     * 
     * @param 实体
     * @author 赵忠诚
     */
    public int insert(ClassSchool e);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public ClassSchool findById(long id);

    /**
     * 根据id找到实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int update(ClassSchool e);

    /**
     * 根据id删除实体信息
     * 
     * @param id
     * @return
     * @author 赵忠诚
     */
    public int del(ClassSchool e);

    /**
     * 班级升级
     * 
     * @param xxxz：小学学制；bybgradeid：毕业班gradeid；bxlxm：办学类型码
     * @return
     * @author 赵万锋
     */
    public int upgrade(long orgid, int xxxz, int bybgradeid, String bxlxm) throws Exception;
}
