package com.njpes.www.service.baseinfo.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.EcpsychicteamMapper;
import com.njpes.www.entity.baseinfo.organization.Ecpsychicteam;

@Service("ecpsychicteamService")
public class EcpsychicteamServiceImpl implements EcpsychicteamServiceI {

    @Autowired
    public EcpsychicteamMapper ecpsychicteamMapper;

    @Override
    public int insert(Ecpsychicteam e) {
        return ecpsychicteamMapper.insertSelective(e);
    }

    @Override
    public Ecpsychicteam findById(int id) {
        return ecpsychicteamMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Ecpsychicteam e) {
        return ecpsychicteamMapper.updateByPrimaryKeySelective(e);
    }

    @Override
    public int del(Ecpsychicteam e) {
        return ecpsychicteamMapper.deleteByPrimaryKey(e.getId());
    }

}
