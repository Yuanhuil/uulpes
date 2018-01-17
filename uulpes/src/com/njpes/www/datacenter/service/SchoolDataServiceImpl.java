package com.njpes.www.datacenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.datacenter.dao.SchoolDataMapper;
import com.njpes.www.datacenter.entity.SchoolData;

@Service("schoolDataService")
public class SchoolDataServiceImpl implements SchoolDataServiceI {

    @Autowired
    private SchoolDataMapper SchoolDataMapper;

    @Override
    public List<SchoolData> getSchoolByCondition(SchoolData schoolData) {
        return SchoolDataMapper.getSchoolByCondition(schoolData);
    }

}