package com.njpes.www.service.baseinfo.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.SchoolPsychicteamMapper;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicteam;

/**
 * 学校相关服务
 * 
 * @author 赵忠诚
 */
@Service("schoolpsychicteamService")
public class SchoolpsychicteamServiceImpl implements SchoolpsychicteamServiceI {
    @Autowired
    SchoolPsychicteamMapper schoolPsychicteamMapper;

    @Override
    public int insert(SchoolPsychicteam e) {
        return schoolPsychicteamMapper.insertSelective(e);
    }

    @Override
    public SchoolPsychicteam findById(int id) {
        return schoolPsychicteamMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(SchoolPsychicteam e) {
        return schoolPsychicteamMapper.updateByPrimaryKeyWithBLOBs(e);
    }

    @Override
    public int del(SchoolPsychicteam e) {
        return schoolPsychicteamMapper.deleteByPrimaryKey(e.getId());
    }

}
