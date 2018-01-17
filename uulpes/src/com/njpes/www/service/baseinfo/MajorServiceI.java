package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.organization.Major;

public interface MajorServiceI {

    public List<Major> selectAllMajors();

    public int insert(Major entity);
}
