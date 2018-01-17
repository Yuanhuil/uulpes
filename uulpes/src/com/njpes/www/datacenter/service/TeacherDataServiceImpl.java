package com.njpes.www.datacenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.datacenter.dao.TeacherDataMapper;
import com.njpes.www.datacenter.entity.TeacherData;

@Service("teacherDataService")
public class TeacherDataServiceImpl implements TeacherDataServiceI {

    @Autowired
    private TeacherDataMapper teacherDataMapper;

    @Override
    public List<TeacherData> getTeacherByCondition(TeacherData teacherData) {
        return teacherDataMapper.getTeacherByCondition(teacherData);
    }

}
