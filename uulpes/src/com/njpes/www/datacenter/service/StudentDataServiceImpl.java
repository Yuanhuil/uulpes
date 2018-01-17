package com.njpes.www.datacenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.datacenter.dao.StudentDataMapper;
import com.njpes.www.datacenter.entity.StudentData;

@Service("studentDataService")
public class StudentDataServiceImpl implements StudentDataServiceI {

    @Autowired
    private StudentDataMapper studentDataMapper;

    @Override
    public List<StudentData> getStudentByCondition(StudentData studentData) {
        return studentDataMapper.getStudentByCondition(studentData);
    }

}
