package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.TeamRecordWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface TeamRecordServiceI {

    public int updateTeamRecordWithBLOBs(TeamRecordWithBLOBs recordWithBLOBs);

    public int saveTeamRecordWithBLOBs(TeamRecordWithBLOBs recordWithBLOBs);

    public int delTeamRecordWithBLOBs(TeamRecordWithBLOBs org);

    public TeamRecordWithBLOBs selectByPrimaryKey(long id);

    public List<TeamRecordWithBLOBs> selectListByTeamRecordWithBLOBs(TeamRecordWithBLOBs recordWithBLOBs,
            PageParameter page, Date beginDate, Date endDate);

}
