package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.WarningInterveneT;
import com.njpes.www.entity.consultcenter.WarningInterveneTWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface WarningInterveneTServiceI {

    public int updateEntity(WarningInterveneTWithBLOBs warningInterveneT);

    public int saveEntity(WarningInterveneTWithBLOBs warningInterveneT);

    public int delEntity(WarningInterveneT warningInterveneT);

    public WarningInterveneTWithBLOBs selectByPrimaryKey(long id);

    public List<WarningInterveneT> selectListByEntity(WarningInterveneT warningInterveneT, PageParameter page,
            Date beginDate, Date endDate, int step);

    public List<WarningInterveneTWithBLOBs> selectListByEntityWithBLOBs(WarningInterveneTWithBLOBs warningInterveneT,
            PageParameter pageParameter, Date beginDate, Date endDate, int step);

}
