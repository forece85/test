package com.ganapathy.cricscorer;

import android.app.Activity;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MatchStatsHelper {
    public static String createMatchSummary(Activity activity, dtoMatch refMatch) {
        StringBuilder data = new StringBuilder(5000);
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String firstBattedTeamName = MatchHelper.getTeamName(refMatch, firstBatted);
        String secondBatted = MatchHelper.getSecondBatted(refMatch);
        String secondBattedTeamName = MatchHelper.getTeamName(refMatch, secondBatted);
        data.append("<html>");
        data.append(Utility.createStyleSheet());
        data.append("<body style=\"width:800px\">");
        data.append("<div class=\"CSSTableGenerator\">");
        data.append("<h3><u>MATCH SCORE SHEET</u></h3>");
        data.append(createMatchDetails(refMatch));
        data.append("<h3>" + firstBattedTeamName + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings</h3>");
        data.append(createBatsmenStats(activity, refMatch, firstBatted, "1"));
        data.append(createMatchScore(activity, refMatch, firstBatted, "1"));
        data.append(createMatchTeams(firstBattedTeamName + " team: " + getCommaSeparatedTeamPlayers(activity, refMatch, firstBatted)));
        data.append(createBowlerStats(activity, refMatch, firstBatted, "1"));
        data.append(createPartnershipStats(activity, refMatch, firstBatted, "1"));
        data.append(createOversByOver(activity, refMatch, firstBatted, "1"));
        if (refMatch.getCurrentSession() > 1) {
            data.append("<h3>" + secondBattedTeamName + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings</h3>");
            data.append(createBatsmenStats(activity, refMatch, secondBatted, "1"));
            data.append(createMatchScore(activity, refMatch, secondBatted, "1"));
            data.append(createMatchTeams(secondBattedTeamName + " team: " + getCommaSeparatedTeamPlayers(activity, refMatch, secondBatted)));
            data.append(createBowlerStats(activity, refMatch, secondBatted, "1"));
            data.append(createPartnershipStats(activity, refMatch, secondBatted, "1"));
            data.append(createOversByOver(activity, refMatch, secondBatted, "1"));
        }
        if (refMatch.getNoOfInngs() > 1 && refMatch.getCurrentSession() > 2) {
            String thirdBatted;
            if (refMatch.getFollowOn()) {
                thirdBatted = secondBatted;
            } else {
                thirdBatted = firstBatted;
            }
            data.append("<h3>" + MatchHelper.getTeamName(refMatch, thirdBatted) + " - 2nd Innings" + (refMatch.getFollowOn() ? "(FOLLOW ON)" : "") + "</h3>");
            data.append(createBatsmenStats(activity, refMatch, thirdBatted, "2"));
            data.append(createMatchScore(activity, refMatch, thirdBatted, "2"));
            data.append(createMatchTeams(MatchHelper.getTeamName(refMatch, thirdBatted) + " team: " + getCommaSeparatedTeamPlayers(activity, refMatch, thirdBatted)));
            data.append(createBowlerStats(activity, refMatch, thirdBatted, "2"));
            data.append(createPartnershipStats(activity, refMatch, thirdBatted, "2"));
            data.append(createOversByOver(activity, refMatch, thirdBatted, "2"));
            if (refMatch.getCurrentSession() > 3) {
                String lastBatted;
                if (refMatch.getFollowOn()) {
                    lastBatted = firstBatted;
                } else {
                    lastBatted = secondBatted;
                }
                data.append("<h3>" + MatchHelper.getTeamName(refMatch, lastBatted) + " - 2nd Innings</h3>");
                data.append(createBatsmenStats(activity, refMatch, lastBatted, "2"));
                data.append(createMatchScore(activity, refMatch, lastBatted, "2"));
                data.append(createMatchTeams(MatchHelper.getTeamName(refMatch, lastBatted) + " team: " + getCommaSeparatedTeamPlayers(activity, refMatch, lastBatted)));
                data.append(createBowlerStats(activity, refMatch, lastBatted, "2"));
                data.append(createPartnershipStats(activity, refMatch, lastBatted, "2"));
                data.append(createOversByOver(activity, refMatch, lastBatted, "2"));
            }
        }
        data.append(Utility.createFooter());
        data.append("</div>");
        data.append("</body></html>");
        return data.toString();
    }

    public static String createMatchDetails(dtoMatch refMatch) {
        String output = (((("<table>" + "<tr><th colspan=\"4\"><div width=\"100%\">MATCH DETAILS</div></th></tr>") + "<tr><td>Match ID</td><td>" + refMatch.getMatchID() + "</td><td>Date and time</td><td>" + refMatch.getDateTime() + "</td></tr>") + "<tr><td>Venue</td><td>" + refMatch.getVenue() + "</td><td>Overs</td><td>" + (refMatch.getOvers() == 4479 ? "Unlimited" : Integer.valueOf(refMatch.getOvers())) + "</td></tr>") + "<tr><td>Team 1</td><td>" + refMatch.getTeam1Name() + "</td><td>Team 2</td><td>" + refMatch.getTeam2Name() + "</td></tr>") + "<tr><td>Toss Won by</td><td>" + refMatch.getTossWonTeam() + "</td><td>Opted to</td><td>" + refMatch.getOptedTo() + "</td></tr>";
        if (!refMatch.getMatchResult().trim().equalsIgnoreCase("")) {
            output = output + "<tr><td>Match Result</td><td colspan=\"3\"><div width=\"100%\">" + refMatch.getMatchResult().toUpperCase(Locale.US) + "</div></td></tr>";
        }
        output = output + "<tr><th colspan=\"4\"><div width=\"100%\">MATCH SETTINGS</div></th></tr>";
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        return (((((output + "<tr><td>Wide ball</td><td>" + (settings.getWide() == 1 ? "Yes" : "No") + "</td><td>Wide ball Re-ball</td><td>" + (settings.getWideReball() == 1 ? "Yes" : "No") + "</td></tr>") + "<tr><td>No ball</td><td>" + (settings.getNoball() == 1 ? "Yes" : "No") + "</td><td>No ball Re-ball</td><td>" + (settings.getNoballReball() == 1 ? "Yes" : "No") + "</td></tr>") + "<tr><td>Wide ball Run</td><td>" + settings.getWideRun() + "</td><td>No ball Run</td><td>" + settings.getNoballRun() + "</td></tr>") + "<tr><td>Leg Byes</td><td>" + (settings.getLegbyes() == 1 ? "Yes" : "No") + "</td><td>Byes</td><td>" + (settings.getByes() == 1 ? "Yes" : "No") + "</td></tr>") + "<tr><td>LBW</td><td>" + (settings.getLbw() == 1 ? "Yes" : "No") + "</td><td>Balls per Over</td><td>" + settings.getBpo() + " " + (settings.getRestrictBpo() == 1 ? "restricted to " + settings.getMaxBpo() : "") + "</td></tr>") + "</table>";
    }

    public static String createBatsmenStats(Activity activity, dtoMatch refMatch, String battingTeam, String inng) {
        DatabaseHandler db = new DatabaseHandler(activity);
        List<dtoBatsStats> batsmenStat = db.getBatsmanStatsForMatchSheet(refMatch.getMatchID(), battingTeam, inng);
        db.close();
        String table = ("<table>" + "<tr><th colspan=\"7\"><div width=\"100%\">SCORECARD</div></th></tr>") + "<tr><th>Batsmen</th><th width=\"50%\"><div width=\"100%\">Runs by ball faced</div></th><th></th><th>Runs</th><th>S/R</th><th>6's</th><th>4's</th></tr>";
        for (dtoBatsStats row : batsmenStat) {
            if (row.getBowlerName().trim().equalsIgnoreCase("") && row.getWicketAssist().trim().equalsIgnoreCase("")) {
                row.setWicketAssist("n.o.");
            }
            table = ((table + "<tr>") + "<td nowrap>" + Utility.toDisplayCase(row.getBatsmanName()) + "</td>") + "<td><div width=\"100%\">" + row.getRunsByBall() + "</div></td>";
            if (row.getWicketHow().contains("Run out")) {
                if (row.getWicketAssist().trim().equalsIgnoreCase("")) {
                    table = table + "<td>Run out</td>";
                } else {
                    table = table + "<td>Run out (" + Utility.toDisplayCase(row.getWicketAssist()) + ")</td>";
                }
            } else if (row.getWicketHow().contains("Retired")) {
                table = table + "<td>Retired</td>";
            } else if (row.getWicketHow().contains("hit") || row.getWicketHow().contains("timed") || row.getWicketHow().contains("handled") || row.getWicketHow().contains("obstruct")) {
                table = table + "<td>" + row.getWicketHow() + "</td>";
            } else if (row.getWicketAssist().contains("n.o.")) {
                table = table + "<td>not out</td>";
            } else if (row.getBowlerName().trim().equalsIgnoreCase("")) {
                table = table + "<td></td>";
            } else {
                table = table + "<td>" + ("" + row.getWicketHow() + " " + Utility.toDisplayCase(row.getWicketAssist()) + " b " + Utility.toDisplayCase(row.getBowlerName())).trim() + "</td>";
            }
            if (row.getRuns() == 0 && row.getBalls() == 0) {
                table = table + "<td nowrap>0 (0)</td>";
            } else {
                table = table + "<td nowrap>" + row.getRuns() + " (" + row.getBalls() + ")</td>";
            }
            table = (((table + "<td nowrap>" + (row.getRunRate() == 0 ? "" : Integer.valueOf(row.getRunRate())) + "</td>") + "<td nowrap>" + (row.getSixes() == 0 ? "" : Integer.valueOf(row.getSixes())) + "</td>") + "<td nowrap>" + (row.getFours() == 0 ? "&nbsp;" : Integer.valueOf(row.getFours())) + "</td>") + "</tr>";
        }
        return table + "</table>";
    }

    public static String createMatchScore(Activity activity, dtoMatch refMatch, String team, String inng) {
        dtoMatchStats matchStatTeam = MatchHelper.getMatchScore(activity.getApplicationContext(), refMatch, team, inng);
        String output = "<table>";
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
        return ((output + "<tr><th style=\"width:50px\">Overs:</th><td style=\"width:50px\">" + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo() + "</td><th style=\"width:50px\">Extras:</th><td>" + extraText + "</td><th style=\"width:80px\">Total Score:</th><td style=\"width:65px\"><font size=\"+1\"><b>" + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount() + "</b></font>" + (matchStatTeam.getEOIDesc().equalsIgnoreCase("Declare") ? " dec" : "") + "</td></tr>") + "<tr><th style=\"width:50px\">FOW:</th><td colspan=\"5\"><div style=\"width:100%\">" + getFOW(activity, refMatch, team, inng) + "</div></td></tr>") + "</table>";
    }

    public static String getFOW(Activity activity, dtoMatch refMatch, String team, String inng) {
        DatabaseHandler db = new DatabaseHandler(activity);
        String fow = db.getFallOfWickets(refMatch.getMatchID(), team, inng);
        db.close();
        return fow != null ? fow : "";
    }

    public static String createMatchTeams(String players) {
        return ("<table>" + "<tr><td>" + players + "</td></tr>") + "</table>";
    }

    public static String getCommaSeparatedTeamPlayers(Activity activity, dtoMatch refMatch, String team) {
        String playerNames = "";
        try {
            DatabaseHandler db = new DatabaseHandler(activity);
            List<String> players = db.getMatchTeamPlayers(refMatch.getMatchID(), team);
            db.close();
            for (String player : players) {
                if (playerNames.equalsIgnoreCase("")) {
                    playerNames = playerNames + Utility.toDisplayCase(player);
                } else {
                    playerNames = playerNames + ", " + Utility.toDisplayCase(player);
                }
            }
            return playerNames;
        } catch (Exception e) {
            return playerNames + ", " + e.getMessage();
        }
    }

    public static String createBowlerStats(Activity activity, dtoMatch refMatch, String bowlingTeam, String inng) {
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        DatabaseHandler db = new DatabaseHandler(activity);
        List<dtoBowlerStat> bowlerStat = db.getBowlerStats(refMatch.getMatchID(), bowlingTeam, inng);
        db.close();
        String table = ("<table>" + "<tr><th colspan=\"9\"><div width=\"100%\">BOWLING STATS</div></th></tr>") + "<tr><th>Bowler</th><th>Overs</th><th>Runs</th><th>Wkts</th><th>Econ</th><th>Avg</th><th>Mdns</th><th>Wd</th><th>Nb</th></tr>";
        for (dtoBowlerStat row : bowlerStat) {
            table = (table + "<tr>") + "<td>" + Utility.toDisplayCase(row.getBowlerName()) + "</td>";
            if (settings.getRestrictBpo() == 1) {
                table = table + "<td>" + row.getCalculatedOvers() + "." + row.getCalculatedBalls() + "</td>";
            } else {
                table = table + "<td>" + row.getOvers() + "." + row.getBalls() + "</td>";
            }
            table = ((table + "<td>" + row.getRuns() + "</td>") + "<td>" + row.getWickets() + "</td>") + "<td>" + row.getRunRate() + "</td>";
            if (row.getWickets() > 0) {
                table = table + "<td>" + new DecimalFormat("0.00").format((((double) row.getRuns()) * 1.0d) / ((double) row.getWickets())) + "</td>";
            } else {
                table = table + "<td>-</td>";
            }
            table = (((table + "<td>" + row.getMaidenOvers() + "</td>") + "<td>" + row.getWides() + "</td>") + "<td>" + row.getNoballs() + "</td>") + "</tr>";
        }
        return table + "</table>";
    }

    public static String createPartnershipStats(Activity activity, dtoMatch refMatch, String battingTeam, String inng) {
        DatabaseHandler db = new DatabaseHandler(activity);
        PartnershipStatsList statList = db.getPartnershipData(refMatch.getMatchID(), battingTeam, inng);
        db.close();
        String table = ("<table>" + "<tr><th colspan=\"4\"><div width=\"100%\">PARTNERSHIP STATS</div></th></tr>") + "<tr><th>Batsmen</th><th>Partnership</th><th>Batsmen</th><th>Extras</th></tr>";
        Iterator it = statList.iterator();
        while (it.hasNext()) {
            dtoPartnershipStats row = (dtoPartnershipStats) it.next();
            table = (((((table + "<tr>") + "<td>" + Utility.toDisplayCase(row.striker1) + " (" + row.striker1Runs + ")</td>") + "<td>" + (((row.striker1Runs + row.striker2Runs) + row.striker1Extras) + row.striker2Extras) + " (" + (row.striker1Balls + row.striker2Balls) + ")</td>") + "<td>" + Utility.toDisplayCase(row.striker2) + " (" + row.striker2Runs + ")</td>") + "<td>" + (row.striker1Extras + row.striker2Extras) + "</td>") + "</tr>";
        }
        return table + "</table>";
    }

    public static String createOversByOver(Activity activity, dtoMatch refMatch, String bowlingTeam, String inng) {
        DatabaseHandler db = new DatabaseHandler(activity);
        List<dtoBowlerStat> bowlerStat = db.getBowlerStatsForMatchSheet(refMatch.getMatchID(), bowlingTeam, inng);
        db.close();
        String table = ("<table>" + "<tr><th colspan=\"8\"><div width=\"100%\">OVER BY OVER</div></th></tr>") + "<tr><th>Bowler</th><th>Over No.</th><th>Ball by ball</th><th>Runs</th><th>Wides</th><th>Noballs</th><th>Wickets</th><th>Score</th></tr>";
        for (dtoBowlerStat row : bowlerStat) {
            table = (((((((((table + "<tr>") + "<td>" + Utility.toDisplayCase(row.getBowlerName()) + "</td>") + "<td>" + (row.getOvers() + 1) + "</td>") + "<td>" + row.getBallByBall() + "</td>") + "<td>" + row.getRuns() + "</td>") + "<td>" + row.getWides() + "</td>") + "<td>" + row.getNoballs() + "</td>") + "<td>" + row.getWickets() + "</td>") + "<td>" + row.getScore() + "</td>") + "</tr>";
        }
        return table + "</table>";
    }

    public static String createBallByBallReport(Activity activity, dtoMatch refMatch) {
        StringBuilder data = new StringBuilder(5000);
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String firstBattedTeamName = MatchHelper.getTeamName(refMatch, firstBatted);
        String secondBatted = MatchHelper.getSecondBatted(refMatch);
        String secondBattedTeamName = MatchHelper.getTeamName(refMatch, secondBatted);
        data.append("<html>");
        data.append(Utility.createStyleSheet());
        data.append("<body style=\"width:800px\">");
        data.append("<div class=\"CSSTableGenerator\">");
        data.append("<h3><u>MATCH REPORT - BALL BY BALL STATUS</u></h3>");
        data.append(createMatchDetails(refMatch));
        data.append("<h3>" + firstBattedTeamName + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings</h3>");
        data.append(createMatchScore(activity, refMatch, firstBatted, "1"));
        data.append(createBallByBallStats(activity, refMatch, firstBatted, "1"));
        if (refMatch.getCurrentSession() > 1) {
            data.append("<h3>" + secondBattedTeamName + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings</h3>");
            data.append(createMatchScore(activity, refMatch, secondBatted, "1"));
            data.append(createBallByBallStats(activity, refMatch, secondBatted, "1"));
        }
        if (refMatch.getNoOfInngs() > 1 && refMatch.getCurrentSession() > 2) {
            String thirdBatted;
            if (refMatch.getFollowOn()) {
                thirdBatted = secondBatted;
            } else {
                thirdBatted = firstBatted;
            }
            data.append("<h3>" + MatchHelper.getTeamName(refMatch, thirdBatted) + " - 2nd Innings" + (refMatch.getFollowOn() ? "(FOLLOW ON)" : "") + "</h3>");
            data.append(createMatchScore(activity, refMatch, thirdBatted, "2"));
            data.append(createBallByBallStats(activity, refMatch, thirdBatted, "2"));
            if (refMatch.getCurrentSession() > 3) {
                String lastBatted;
                if (refMatch.getFollowOn()) {
                    lastBatted = firstBatted;
                } else {
                    lastBatted = secondBatted;
                }
                data.append("<h3>" + MatchHelper.getTeamName(refMatch, lastBatted) + " - 2nd Innings</h3>");
                data.append(createMatchScore(activity, refMatch, lastBatted, "2"));
                data.append(createBallByBallStats(activity, refMatch, lastBatted, "2"));
            }
        }
        data.append(Utility.createFooter());
        data.append("</div>");
        data.append("</body></html>");
        return data.toString();
    }

    public static String createBallByBallStats(Activity activity, dtoMatch refMatch, String battingTeam, String inng) {
        DatabaseHandler db = new DatabaseHandler(activity);
        List<dtoBallByBallStat> batsmenStat = db.getBallByBallReport(refMatch.getMatchID(), battingTeam, inng);
        db.close();
        String table = "<table>" + "<tr><th>Over</th><th>Striker</th><th>Non-striker</th><th>Bowler</th><th>Has Extra</th><th>Total Score</th><th>This Over</th></tr>";
        for (dtoBallByBallStat row : batsmenStat) {
            table = ((((((((table + "<tr>") + "<td>" + row.getOvers() + "." + row.getBallNo() + "</td>") + "<td>" + Utility.toDisplayCase(row.getStriker()) + "</td>") + "<td>" + Utility.toDisplayCase(row.getNonStriker()) + "</td>") + "<td>" + row.getBowler() + "</td>") + "<td>" + row.getHasExtra() + "</td>") + "<td>" + row.getTotalScore() + "</td>") + "<td>" + row.getBallByBall() + "</td>") + "</tr>";
        }
        return table + "</table>";
    }

    public static String createClassicScorecard(Activity activity, dtoMatch refMatch, String width) {
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String secondBatted = MatchHelper.getSecondBatted(refMatch);
        String data = ((((((((("" + "<html>") + Utility.createStyleSheet()) + "<body style=\"width:" + width + "px\">") + "<div class=\"CSSTableGenerator\">") + createClassicMatchHeader(refMatch)) + "<h3>" + MatchHelper.getTeamName(refMatch, firstBatted) + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings</h3>") + createClassicBatsmenStats(activity, refMatch, firstBatted, "1")) + createMatchScore(activity, refMatch, firstBatted, "1")) + createMatchTeams(MatchHelper.getTeamName(refMatch, firstBatted) + " team: " + getCommaSeparatedTeamPlayers(activity, refMatch, firstBatted))) + createBowlerStats(activity, refMatch, firstBatted, "1");
        if (refMatch.getCurrentSession() > 1) {
            data = ((((data + "<h3>" + MatchHelper.getTeamName(refMatch, secondBatted) + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings</h3>") + createClassicBatsmenStats(activity, refMatch, secondBatted, "1")) + createMatchScore(activity, refMatch, secondBatted, "1")) + createMatchTeams(MatchHelper.getTeamName(refMatch, secondBatted) + " team: " + getCommaSeparatedTeamPlayers(activity, refMatch, secondBatted))) + createBowlerStats(activity, refMatch, secondBatted, "1");
        }
        if (refMatch.getNoOfInngs() > 1 && refMatch.getCurrentSession() > 2) {
            String thirdBatted;
            if (refMatch.getFollowOn()) {
                thirdBatted = secondBatted;
            } else {
                thirdBatted = firstBatted;
            }
            data = (((data + "<h3>" + MatchHelper.getTeamName(refMatch, thirdBatted) + " - 2nd Innings" + (refMatch.getFollowOn() ? "(FOLLOW ON)" : "") + "</h3>") + createClassicBatsmenStats(activity, refMatch, thirdBatted, "2")) + createMatchScore(activity, refMatch, thirdBatted, "2")) + createBowlerStats(activity, refMatch, thirdBatted, "2");
            if (refMatch.getCurrentSession() > 3) {
                String lastBatted;
                if (refMatch.getFollowOn()) {
                    lastBatted = firstBatted;
                } else {
                    lastBatted = secondBatted;
                }
                data = (((data + "<h3>" + MatchHelper.getTeamName(refMatch, lastBatted) + " - 2nd Innings</h3>") + createClassicBatsmenStats(activity, refMatch, lastBatted, "2")) + createMatchScore(activity, refMatch, lastBatted, "2")) + createBowlerStats(activity, refMatch, lastBatted, "2");
            }
        }
        return ((data + Utility.createFooter()) + "</div>") + "</body></html>";
    }

    public static String createClassicMatchHeader(dtoMatch refMatch) {
        String output = (("" + "<h3>" + refMatch.getTeam1Name() + " vs " + refMatch.getTeam2Name()) + "<br />Played on " + refMatch.getDateTime()) + "<br />" + (refMatch.getOvers() == 4479 ? "Unlimited" : Integer.valueOf(refMatch.getOvers())) + " Overs, Toss won by " + refMatch.getTossWonTeam() + " and opted to " + refMatch.getOptedTo();
        if (!refMatch.getVenue().trim().equalsIgnoreCase("")) {
            output = output + "<br />" + refMatch.getVenue();
        }
        return output + "</h3>";
    }

    public static String createClassicBatsmenStats(Activity activity, dtoMatch refMatch, String battingTeam, String inng) {
        DatabaseHandler db = new DatabaseHandler(activity);
        List<dtoBatsStats> batsmenStat = db.getBatsmanStatsForMatchSheet(refMatch.getMatchID(), battingTeam, inng);
        db.close();
        String table = "<table>" + "<tr><th>Batsmen</th><th width=\"50%\">&nbsp;</th><th>Runs</th><th>S/R</th><th>6's</th><th>4's</th></tr>";
        for (dtoBatsStats row : batsmenStat) {
            if (row.getBowlerName().trim().equalsIgnoreCase("") && row.getWicketAssist().trim().equalsIgnoreCase("")) {
                row.setWicketAssist("n.o.");
            }
            table = (table + "<tr>") + "<td nowrap>" + Utility.toDisplayCase(row.getBatsmanName()) + "</td>";
            if (row.getWicketHow().contains("Run out")) {
                if (row.getWicketAssist().trim().equalsIgnoreCase("")) {
                    table = table + "<td>Run out</td>";
                } else {
                    table = table + "<td>Run out (" + Utility.toDisplayCase(row.getWicketAssist()) + ")</td>";
                }
            } else if (row.getWicketHow().contains("Retired")) {
                table = table + "<td>Retired</td>";
            } else if (row.getWicketHow().contains("hit") || row.getWicketHow().contains("timed") || row.getWicketHow().contains("handled") || row.getWicketHow().contains("obstruct")) {
                table = table + "<td>" + row.getWicketHow() + "</td>";
            } else if (row.getWicketAssist().contains("n.o.")) {
                table = table + "<td>not out</td>";
            } else if (row.getBowlerName().trim().equalsIgnoreCase("")) {
                table = table + "<td></td>";
            } else {
                table = table + "<td>" + ("" + row.getWicketHow() + " " + Utility.toDisplayCase(row.getWicketAssist()) + " b " + Utility.toDisplayCase(row.getBowlerName())).trim() + "</td>";
            }
            if (row.getRuns() == 0 && row.getBalls() == 0) {
                table = table + "<td nowrap>0 (0)</td>";
            } else {
                table = table + "<td nowrap>" + row.getRuns() + " (" + row.getBalls() + ")</td>";
            }
            table = (((table + "<td nowrap>" + (row.getRunRate() == 0 ? "" : Integer.valueOf(row.getRunRate())) + "</td>") + "<td nowrap>" + (row.getSixes() == 0 ? "" : Integer.valueOf(row.getSixes())) + "</td>") + "<td nowrap>" + (row.getFours() == 0 ? "&nbsp;" : Integer.valueOf(row.getFours())) + "</td>") + "</tr>";
        }
        return table + "</table>";
    }
}
