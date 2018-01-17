package com.njpes.www.entity.consultcenter;

import java.util.List;

public class TeamPage {
    private Team team;
    private List<TeamPerson> teamPersons;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<TeamPerson> getTeamPersons() {
        return teamPersons;
    }

    public void setTeamPersons(List<TeamPerson> teamPersons) {
        this.teamPersons = teamPersons;
    }

}