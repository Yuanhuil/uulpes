package com.njpes.www.service.baseinfo;

import com.njpes.www.dao.baseinfo.GroupMapper;
import com.njpes.www.entity.baseinfo.Group;

public class GroupServiceImpl implements GroupServiceI {

    private GroupMapper groupMapper;

    public GroupMapper getGroupMapper() {
        return groupMapper;
    }

    public void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(Group record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(Group record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Group selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(Group record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Group record) {
        // TODO Auto-generated method stub
        return 0;
    }

}
