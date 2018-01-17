package com.njpes.www.datacenter.service;

import java.util.List;

import com.njpes.www.datacenter.entity.TeacherData;

public interface TeacherDataServiceI {

    List<TeacherData> getTeacherByCondition(TeacherData teacherData);

}
