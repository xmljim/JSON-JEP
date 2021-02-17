package org.ghotibeaun.json.marshalling.classes;

import java.util.List;

import org.ghotibeaun.json.converters.annotation.JSONElement;
import org.ghotibeaun.json.converters.annotation.TargetClass;

public class MarshallingTest2 extends AbstractMarshallingTest2 {

    @JSONElement(key = "complexArray")
    @TargetClass(BaseballTeam.class)
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
