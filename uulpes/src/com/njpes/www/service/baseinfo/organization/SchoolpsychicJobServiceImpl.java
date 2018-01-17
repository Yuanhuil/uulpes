package com.njpes.www.service.baseinfo.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.SchoolPsychicjobMapper;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicjob;

/**
 * 学校相关服务
 * 
 * @author 赵忠诚
 */
@Service("schoolpsychicJobService")
public class SchoolpsychicJobServiceImpl implements SchoolpsychicJobServiceI {

    @Autowired
    SchoolPsychicjobMapper schoolPsychicjobMapper;

    @Override
    public int insert(SchoolPsychicjob e) {
        return schoolPsychicjobMapper.insertSelective(e);
    }

    @Override
    public SchoolPsychicjob findById(int id) {
        return schoolPsychicjobMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(SchoolPsychicjob e) {
        return schoolPsychicjobMapper.updateByPrimaryKeyWithBLOBs(e);
    }

    @Override
    public int del(SchoolPsychicjob e) {
        return schoolPsychicjobMapper.deleteByPrimaryKey(e.getId());
    }

}
