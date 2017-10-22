package com.ganapathy.cricscorer;

public class dtoBallByBallStat {
    public String _ballByBall;
    public int _ballno;
    public String _bowler;
    public String _hasExtra;
    public String _nonStriker;
    public int _overs;
    public String _striker;
    public int _totalScore;

    public int getOvers() {
        return this._overs;
    }

    public void setOvers(int value) {
        this._overs = value;
    }

    public int getBallNo() {
        return this._ballno;
    }

    public void setBallNo(int value) {
        this._ballno = value;
    }

    public String getStriker() {
        return this._striker;
    }

    public void setStriker(String value) {
        this._striker = value;
    }

    public String getNonStriker() {
        return this._nonStriker;
    }

    public void setNonStriker(String value) {
        this._nonStriker = value;
    }

    public String getBowler() {
        return this._bowler;
    }

    public void setBowler(String value) {
        this._bowler = value;
    }

    public String getHasExtra() {
        return this._hasExtra;
    }

    public void setHasExtra(String value) {
        this._hasExtra = value;
    }

    public int getTotalScore() {
        return this._totalScore;
    }

    public void setTotalScore(int value) {
        this._totalScore = value;
    }

    public String getBallByBall() {
        return this._ballByBall;
    }

    public void setBallByBall(String value) {
        this._ballByBall = value;
    }
}
