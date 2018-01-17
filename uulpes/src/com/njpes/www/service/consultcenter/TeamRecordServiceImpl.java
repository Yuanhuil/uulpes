package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.TeamRecordMapper;
import com.njpes.www.entity.consultcenter.TeamRecordWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("teamRecordService")
public class TeamRecordServiceImpl implements TeamRecordServiceI {
    @Autowired
    private TeamRecordMapper teamRecordMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.TeamRecordServiceI#
     * updateTeamRecordWithBLOBs(com.njpes.www.entity.consultcenter.
     * TeamRecordWithBLOBs)
     */
    @Override
    public int updateTeamRecordWithBLOBs(TeamRecordWithBLOBs recordWithBLOBs) {
        // TODO Auto-generated method stub
        return teamRecordMapper.updateByPrimaryKeyWithBLOBs(recordWithBLOBs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.TeamRecordServiceI#
     * saveTeamRecordWithBLOBs(com.njpes.www.entity.consultcenter.
     * TeamRecordWithBLOBs)
     */
    @Override
    public int saveTeamRecordWithBLOBs(TeamRecordWithBLOBs recordWithBLOBs) {
        // TODO Auto-generated method stub
        return teamRecordMapper.insert(recordWithBLOBs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.TeamRecordServiceI#
     * delTeamRecordWithBLOBs(com.njpes.www.entity.consultcenter.
     * TeamRecordWithBLOBs)
     */
    @Override
    public int delTeamRecordWithBLOBs(TeamRecordWithBLOBs recordWithBLOBs) {
        // TODO Auto-generated method stub
        return teamRecordMapper.deleteByPrimaryKey(recordWithBLOBs.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamRecordServiceI#selectByPrimaryKey
     * (long)
     */
    @Override
    public TeamRecordWithBLOBs selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return teamRecordMapper.selectByPrimaryKey(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.TeamRecordServiceI#
     * selectListByTeamRecordWithBLOBs(com.njpes.www.entity.consultcenter.
     * TeamRecordWithBLOBs, com.njpes.www.utils.PageParameter, java.util.Date,
     * java.util.Date)
     */
    @Override
    public List<TeamRecordWithBLOBs> selectListByTeamRecordWithBLOBs(TeamRecordWithBLOBs recordWithBLOBs,
            PageParameter page, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return teamRecordMapper.selectEntityByPage(recordWithBLOBs, page, beginDate, endDate);
    }

}
