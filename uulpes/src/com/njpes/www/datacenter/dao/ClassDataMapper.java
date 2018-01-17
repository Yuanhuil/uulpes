package com.njpes.www.datacenter.dao;

import java.util.List;

import com.njpes.www.datacenter.entity.ClassData;

public interface ClassDataMapper {

    List<ClassData> getClassByCondition(ClassData classData);

}
