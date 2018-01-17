package com.njpes.www.datacenter.dao;

import java.util.List;

import com.njpes.www.datacenter.entity.StudentData;

public interface StudentDataMapper {

    List<StudentData> getStudentByCondition(StudentData studentData);

}
