package com.njpes.www.service.baseinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;

@Service("classService")
public class ClassServiceImpl implements ClassServiceI {
    @Autowired
    private ClassSchoolMapper classSchoolMapper;

    /*
     * public ClassSchoolMapper getClassMapper() { return classSchoolMapper; }
     * 
     * public void setClassMapper(ClassSchoolMapper classMapper) {
     * this.classSchoolMapper = classMapper; }
     */

    @Override
    public int deleteByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(ClassSchool record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(ClassSchool record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ClassSchool selectByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(ClassSchool record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(ClassSchool record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(ClassSchool record) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<ClassSchool> selectByBzrgh(String bzrgh) {
        return classSchoolMapper.selectByBzrgh(bzrgh);
    }

    @Override
    public List<ClassSchool> selectByBzrAccountid(long accountid) {
        // TODO Auto-generated method stub
        return classSchoolMapper.selectByBzrAccountid(accountid);
    }

}
