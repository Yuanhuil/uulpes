package com.njpes.www.service.baseinfo;

import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.GradeMapper;
import com.njpes.www.entity.baseinfo.organization.Grade;

@Service("gradeService")
public class GradeServiceImpl implements GradeServiceI {

    private GradeMapper gradeMapper;

    public GradeMapper getGradeMapper() {
        return gradeMapper;
    }

    public void setGradeMapper(GradeMapper gradeMapper) {
        this.gradeMapper = gradeMapper;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(Grade record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(Grade record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Grade selectByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(Grade record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Grade record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Grade record) {
        // TODO Auto-generated method stub
        return 0;
    }
}
