package com.ganapathy.cricscorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class dtoMatch {
    private dtoAdditionalSettings _addlSettings = new dtoAdditionalSettings();
    private int _currentSession;
    private String _dateTime;
    private boolean _followOn = false;
    private boolean _isMatchReopened = false;
    private dtoScoreBall _lastScoreball;
    private String _matchID;
    private String _matchName;
    private String _matchResult;
    private int _noOfDays;
    private int _noOfInngs;
    private String _optedTo;
    private int _overs;
    private int _target;
    private String _team1Name;
    private dtoTeamPlayerList _team1Players = new dtoTeamPlayerList();
    private List<dtoScoreBall> _team1Scorecard = new ArrayList();
    private String _team2Name;
    private dtoTeamPlayerList _team2Players = new dtoTeamPlayerList();
    private List<dtoScoreBall> _team2Scorecard = new ArrayList();
    private String _tossWonBy;
    private String _venue;

    public boolean getIsMatchReopened() {
        return this._isMatchReopened;
    }

    public void setIsMatchReopened(boolean value) {
        this._isMatchReopened = value;
    }

    public dtoScoreBall getLastScoreball() {
        return this._lastScoreball;
    }

    public void setLastScoreball(dtoScoreBall value) {
        this._lastScoreball = value;
    }

    public String getMatchID() {
        return this._matchID;
    }

    public void setMatchID(String value) {
        this._matchID = value;
    }

    public String getDateTime() {
        return this._dateTime;
    }

    public void setDateTime(String value) {
        this._dateTime = value;
    }

    public String getVenue() {
        return this._venue;
    }

    public void setVenue(String value) {
        this._venue = value;
    }

    public String getMatchName() {
        return this._matchName;
    }

    public void setMatchName(String value) {
        if (value == null || value.equalsIgnoreCase("null") || value.trim().equalsIgnoreCase("")) {
            value = "";
        }
        this._matchName = value;
    }

    public String getTeam1Name() {
        return this._team1Name;
    }

    public void setTeam1Name(String value) {
        this._team1Name = value;
    }

    public String getTeam2Name() {
        return this._team2Name;
    }

    public void setTeam2Name(String value) {
        this._team2Name = value;
    }

    public dtoTeamPlayerList getTeam1Players() {
        return this._team1Players;
    }

    public void setTeam1Players(dtoTeamPlayerList values) {
        this._team1Players = values;
    }

    public void addTeam1Player(String value) {
        dtoTeamPlayer newPlayer = new dtoTeamPlayer();
        newPlayer.setPlayerName(value);
        newPlayer.setPlayerOrder(this._team1Players == null ? 0 : this._team1Players.size());
        addTeam1Player(newPlayer);
    }

    public void addTeam1Player(dtoTeamPlayer value) {
        if (this._team1Players == null) {
            this._team1Players = new dtoTeamPlayerList();
        }
        boolean exists = false;
        Iterator it = this._team1Players.iterator();
        while (it.hasNext()) {
            if (((dtoTeamPlayer) it.next()).getPlayerName().trim().replace(" ", "").equalsIgnoreCase(value.getPlayerName().trim().replace(" ", ""))) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            this._team1Players.add(value);
        }
    }

    public void clearTeam1Players() {
        this._team1Players = new dtoTeamPlayerList();
    }

    public dtoTeamPlayerList getTeam2Players() {
        return this._team2Players;
    }

    public void setTeam2Players(dtoTeamPlayerList values) {
        this._team2Players = values;
    }

    public void addTeam2Player(String value) {
        dtoTeamPlayer newPlayer = new dtoTeamPlayer();
        newPlayer.setPlayerName(value);
        newPlayer.setPlayerOrder(this._team2Players == null ? 0 : this._team2Players.size());
        addTeam2Player(newPlayer);
    }

    public void addTeam2Player(dtoTeamPlayer value) {
        if (this._team2Players == null) {
            this._team2Players = new dtoTeamPlayerList();
        }
        boolean exists = false;
        Iterator it = this._team2Players.iterator();
        while (it.hasNext()) {
            if (((dtoTeamPlayer) it.next()).getPlayerName().trim().replace(" ", "").equalsIgnoreCase(value.getPlayerName().trim().replace(" ", ""))) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            this._team2Players.add(value);
        }
    }

    public void clearTeam2Players() {
        this._team2Players = new dtoTeamPlayerList();
    }

    public String getTossWonBy() {
        return this._tossWonBy;
    }

    public void setTossWonBy(String value) {
        this._tossWonBy = value;
    }

    public String getOptedTo() {
        return this._optedTo;
    }

    public void setOptedTo(String value) {
        this._optedTo = value;
    }

    public int getOvers() {
        return this._overs;
    }

    public void setOvers(int value) {
        this._overs = value;
    }

    public dtoAdditionalSettings getAdditionalSettings() {
        return this._addlSettings;
    }

    public void setAdditionalSettings(dtoAdditionalSettings value) {
        this._addlSettings = value;
    }

    public List<dtoScoreBall> getTeam1Scorecard() {
        return this._team1Scorecard;
    }

    public void setTeam1Scorecard(List<dtoScoreBall> values) {
        this._team1Scorecard = values;
    }

    public List<dtoScoreBall> getTeam2Scorecard() {
        return this._team2Scorecard;
    }

    public void setTeam2Scorecard(List<dtoScoreBall> values) {
        this._team2Scorecard = values;
    }

    public int getTarget() {
        return this._target;
    }

    public void setTarget(int value) {
        this._target = value;
    }

    public String getMatchResult() {
        if (this._matchResult != null) {
            return this._matchResult;
        }
        return "";
    }

    public void setMatchResult(String value) {
        this._matchResult = value;
    }

    public void addScoreBall(dtoScoreBall value) {
        if (isTeam1Batting()) {
            if (this._team1Scorecard == null) {
                this._team1Scorecard = new ArrayList();
            }
            this._team1Scorecard.add(value);
            return;
        }
        if (this._team2Scorecard == null) {
            this._team2Scorecard = new ArrayList();
        }
        this._team2Scorecard.add(value);
    }

    public String getCurrentBattingTeamNo() {
        if (isTeam1Batting()) {
            return "1";
        }
        return "2";
    }

    public String getCurrentBowlingTeamNo() {
        if (isTeam1Batting()) {
            return "2";
        }
        return "1";
    }

    public void clearScorecard() {
        this._team1Scorecard = new ArrayList();
        this._team2Scorecard = new ArrayList();
    }

    public int getNoOfInngs() {
        return this._noOfInngs;
    }

    public void setNoOfInngs(int value) {
        if (value <= 0) {
            value = 1;
        }
        this._noOfInngs = value;
    }

    public int getNoOfDays() {
        return this._noOfDays;
    }

    public void setNoOfDays(int value) {
        if (value <= 0) {
            value = 1;
        }
        this._noOfDays = value;
    }

    public int getCurrentSession() {
        return this._currentSession;
    }

    public void setCurrentSession(int value) {
        this._currentSession = value;
    }

    public boolean getFollowOn() {
        return this._followOn;
    }

    public void setFollowOn(boolean value) {
        this._followOn = value;
    }

    public boolean isFirstInnings() {
        return this._currentSession == 1;
    }

    public boolean isFinalInnings() {
        return this._currentSession == this._noOfInngs * 2;
    }

    public boolean isFinalButOneInnings() {
        return this._currentSession == (this._noOfInngs * 2) + -1;
    }

    public String getFirstBattedTeam() {
        return "TEAM" + getFirstBattedTeamNo();
    }

    public String getFirstBattedTeamNo() {
        String _firstBatted = "";
        if (this._tossWonBy.equalsIgnoreCase("Team 1")) {
            if (this._optedTo.equalsIgnoreCase("BAT")) {
                return "1";
            }
            return "2";
        } else if (this._optedTo.equalsIgnoreCase("BAT")) {
            return "2";
        } else {
            return "1";
        }
    }

    public String getTossWonTeam() {
        if (this._tossWonBy.equalsIgnoreCase("Team 1")) {
            if (this._optedTo.equalsIgnoreCase("BAT")) {
                return this._team1Name;
            }
            return this._team2Name;
        } else if (this._optedTo.equalsIgnoreCase("BAT")) {
            return this._team2Name;
        } else {
            return this._team1Name;
        }
    }

    public boolean isTeam1Batting() {
        if (getFirstBattedTeamNo().equals("1")) {
            if (this._currentSession == 1) {
                return true;
            }
            if (this._noOfInngs > 1) {
                if (this._currentSession == 3 && !this._followOn) {
                    return true;
                }
                if (this._currentSession == 4 && this._followOn) {
                    return true;
                }
            }
        } else if (this._currentSession == 2) {
            return true;
        } else {
            if (this._noOfInngs > 1) {
                if (this._currentSession == 4 && !this._followOn) {
                    return true;
                }
                if (this._currentSession == 3 && this._followOn) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getCurrentTeamNo() {
        if ((!isFirstInnings() || !getFirstBattedTeamNo().equalsIgnoreCase("1")) && (isFirstInnings() || !getFirstBattedTeamNo().equalsIgnoreCase("2"))) {
            return "2";
        }
        return "1";
    }

    public String getCurrentBattingTeamName() {
        if (isTeam1Batting()) {
            return this._team1Name;
        }
        return this._team2Name;
    }

    public int getCurrentInningsNo() {
        return (int) Math.ceil(((double) this._currentSession) / 2.0d);
    }

    public String getCurrentInningsText() {
        return (getCurrentInningsNo() == 1 ? "1st " : "2nd ") + " Innings";
    }
}
