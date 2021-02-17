package org.ghotibeaun.json.converter.classes;

public class BaseballTeam implements ITeam {
    private String team;
    private String league;

    public BaseballTeam() {

    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String getTeam() {
        return team;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    @Override
    public String getLeague() {
        return league;
    }

}
