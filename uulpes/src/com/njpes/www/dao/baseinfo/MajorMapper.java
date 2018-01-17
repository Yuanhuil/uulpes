package com.njpes.www.dao.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.Permission;
import com.njpes.www.entity.baseinfo.organization.Major;

public interface MajorMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Major major);

    int insertSelective(Major major);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Major major);

    int updateByPrimaryKey(Major major);

    List<Major> selectAllMajors();

}