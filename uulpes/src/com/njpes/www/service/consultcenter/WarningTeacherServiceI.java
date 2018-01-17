package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.WarningInterveneTWithBLOBs;
import com.njpes.www.entity.consultcenter.WarningTeacher;
import com.njpes.www.utils.PageParameter;

public interface WarningTeacherServiceI {

    public List<WarningTeacher> selectListByEntity(WarningTeacher warningTeacher, PageParameter page, Date beginDate,
            Date endDate);

    /**
     * @Description:
     * @param warningTeacher
     * @return
     */
    public int updateIswarnsure(WarningTeacher warningTeacher);

    public WarningInterveneTWithBLOBs WarningTeacher2WarningInterveneT(WarningTeacher warningTeacher);

    public WarningTeacher selectEntityById(long parseLong);

}
