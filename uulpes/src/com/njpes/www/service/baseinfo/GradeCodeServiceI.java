package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.organization.GradeCode;

public interface GradeCodeServiceI {

    public List<GradeCode> getAllGrades();

    public GradeCode getGradecodeTitile(int id);
}
