package com.njpes.www.service.baseinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.MajorMapper;
import com.njpes.www.entity.baseinfo.organization.Major;

@Service("majorService")
public class MajorServiceImpl implements MajorServiceI {

    @Autowired
    private MajorMapper majorMapper;
    
    @Override
    public List<Major> selectAllMajors() {
        return majorMapper.selectAllMajors();
    }

    @Override
    public int insert(Major entity) {
        return majorMapper.insert(entity);
    }

}
