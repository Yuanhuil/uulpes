package com.njpes.www.service.systeminfo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.systeminfo.ExamdoStudentMapper;
import com.njpes.www.entity.systeminfo.ExamdoStudent;
import com.njpes.www.utils.PageParameter;

@Service("ExamdoStudentService")
public class ExamdoStudentServiceImpl implements ExamdoStudentServiceI {
    @Autowired
    private ExamdoStudentMapper examdoStudentMapper;

    @Override
    public List<ExamdoStudent> selectListByEntity(ExamdoStudent ExamdoStudent, PageParameter page, Date beginDate,
            Date endDate) {
        // TODO Auto-generated method stub
        return examdoStudentMapper.selectEntityByPage(ExamdoStudent, page, beginDate, endDate);
    }

}
