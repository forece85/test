package com.ganapathy.cricscorer;

public class dtoWagonWheelDetail {
    private int _ballNo;
    private int _isWicket;
    private int _runsThisBall;
    private int _totalExtras;
    private int _xCord;
    private int _yCord;

    public int getBallNo() {
        return this._ballNo;
    }

    public void setBallNo(int value) {
        this._ballNo = value;
    }

    public int getRunsThisBall() {
        return this._runsThisBall;
    }

    public void setRunsThisBall(int value) {
        this._runsThisBall = value;
    }

    public int getXCord() {
        return this._xCord;
    }

    public void setXCord(int value) {
        this._xCord = value;
    }

    public int getYCord() {
        return this._yCord;
    }

    public void setYCord(int value) {
        this._yCord = value;
    }

    public int getIsWicket() {
        return this._isWicket;
    }

    public void setIsWicket(int value) {
        this._isWicket = value;
    }

    public int getTotalExtras() {
        return this._totalExtras;
    }

    public void setTotalExtras(int value) {
        this._totalExtras = value;
    }
}
