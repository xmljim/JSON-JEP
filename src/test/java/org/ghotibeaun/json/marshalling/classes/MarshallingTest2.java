package org.ghotibeaun.json.marshalling.classes;

import java.util.List;

import org.ghotibeaun.json.marshalling.JSONMapping;
import org.ghotibeaun.json.marshalling.JSONTargetClass;

public class MarshallingTest2 extends AbstractMarshallingTest2 {

    @JSONMapping(key = "complexArray")
    @JSONTargetClass(BaseballTeam.class)
    private List<ITeam> teams;

    public MarshallingTest2() {

    }

    public List<ITeam> getTeams() {
        return teams;
    }

    public void setTeams(List<ITeam> teams) {
        this.teams = teams;
    }


}
