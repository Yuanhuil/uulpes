package com.njpes.www.datacenter.service;

import java.util.List;

import com.njpes.www.datacenter.entity.ClassData;

public interface ClassDataServiceI {

    List<ClassData> getClassByCondition(ClassData classData);

}
