package com.ganapathy.cricscorer;

public class dtoBatsStats {
    private int _balls;
    private String _batsmanName;
    private String _bowlerName;
    private int _firstBallNo;
    private int _fours;
    private int _runRate;
    private int _runs;
    private String _runsByBall;
    private int _sixes;
    private String _wicketAssist;
    private String _wicketHow;

    public String getBatsmanName() {
        return "" + this._batsmanName;
    }

    public void setBatsmanName(String value) {
        this._batsmanName = value;
    }

    public String getRunsByBall() {
        return "" + this._runsByBall;
    }

    public void setRunsByBall(String value) {
        this._runsByBall = value;
    }

    public String getWicketHow() {
        return "" + this._wicketHow;
    }

    public void setWicketHow(String value) {
        this._wicketHow = value;
    }

    public String getWicketAssist() {
        return "" + this._wicketAssist;
    }

    public void setWicketAssist(String value) {
        this._wicketAssist = value;
    }

    public String getBowlerName() {
        return "" + this._bowlerName;
    }

    public void setBowlerName(String value) {
        this._bowlerName = value;
    }

    public int getRuns() {
        return this._runs;
    }

    public void setRuns(int value) {
        this._runs = value;
    }

    public int getBalls() {
        return this._balls;
    }

    public void setBalls(int value) {
        this._balls = value;
    }

    public int getRunRate() {
        return this._runRate;
    }

    public void setRunRate(int value) {
        this._runRate = value;
    }

    public int getSixes() {
        return this._sixes;
    }

    public void setSixes(int value) {
        this._sixes = value;
    }

    public int getFours() {
        return this._fours;
    }

    public void setFours(int value) {
        this._fours = value;
    }

    public int getFirstBallNo() {
        return this._firstBallNo;
    }

    public void setFirstBallNo(int value) {
        this._firstBallNo = value;
    }
}
