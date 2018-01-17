package com.njpes.www.service.systeminfo;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.systeminfo.ExamdoTeacher;
import com.njpes.www.utils.PageParameter;

public interface ExamdoTeacherServiceI {
    public List<ExamdoTeacher> selectListByEntity(ExamdoTeacher ExamdoTeacher, PageParameter page, Date beginDate,
            Date endDate);
}
