package com.ganapathy.cricscorer;

public class dtoScoreBall implements Cloneable {
    private String _ballByBall;
    private int _ballNo;
    private int _ballsFacedNonStriker;
    private int _ballsFacedStriker;
    private int _ballsThisOver;
    private String _bowlerName;
    private int _bsxCord;
    private int _bsyCord;
    private int _dayNo;
    private String _eoiDescription;
    private int _flagEOD;
    private int _flagEOI;
    private int _inngNo;
    private boolean _isByes;
    private boolean _isLegByes;
    private boolean _isNoball;
    private int _isPenalty;
    private boolean _isStrike;
    private boolean _isWicket;
    private boolean _isWide;
    private String _nonStrikerName;
    private int _nonStrikerRuns;
    private int _overNo;
    private int _runsScored;
    private int _runsThisBall;
    private String _strikerName;
    private int _strikerRuns;
    private int _target;
    private String _teamNo;
    private int _totalBallNo;
    private int _totalExtras;
    private int _totalScore;
    private String _wicketAssist;
    private int _wicketCount;
    private String _wicketHow;
    private int _xCord;
    private int _yCord;

    public String getTeamNo() {
        return this._teamNo;
    }

    public void setTeamNo(String value) {
        this._teamNo = value;
    }

    public int getTotalBallNo() {
        return this._totalBallNo;
    }

    public void setTotalBallNo(int value) {
        this._totalBallNo = value;
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

    public String getStrikerName() {
        return this._strikerName;
    }

    public void setStrikerName(String value) {
        this._strikerName = value;
    }

    public String getNonStrikerName() {
        return this._nonStrikerName;
    }

    public void setNonStrikerName(String value) {
        this._nonStrikerName = value;
    }

    public int getBallsFacedStriker() {
        return this._ballsFacedStriker;
    }

    public void setBallsFacedStriker(int value) {
        this._ballsFacedStriker = value;
    }

    public int getBallsFacedNonStriker() {
        return this._ballsFacedNonStriker;
    }

    public void setBallsFacedNonStriker(int value) {
        this._ballsFacedNonStriker = value;
    }

    public String getBowlerName() {
        return this._bowlerName;
    }

    public void setBowlerName(String value) {
        this._bowlerName = value;
    }

    public boolean getIsStrike() {
        return this._isStrike;
    }

    public void setIsStrike(boolean value) {
        this._isStrike = value;
    }

    public boolean getIsWide() {
        return this._isWide;
    }

    public void setIsWide(boolean value) {
        this._isWide = value;
    }

    public boolean getIsNoball() {
        return this._isNoball;
    }

    public void setIsNoball(boolean value) {
        this._isNoball = value;
    }

    public boolean getIsLegByes() {
        return this._isLegByes;
    }

    public void setIsLegByes(boolean value) {
        this._isLegByes = value;
    }

    public boolean getIsByes() {
        return this._isByes;
    }

    public void setIsByes(boolean value) {
        this._isByes = value;
    }

    public boolean getIsWicket() {
        return this._isWicket;
    }

    public void setIsWicket(boolean value) {
        this._isWicket = value;
    }

    public String getWicketHow() {
        return this._wicketHow;
    }

    public void setWicketHow(String value) {
        this._wicketHow = value;
    }

    public String getWicketAssist() {
        return this._wicketAssist;
    }

    public void setWicketAssist(String value) {
        this._wicketAssist = value;
    }

    public int getRunsScored() {
        return this._runsScored;
    }

    public void setRunsScored(int value) {
        this._runsScored = value;
    }

    public int getRunsThisBall() {
        return this._runsThisBall;
    }

    public void setRunsThisBall(int value) {
        this._runsThisBall = value;
    }

    public String getBallByBall() {
        return this._ballByBall;
    }

    public void setBallByBall(String value) {
        this._ballByBall = value;
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

    public int getStrikerRuns() {
        return this._strikerRuns;
    }

    public void setStrikerRuns(int value) {
        this._strikerRuns = value;
    }

    public int getNonStrikerRuns() {
        return this._nonStrikerRuns;
    }

    public void setNonStrikerRuns(int value) {
        this._nonStrikerRuns = value;
    }

    public int getTarget() {
        return this._target;
    }

    public void setTarget(int value) {
        this._target = value;
    }

    public int getTotalExtras() {
        return this._totalExtras;
    }

    public void setTotalExtras(int value) {
        this._totalExtras = value;
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

    public int getbsXCord() {
        return this._bsxCord;
    }

    public void setbsXCord(int value) {
        this._bsxCord = value;
    }

    public int getbsYCord() {
        return this._bsyCord;
    }

    public void setbsYCord(int value) {
        this._bsyCord = value;
    }

    public int getBallsThisOver() {
        return this._ballsThisOver;
    }

    public void setBallsThisOver(int value) {
        this._ballsThisOver = value;
    }

    public int getInningNo() {
        if (this._inngNo <= 0) {
            this._inngNo = 1;
        }
        return this._inngNo;
    }

    public void setInningNo(int value) {
        this._inngNo = value;
    }

    public int getDayNo() {
        if (this._dayNo <= 0) {
            this._dayNo = 1;
        }
        return this._dayNo;
    }

    public void setDayNo(int value) {
        this._dayNo = value;
    }

    public int getFlagEOD() {
        return this._flagEOD;
    }

    public void setFlagEOD(int value) {
        this._flagEOD = value;
    }

    public int getFlagEOI() {
        return this._flagEOI;
    }

    public void setFlagEOI(int value) {
        this._flagEOI = value;
    }

    public String getEOIDescription() {
        return this._eoiDescription;
    }

    public void setEOIDescription(String value) {
        this._eoiDescription = value;
    }

    public int getIsPenalty() {
        return this._isPenalty == Integer.MIN_VALUE ? 0 : this._isPenalty;
    }

    public void setIsPenalty(int value) {
        this._isPenalty = value;
    }

    public boolean isStrike() {
        return (((this._isWide | this._isNoball) | this._isLegByes) | this._isByes) == 0;
    }

    public boolean isFlagEOD() {
        return this._flagEOD == 1;
    }

    public boolean isFlagEOI() {
        return Utility.isBitSet(this._flagEOI, 0);
    }

    public boolean isFollowOnInnings() {
        return Utility.isBitSet(this._flagEOI, 1);
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
