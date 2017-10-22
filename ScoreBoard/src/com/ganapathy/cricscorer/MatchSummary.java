package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MatchSummary {
    public static List<dtoMatchSummary> populateMatchSummaryDetails(Activity activity, View view) {
        dtoMatch refMatch = ((CricScorerApp) activity.getApplication()).currentMatch;
        ((TextView) view.findViewById(C0252R.id.labelTeams)).setText(refMatch.getTeam1Name() + " vs " + refMatch.getTeam2Name());
        String result = "" + refMatch.getMatchResult();
        if (result.trim().equalsIgnoreCase("")) {
            ((TextView) view.findViewById(C0252R.id.matchResult)).setVisibility(8);
        } else {
            ((TextView) view.findViewById(C0252R.id.matchResult)).setText(result);
        }
        String team = "";
        String inng = "1";
        List<dtoMatchSummary> summaryList = new ArrayList();
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String secondBatted = MatchHelper.getSecondBatted(refMatch);
        team = firstBatted;
        inng = "1";
        dtoMatchSummary summary1 = new dtoMatchSummary();
        summary1.title = MatchHelper.getTeamName(refMatch, firstBatted) + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings";
        populateScoreDetails(activity, summary1, team, inng);
        populateBatsmenStats(activity, summary1, team, inng);
        populateBowlerStats(activity, summary1, team, inng);
        summaryList.add(summary1);
        if (refMatch.getCurrentSession() > 1) {
            team = secondBatted;
            inng = "1";
            dtoMatchSummary summary2 = new dtoMatchSummary();
            summary2.title = MatchHelper.getTeamName(refMatch, secondBatted) + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings";
            populateScoreDetails(activity, summary2, team, inng);
            populateBatsmenStats(activity, summary2, team, inng);
            populateBowlerStats(activity, summary2, team, inng);
            summaryList.add(summary2);
        }
        if (refMatch.getNoOfInngs() > 1 && refMatch.getCurrentSession() > 2) {
            String thirdBatted;
            if (refMatch.getFollowOn()) {
                thirdBatted = secondBatted;
            } else {
                thirdBatted = firstBatted;
            }
            team = thirdBatted;
            inng = "2";
            dtoMatchSummary summary3 = new dtoMatchSummary();
            summary3.title = MatchHelper.getTeamName(refMatch, thirdBatted) + " - 2nd Innings" + (refMatch.getFollowOn() ? "(FOLLOW ON)" : "");
            populateScoreDetails(activity, summary3, team, inng);
            populateBatsmenStats(activity, summary3, team, inng);
            populateBowlerStats(activity, summary3, team, inng);
            summaryList.add(summary3);
            if (refMatch.getCurrentSession() > 3) {
                String lastBatted;
                if (refMatch.getFollowOn()) {
                    lastBatted = firstBatted;
                } else {
                    lastBatted = secondBatted;
                }
                team = lastBatted;
                inng = "2";
                dtoMatchSummary summary4 = new dtoMatchSummary();
                summary4.title = MatchHelper.getTeamName(refMatch, lastBatted) + " - 2nd Innings";
                populateScoreDetails(activity, summary4, team, inng);
                populateBatsmenStats(activity, summary4, team, inng);
                populateBowlerStats(activity, summary4, team, inng);
                summaryList.add(summary4);
            }
        }
        return summaryList;
    }

    public static void populateScoreDetails(Activity activity, dtoMatchSummary summary, String team, String inngNo) {
        dtoMatchStats matchStatTeam = MatchHelper.getMatchScore(activity.getApplicationContext(), ((CricScorerApp) activity.getApplication()).currentMatch, team, inngNo);
        summary.overs = "" + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo();
        String extraText = "" + matchStatTeam.getTotalExtras();
        if (matchStatTeam.getTotalExtras() > 0) {
            extraText = extraText + " (";
        }
        if (matchStatTeam.getWides() > 0) {
            extraText = extraText + " w:" + matchStatTeam.getWides();
        }
        if (matchStatTeam.getNoballs() > 0) {
            extraText = extraText + " n:" + matchStatTeam.getNoballs();
        }
        if (matchStatTeam.getLegByes() > 0) {
            extraText = extraText + " l:" + matchStatTeam.getLegByes();
        }
        if (matchStatTeam.getByes() > 0) {
            extraText = extraText + " b:" + matchStatTeam.getByes();
        }
        if (matchStatTeam.getPenalty() > 0) {
            extraText = extraText + " p:" + matchStatTeam.getPenalty();
        }
        if (matchStatTeam.getTotalExtras() > 0) {
            extraText = extraText + " )";
        }
        summary.extras = extraText;
        summary.totalScore = "" + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount();
        summary.totalScoreDesc = ("" + matchStatTeam.getEOIDesc()).trim();
    }

    public static void populateBatsmenStats(Activity activity, dtoMatchSummary summary, String battingTeam, String inng) {
        dtoMatch refMatch = ((CricScorerApp) activity.getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(activity);
        List<dtoBatsStats> batsmenStat = db.getHighestRunBatsmen(refMatch.getMatchID(), battingTeam, inng, 3);
        db.close();
        int count = 0;
        for (dtoBatsStats row : batsmenStat) {
            if (count == 0) {
                summary.bats1 = "" + row.getBatsmanName();
                summary.bats1Score = "" + row.getRuns() + " (" + row.getBalls() + ")";
            } else if (count == 1) {
                summary.bats2 = "" + row.getBatsmanName();
                summary.bats2Score = "" + row.getRuns() + " (" + row.getBalls() + ")";
            } else {
                summary.bats3 = "" + row.getBatsmanName();
                summary.bats3Score = "" + row.getRuns() + " (" + row.getBalls() + ")";
            }
            count++;
        }
    }

    public static void populateBowlerStats(Activity activity, dtoMatchSummary summary, String bowlingTeam, String inngNo) {
        dtoMatch refMatch = ((CricScorerApp) activity.getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(activity);
        List<dtoBowlerStat> bowlerStat = db.getHighWicketTakers(refMatch.getMatchID(), bowlingTeam, inngNo, 3);
        db.close();
        int count = 0;
        for (dtoBowlerStat row : bowlerStat) {
            if (count == 0) {
                summary.bowl1 = "" + row.getBowlerName();
                summary.bowl1Overs = "" + row.getRuns() + "-" + row.getWickets();
            } else if (count == 1) {
                summary.bowl2 = "" + row.getBowlerName();
                summary.bowl2Overs = "" + row.getRuns() + "-" + row.getWickets();
            } else if (count == 2) {
                summary.bowl3 = "" + row.getBowlerName();
                summary.bowl3Overs = "" + row.getRuns() + "-" + row.getWickets();
            }
            count++;
        }
    }
}
