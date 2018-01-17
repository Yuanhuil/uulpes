package com.njpes.www.datacenter.dao;

import java.util.List;

import com.njpes.www.datacenter.entity.TeacherData;

public interface TeacherDataMapper {

    List<TeacherData> getTeacherByCondition(TeacherData teacherData);

}
