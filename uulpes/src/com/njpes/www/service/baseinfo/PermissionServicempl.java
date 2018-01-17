package com.njpes.www.service.baseinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.PermissionMapper;
import com.njpes.www.entity.baseinfo.Permission;

@Service("permissionService")
public class PermissionServicempl implements PermissionServiceI {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Permission findOne(Long permId) {
        return permissionMapper.selectByPrimaryKey(permId);
    }

    @Override
    public List<Permission> selectAllPermissions() {
        return permissionMapper.selectAllPermissions();
    }

}
