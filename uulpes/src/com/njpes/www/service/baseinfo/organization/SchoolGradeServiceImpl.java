package com.njpes.www.service.baseinfo.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.SchoolGradeMapper;
import com.njpes.www.entity.baseinfo.organization.SchoolGrade;

@Service("schoolGradeService")
public class SchoolGradeServiceImpl implements SchoolGradeServiceI {

    @Autowired
    private SchoolGradeMapper schoolGradeMapper;

    @Override
    public int insert(SchoolGrade e) {
        return schoolGradeMapper.insertSelective(e);
    }

    @Override
    public SchoolGrade findById(int id) {
        return schoolGradeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(SchoolGrade e) {
        return schoolGradeMapper.updateByPrimaryKeySelective(e);
    }

    @Override
    public int del(SchoolGrade e) {
        return schoolGradeMapper.deleteByPrimaryKey(e.getId());
    }

    @Override
    public int delAllGradesInSchool(long schoolid) {
        // TODO Auto-generated method stub
        return 0;
    }

}
