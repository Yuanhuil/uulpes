package com.njpes.www.service.baseinfo;

import com.njpes.www.entity.baseinfo.Group;

public interface GroupServiceI {

    int deleteByPrimaryKey(Long id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);
}
