package com.ganapathy.cricscorer;

public class dtoTeamPlayer {
    boolean _checked;
    int _handedBat;
    int _handedBowl;
    int _isBatsman;
    int _isBowler;
    int _isCaptain;
    int _isKeeper;
    String _playerName;
    int _playerOrder;
    String _teamName;
    int _whatBowler;

    public dtoTeamPlayer(String teamName, String playerName) {
        this._teamName = teamName;
        this._playerName = playerName;
    }

    public String getTeamName() {
        return this._teamName;
    }

    public void setTeamName(String value) {
        this._teamName = value;
    }

    public String getPlayerName() {
        return this._playerName;
    }

    public void setPlayerName(String value) {
        this._playerName = value;
    }

    public int getPlayerOrder() {
        return this._playerOrder;
    }

    public void setPlayerOrder(int value) {
        this._playerOrder = value;
    }

    public int getHandedBat() {
        return this._handedBat;
    }

    public void setHandedBat(int value) {
        this._handedBat = value;
    }

    public int getHandedBowl() {
        return this._handedBowl;
    }

    public void setHandedBowl(int value) {
        this._handedBowl = value;
    }

    public int getWhatBowler() {
        return this._whatBowler;
    }

    public void setWhatBowler(int value) {
        this._whatBowler = value;
    }

    public int getIsBatsman() {
        return this._isBatsman;
    }

    public void setIsBatsman(int value) {
        this._isBatsman = value;
    }

    public int getIsBowler() {
        return this._isBowler;
    }

    public void setIsBowler(int value) {
        this._isBowler = value;
    }

    public int getIsKeeper() {
        return this._isKeeper;
    }

    public void setIsKeeper(int value) {
        this._isKeeper = value;
    }

    public int getIsCaptain() {
        return this._isCaptain;
    }

    public void setIsCaptain(int value) {
        this._isCaptain = value;
    }

    public boolean getIsChecked() {
        return this._checked;
    }

    public void setIsChecked(boolean value) {
        this._checked = value;
    }
}
