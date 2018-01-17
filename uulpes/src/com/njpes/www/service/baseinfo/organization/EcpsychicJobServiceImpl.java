package com.njpes.www.service.baseinfo.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.EcpsychicjobMapper;
import com.njpes.www.entity.baseinfo.organization.Ecpsychicjob;

@Service("ecpsychicJobService")
public class EcpsychicJobServiceImpl implements EcpsychicJobServiceI {
    @Autowired
    public EcpsychicjobMapper ecpsychicjobMapper;

    @Override
    public int insert(Ecpsychicjob e) {
        return ecpsychicjobMapper.insertSelective(e);
    }

    @Override
    public Ecpsychicjob findById(int id) {
        return ecpsychicjobMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Ecpsychicjob e) {
        return ecpsychicjobMapper.updateByPrimaryKeySelective(e);
    }

    @Override
    public int del(Ecpsychicjob e) {
        return ecpsychicjobMapper.deleteByPrimaryKey(e.getId());
    }

}
