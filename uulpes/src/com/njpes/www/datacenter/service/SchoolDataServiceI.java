package com.njpes.www.datacenter.service;

import java.util.List;

import com.njpes.www.datacenter.entity.SchoolData;

public interface SchoolDataServiceI {

    List<SchoolData> getSchoolByCondition(SchoolData schoolData);

}
