package com.njpes.www.service.consultcenter;

import java.util.List;

import com.njpes.www.entity.consultcenter.Team;
import com.njpes.www.entity.consultcenter.TeamPage;
import com.njpes.www.utils.PageParameter;

public interface TeamServiceI {

    public int updateTeam(Team team);

    public int saveTeam(Team team);

    public int delTeam(Team team);

    public Team selectByPrimaryKey(long id);

    public List<Team> selectListByTeam(Team team, PageParameter page);

    /**
     * @Description:
     * @param teamPage
     * @return
     */
    public String saveTeamPage(TeamPage teamPage);

    /**
     * @Description:
     * @param team
     * @return
     */
    public String delTeamPage(Team team);

    public List<Long> getTeamPersonIds(Long teamid);

}
