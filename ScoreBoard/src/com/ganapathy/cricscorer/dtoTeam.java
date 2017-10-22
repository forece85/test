package com.ganapathy.cricscorer;

public class dtoTeam {
    private String _matchIds;
    private String _teamName;

    public dtoTeam(String teamName, String matchIds) {
        this._teamName = teamName;
        this._matchIds = matchIds;
    }

    public String getTeamName() {
        return this._teamName;
    }

    public void setTeamName(String value) {
        this._teamName = value;
    }

    public String getMatchIds() {
        return ("'" + this._matchIds.replace("~", "','") + "'").replace(",''", "").replace("''", "");
    }

    public void setMatchIds(String value) {
        this._matchIds = value;
    }
}
