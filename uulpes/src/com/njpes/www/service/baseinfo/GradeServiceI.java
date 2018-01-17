package com.njpes.www.service.baseinfo;

import com.njpes.www.entity.baseinfo.organization.Grade;

public interface GradeServiceI {

    int deleteByPrimaryKey(Integer id);

    int insert(Grade record);

    int insertSelective(Grade record);

    Grade selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Grade record);

    int updateByPrimaryKeyWithBLOBs(Grade record);

    int updateByPrimaryKey(Grade record);
}
