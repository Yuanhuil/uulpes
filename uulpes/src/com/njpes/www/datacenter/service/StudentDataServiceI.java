package com.njpes.www.datacenter.service;

import java.util.List;

import com.njpes.www.datacenter.entity.StudentData;

public interface StudentDataServiceI {

    List<StudentData> getStudentByCondition(StudentData studentData);

}
