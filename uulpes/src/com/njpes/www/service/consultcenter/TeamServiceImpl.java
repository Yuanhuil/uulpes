package com.njpes.www.service.consultcenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.consultcenter.TeamMapper;
import com.njpes.www.dao.consultcenter.TeamPersonMapper;
import com.njpes.www.entity.consultcenter.Team;
import com.njpes.www.entity.consultcenter.TeamPage;
import com.njpes.www.entity.consultcenter.TeamPerson;
import com.njpes.www.utils.PageParameter;

@Service("teamService")
public class TeamServiceImpl implements TeamServiceI {
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private TeamPersonMapper teamPersonMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamServiceI#updateTeam(com.njpes
     * .www.entity.consultcenter.Team)
     */
    @Override
    public int updateTeam(Team team) {
        // TODO Auto-generated method stub
        return teamMapper.updateByPrimaryKey(team);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamServiceI#saveTeam(com.njpes.www
     * .entity.consultcenter.Team)
     */
    @Override
    public int saveTeam(Team team) {
        // TODO Auto-generated method stub
        return teamMapper.insert(team);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamServiceI#delTeam(com.njpes.www
     * .entity.consultcenter.Team)
     */
    @Override
    public int delTeam(Team team) {
        // TODO Auto-generated method stub
        return teamMapper.deleteByPrimaryKey(team.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamServiceI#selectByPrimaryKey(long)
     */
    @Override
    public Team selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return teamMapper.selectByPrimaryKey(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamServiceI#selectListByTeam(com
     * .njpes.www.entity.consultcenter.Team, com.njpes.www.utils.PageParameter,
     * java.util.Date, java.util.Date)
     */
    @Override
    public List<Team> selectListByTeam(Team team, PageParameter page) {
        // TODO Auto-generated method stub
        return teamMapper.selectEntityByPage(team, page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamServiceI#saveTeamPage(com.njpes
     * .www.entity.consultcenter.TeamPage)
     */
    @Override
    public String saveTeamPage(TeamPage teamPage) {
        String str = "";
        // TODO Auto-generated method stub
        Team team = teamPage.getTeam();
        List<TeamPerson> teamPersons = teamPage.getTeamPersons();

        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if (team.getId() != null) {
                Map<Long, Long> oldMap = new HashMap<Long, Long>();
                List<Long> ids = this.getTeamPersonIds(team.getId());
                for (Long long1 : ids) {
                    oldMap.put(long1, long1);
                }
                Map<Long, Long> newMap = new HashMap<Long, Long>();
                this.teamMapper.updateByPrimaryKey(team);
                if (teamPersons != null && teamPersons.size() > 0) {
                    for (TeamPerson teamPerson : teamPersons) {
                        if (teamPerson != null && teamPerson.getMemberid() != null) {
                            teamPerson.setTeamid(team.getId());
                            long mid = teamPerson.getMemberid();

                            if (oldMap.get(mid) != null) {
                                oldMap.remove(mid);
                            } else if (newMap.get(mid) == null) {
                                this.teamPersonMapper.insert(teamPerson);
                            }
                            newMap.put(mid, mid);

                        }

                    }

                }
                if (oldMap.size() > 0) {
                    List<Long> list = new ArrayList<Long>();
                    list.addAll(oldMap.keySet());
                    this.teamPersonMapper.delTeamPersonByPIds(team.getId(), list);
                }
                team.setPersonnum(newMap.size());
                this.teamMapper.updateByPrimaryKey(team);
                str = "更新";
            } else {
                team.setCreatetime(new Date());
                this.teamMapper.insert(team);
                if (teamPersons != null && teamPersons.size() > 0) {
                    Map<Long, Long> newMap = new HashMap<Long, Long>();
                    for (TeamPerson teamPerson : teamPersons) {
                        if (teamPerson != null && teamPerson.getMemberid() != null) {
                            teamPerson.setTeamid(team.getId());
                            long mid = teamPerson.getMemberid();

                            if (newMap.get(mid) == null) {
                                this.teamPersonMapper.insert(teamPerson);
                            }
                            newMap.put(mid, mid);

                        }
                    }
                    team.setPersonnum(newMap.size());
                    this.teamMapper.updateByPrimaryKey(team);
                }
                str = "添加";
            }
            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            txManager.rollback(status);
            str += "失败";
            return str;
        }
        str += "成功";
        return str;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.TeamServiceI#delTeamPage(com.njpes
     * .www.entity.consultcenter.Team)
     */
    @Override
    public String delTeamPage(Team team) {
        String str = "删除成功";
        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);

        try {
            if (team.getId() > 0) {
                long id = team.getId();
                TeamPerson teamPerson = new TeamPerson();
                teamPerson.setTeamid(id);
                this.teamPersonMapper.delByEntity(teamPerson);
                this.teamMapper.deleteByPrimaryKey(id);
            } else {
                str = "删除失败";
            }

            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            txManager.rollback(status);
            str = "删除失败";
        }
        return str;
    }

    @Override
    public List<Long> getTeamPersonIds(Long teamid) {
        // TODO Auto-generated method stub
        return this.teamPersonMapper.getTeamPersonIds(teamid);
    }

}
