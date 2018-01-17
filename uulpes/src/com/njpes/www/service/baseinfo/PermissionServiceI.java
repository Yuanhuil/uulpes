package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.Permission;

public interface PermissionServiceI {

    /**
     * 根据权限id查询权限信息
     * 
     * @param permId
     * @return Permission
     * @author 赵忠诚
     * @version 0.1
     */
    public Permission findOne(Long permId);

    /**
     * 查询所有的权限
     * 
     * @return Permission
     * @author 赵忠诚
     * @version 0.1
     */
    public List<Permission> selectAllPermissions();
}
