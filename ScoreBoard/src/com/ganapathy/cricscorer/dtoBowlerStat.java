package com.ganapathy.cricscorer;

public class dtoBowlerStat {
    private String _ballByBall;
    private int _balls;
    private String _bowlerName;
    private int _calculatedBalls;
    private int _calculatedOvers;
    private int _maidenOvers;
    private int _noballs;
    private int _overs;
    private double _runRate;
    private int _runs;
    private String _score;
    private int _wickets;
    private int _wides;

    public String getBowlerName() {
        return "" + this._bowlerName;
    }

    public void setBowlerName(String value) {
        this._bowlerName = value;
    }

    public int getOvers() {
        return this._overs;
    }

    public void setOvers(int value) {
        this._overs = value;
    }

    public int getBalls() {
        return this._balls;
    }

    public void setBalls(int value) {
        this._balls = value;
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

    public double getRunRate() {
        return this._runRate;
    }

    public void setRunRate(double value) {
        this._runRate = value;
    }

    public int getCalculatedOvers() {
        return this._calculatedOvers;
    }

    public void setCalculatedOvers(int value) {
        this._calculatedOvers = value;
    }

    public int getCalculatedBalls() {
        return this._calculatedBalls;
    }

    public void setCalculatedBalls(int value) {
        this._calculatedBalls = value;
    }

    public String getBallByBall() {
        return "" + this._ballByBall;
    }

    public void setBallByBall(String value) {
        this._ballByBall = value;
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

    public int getMaidenOvers() {
        return this._maidenOvers;
    }

    public void setMaidenOvers(int value) {
        this._maidenOvers = value;
    }

    public String getScore() {
        return "" + this._score;
    }

    public void setScore(String value) {
        this._score = value;
    }
}
