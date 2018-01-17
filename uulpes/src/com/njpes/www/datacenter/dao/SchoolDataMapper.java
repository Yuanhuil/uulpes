package com.njpes.www.datacenter.dao;

import java.util.List;

import com.njpes.www.datacenter.entity.SchoolData;

public interface SchoolDataMapper {

    List<SchoolData> getSchoolByCondition(SchoolData schoolData);

}
