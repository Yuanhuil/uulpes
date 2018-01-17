package com.njpes.www.service.systeminfo;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.systeminfo.ExamdoStudent;
import com.njpes.www.utils.PageParameter;

public interface ExamdoStudentServiceI {
    public List<ExamdoStudent> selectListByEntity(ExamdoStudent ExamdoStudent, PageParameter page, Date beginDate,
            Date endDate);
}
