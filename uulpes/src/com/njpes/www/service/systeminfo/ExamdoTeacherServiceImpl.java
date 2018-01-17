package com.njpes.www.service.systeminfo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.systeminfo.ExamdoTeacherMapper;
import com.njpes.www.entity.systeminfo.ExamdoTeacher;
import com.njpes.www.utils.PageParameter;

@Service("ExamdoTeacherService")
public class ExamdoTeacherServiceImpl implements ExamdoTeacherServiceI {
    @Autowired
    private ExamdoTeacherMapper examdoTeacherMapper;

    @Override
    public List<ExamdoTeacher> selectListByEntity(ExamdoTeacher ExamdoTeacher, PageParameter page, Date beginDate,
            Date endDate) {
        // TODO Auto-generated method stub
        return examdoTeacherMapper.selectEntityByPage(ExamdoTeacher, page, beginDate, endDate);
    }

}
