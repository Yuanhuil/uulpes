package com.njpes.www.datacenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.datacenter.dao.ClassDataMapper;
import com.njpes.www.datacenter.entity.ClassData;

@Service("classDataService")
public class ClassDataServiceImpl implements ClassDataServiceI {

    @Autowired
    private ClassDataMapper ClassDataMapper;

    @Override
    public List<ClassData> getClassByCondition(ClassData classData) {
        return ClassDataMapper.getClassByCondition(classData);
    }

}
