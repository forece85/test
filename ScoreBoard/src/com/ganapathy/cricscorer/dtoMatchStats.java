package com.ganapathy.cricscorer;

public class dtoMatchStats {
    private int _ballNo;
    private int _ballsThisOver;
    private int _byes;
    private String _eoiDesc;
    private int _legbyes;
    private int _noballs;
    private int _overNo;
    private int _penalty;
    private int _teamNo;
    private int _totalExtras;
    private int _totalScore;
    private int _wicketCount;
    private int _wides;

    public int getTeamNo() {
        return this._teamNo;
    }

    public void setTeamNo(int value) {
        this._teamNo = value;
    }

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

    public int getTotalScore() {
        return this._totalScore;
    }

    public void setTotalScore(int value) {
        this._totalScore = value;
    }

    public int getWicketCount() {
        return this._wicketCount;
    }

    public void setWicketCount(int value) {
        this._wicketCount = value;
    }

    public int getTotalExtras() {
        return this._totalExtras;
    }

    public void setTotalExtras(int value) {
        this._totalExtras = value;
    }

    public int getWides() {
        return this._wides;
    }

    public void setWides(int value) {
        this._wides = value;
    }

    public int getNoballs() {
        return this._noballs;
    }

    public void setNoballs(int value) {
        this._noballs = value;
    }

    public int getLegByes() {
        return this._legbyes;
    }

    public void setLegByes(int value) {
        this._legbyes = value;
    }

    public int getByes() {
        return this._byes;
    }

    public void setByes(int value) {
        this._byes = value;
    }

    public int getBallsThisOver() {
        return this._ballsThisOver;
    }

    public void setBallsThisOver(int value) {
        this._ballsThisOver = value;
    }

    public String getEOIDesc() {
        return (this._eoiDesc == null || this._eoiDesc.equalsIgnoreCase("null")) ? "" : this._eoiDesc;
    }

    public void setEOIDesc(String value) {
        this._eoiDesc = value;
    }

    public int getPenalty() {
        return this._penalty;
    }

    public void setPenalty(int value) {
        this._penalty = value;
    }
}
