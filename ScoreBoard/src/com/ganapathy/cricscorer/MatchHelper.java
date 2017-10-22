package com.ganapathy.cricscorer;

import android.content.Context;

public class MatchHelper {
    public static String getFirstBatted(dtoMatch refMatch) {
        String _firstBatted = "";
        if (refMatch.getTossWonBy().equalsIgnoreCase("Team 1")) {
            if (refMatch.getOptedTo().equalsIgnoreCase("BAT")) {
                return "1";
            }
            return "2";
        } else if (refMatch.getOptedTo().equalsIgnoreCase("BAT")) {
            return "2";
        } else {
            return "1";
        }
    }

    public static String getSecondBatted(dtoMatch refMatch) {
        String _secondBatted = "";
        if (refMatch.getTossWonBy().equalsIgnoreCase("Team 1")) {
            if (refMatch.getOptedTo().equalsIgnoreCase("BAT")) {
                return "2";
            }
            return "1";
        } else if (refMatch.getOptedTo().equalsIgnoreCase("BAT")) {
            return "1";
        } else {
            return "2";
        }
    }

    public static String getTeamName(dtoMatch refMatch, String team) {
        if (team.equalsIgnoreCase("1")) {
            return refMatch.getTeam1Name();
        }
        return refMatch.getTeam2Name();
    }

    public static dtoMatchStats getMatchScore(Context context, dtoMatch refMatch, String team, String inng) {
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        DatabaseHandler db = new DatabaseHandler(context);
        dtoMatchStats matchStatTeam = db.getMatchStats(refMatch.getMatchID(), team, inng);
        db.close();
        if (settings.getRestrictBpo() == 1) {
            if (matchStatTeam.getBallsThisOver() == settings.getMaxBpo()) {
                matchStatTeam.setOverNo(matchStatTeam.getOverNo() + 1);
                matchStatTeam.setBallNo(0);
            }
        } else if (matchStatTeam.getBallNo() == settings.getBpo()) {
            matchStatTeam.setOverNo(matchStatTeam.getOverNo() + 1);
            matchStatTeam.setBallNo(0);
        }
        return matchStatTeam;
    }

    public static void updateMatchSessions(dtoMatch refMatch) {
        dtoScoreBall ball = refMatch.getLastScoreball();
        if (ball != null) {
            if (ball.isFollowOnInnings()) {
                refMatch.setFollowOn(true);
            }
            if (ball.getTeamNo().equalsIgnoreCase(refMatch.getFirstBattedTeamNo()) && ball.getInningNo() == 1) {
                refMatch.setCurrentSession(1);
                return;
            } else if (!ball.getTeamNo().equalsIgnoreCase(refMatch.getFirstBattedTeamNo()) && ball.getInningNo() == 1) {
                refMatch.setCurrentSession(2);
                return;
            } else if (ball.getTeamNo().equalsIgnoreCase(refMatch.getFirstBattedTeamNo()) && ball.getInningNo() == 2 && !refMatch.getFollowOn()) {
                refMatch.setCurrentSession(3);
                return;
            } else if (!ball.getTeamNo().equalsIgnoreCase(refMatch.getFirstBattedTeamNo()) && ball.getInningNo() == 2 && !refMatch.getFollowOn()) {
                refMatch.setCurrentSession(4);
                return;
            } else if (ball.getTeamNo().equalsIgnoreCase(refMatch.getFirstBattedTeamNo()) && ball.getInningNo() == 2 && refMatch.getFollowOn()) {
                refMatch.setCurrentSession(4);
                return;
            } else if (!ball.getTeamNo().equalsIgnoreCase(refMatch.getFirstBattedTeamNo()) && ball.getInningNo() == 2 && refMatch.getFollowOn()) {
                refMatch.setCurrentSession(3);
                return;
            } else {
                refMatch.setCurrentSession(1);
                return;
            }
        }
        refMatch.setCurrentSession(1);
    }
}
