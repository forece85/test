package com.ganapathy.cricscorer;

import java.util.Locale;

public class dtoOverallBatsStat {
    private int _balls;
    private int _dots;
    private int _fifties;
    private int _fours;
    private boolean _hasDetails;
    private String _highScore;
    private int _innings;
    private int _matches;
    private int _notOuts;
    private int _ones;
    private int _runs;
    private boolean _showDetails;
    private int _sixes;
    private String _strikerName;
    private int _threes;
    private int _tons;
    private int _twos;

    public boolean hasDetails() {
        return this._hasDetails;
    }

    public void hasDetails(boolean value) {
        this._hasDetails = value;
    }

    public String getStrikerName() {
        return this._strikerName;
    }

    public void setStrikerName(String value) {
        this._strikerName = value;
    }

    public int getMatches() {
        return this._matches;
    }

    public void setMatches(int value) {
        this._matches = value;
    }

    public int getInnings() {
        return this._innings;
    }

    public void setInnings(int value) {
        this._innings = value;
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

    public int getThrees() {
        return this._threes;
    }

    public void setThrees(int value) {
        this._threes = value;
    }

    public int getTwos() {
        return this._twos;
    }

    public void setTwos(int value) {
        this._twos = value;
    }

    public int getOnes() {
        return this._ones;
    }

    public void setOnes(int value) {
        this._ones = value;
    }

    public int getDots() {
        return this._dots;
    }

    public void setDots(int value) {
        this._dots = value;
    }

    public int getNotOuts() {
        return this._notOuts;
    }

    public void setNotOuts(int value) {
        this._notOuts = value;
    }

    public String getHighScore() {
        return this._highScore;
    }

    public void setHighScore(String value) {
        this._highScore = value;
    }

    public boolean getShowDetails() {
        return this._showDetails;
    }

    public void setShowDetails(boolean value) {
        this._showDetails = value;
    }

    public int getFifties() {
        return this._fifties;
    }

    public void setFifties(int value) {
        this._fifties = value;
    }

    public int getTons() {
        return this._tons;
    }

    public void setTons(int value) {
        this._tons = value;
    }

    public String getStrikeRate() {
        return String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf(((((double) this._runs) * 1.0d) / (((double) this._balls) * 1.0d)) * 100.0d)});
    }

    public String getAverage() {
        if (this._matches - this._notOuts <= 0) {
            return "-";
        }
        return String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf((((double) this._runs) * 1.0d) / (((double) (this._matches - this._notOuts)) * 1.0d))});
    }

    public void copy(dtoOverallBatsStat value) {
        this._matches = value.getMatches();
        this._innings = value.getInnings();
        this._runs = value.getRuns();
        this._balls = value.getBalls();
        this._sixes = value.getSixes();
        this._fours = value.getFours();
        this._threes = value.getThrees();
        this._twos = value.getTwos();
        this._ones = value.getOnes();
        this._dots = value.getDots();
        this._notOuts = value.getNotOuts();
        this._highScore = value.getHighScore();
        this._fifties = value.getFifties();
        this._tons = value.getTons();
    }
}
