package com.ganapathy.cricscorer;

public class dtoBall {
    public int BallNo;
    public String BallType;
    public String BowlerName;
    public String GroupTitle;
    public int InngNo;
    public String NonStrikerName;
    public String NonStrikerRuns;
    public int OverNo;
    public int RunsThisBall;
    public String StrikerName;
    public String StrikerRuns;
    public int TeamNo;
    public int TotalBallNo;

    public String getTitle() {
        return "Over " + (this.OverNo + 1) + " - " + this.BowlerName;
    }

    public String getFirstLine() {
        return "" + this.OverNo + "." + this.BallNo + ", " + this.BallType + (!this.BallType.equalsIgnoreCase("") ? ", " : "") + this.RunsThisBall + " run" + (this.RunsThisBall > 1 ? "s" : "");
    }

    public String getSecondLine() {
        return this.StrikerName + " - " + this.StrikerRuns + ", " + this.NonStrikerName + " - " + this.NonStrikerRuns;
    }
}
