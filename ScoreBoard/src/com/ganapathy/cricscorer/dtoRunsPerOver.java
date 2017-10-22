package com.ganapathy.cricscorer;

public class dtoRunsPerOver {
    private int _ballNo;
    private int _overNo;
    private int _runs;
    private int _wickets;

    public int getOverNo() {
        return this._overNo;
    }

    public void setOverNo(int value) {
        this._overNo = value;
    }

    public int getBallNo() {
        return this._ballNo;
    }

    public void setBallNo(int value) {
        this._ballNo = value;
    }

    public int getRuns() {
        return this._runs;
    }

    public void setRuns(int value) {
        this._runs = value;
    }

    public int getWickets() {
        return this._wickets;
    }

    public void setWickets(int value) {
        this._wickets = value;
    }

    public dtoRunsPerOver(int overNo, int ballNo, int runs, int wickets) {
        this._overNo = overNo;
        this._ballNo = ballNo;
        this._runs = runs;
        this._wickets = wickets;
    }
}
