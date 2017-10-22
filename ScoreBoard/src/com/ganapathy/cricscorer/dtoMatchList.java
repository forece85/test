package com.ganapathy.cricscorer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dtoMatchList {
    private String _dateTime;
    private String _matchID;
    private String _matchName;
    private String _overs;
    private String _result;
    private String _teams;
    private String _toss;

    public String getMatchID() {
        return this._matchID;
    }

    public void setMatchID(String value) {
        this._matchID = value;
    }

    public String getMatchDateTime() {
        return this._dateTime;
    }

    public Date getMatchDateTimeAsDate() {
        SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/yy kk:mm:ss", Locale.getDefault());
        Date convertedDate = new Date();
        try {
            convertedDate = shortDate.parse(this._dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public void setMatchDateTime(String value) {
        this._dateTime = value;
    }

    public String getTeams() {
        return this._teams;
    }

    public void setTeams(String value) {
        this._teams = value;
    }

    public String getToss() {
        return this._toss;
    }

    public void setToss(String value) {
        this._toss = value;
    }

    public String getOvers() {
        return this._overs;
    }

    public void setOvers(String value) {
        this._overs = value;
    }

    public String getResult() {
        if (this._result != null) {
            return this._result;
        }
        return "";
    }

    public void setResult(String value) {
        this._result = value;
    }

    public String getMatchName() {
        if (this._matchName == null || ("" + this._matchName).equalsIgnoreCase("") || this._matchName.equalsIgnoreCase("null")) {
            return "";
        }
        return this._matchName;
    }

    public void setMatchName(String value) {
        this._matchName = value;
    }
}
