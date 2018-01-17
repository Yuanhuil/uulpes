package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.organization.ClassSchool;

public interface ClassServiceI {

    int deleteByPrimaryKey(Integer id);

    int insert(ClassSchool record);

    int insertSelective(ClassSchool record);

    ClassSchool selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassSchool record);

    int updateByPrimaryKeyWithBLOBs(ClassSchool record);

    int updateByPrimaryKey(ClassSchool record);

    List<ClassSchool> selectByBzrgh(String bzrgh);

    List<ClassSchool> selectByBzrAccountid(long accountid);

}
