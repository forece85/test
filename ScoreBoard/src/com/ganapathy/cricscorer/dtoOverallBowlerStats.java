package com.ganapathy.cricscorer;

import java.util.Locale;

public class dtoOverallBowlerStats {
    private int _balls;
    private double _best;
    private String _bowlerName;
    private int _dots;
    private int _fiveWktsHaul;
    private int _fours;
    private boolean _hasDetails;
    private int _innings;
    private int _maidens;
    private int _matches;
    private int _noballs;
    private int _overs;
    private int _runs;
    private boolean _showDetails;
    private int _sixes;
    private int _wickets;
    private int _wides;

    public boolean hasDetails() {
        return this._hasDetails;
    }

    public void hasDetails(boolean value) {
        this._hasDetails = value;
    }

    public String getBowlerName() {
        return this._bowlerName;
    }

    public void setBowlerName(String value) {
        this._bowlerName = value;
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

    public int getWickets() {
        return this._wickets;
    }

    public void setWickets(int value) {
        this._wickets = value;
    }

    public int getMaidens() {
        return this._maidens;
    }

    public void setMaidens(int value) {
        this._maidens = value;
    }

    public int getDots() {
        return this._dots;
    }

    public void setDots(int value) {
        this._dots = value;
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

    public double getBest() {
        return this._best;
    }

    public void setBest(double value) {
        this._best = value;
    }

    public boolean getShowDetails() {
        return this._showDetails;
    }

    public void setShowDetails(boolean value) {
        this._showDetails = value;
    }

    public int getFiveWktsHaul() {
        return this._fiveWktsHaul;
    }

    public void setFiveWktsHaul(int value) {
        this._fiveWktsHaul = value;
    }

    public String getTotalOvers() {
        return String.format(Locale.getDefault(), "%d.%d", new Object[]{Integer.valueOf(this._overs), Integer.valueOf(this._balls)});
    }

    public String getRunRate() {
        return String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf((((double) this._runs) * 1.0d) / (((double) this._overs) + (((double) this._balls) / 6.0d)))});
    }

    public String getAverage() {
        if (this._wickets <= 0) {
            return "-";
        }
        return String.format(Locale.getDefault(), "%.1f", new Object[]{Double.valueOf((((double) this._runs) * 1.0d) / ((double) this._wickets))});
    }

    public String getBowlerBest() {
        int wicks = (int) this._best;
        int wruns = 999 - (((int) (this._best * 1000.0d)) % 1000);
        return String.format(Locale.getDefault(), "%d/%d", new Object[]{Integer.valueOf(wicks), Integer.valueOf(wruns)});
    }

    public void copy(dtoOverallBowlerStats value) {
        this._matches = value.getMatches();
        this._innings = value.getInnings();
        this._overs = value.getOvers();
        this._balls = value.getBalls();
        this._runs = value.getRuns();
        this._wides = value.getWides();
        this._noballs = value.getNoballs();
        this._wickets = value.getWickets();
        this._dots = value.getDots();
        this._sixes = value.getSixes();
        this._fours = value.getFours();
        this._maidens = value.getMaidens();
        this._best = value.getBest();
        this._fiveWktsHaul = value.getFiveWktsHaul();
    }
}
