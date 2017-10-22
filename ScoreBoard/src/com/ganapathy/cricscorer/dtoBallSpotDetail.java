package com.ganapathy.cricscorer;

public class dtoBallSpotDetail {
    private int _ballNo;
    private int _bsxCord;
    private int _bsyCord;
    private int _isNoball;
    private int _isWicket;
    private int _isWide;
    private int _runsThisBall;

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

    public int getBSXCord() {
        return this._bsxCord;
    }

    public void setBSXCord(int value) {
        this._bsxCord = value;
    }

    public int getBSYCord() {
        return this._bsyCord;
    }

    public void setBSYCord(int value) {
        this._bsyCord = value;
    }

    public int getIsWicket() {
        return this._isWicket;
    }

    public void setIsWicket(int value) {
        this._isWicket = value;
    }

    public int getIsWide() {
        return this._isWide;
    }

    public void setIsWide(int value) {
        this._isWide = value;
    }

    public int getIsNoball() {
        return this._isNoball;
    }

    public void setIsNoball(int value) {
        this._isNoball = value;
    }
}
