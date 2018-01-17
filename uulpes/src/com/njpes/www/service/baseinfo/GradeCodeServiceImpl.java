package com.njpes.www.service.baseinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.GradeCodeMapper;
import com.njpes.www.entity.baseinfo.organization.GradeCode;

@Service("gradeCodeService")
public class GradeCodeServiceImpl implements GradeCodeServiceI {

    @Autowired
    private GradeCodeMapper gradeCodeMapper;

    public List<GradeCode> getAllGrades() {
        // TODO Auto-generated method stub
        return gradeCodeMapper.getAllGrades();
    }

    @Override
    public GradeCode getGradecodeTitile(int id) {
        // TODO Auto-generated method stub
        return gradeCodeMapper.selectByPrimaryKey((byte) id);
    }

}
