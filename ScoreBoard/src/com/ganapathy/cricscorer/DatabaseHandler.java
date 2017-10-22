package com.ganapathy.cricscorer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String BALLSPEROVER = "BPO";
    private static final String BALLS_THIS_OVER = "BallsThisOver";
    private static final String BALL_BY_BALL = "BallByBall";
    private static final String BALL_NO = "BallNo";
    private static final String BALL_SPOT = "BallSpot";
    private static final String BOWLER_NAME = "BowlerName";
    private static final String BS_X_CORD = "BsXCord";
    private static final String BS_Y_CORD = "BsYCord";
    private static final String BYES = "Byes";
    private static final String DATABASE_NAME = "ScoreManager";
    private static final int DATABASE_VERSION = 12;
    private static final String DAY_NO = "DayNo";
    private static final String EOI_DESC = "EOIDesc";
    private static final String FLAG_EOD = "FEOD";
    private static final String FLAG_EOI = "FEOI";
    private static final String HANDED_BAT = "HandedBat";
    private static final String HANDED_BOWL = "HandedBowl";
    private static final String INLINEWWANDBS_CONFIGURATION = "InlineWwBs";
    private static final String INNG_NO = "InngNo";
    private static final String IS_BATSMAN = "IsBatsman";
    private static final String IS_BOWLER = "IsBowler";
    private static final String IS_BYES = "IsByes";
    private static final String IS_CAPTAIN = "IsCaptain";
    private static final String IS_KEEPER = "IsKeeper";
    private static final String IS_LEGBYES = "IsLegByes";
    private static final String IS_NOBALL = "IsNoball";
    private static final String IS_PENALTY = "IsPenalty";
    private static final String IS_WICKET = "IsWicket";
    private static final String IS_WIDE = "IsWide";
    private static final String LBW = "Lbw";
    private static final String LEG_BYES = "LegByes";
    private static final String MAIL_CONFIGURATION = "MailIds";
    private static final String MATCH_DATETIME = "MatchDateTime";
    private static final String MATCH_ID = "MatchID";
    private static final String MATCH_NAME = "MatchName";
    private static final String MATCH_OVERS = "Overs";
    private static final String MATCH_RESULT = "Result";
    private static final String MAX_BALLSPEROVER = "MaxBPO";
    private static final String NOBALL_RUN = "NoballRun";
    private static final String NONSTRIKER_BALLS = "NonStrikerBallsFaced";
    private static final String NONSTRIKER_NAME = "NonStrikerName";
    private static final String NONSTRIKER_RUNS = "NonStrikerRuns";
    private static final String NO_BALL = "NoBall";
    private static final String NO_BALL_REBALL = "NoballReball";
    private static final String NO_OF_DAYS = "NoOfDays";
    private static final String NO_OF_INNGS = "NoOfInngs";
    private static final String OPTED_TO = "OptedTo";
    private static final String OVER_NO = "OverNo";
    private static final String PLAYER_NAME = "PlayerName";
    private static final String PLAYER_ORDER = "PlayerOrder";
    private static final String PRESET_TEAMS = "PresetTeams";
    private static final String RESTRICT_BALLSPEROVER = "RestrictBPO";
    private static final String RUNS_THIS_BALL = "RunsThisBall";
    private static final String RUN_SCORED = "RunScored";
    private static final String STRIKER_BALLS = "StrikerBallsFaced";
    private static final String STRIKER_NAME = "StrikerName";
    private static final String STRIKER_RUNS = "StrikerRuns";
    private static final String TABLE_CONFIGURATION = "Configuration";
    private static final String TABLE_DEFAULT_ADDITIONAL_SETTINGS = "DefaultAdditionalSettings";
    private static final String TABLE_DEFAULT_TEAMS = "DefaultTeams";
    private static final String TABLE_MANAGE_TEAMS = "ManageTeams";
    private static final String TABLE_MATCH_MASTER = "MatchMaster";
    private static final String TABLE_MATCH_SCOREBOARD = "MatchScoreBoard";
    private static final String TABLE_MATCH_TEAMS = "MatchTeams";
    private static final String TARGET = "Target";
    private static final String TEAM1_NAME = "Team1Name";
    private static final String TEAM2_NAME = "Team2Name";
    private static final String TEAM_NAME = "TeamName";
    private static final String TEAM_NO = "Team";
    private static final String THEME_CONFIGURATION = "ThemeId";
    private static final String TOSS_WON_BY = "TossWonBy";
    private static final String TOTAL_BALL_NO = "TotalBallNo";
    private static final String TOTAL_EXTRAS = "TotalExtras";
    private static final String TOTAL_SCORE = "TotalScore";
    private static final String VENUE = "Venue";
    private static final String WAGON_WHEEL = "WagonWheel";
    private static final String WDNBTOBOWLER_CONFIGURATION = "WdNbToBowler";
    private static final String WHAT_BOWLER = "WhatBowler";
    private static final String WICKET_ASSIST = "WicketAssist";
    private static final String WICKET_COUNT = "WicketCount";
    private static final String WICKET_HOW = "WicketHow";
    private static final String WIDE_BALL = "WideBall";
    private static final String WIDE_BALL_REBALL = "WideReball";
    private static final String WIDE_RUN = "WideRun";
    private static final String WW_FOR_DOT = "WwDot";
    private static final String WW_FOR_WICKET = "WwWicket";
    private static final String WW_X_CORD = "XCord";
    private static final String WW_Y_CORD = "YCord";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 12);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEFAULT_TEAMS_TABLE = "CREATE TABLE IF NOT EXISTS DefaultTeams(Team TEXT,PlayerName TEXT )";
        db.execSQL("CREATE TABLE IF NOT EXISTS DefaultTeams(Team TEXT,PlayerName TEXT )");
        String CREATE_DEFAULT_ADDITIONAL_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS DefaultAdditionalSettings(WideBall INT,WideReball INT, NoBall INT, NoballReball INT, LegByes INT, Byes INT, Lbw INT, WagonWheel INT, BallSpot INT, PresetTeams INT, WideRun INT, NoballRun INT, RestrictBPO INT, MaxBPO INT, BPO INT NOT NULL DEFAULT 6, WwDot INT DEFAULT 0, WwWicket INT DEFAULT 0)";
        db.execSQL("CREATE TABLE IF NOT EXISTS DefaultAdditionalSettings(WideBall INT,WideReball INT, NoBall INT, NoballReball INT, LegByes INT, Byes INT, Lbw INT, WagonWheel INT, BallSpot INT, PresetTeams INT, WideRun INT, NoballRun INT, RestrictBPO INT, MaxBPO INT, BPO INT NOT NULL DEFAULT 6, WwDot INT DEFAULT 0, WwWicket INT DEFAULT 0)");
        String CREATE_MATCH_MASTER_TABLE = "CREATE TABLE IF NOT EXISTS MatchMaster(MatchID TEXT,MatchDateTime TEXT,Venue TEXT,Team1Name TEXT,Team2Name TEXT,TossWonBy TEXT,OptedTo TEXT,Overs INT,WideBall INT,WideReball INT, NoBall INT, NoballReball INT, LegByes INT, Byes INT, Lbw INT, WagonWheel INT, BallSpot INT, PresetTeams INT, WideRun INT, NoballRun INT, RestrictBPO INT, MaxBPO INT, BPO INT NOT NULL DEFAULT 6,Result TEXT, WwDot INT DEFAULT 0, WwWicket INT DEFAULT 0, NoOfInngs  INT DEFAULT 1,NoOfDays  INT DEFAULT 1,MatchName  TEXT)";
        db.execSQL("CREATE TABLE IF NOT EXISTS MatchMaster(MatchID TEXT,MatchDateTime TEXT,Venue TEXT,Team1Name TEXT,Team2Name TEXT,TossWonBy TEXT,OptedTo TEXT,Overs INT,WideBall INT,WideReball INT, NoBall INT, NoballReball INT, LegByes INT, Byes INT, Lbw INT, WagonWheel INT, BallSpot INT, PresetTeams INT, WideRun INT, NoballRun INT, RestrictBPO INT, MaxBPO INT, BPO INT NOT NULL DEFAULT 6,Result TEXT, WwDot INT DEFAULT 0, WwWicket INT DEFAULT 0, NoOfInngs  INT DEFAULT 1,NoOfDays  INT DEFAULT 1,MatchName  TEXT)");
        String CREATE_MATCH_TEAMS_TABLE = "CREATE TABLE IF NOT EXISTS MatchTeams(MatchID TEXT,Team TEXT,PlayerName TEXT, PlayerOrder INT, HandedBat INT,HandedBowl INT,WhatBowler INT,IsBatsman INT,IsBowler INT,IsCaptain INT,IsKeeper INT )";
        db.execSQL("CREATE TABLE IF NOT EXISTS MatchTeams(MatchID TEXT,Team TEXT,PlayerName TEXT, PlayerOrder INT, HandedBat INT,HandedBowl INT,WhatBowler INT,IsBatsman INT,IsBowler INT,IsCaptain INT,IsKeeper INT )");
        String CREATE_MATCH_SCOREBOARD_TABLE = "CREATE TABLE IF NOT EXISTS MatchScoreBoard(MatchID TEXT,Team TEXT,TotalBallNo INT,OverNo INT,BallNo INT,StrikerName TEXT,NonStrikerName TEXT, BowlerName TEXT, IsWide INT, IsNoball INT, IsLegByes INT, IsByes INT,IsWicket INT, WicketHow TEXT, WicketAssist TEXT, RunScored INT, RunsThisBall INT, BallByBall TEXT, TotalScore INT,WicketCount INT,StrikerRuns INT,NonStrikerRuns INT,StrikerBallsFaced INT,NonStrikerBallsFaced INT,Target INT,XCord INT,YCord INT,BsXCord INT,BsYCord INT,TotalExtras INT,BallsThisOver INT,InngNo INT DEFAULT 1,DayNo INT DEFAULT 1,FEOD INT,FEOI INT,EOIDesc TEXT,IsPenalty INT DEFAULT 0)";
        db.execSQL("CREATE TABLE IF NOT EXISTS MatchScoreBoard(MatchID TEXT,Team TEXT,TotalBallNo INT,OverNo INT,BallNo INT,StrikerName TEXT,NonStrikerName TEXT, BowlerName TEXT, IsWide INT, IsNoball INT, IsLegByes INT, IsByes INT,IsWicket INT, WicketHow TEXT, WicketAssist TEXT, RunScored INT, RunsThisBall INT, BallByBall TEXT, TotalScore INT,WicketCount INT,StrikerRuns INT,NonStrikerRuns INT,StrikerBallsFaced INT,NonStrikerBallsFaced INT,Target INT,XCord INT,YCord INT,BsXCord INT,BsYCord INT,TotalExtras INT,BallsThisOver INT,InngNo INT DEFAULT 1,DayNo INT DEFAULT 1,FEOD INT,FEOI INT,EOIDesc TEXT,IsPenalty INT DEFAULT 0)");
        String CREATE_CONFIGURATION_TABLE = "CREATE TABLE IF NOT EXISTS Configuration(MailIds TEXT,ThemeId INT,WdNbToBowler INT DEFAULT 0, InlineWwBs INT DEFAULT 0)";
        db.execSQL("CREATE TABLE IF NOT EXISTS Configuration(MailIds TEXT,ThemeId INT,WdNbToBowler INT DEFAULT 0, InlineWwBs INT DEFAULT 0)");
        String CREATE_MANAGE_TEAMS_TABLE = "CREATE TABLE IF NOT EXISTS ManageTeams(TeamName TEXT,PlayerName TEXT, PlayerOrder INT, HandedBat INT,HandedBowl INT,WhatBowler INT,IsBatsman INT,IsBowler INT,IsCaptain INT,IsKeeper INT )";
        db.execSQL("CREATE TABLE IF NOT EXISTS ManageTeams(TeamName TEXT,PlayerName TEXT, PlayerOrder INT, HandedBat INT,HandedBowl INT,WhatBowler INT,IsBatsman INT,IsBowler INT,IsCaptain INT,IsKeeper INT )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
        if (oldVersion < 6) {
            String ALTER_DEFAULT_ADDITIONAL_SETTINGS_TABLE = "ALTER TABLE DefaultAdditionalSettings ADD COLUMN BPO INT NOT NULL DEFAULT 6";
            db.execSQL("ALTER TABLE DefaultAdditionalSettings ADD COLUMN BPO INT NOT NULL DEFAULT 6");
            String ALTER_MATCH_MASTER_TABLE = "ALTER TABLE MatchMaster ADD COLUMN BPO INT NOT NULL DEFAULT 6";
            db.execSQL("ALTER TABLE MatchMaster ADD COLUMN BPO INT NOT NULL DEFAULT 6");
        }
        if (oldVersion < 7) {
            ALTER_MATCH_MASTER_TABLE = "ALTER TABLE MatchMaster ADD COLUMN Result TEXT";
            db.execSQL("ALTER TABLE MatchMaster ADD COLUMN Result TEXT");
            db.execSQL("UPDATE MatchScoreBoard SET XCord = XCord*2 WHERE XCord IS NOT NULL");
            db.execSQL("UPDATE MatchScoreBoard SET YCord = YCord*2 WHERE YCord IS NOT NULL");
            db.execSQL("UPDATE MatchScoreBoard SET BsXCord = BsXCord*2 WHERE BsXCord IS NOT NULL");
            db.execSQL("UPDATE MatchScoreBoard SET BsYCord = BsYCord*2 WHERE BsYCord IS NOT NULL");
        }
        if (oldVersion < 8) {
            String ALTER_CONFIGURATION_TABLE = "ALTER TABLE Configuration ADD COLUMN ThemeId INT";
            db.execSQL("ALTER TABLE Configuration ADD COLUMN ThemeId INT");
        }
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE DefaultAdditionalSettings ADD COLUMN WwDot INT DEFAULT 0");
            db.execSQL("ALTER TABLE DefaultAdditionalSettings ADD COLUMN WwWicket INT DEFAULT 0");
            db.execSQL("ALTER TABLE MatchMaster ADD COLUMN WwDot INT DEFAULT 0");
            db.execSQL("ALTER TABLE MatchMaster ADD COLUMN WwWicket INT DEFAULT 0");
        }
        if (oldVersion < 10) {
            ALTER_CONFIGURATION_TABLE = "ALTER TABLE Configuration ADD COLUMN WdNbToBowler INT DEFAULT 0";
            db.execSQL("ALTER TABLE Configuration ADD COLUMN WdNbToBowler INT DEFAULT 0");
        }
        if (oldVersion < 11) {
            ALTER_MATCH_MASTER_TABLE = "ALTER TABLE MatchMaster ADD COLUMN MatchName TEXT";
            db.execSQL("ALTER TABLE MatchMaster ADD COLUMN MatchName TEXT");
            db.execSQL("ALTER TABLE ManageTeams ADD COLUMN HandedBat INT");
            db.execSQL("ALTER TABLE ManageTeams ADD COLUMN HandedBowl INT");
            db.execSQL("ALTER TABLE ManageTeams ADD COLUMN WhatBowler INT");
            db.execSQL("ALTER TABLE ManageTeams ADD COLUMN IsBatsman INT");
            db.execSQL("ALTER TABLE ManageTeams ADD COLUMN IsBowler INT");
            db.execSQL("ALTER TABLE ManageTeams ADD COLUMN IsCaptain INT");
            db.execSQL("ALTER TABLE ManageTeams ADD COLUMN IsKeeper INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN PlayerOrder INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN HandedBat INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN HandedBowl INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN WhatBowler INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN IsBatsman INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN IsBowler INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN IsCaptain INT");
            db.execSQL("ALTER TABLE MatchTeams ADD COLUMN IsKeeper INT");
            ALTER_CONFIGURATION_TABLE = "ALTER TABLE Configuration ADD COLUMN InlineWwBs INT DEFAULT 0";
            db.execSQL("ALTER TABLE Configuration ADD COLUMN InlineWwBs INT DEFAULT 0");
        }
        if (oldVersion < 12) {
            db.execSQL("ALTER TABLE MatchMaster ADD COLUMN NoOfInngs INT DEFAULT 1");
            db.execSQL("ALTER TABLE MatchMaster ADD COLUMN NoOfDays INT DEFAULT 1");
            db.execSQL("ALTER TABLE MatchScoreBoard ADD COLUMN InngNo INT DEFAULT 1");
            db.execSQL("ALTER TABLE MatchScoreBoard ADD COLUMN DayNo INT DEFAULT 1");
            db.execSQL("ALTER TABLE MatchScoreBoard ADD COLUMN FEOD INT");
            db.execSQL("ALTER TABLE MatchScoreBoard ADD COLUMN FEOI INT");
            db.execSQL("ALTER TABLE MatchScoreBoard ADD COLUMN EOIDesc TEXT");
            db.execSQL("ALTER TABLE MatchScoreBoard ADD COLUMN IsPenalty INT DEFAULT 0");
        }
    }

    public List<String> getMatchTeamPlayers(String matchID, String teamNo) {
        String oppTeam = "2";
        if (teamNo.equalsIgnoreCase("2")) {
            oppTeam = "1";
        }
        List<String> team1List = new ArrayList();
        String selectQuery = ((("SELECT DISTINCT UPPER(TRIM(PlayerName)) As PlayerName FROM MatchTeams WHERE MatchID = ? AND Team = ?" + " UNION SELECT DISTINCT UPPER(TRIM(StrikerName)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ?") + " UNION SELECT DISTINCT UPPER(TRIM(NonStrikerName)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ?") + " UNION SELECT DISTINCT UPPER(TRIM(BowlerName)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ?") + " UNION SELECT DISTINCT UPPER(TRIM(WicketAssist)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ? AND NULLIF(TRIM(WicketAssist), '') IS NOT NULL ORDER BY UPPER(TRIM(PlayerName))";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{matchID, teamNo, matchID, teamNo, matchID, teamNo, matchID, oppTeam, matchID, oppTeam});
        if (cursor.moveToFirst()) {
            do {
                if (!(cursor.isNull(0) || cursor.getString(0).equalsIgnoreCase("null") || cursor.getString(0).equalsIgnoreCase(""))) {
                    team1List.add(Utility.toDisplayCase(cursor.getString(0)));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return team1List;
    }

    public dtoTeamPlayerList getMatchTeamPlayersWithDetails(String matchID, String teamNo) {
        String oppTeam = "2";
        if (teamNo.equalsIgnoreCase("2")) {
            oppTeam = "1";
        }
        dtoTeamPlayerList team1List = new dtoTeamPlayerList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT B.PlayerName, A.HandedBat, A.HandedBowl, A.WhatBowler, A.IsBatsman, A.IsBowler, A.IsCaptain, A.IsKeeper, A.PlayerOrder FROM ( SELECT DISTINCT UPPER(TRIM(PlayerName)) As PlayerName FROM MatchTeams WHERE MatchID = ? AND Team = ? UNION SELECT DISTINCT UPPER(TRIM(StrikerName)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ? UNION SELECT DISTINCT UPPER(TRIM(NonStrikerName)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ? UNION SELECT DISTINCT UPPER(TRIM(BowlerName)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ? UNION SELECT DISTINCT UPPER(TRIM(WicketAssist)) FROM MatchScoreBoard WHERE MatchID = ? AND Team = ? AND NULLIF(TRIM(WicketAssist), '') IS NOT NULL) B LEFT OUTER JOIN MatchTeams A ON B.PlayerName = UPPER(TRIM(A.PlayerName)) AND MatchID = ? AND Team = ? ORDER BY UPPER(TRIM(B.PlayerName))", new String[]{matchID, teamNo, matchID, teamNo, matchID, teamNo, matchID, oppTeam, matchID, oppTeam, matchID, teamNo});
        if (cursor.moveToFirst()) {
            do {
                if (!(cursor.isNull(0) || cursor.getString(0).equalsIgnoreCase("null") || cursor.getString(0).equalsIgnoreCase(""))) {
                    dtoTeamPlayer player = new dtoTeamPlayer();
                    player.setPlayerName(Utility.toDisplayCase(cursor.getString(0)));
                    player.setHandedBat(cursor.isNull(1) ? 0 : cursor.getInt(1));
                    player.setHandedBowl(cursor.isNull(2) ? 0 : cursor.getInt(2));
                    player.setWhatBowler(cursor.isNull(3) ? 0 : cursor.getInt(3));
                    player.setIsBatsman(cursor.isNull(4) ? 0 : cursor.getInt(4));
                    player.setIsBowler(cursor.isNull(5) ? 0 : cursor.getInt(5));
                    player.setIsCaptain(cursor.isNull(6) ? 0 : cursor.getInt(6));
                    player.setIsKeeper(cursor.isNull(7) ? 0 : cursor.getInt(7));
                    player.setPlayerOrder(cursor.isNull(8) ? 0 : cursor.getInt(8));
                    team1List.add(player);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return team1List;
    }

    public int getMatchCount() {
        int matchCount = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Count(MatchID) FROM MatchMaster", null);
        if (cursor.moveToFirst()) {
            matchCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return matchCount;
    }

    public boolean checkMatchExists(String matchID) {
        int matchCount = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Count(MatchID) FROM MatchMaster WHERE MatchID = ?", new String[]{matchID});
        if (cursor.moveToFirst()) {
            matchCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        if (matchCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkTeamExists(String teamName) {
        int teamCount = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(TeamName) FROM ManageTeams WHERE TeamName LIKE ?", new String[]{teamName});
        if (cursor.moveToFirst()) {
            teamCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        if (teamCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkPlayerExistsInTeam(String matchID, String teamNo, String playerName) {
        int matchCount = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Count(*) FROM MatchTeams WHERE MatchID = ? AND Team = ? AND PlayerName = ?", new String[]{matchID, teamNo, playerName});
        if (cursor.moveToFirst()) {
            matchCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        if (matchCount > 0) {
            return true;
        }
        return false;
    }

    public void deleteMatch(String matchID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MATCH_TEAMS, "MatchID = ?", new String[]{matchID});
        db.delete(TABLE_MATCH_MASTER, "MatchID = ?", new String[]{matchID});
        db.delete(TABLE_MATCH_SCOREBOARD, "MatchID = ?", new String[]{matchID});
        db.close();
    }

    public void deleteLastMatchScoreBall(String matchID, int lastBallNo) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MATCH_SCOREBOARD, "MatchID = ? AND TotalBallNo = ?", new String[]{matchID, "" + lastBallNo});
        db.close();
    }

    public void updateTeamName(String matchID, String updatedTeamName, String teamNo) {
        SQLiteDatabase db = getWritableDatabase();
        String strFilter = "MatchID = ?";
        ContentValues args = new ContentValues();
        if (teamNo.equals("1")) {
            args.put(TEAM1_NAME, updatedTeamName);
        } else {
            args.put(TEAM2_NAME, updatedTeamName);
        }
        db.update(TABLE_MATCH_MASTER, args, strFilter, new String[]{matchID});
        db.close();
    }

    public void updateMatchDetails(dtoMatch match) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(MATCH_DATETIME, match.getDateTime());
        args.put(TEAM1_NAME, match.getTeam1Name());
        args.put(TEAM2_NAME, match.getTeam2Name());
        args.put(VENUE, match.getVenue());
        args.put(MATCH_NAME, match.getMatchName());
        args.put(MATCH_OVERS, Integer.valueOf(match.getOvers()));
        args.put(NO_OF_INNGS, Integer.valueOf(match.getNoOfInngs()));
        args.put(NO_OF_DAYS, Integer.valueOf(match.getNoOfDays()));
        db.update(TABLE_MATCH_MASTER, args, "MatchID = ?", new String[]{match.getMatchID()});
        db.close();
    }

    public void updateTeamMember(String matchID, String oldTeamMember, String updatedTeamMember, String teamNo, boolean playerExists) {
        SQLiteDatabase db;
        if (playerExists && checkPlayerExistsInTeam(matchID, teamNo, oldTeamMember)) {
            db = getWritableDatabase();
            db.delete(TABLE_MATCH_TEAMS, "TRIM(PlayerName) = ? AND Team = ?", new String[]{oldTeamMember, teamNo});
            db.close();
        }
        db = getWritableDatabase();
        String bowlingTeamNo = "";
        if (teamNo.equalsIgnoreCase("1")) {
            bowlingTeamNo = "2";
        } else {
            bowlingTeamNo = "1";
        }
        ContentValues args;
        if (oldTeamMember.trim().equalsIgnoreCase("")) {
            args = new ContentValues();
            args.put(MATCH_ID, matchID);
            args.put(TEAM_NO, teamNo);
            args.put(PLAYER_NAME, updatedTeamMember);
            db.insert(TABLE_MATCH_TEAMS, null, args);
        } else {
            args = new ContentValues();
            args.put(PLAYER_NAME, updatedTeamMember);
            db.update(TABLE_MATCH_TEAMS, args, "MatchID = ? AND Team = ? AND TRIM(PlayerName) LIKE ?", new String[]{matchID, teamNo, oldTeamMember});
            args = new ContentValues();
            args.put(STRIKER_NAME, updatedTeamMember);
            db.update(TABLE_MATCH_SCOREBOARD, args, "MatchID = ? AND Team = ? AND TRIM(StrikerName) LIKE ?", new String[]{matchID, teamNo, oldTeamMember});
            args = new ContentValues();
            args.put(NONSTRIKER_NAME, updatedTeamMember);
            db.update(TABLE_MATCH_SCOREBOARD, args, "MatchID = ? AND Team = ? AND TRIM(NonStrikerName) LIKE ?", new String[]{matchID, teamNo, oldTeamMember});
            args = new ContentValues();
            args.put(BOWLER_NAME, updatedTeamMember);
            db.update(TABLE_MATCH_SCOREBOARD, args, "MatchID = ? AND Team = ? AND TRIM(BowlerName) LIKE ?", new String[]{matchID, bowlingTeamNo, oldTeamMember});
            args = new ContentValues();
            args.put(WICKET_ASSIST, updatedTeamMember);
            db.update(TABLE_MATCH_SCOREBOARD, args, "MatchID = ? AND Team = ? AND TRIM(WicketAssist) LIKE ?", new String[]{matchID, bowlingTeamNo, oldTeamMember});
        }
        db.close();
    }

    public dtoMatch addMatchScoreBall(dtoMatch dtoValues) {
        dtoMatch notSavedBalls = new dtoMatch();
        notSavedBalls.setMatchID(dtoValues.getMatchID());
        SQLiteDatabase db = getWritableDatabase();
        List<dtoScoreBall> team1ScoreBalls = dtoValues.getTeam1Scorecard();
        List<dtoScoreBall> team2ScoreBalls = dtoValues.getTeam2Scorecard();
        List<dtoScoreBall> notSavedTeam1Balls = new ArrayList();
        List<dtoScoreBall> notSavedTeam2Balls = new ArrayList();
        boolean isErrorOccured = false;
        for (dtoScoreBall ball : team1ScoreBalls) {
            if (isErrorOccured) {
                notSavedTeam1Balls.add(ball);
            } else {
                ContentValues values;
                values = new ContentValues();
                values.put(MATCH_ID, dtoValues.getMatchID());
                values.put(TEAM_NO, "1");
                values.put(TOTAL_BALL_NO, Integer.valueOf(ball.getTotalBallNo()));
                values.put(INNG_NO, Integer.valueOf(ball.getInningNo()));
                values.put(DAY_NO, Integer.valueOf(ball.getDayNo()));
                values.put(OVER_NO, Integer.valueOf(ball.getOverNo()));
                values.put(BALL_NO, Integer.valueOf(ball.getBallNo()));
                values.put(STRIKER_NAME, ball.getStrikerName());
                values.put(NONSTRIKER_NAME, ball.getNonStrikerName());
                values.put(BALL_NO, Integer.valueOf(ball.getBallNo()));
                values.put(BOWLER_NAME, ball.getBowlerName());
                values.put(IS_WIDE, Boolean.valueOf(ball.getIsWide()));
                values.put(IS_NOBALL, Boolean.valueOf(ball.getIsNoball()));
                values.put(IS_LEGBYES, Boolean.valueOf(ball.getIsLegByes()));
                values.put(IS_BYES, Boolean.valueOf(ball.getIsByes()));
                values.put(IS_WICKET, Boolean.valueOf(ball.getIsWicket()));
                values.put(WICKET_HOW, ball.getWicketHow());
                values.put(WICKET_ASSIST, ball.getWicketAssist());
                values.put(RUN_SCORED, Integer.valueOf(ball.getRunsScored()));
                values.put(RUNS_THIS_BALL, Integer.valueOf(ball.getRunsThisBall()));
                values.put(BALL_BY_BALL, ball.getBallByBall());
                values.put(TOTAL_SCORE, Integer.valueOf(ball.getTotalScore()));
                values.put(WICKET_COUNT, Integer.valueOf(ball.getWicketCount()));
                values.put(STRIKER_RUNS, Integer.valueOf(ball.getStrikerRuns()));
                values.put(NONSTRIKER_RUNS, Integer.valueOf(ball.getNonStrikerRuns()));
                values.put(STRIKER_BALLS, Integer.valueOf(ball.getBallsFacedStriker()));
                values.put(NONSTRIKER_BALLS, Integer.valueOf(ball.getBallsFacedNonStriker()));
                values.put(TARGET, Integer.valueOf(ball.getTarget()));
                values.put(TOTAL_EXTRAS, Integer.valueOf(ball.getTotalExtras()));
                values.put(WW_X_CORD, Integer.valueOf(ball.getXCord()));
                values.put(WW_Y_CORD, Integer.valueOf(ball.getYCord()));
                values.put(BS_X_CORD, Integer.valueOf(ball.getbsXCord()));
                values.put(BS_Y_CORD, Integer.valueOf(ball.getbsYCord()));
                values.put(BALLS_THIS_OVER, Integer.valueOf(ball.getBallsThisOver()));
                values.put(FLAG_EOD, Integer.valueOf(ball.getFlagEOD()));
                values.put(FLAG_EOI, Integer.valueOf(ball.getFlagEOI()));
                values.put(EOI_DESC, ball.getEOIDescription());
                values.put(IS_PENALTY, Integer.valueOf(ball.getIsPenalty()));
                if (db.insert(TABLE_MATCH_SCOREBOARD, null, values) < 0) {
                    notSavedTeam1Balls.add(ball);
                    isErrorOccured = true;
                }
            }
        }
        isErrorOccured = false;
        for (dtoScoreBall ball2 : team2ScoreBalls) {
            if (isErrorOccured) {
                notSavedTeam2Balls.add(ball2);
            } else {
                values = new ContentValues();
                values.put(MATCH_ID, dtoValues.getMatchID());
                values.put(TEAM_NO, "2");
                values.put(TOTAL_BALL_NO, Integer.valueOf(ball2.getTotalBallNo()));
                values.put(INNG_NO, Integer.valueOf(ball2.getInningNo()));
                values.put(DAY_NO, Integer.valueOf(ball2.getDayNo()));
                values.put(OVER_NO, Integer.valueOf(ball2.getOverNo()));
                values.put(BALL_NO, Integer.valueOf(ball2.getBallNo()));
                values.put(STRIKER_NAME, ball2.getStrikerName());
                values.put(NONSTRIKER_NAME, ball2.getNonStrikerName());
                values.put(BOWLER_NAME, ball2.getBowlerName());
                values.put(IS_WIDE, Boolean.valueOf(ball2.getIsWide()));
                values.put(IS_NOBALL, Boolean.valueOf(ball2.getIsNoball()));
                values.put(IS_LEGBYES, Boolean.valueOf(ball2.getIsLegByes()));
                values.put(IS_BYES, Boolean.valueOf(ball2.getIsByes()));
                values.put(IS_WICKET, Boolean.valueOf(ball2.getIsWicket()));
                values.put(WICKET_HOW, ball2.getWicketHow());
                values.put(WICKET_ASSIST, ball2.getWicketAssist());
                values.put(RUN_SCORED, Integer.valueOf(ball2.getRunsScored()));
                values.put(RUNS_THIS_BALL, Integer.valueOf(ball2.getRunsThisBall()));
                values.put(BALL_BY_BALL, ball2.getBallByBall());
                values.put(TOTAL_SCORE, Integer.valueOf(ball2.getTotalScore()));
                values.put(WICKET_COUNT, Integer.valueOf(ball2.getWicketCount()));
                values.put(STRIKER_RUNS, Integer.valueOf(ball2.getStrikerRuns()));
                values.put(NONSTRIKER_RUNS, Integer.valueOf(ball2.getNonStrikerRuns()));
                values.put(STRIKER_BALLS, Integer.valueOf(ball2.getBallsFacedStriker()));
                values.put(NONSTRIKER_BALLS, Integer.valueOf(ball2.getBallsFacedNonStriker()));
                values.put(TARGET, Integer.valueOf(ball2.getTarget()));
                values.put(TOTAL_EXTRAS, Integer.valueOf(ball2.getTotalExtras()));
                values.put(WW_X_CORD, Integer.valueOf(ball2.getXCord()));
                values.put(WW_Y_CORD, Integer.valueOf(ball2.getYCord()));
                values.put(BS_X_CORD, Integer.valueOf(ball2.getbsXCord()));
                values.put(BS_Y_CORD, Integer.valueOf(ball2.getbsYCord()));
                values.put(BALLS_THIS_OVER, Integer.valueOf(ball2.getBallsThisOver()));
                values.put(FLAG_EOD, Integer.valueOf(ball2.getFlagEOD()));
                values.put(FLAG_EOI, Integer.valueOf(ball2.getFlagEOI()));
                values.put(EOI_DESC, ball2.getEOIDescription());
                values.put(IS_PENALTY, Integer.valueOf(ball2.getIsPenalty()));
                if (db.insert(TABLE_MATCH_SCOREBOARD, null, values) < 0) {
                    notSavedTeam2Balls.add(ball2);
                    isErrorOccured = true;
                }
            }
        }
        if (!notSavedTeam1Balls.isEmpty()) {
            notSavedBalls.setTeam1Scorecard(notSavedTeam1Balls);
        }
        if (!notSavedTeam2Balls.isEmpty()) {
            notSavedBalls.setTeam2Scorecard(notSavedTeam2Balls);
        }
        db.close();
        return notSavedBalls;
    }

    public void updateRetiredOutBatsman(String matchID, int who) {
        SQLiteDatabase db = getWritableDatabase();
        String strFilter = "MatchID = ? and TotalBallNo = (Select MAX(TotalBallNo) FROM MatchScoreBoard WHERE MatchID = ?)";
        ContentValues args = new ContentValues();
        if (who == 1) {
            args.put(WICKET_HOW, "Retired (striker)");
        } else {
            args.put(WICKET_HOW, "Retired (non-striker)");
        }
        args.put(IS_WICKET, Boolean.valueOf(true));
        db.update(TABLE_MATCH_SCOREBOARD, args, strFilter, new String[]{matchID, matchID});
        db.close();
    }

    public int findRetiredOutBallNo(String matchID, String playerName, String teamNo, String inngNo) {
        SQLiteDatabase db = getWritableDatabase();
        int returnValue = -1;
        Cursor mCount = db.rawQuery(("SELECT TotalBallNo FROM MatchScoreBoard" + " WHERE MatchID LIKE ? AND InngNo = ? AND Team = ? AND ((WicketHow LIKE 'Retired (striker)' AND StrikerName LIKE ?) OR (WicketHow LIKE 'Retired (non-striker)' AND NonStrikerName LIKE ?))") + " ORDER BY TotalBallNo DESC", new String[]{matchID, inngNo, teamNo, playerName, playerName});
        if (mCount.moveToFirst()) {
            returnValue = mCount.getInt(0);
        }
        mCount.close();
        db.close();
        return returnValue;
    }

    public void clearRetiredOutBatsman(String matchID, int totalBallNo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(WICKET_HOW, "");
        args.put(IS_WICKET, Boolean.valueOf(false));
        db.update(TABLE_MATCH_SCOREBOARD, args, "MatchID = ? and TotalBallNo = ?", new String[]{matchID, "" + totalBallNo});
        db.close();
    }

    public List<String> getRunsForRetiredOutBatsman(String matchID, String playerName, String teamNo, String inngNo) {
        List<String> returnResult = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCount = db.rawQuery(("SELECT StrikerRuns,StrikerBallsFaced,NonStrikerRuns,NonStrikerBallsFaced,WicketHow,TotalBallNo FROM MatchScoreBoard" + " WHERE MatchID LIKE ? AND InngNo = ? AND Team = ? AND StrikerName LIKE ?") + " ORDER BY TotalBallNo DESC", new String[]{matchID, inngNo, teamNo, playerName});
        int count = 0;
        String[] result = new String[6];
        if (mCount.moveToFirst()) {
            count = mCount.getCount();
            result[0] = mCount.getString(0);
            result[1] = mCount.getString(1);
            result[2] = mCount.getString(2);
            result[3] = mCount.getString(3);
            result[4] = mCount.getString(4);
            result[5] = mCount.getString(5);
        }
        mCount.close();
        if (count > 0) {
            returnResult.add(result[0]);
            returnResult.add(result[1]);
            returnResult.add(result[5]);
        }
        db.close();
        return returnResult;
    }

    public void addPlayerIntoMatch(String matchID, String teamNo, String playerName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues = new ContentValues();
        contentValues.put(MATCH_ID, matchID);
        contentValues.put(TEAM_NO, teamNo);
        contentValues.put(PLAYER_NAME, playerName);
        db.insert(TABLE_MATCH_TEAMS, null, contentValues);
        db.close();
    }

    public void createMatch(dtoMatch dtoValues) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MATCH_ID, dtoValues.getMatchID());
        values.put(MATCH_DATETIME, dtoValues.getDateTime());
        values.put(VENUE, dtoValues.getVenue());
        values.put(TEAM1_NAME, dtoValues.getTeam1Name());
        values.put(TEAM2_NAME, dtoValues.getTeam2Name());
        values.put(TOSS_WON_BY, dtoValues.getTossWonBy());
        values.put(OPTED_TO, dtoValues.getOptedTo());
        values.put(MATCH_OVERS, Integer.valueOf(dtoValues.getOvers()));
        values.put(MATCH_NAME, dtoValues.getMatchName());
        values.put(NO_OF_INNGS, Integer.valueOf(dtoValues.getNoOfInngs()));
        values.put(NO_OF_DAYS, Integer.valueOf(dtoValues.getNoOfDays()));
        dtoAdditionalSettings settings = dtoValues.getAdditionalSettings();
        values.put(WIDE_BALL, Integer.valueOf(settings.getWide()));
        values.put(WIDE_BALL_REBALL, Integer.valueOf(settings.getWideReball()));
        values.put(NO_BALL, Integer.valueOf(settings.getNoball()));
        values.put(NO_BALL_REBALL, Integer.valueOf(settings.getNoballReball()));
        values.put(LEG_BYES, Integer.valueOf(settings.getLegbyes()));
        values.put(BYES, Integer.valueOf(settings.getByes()));
        values.put(LBW, Integer.valueOf(settings.getLbw()));
        values.put(WAGON_WHEEL, Integer.valueOf(settings.getWagonWheel()));
        values.put(BALL_SPOT, Integer.valueOf(settings.getBallSpot()));
        values.put(PRESET_TEAMS, Integer.valueOf(settings.getPresetTeams()));
        values.put(WIDE_RUN, Integer.valueOf(settings.getWideRun()));
        values.put(NOBALL_RUN, Integer.valueOf(settings.getNoballRun()));
        values.put(RESTRICT_BALLSPEROVER, Integer.valueOf(settings.getRestrictBpo()));
        values.put(MAX_BALLSPEROVER, Integer.valueOf(settings.getMaxBpo()));
        values.put(BALLSPEROVER, Integer.valueOf(settings.getBpo()));
        values.put(WW_FOR_DOT, Integer.valueOf(settings.getWwForDotBall()));
        values.put(WW_FOR_WICKET, Integer.valueOf(settings.getWwForWicket()));
        db.insert(TABLE_MATCH_MASTER, null, values);
        int playOrder = 0;
        Iterator it = dtoValues.getTeam1Players().iterator();
        while (it.hasNext()) {
            dtoTeamPlayer playerName = (dtoTeamPlayer) it.next();
            if (!playerName.getPlayerName().trim().equalsIgnoreCase("")) {
                values = new ContentValues();
                values.put(MATCH_ID, dtoValues.getMatchID());
                values.put(TEAM_NO, CricScorerApp.getContext().getString(C0252R.string.dbTeam1));
                values.put(PLAYER_NAME, playerName.getPlayerName());
                values.put(HANDED_BAT, Integer.valueOf(playerName.getHandedBat()));
                values.put(HANDED_BOWL, Integer.valueOf(playerName.getHandedBowl()));
                values.put(WHAT_BOWLER, Integer.valueOf(playerName.getWhatBowler()));
                values.put(IS_BATSMAN, Integer.valueOf(playerName.getIsBatsman()));
                values.put(IS_BOWLER, Integer.valueOf(playerName.getIsBowler()));
                values.put(IS_KEEPER, Integer.valueOf(playerName.getIsKeeper()));
                values.put(IS_CAPTAIN, Integer.valueOf(playerName.getIsCaptain()));
                int playOrder2 = playOrder + 1;
                values.put(PLAYER_ORDER, Integer.valueOf(playOrder));
                db.insert(TABLE_MATCH_TEAMS, null, values);
                playOrder = playOrder2;
            }
        }
        playOrder = 0;
        it = dtoValues.getTeam2Players().iterator();
        while (it.hasNext()) {
            playerName = (dtoTeamPlayer) it.next();
            if (!playerName.getPlayerName().trim().equalsIgnoreCase("")) {
                values = new ContentValues();
                values.put(MATCH_ID, dtoValues.getMatchID());
                values.put(TEAM_NO, CricScorerApp.getContext().getString(C0252R.string.dbTeam2));
                values.put(PLAYER_NAME, playerName.getPlayerName());
                values.put(HANDED_BAT, Integer.valueOf(playerName.getHandedBat()));
                values.put(HANDED_BOWL, Integer.valueOf(playerName.getHandedBowl()));
                values.put(WHAT_BOWLER, Integer.valueOf(playerName.getWhatBowler()));
                values.put(IS_BATSMAN, Integer.valueOf(playerName.getIsBatsman()));
                values.put(IS_BOWLER, Integer.valueOf(playerName.getIsBowler()));
                values.put(IS_KEEPER, Integer.valueOf(playerName.getIsKeeper()));
                values.put(IS_CAPTAIN, Integer.valueOf(playerName.getIsCaptain()));
                playOrder2 = playOrder + 1;
                values.put(PLAYER_ORDER, Integer.valueOf(playOrder));
                db.insert(TABLE_MATCH_TEAMS, null, values);
                playOrder = playOrder2;
            }
        }
        db.close();
    }

    public void updateMatchSettings(dtoMatch dtoValues) {
        dtoAdditionalSettings settings = dtoValues.getAdditionalSettings();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDE_BALL, Integer.valueOf(settings.getWide()));
        values.put(WIDE_BALL_REBALL, Integer.valueOf(settings.getWideReball()));
        values.put(NO_BALL, Integer.valueOf(settings.getNoball()));
        values.put(NO_BALL_REBALL, Integer.valueOf(settings.getNoballReball()));
        values.put(LEG_BYES, Integer.valueOf(settings.getLegbyes()));
        values.put(BYES, Integer.valueOf(settings.getByes()));
        values.put(LBW, Integer.valueOf(settings.getLbw()));
        values.put(WAGON_WHEEL, Integer.valueOf(settings.getWagonWheel()));
        values.put(BALL_SPOT, Integer.valueOf(settings.getBallSpot()));
        values.put(PRESET_TEAMS, Integer.valueOf(settings.getPresetTeams()));
        values.put(WW_FOR_DOT, Integer.valueOf(settings.getWwForDotBall()));
        values.put(WW_FOR_WICKET, Integer.valueOf(settings.getWwForWicket()));
        db.update(TABLE_MATCH_MASTER, values, "MatchID = ?", new String[]{dtoValues.getMatchID()});
        db.close();
    }

    public dtoConfiguration getConfiguration() {
        dtoConfiguration config = new dtoConfiguration();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MailIds, ThemeId, WdNbToBowler, InlineWwBs FROM Configuration", null);
        if (cursor.moveToFirst()) {
            config.setMailIds(cursor.getString(0));
            config.setThemeId(cursor.getInt(1));
            config.setWdNbBowlerStats(cursor.getInt(2));
            config.setInlineWwBs(cursor.getInt(3));
        }
        cursor.close();
        db.close();
        return config;
    }

    public void addConfiguration(dtoConfiguration config) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MAIL_CONFIGURATION, "" + config.getMailIds());
        values.put(THEME_CONFIGURATION, "" + config.getThemeId());
        values.put(WDNBTOBOWLER_CONFIGURATION, "" + config.getWdNbBowlerStats());
        values.put(INLINEWWANDBS_CONFIGURATION, "" + config.getInlineWwBs());
        db.delete(TABLE_CONFIGURATION, null, null);
        db.insert(TABLE_CONFIGURATION, null, values);
        db.close();
    }

    public void addDefaultAdditionalSettings() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCount = db.rawQuery("SELECT * FROM DefaultAdditionalSettings", null);
        int count = 0;
        if (mCount.moveToFirst()) {
            count = mCount.getInt(0);
        }
        mCount.close();
        if (count <= 0) {
            ContentValues values = new ContentValues();
            values.put(WIDE_BALL, Integer.valueOf(1));
            values.put(WIDE_BALL_REBALL, Integer.valueOf(1));
            values.put(NO_BALL, Integer.valueOf(1));
            values.put(NO_BALL_REBALL, Integer.valueOf(1));
            values.put(LEG_BYES, Integer.valueOf(0));
            values.put(BYES, Integer.valueOf(1));
            values.put(LBW, Integer.valueOf(0));
            values.put(WAGON_WHEEL, Integer.valueOf(0));
            values.put(BALL_SPOT, Integer.valueOf(0));
            values.put(PRESET_TEAMS, Integer.valueOf(0));
            values.put(WIDE_RUN, Integer.valueOf(1));
            values.put(NOBALL_RUN, Integer.valueOf(1));
            values.put(RESTRICT_BALLSPEROVER, Integer.valueOf(0));
            values.put(MAX_BALLSPEROVER, Integer.valueOf(8));
            values.put(BALLSPEROVER, Integer.valueOf(6));
            values.put(WW_FOR_DOT, Integer.valueOf(0));
            values.put(WW_FOR_WICKET, Integer.valueOf(0));
            db.insert(TABLE_DEFAULT_ADDITIONAL_SETTINGS, null, values);
        }
        db.close();
    }

    public void saveDefaultAdditionalSettings(dtoAdditionalSettings dtoValues) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDE_BALL, Integer.valueOf(dtoValues.getWide()));
        values.put(WIDE_BALL_REBALL, Integer.valueOf(dtoValues.getWideReball()));
        values.put(NO_BALL, Integer.valueOf(dtoValues.getNoball()));
        values.put(NO_BALL_REBALL, Integer.valueOf(dtoValues.getNoballReball()));
        values.put(LEG_BYES, Integer.valueOf(dtoValues.getLegbyes()));
        values.put(BYES, Integer.valueOf(dtoValues.getByes()));
        values.put(LBW, Integer.valueOf(dtoValues.getLbw()));
        values.put(WAGON_WHEEL, Integer.valueOf(dtoValues.getWagonWheel()));
        values.put(BALL_SPOT, Integer.valueOf(dtoValues.getBallSpot()));
        values.put(PRESET_TEAMS, Integer.valueOf(dtoValues.getPresetTeams()));
        values.put(WIDE_RUN, Integer.valueOf(dtoValues.getWideRun()));
        values.put(NOBALL_RUN, Integer.valueOf(dtoValues.getNoballRun()));
        values.put(RESTRICT_BALLSPEROVER, Integer.valueOf(dtoValues.getRestrictBpo()));
        values.put(MAX_BALLSPEROVER, Integer.valueOf(dtoValues.getMaxBpo()));
        values.put(BALLSPEROVER, Integer.valueOf(dtoValues.getBpo()));
        values.put(WW_FOR_DOT, Integer.valueOf(dtoValues.getWwForDotBall()));
        values.put(WW_FOR_WICKET, Integer.valueOf(dtoValues.getWwForWicket()));
        db.delete(TABLE_DEFAULT_ADDITIONAL_SETTINGS, null, null);
        db.insert(TABLE_DEFAULT_ADDITIONAL_SETTINGS, null, values);
        db.close();
    }

    public dtoAdditionalSettings getDefaultAdditionalSettings() {
        dtoAdditionalSettings additionalSettings = new dtoAdditionalSettings();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DefaultAdditionalSettings", null);
        if (cursor.moveToFirst()) {
            additionalSettings.setWide(cursor.getInt(cursor.getColumnIndex(WIDE_BALL)));
            additionalSettings.setWideReball(cursor.getInt(cursor.getColumnIndex(WIDE_BALL_REBALL)));
            additionalSettings.setNoball(cursor.getInt(cursor.getColumnIndex(NO_BALL)));
            additionalSettings.setNoballReball(cursor.getInt(cursor.getColumnIndex(NO_BALL_REBALL)));
            additionalSettings.setLegbyes(cursor.getInt(cursor.getColumnIndex(LEG_BYES)));
            additionalSettings.setByes(cursor.getInt(cursor.getColumnIndex(BYES)));
            additionalSettings.setLbw(cursor.getInt(cursor.getColumnIndex(LBW)));
            additionalSettings.setWagonWheel(cursor.getInt(cursor.getColumnIndex(WAGON_WHEEL)));
            additionalSettings.setBallSpot(cursor.getInt(cursor.getColumnIndex(BALL_SPOT)));
            additionalSettings.setPresetTeams(cursor.getInt(cursor.getColumnIndex(PRESET_TEAMS)));
            additionalSettings.setWideRun(cursor.getInt(cursor.getColumnIndex(WIDE_RUN)));
            additionalSettings.setNoballRun(cursor.getInt(cursor.getColumnIndex(NOBALL_RUN)));
            additionalSettings.setRestrictBpo(cursor.getInt(cursor.getColumnIndex(RESTRICT_BALLSPEROVER)));
            additionalSettings.setMaxBpo(cursor.getInt(cursor.getColumnIndex(MAX_BALLSPEROVER)));
            additionalSettings.setBpo(cursor.getInt(cursor.getColumnIndex(BALLSPEROVER)));
            additionalSettings.setWwForDotBall(cursor.getInt(cursor.getColumnIndex(WW_FOR_DOT)));
            additionalSettings.setWwForWicket(cursor.getInt(cursor.getColumnIndex(WW_FOR_WICKET)));
        }
        cursor.close();
        db.close();
        return additionalSettings;
    }

    public void addTeamPlayer(String team, String playerName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NO, team);
        values.put(PLAYER_NAME, playerName);
        db.insert(TABLE_DEFAULT_TEAMS, null, values);
        db.close();
    }

    public void initializeDefaultTeams() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCount = db.rawQuery("SELECT * FROM DefaultTeams", null);
        int count = 0;
        if (mCount.moveToFirst()) {
            count = mCount.getInt(0);
        }
        mCount.close();
        if (count <= 0) {
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 1')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 2')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 3')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 4')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 5')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 6')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 7')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 8')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 9')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 10')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 11')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 12')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 13')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 14')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam1) + "', 'Home 15')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 1')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 2')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 3')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 4')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 5')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 6')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 7')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 8')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 9')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 10')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 11')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 12')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 13')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 14')");
            db.execSQL("INSERT INTO DefaultTeams VALUES ( '" + CricScorerApp.getContext().getString(C0252R.string.dbTeam2) + "', 'Away 15')");
        }
        db.close();
    }

    public void deleteAllTeamPlayers() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_DEFAULT_TEAMS, "Team IN (?, ?)", new String[]{"1", "2"});
        db.close();
    }

    public dtoTeamPlayerList getMinimumTeam1Players() {
        dtoTeamPlayerList teamList = new dtoTeamPlayerList();
        teamList.addPlayer("Home 1");
        teamList.addPlayer("Home 2");
        return teamList;
    }

    public dtoTeamPlayerList getMinimumTeam2Players() {
        dtoTeamPlayerList teamList = new dtoTeamPlayerList();
        teamList.addPlayer("Away 1");
        teamList.addPlayer("Away 2");
        return teamList;
    }

    public List<String> getAllTeam1Players() {
        List<String> team1List = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT PlayerName FROM DefaultTeams WHERE Team = ?", new String[]{CricScorerApp.getContext().getString(C0252R.string.dbTeam1)});
        if (cursor.moveToFirst()) {
            do {
                team1List.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return team1List;
    }

    public List<String> getAllTeam2Players() {
        List<String> team1List = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT PlayerName FROM DefaultTeams WHERE Team = ?", new String[]{CricScorerApp.getContext().getString(C0252R.string.dbTeam2)});
        if (cursor.moveToFirst()) {
            do {
                team1List.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return team1List;
    }

    public List<String> getConsolidatedTeamPlayers() {
        List<String> team1List = new ArrayList();
        String selectQuery = ((("SELECT UPPER(TRIM(PlayerName)) As PlayerName FROM MatchTeams" + " UNION SELECT UPPER(TRIM(StrikerName)) FROM MatchScoreBoard") + " UNION SELECT UPPER(TRIM(NonStrikerName)) FROM MatchScoreBoard") + " UNION SELECT UPPER(TRIM(BowlerName)) FROM MatchScoreBoard") + " UNION SELECT UPPER(TRIM(WicketAssist)) FROM MatchScoreBoard WHERE NULLIF(TRIM(WicketAssist), '') IS NOT NULL ORDER BY UPPER(TRIM(PlayerName))";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                if (!(cursor.isNull(0) || cursor.getString(0).equalsIgnoreCase("null") || cursor.getString(0).equalsIgnoreCase(""))) {
                    team1List.add(Utility.toDisplayCase(cursor.getString(0)));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return team1List;
    }

    public List<dtoTeam> getAllTeams() {
        List<dtoTeam> teamList = new ArrayList();
        teamList.add(new dtoTeam("All Teams", ""));
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT UPPER(TEAM1NAME) AS TEAMNAME FROM MATCHMASTER UNION SELECT UPPER(TEAM2NAME) FROM MATCHMASTER ", null);
        if (cursor.moveToFirst()) {
            do {
                teamList.add(new dtoTeam(Utility.toDisplayCase("" + cursor.getString(0)), ""));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teamList;
    }

    public List<dtoTeam> getAllTournamentNames() {
        List<dtoTeam> teamList = new ArrayList();
        teamList.add(new dtoTeam("All Tournaments", ""));
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT UPPER(A.MATCHNAME) MATCHNAME FROM MATCHMASTER A WHERE A.MATCHNAME IS NOT NULL AND A.MATCHNAME != ''", null);
        if (cursor.moveToFirst()) {
            do {
                teamList.add(new dtoTeam(cursor.getString(0), ""));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teamList;
    }

    public void addManageTeamPlayer(String teamName, String playerName, int playerOrder) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME, teamName);
        values.put(PLAYER_NAME, playerName);
        values.put(PLAYER_ORDER, Integer.valueOf(playerOrder));
        db.insert(TABLE_MANAGE_TEAMS, null, values);
        db.close();
    }

    public void addManageTeamPlayers(dtoTeamPlayerList players) {
        SQLiteDatabase db = getWritableDatabase();
        Iterator it = players.iterator();
        while (it.hasNext()) {
            dtoTeamPlayer player = (dtoTeamPlayer) it.next();
            ContentValues values = new ContentValues();
            values.put(TEAM_NAME, player.getTeamName());
            values.put(PLAYER_NAME, player.getPlayerName());
            values.put(PLAYER_ORDER, Integer.valueOf(player.getPlayerOrder()));
            values.put(HANDED_BAT, Integer.valueOf(player.getHandedBat()));
            values.put(HANDED_BOWL, Integer.valueOf(player.getHandedBowl()));
            values.put(IS_BATSMAN, Integer.valueOf(player.getIsBatsman()));
            values.put(IS_BOWLER, Integer.valueOf(player.getIsBowler()));
            values.put(IS_CAPTAIN, Integer.valueOf(player.getIsCaptain()));
            values.put(IS_KEEPER, Integer.valueOf(player.getIsKeeper()));
            values.put(WHAT_BOWLER, Integer.valueOf(player.getWhatBowler()));
            db.insert(TABLE_MANAGE_TEAMS, null, values);
        }
        db.close();
    }

    public void deleteManageTeamPlayer(String teamName, String playerName) {
        int playerOrder = 9999999;
        String selectQuery = "SELECT PlayerOrder FROM ManageTeams WHERE UPPER(TeamName) = ? AND UPPER(PlayerName) = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT PlayerOrder FROM ManageTeams WHERE UPPER(TeamName) = ? AND UPPER(PlayerName) = ?", new String[]{teamName.toUpperCase(Locale.getDefault()), playerName.toUpperCase(Locale.getDefault())});
        if (cursor.moveToFirst()) {
            playerOrder = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        db = getWritableDatabase();
        db.delete(TABLE_MANAGE_TEAMS, "UPPER(TeamName) = ? AND UPPER(PlayerName) = ?", new String[]{teamName.toUpperCase(Locale.getDefault()), playerName.toUpperCase(Locale.getDefault())});
        String updateQuery = "UPDATE ManageTeams SET PlayerOrder = PlayerOrder - 1 WHERE UPPER(TeamName) = ? AND PlayerOrder >= ?";
        db.execSQL("UPDATE ManageTeams SET PlayerOrder = PlayerOrder - 1 WHERE UPPER(TeamName) = ? AND PlayerOrder >= ?", new Object[]{teamName.toUpperCase(Locale.getDefault()), Integer.valueOf(playerOrder)});
        db.close();
    }

    public void deleteManageTeamPlayers(String teamName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MANAGE_TEAMS, "UPPER(TeamName) = ?", new String[]{teamName.toUpperCase(Locale.getDefault())});
        db.close();
    }

    public boolean checkTeamNameExists(String teamName) {
        int teamCount = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Count(TeamName) FROM ManageTeams WHERE TeamName LIKE ?", new String[]{teamName});
        if (cursor.moveToFirst()) {
            teamCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        if (teamCount > 0) {
            return true;
        }
        return false;
    }

    public List<String> getManageTeamList() {
        List<String> teamList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT UPPER(TeamName) FROM ManageTeams WHERE TeamName IS NOT NULL", null);
        if (cursor.moveToFirst()) {
            do {
                teamList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teamList;
    }

    public List<String> getManageTeamPlayerNames(String teamName) {
        List<String> teamList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT PlayerName FROM ManageTeams WHERE TeamName LIKE ?", new String[]{teamName});
        if (cursor.moveToFirst()) {
            do {
                teamList.add("" + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teamList;
    }

    public dtoTeamPlayerList getManageTeamPlayersList(String teamName) {
        dtoTeamPlayerList teamList = new dtoTeamPlayerList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT TeamName, PlayerName, PlayerOrder, HandedBat, HandedBowl, IsBatsman, IsBowler, IsKeeper, IsCaptain, WhatBowler FROM ManageTeams WHERE TeamName LIKE ?", new String[]{teamName});
        if (cursor.moveToFirst()) {
            do {
                dtoTeamPlayer player = new dtoTeamPlayer();
                player.setTeamName("" + cursor.getString(0));
                player.setPlayerName("" + cursor.getString(1));
                player.setPlayerOrder(cursor.getInt(2));
                player.setHandedBat(cursor.getInt(3));
                player.setHandedBowl(cursor.getInt(4));
                player.setIsBatsman(cursor.getInt(5));
                player.setIsBowler(cursor.getInt(6));
                player.setIsKeeper(cursor.getInt(7));
                player.setIsCaptain(cursor.getInt(8));
                player.setWhatBowler(cursor.getInt(9));
                player.setIsChecked(true);
                teamList.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teamList;
    }

    public dtoTeamPlayer getManageTeamPlayer(String teamName, String playerName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT TeamName, PlayerName, PlayerOrder, HandedBat, HandedBowl, IsBatsman, IsBowler, IsKeeper, IsCaptain, WhatBowler FROM ManageTeams WHERE TeamName LIKE ? AND PlayerName LIKE ?", new String[]{teamName, playerName});
        dtoTeamPlayer player = null;
        if (cursor.moveToFirst()) {
            player = new dtoTeamPlayer();
            player.setTeamName("" + cursor.getString(0));
            player.setPlayerName("" + cursor.getString(1));
            player.setPlayerOrder(cursor.getInt(2));
            player.setHandedBat(cursor.getInt(3));
            player.setHandedBowl(cursor.getInt(4));
            player.setIsBatsman(cursor.getInt(5));
            player.setIsBowler(cursor.getInt(6));
            player.setIsKeeper(cursor.getInt(7));
            player.setIsCaptain(cursor.getInt(8));
            player.setWhatBowler(cursor.getInt(9));
        }
        cursor.close();
        db.close();
        return player;
    }

    public void updateManageTeam(String oldTeamName, String newTeamName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TEAM_NAME, newTeamName);
        db.update(TABLE_MANAGE_TEAMS, args, "TeamName LiKE ?", new String[]{oldTeamName});
        db.close();
    }

    public List<dtoBowlerStat> getBowlerStats(String matchID, String team, String inngNo) {
        return getBowlerStats(matchID, team, inngNo, "");
    }

    public List<dtoBowlerStat> getBowlerStats(String matchID, String team, String inngNo, String orderBy) {
        return getBowlerStats(matchID, team, inngNo, orderBy, 0);
    }

    public List<dtoBowlerStat> getHighWicketTakers(String matchID, String team, String inngNo, int limit) {
        return getBowlerStats(matchID, team, inngNo, "WICKETS DESC, RUNRATE", limit);
    }

    public List<dtoBowlerStat> getBowlerStats(String matchID, String team, String inngNo, String orderBy, int limit) {
        String str;
        dtoConfiguration config = getConfiguration();
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCount = db.rawQuery("SELECT MaxBPO, IFNULL(BPO, 6), WideReball, NoballReball, RestrictBPO FROM MatchMaster WHERE MatchID LIKE ?", new String[]{matchID});
        int maxBallsPerOver = 0;
        int ballsPerOver = 6;
        int wideReball = 0;
        int noBallReball = 0;
        if (mCount.moveToFirst()) {
            maxBallsPerOver = mCount.getInt(0);
            ballsPerOver = mCount.getInt(1);
            wideReball = mCount.getInt(2);
            noBallReball = mCount.getInt(3);
            int restrictBallsPerOver = mCount.getInt(4);
        }
        mCount.close();
        List<dtoBowlerStat> bowlStatList = new ArrayList();
        StringBuilder append = new StringBuilder().append("SELECT A.BOWLERNAME, (SUM(BALLS) - SUM(EXTRABALLS))/ ").append(ballsPerOver).append(" AS OVERS, (SUM(BALLS) - SUM(EXTRABALLS)) % ").append(ballsPerOver).append(" AS BALLS,  SUM(BALLSTHISOVER) / ").append(maxBallsPerOver).append(" AS CALCULATED_OVERS, SUM(BALLSTHISOVER) % ").append(maxBallsPerOver).append(" AS CALCULATED_BALLS,  SUM(RUNS) RUNS, SUM(WICKETS) WICKETS, ROUND((SUM(RUNS)*1.0)/(((SUM(BALLS) - SUM(EXTRABALLS))/").append(ballsPerOver).append(") + (((SUM(BALLS) - SUM(EXTRABALLS)) % ").append(ballsPerOver).append(")/(").append(ballsPerOver).append(" * 1.0))),2) RUNRATE,  SUM(A.ISMAIDEN) AS TOTALMAIDEN, E.WIDES, F.NOBALLS  FROM (  SELECT A.TEAM, A.BOWLERNAME, A.OVERNO, COUNT(A.BALLNO) BALLS,  SUM(IFNULL(C.RUNSTHISBALL, 0)) RUNS, ");
        if (wideReball == 1) {
            str = " SUM(A.ISWIDE)";
        } else {
            str = "0";
        }
        append = append.append(str).append(noBallReball == 1 ? " + SUM(A.ISNOBALL)" : " + 0").append(" EXTRABALLS,  SUM(A.ISWICKET) - SUM(IFNULL(B.RUNOUTS, 0)) WICKETS,  CASE WHEN MAX(A.BALLNO) < ").append(ballsPerOver).append(" AND MAX(A.BALLSTHISOVER) < ").append(maxBallsPerOver).append("  THEN COUNT(A.BALLNO) - SUM(A.ISWIDE) + SUM(A.ISNOBALL)  ELSE MAX(").append(maxBallsPerOver).append(", COUNT(A.BALLSTHISOVER)) END AS BALLSTHISOVER,  CASE WHEN (COUNT(A.BALLNO) >= ").append(ballsPerOver).append(" AND SUM(IFNULL(C.RUNSTHISBALL, 0)) + SUM(A.ISWIDE) + SUM(A.ISNOBALL) + SUM(IFNULL(D.EXTRAS2, 0)) = 0) THEN 1 ELSE 0 END ISMAIDEN  FROM MATCHSCOREBOARD A  LEFT OUTER JOIN (  SELECT B.TOTALBALLNO, 1 AS RUNOUTS FROM MATCHSCOREBOARD B  WHERE B.MATCHID LIKE ? AND INNGNO = ? AND (B.WICKETHOW LIKE 'Run Out%' OR B.WICKETHOW LIKE 'Retired%')  ) B ON A.TOTALBALLNO = B.TOTALBALLNO  LEFT OUTER JOIN (  SELECT C.TOTALBALLNO, C.RUNSTHISBALL + CASE WHEN C.ISBYES+C.ISLEGBYES = 1 AND C.ISWIDE+C.ISNOBALL = 0 THEN -1*C.RUNSCORED ELSE 0 END AS RUNSTHISBALL FROM MATCHSCOREBOARD C  WHERE C.MATCHID LIKE ? AND INNGNO = ? ");
        if (config.getWdNbBowlerStats() == 1) {
            str = " AND C.ISWIDE = 0 AND C.ISNOBALL = 0 ";
        } else {
            str = "";
        }
        String selectQuery = append.append(str).append(" ) C ON A.TOTALBALLNO = C.TOTALBALLNO  LEFT OUTER JOIN (  SELECT D.TOTALBALLNO, D.RUNSTHISBALL AS EXTRAS2 FROM MATCHSCOREBOARD D  WHERE D.MATCHID LIKE ? AND INNGNO = ? AND (D.ISLEGBYES = 1 OR D.ISBYES = 1) AND D.ISWIDE = 0 AND D.ISNOBALL = 0  ) D ON A.TOTALBALLNO = D.TOTALBALLNO  WHERE MATCHID LIKE ? AND INNGNO = ?  GROUP BY TEAM, BOWLERNAME, OVERNO  ) A  LEFT OUTER JOIN (  SELECT E.BOWLERNAME, E.TEAM, SUM(E.RUNSTHISBALL) AS WIDES FROM MATCHSCOREBOARD E  WHERE E.MATCHID LIKE ? AND INNGNO = ? AND E.ISWIDE = 1  GROUP BY E.BOWLERNAME, E.TEAM  ) E ON A.BOWLERNAME = E.BOWLERNAME AND A.TEAM = E.TEAM  LEFT OUTER JOIN (  SELECT F.BOWLERNAME, F.TEAM, SUM(F.RUNSTHISBALL) + SUM(CASE WHEN F.ISBYES = 1 OR F.ISLEGBYES = 1 THEN F.RUNSCORED ELSE 0 END) - SUM(F.RUNSCORED) AS NOBALLS FROM MATCHSCOREBOARD F  WHERE F.MATCHID LIKE ? AND F.INNGNO = ? AND F.ISNOBALL = 1  GROUP BY F.BOWLERNAME, F.TEAM  ) F ON A.BOWLERNAME = F.BOWLERNAME AND A.TEAM = F.TEAM  WHERE A.TEAM = ?  GROUP BY A.TEAM, A.BOWLERNAME").toString();
        if (orderBy.trim().equalsIgnoreCase("")) {
            selectQuery = selectQuery + " ORDER BY MIN(A.OVERNO)";
        } else {
            selectQuery = selectQuery + " ORDER BY " + orderBy;
        }
        selectQuery = selectQuery + ", A.TEAM";
        if (limit > 0) {
            selectQuery = selectQuery + " LIMIT " + limit;
        }
        Cursor cursor = db.rawQuery(selectQuery, new String[]{matchID, inngNo, matchID, inngNo, matchID, inngNo, matchID, inngNo, matchID, inngNo, matchID, inngNo, team});
        if (cursor.moveToFirst()) {
            do {
                dtoBowlerStat obj = new dtoBowlerStat();
                obj.setBowlerName("" + cursor.getString(0));
                obj.setOvers(cursor.getInt(1));
                obj.setBalls(cursor.getInt(2));
                obj.setCalculatedOvers(cursor.getInt(3));
                obj.setCalculatedBalls(cursor.getInt(4));
                obj.setRuns(cursor.getInt(5));
                obj.setWickets(cursor.getInt(6));
                obj.setRunRate(cursor.getDouble(7));
                obj.setMaidenOvers(cursor.getInt(8));
                obj.setWides(cursor.isNull(9) ? 0 : cursor.getInt(9));
                obj.setNoballs(cursor.isNull(10) ? 0 : cursor.getInt(10));
                bowlStatList.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bowlStatList;
    }

    public List<dtoBowlerStat> getBowlerStatsForMatchSheet(String matchID, String team, String inngNo) {
        String str;
        dtoConfiguration config = getConfiguration();
        SQLiteDatabase db = getReadableDatabase();
        List<dtoBowlerStat> bowlStatList = new ArrayList();
        StringBuilder append = new StringBuilder().append("SELECT  A.BOWLERNAME, A.OVERNO, A.BALLBYBALL, E.RUNS, B.WIDES, C.NOBALLS, D.WICKETS, A.SCORE FROM ( SELECT BOWLERNAME, OVERNO, MAX(BALLBYBALL) BALLBYBALL, SUM(RUNSTHISBALL) RUNS, MAX(TOTALSCORE) || '/' || MAX(WICKETCOUNT) AS SCORE FROM MATCHSCOREBOARD WHERE TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY BOWLERNAME, OVERNO ) A  LEFT OUTER JOIN ( SELECT BOWLERNAME, OVERNO, SUM(RUNSTHISBALL) + SUM(CASE WHEN ISBYES+ISLEGBYES = 1 AND ISWIDE+ISNOBALL = 0 THEN -1*RUNSCORED ELSE 0 END) RUNS FROM MATCHSCOREBOARD WHERE TEAM = ? AND MATCHID LIKE ? AND INNGNO = ?");
        if (config.getWdNbBowlerStats() == 1) {
            str = " AND ISWIDE = 0 AND ISNOBALL = 0 ";
        } else {
            str = "";
        }
        Cursor cursor = db.rawQuery(append.append(str).append(" GROUP BY BOWLERNAME, OVERNO ) E ON A.BOWLERNAME = E.BOWLERNAME AND A.OVERNO = E.OVERNO LEFT OUTER JOIN ( SELECT BOWLERNAME, OVERNO, SUM(RUNSTHISBALL) WIDES FROM MATCHSCOREBOARD WHERE ISWIDE = 1 AND TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY BOWLERNAME, OVERNO ) B ON A.BOWLERNAME = B.BOWLERNAME AND A.OVERNO = B.OVERNO LEFT OUTER JOIN ( SELECT BOWLERNAME, OVERNO, SUM(ISNOBALL) + SUM(CASE WHEN ISBYES = 1 OR ISLEGBYES = 1 THEN RUNSCORED ELSE 0 END) NOBALLS FROM MATCHSCOREBOARD WHERE ISNOBALL = 1 AND TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY BOWLERNAME, OVERNO ) C ON A.BOWLERNAME = C.BOWLERNAME AND A.OVERNO = C.OVERNO LEFT OUTER JOIN ( SELECT BOWLERNAME, OVERNO, SUM(ISWICKET) WICKETS FROM MATCHSCOREBOARD WHERE ISWICKET = 1 AND (WICKETHOW NOT LIKE 'Run Out%' AND WICKETHOW NOT LIKE 'Retired%') AND TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY BOWLERNAME, OVERNO ) D ON A.BOWLERNAME = D.BOWLERNAME AND A.OVERNO = D.OVERNO ORDER BY A.OVERNO, A.BOWLERNAME").toString(), new String[]{team, matchID, inngNo, team, matchID, inngNo, team, matchID, inngNo, team, matchID, inngNo, team, matchID, inngNo});
        if (cursor.moveToFirst()) {
            do {
                dtoBowlerStat obj = new dtoBowlerStat();
                obj.setBowlerName("" + cursor.getString(0));
                obj.setOvers(cursor.getInt(1));
                obj.setBallByBall(cursor.getString(2));
                obj.setRuns(cursor.getInt(3));
                obj.setWides(cursor.getInt(4));
                obj.setNoballs(cursor.getInt(5));
                obj.setWickets(cursor.getInt(6));
                obj.setScore(cursor.getString(7));
                bowlStatList.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bowlStatList;
    }

    public List<dtoBallByBallStat> getBallByBallReport(String matchID, String team, String inngNo) {
        List<dtoBallByBallStat> batStatList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  OVERNO, BALLNO, STRIKERNAME, STRIKERRUNS, STRIKERBALLSFACED, NONSTRIKERNAME, NONSTRIKERRUNS, NONSTRIKERBALLSFACED, BOWLERNAME, CASE WHEN ISWIDE+ISNOBALL+ISLEGBYES+ISBYES+ISPENALTY = 0 THEN 'NO' ELSE 'YES' END HASEXTRAS, TOTALSCORE, BALLBYBALL FROM MATCHSCOREBOARD WHERE TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? ORDER BY TOTALBALLNO", new String[]{team, matchID, inngNo});
        if (cursor.moveToFirst()) {
            do {
                dtoBallByBallStat obj = new dtoBallByBallStat();
                obj.setOvers(cursor.getInt(0));
                obj.setBallNo(cursor.getInt(1));
                obj.setStriker(cursor.getString(2) + ":  " + cursor.getInt(3) + " (" + cursor.getInt(4) + ")");
                obj.setNonStriker(cursor.getString(5) + ":  " + cursor.getInt(6) + " (" + cursor.getInt(7) + ")");
                obj.setBowler(cursor.getString(8));
                obj.setHasExtra(cursor.getString(9));
                obj.setTotalScore(cursor.getInt(10));
                obj.setBallByBall(cursor.getString(11));
                batStatList.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return batStatList;
    }

    public List<dtoBatsStats> getBatsmanStats(String matchID, String team, String inngNo) {
        return getBatsmanStats(matchID, team, inngNo, "");
    }

    public List<dtoBatsStats> getBatsmanStats(String matchID, String team, String inngNo, String orderBy) {
        return getBatsmanStats(matchID, team, inngNo, orderBy, 0);
    }

    public List<dtoBatsStats> getHighestRunBatsmen(String matchID, String team, String inngNo, int limit) {
        return getBatsmanStats(matchID, team, inngNo, "A.STRIKERRUNS DESC", limit);
    }

    public List<dtoBatsStats> getBatsmanStats(String matchID, String team, String inngNo, String orderBy, int limit) {
        List<dtoBatsStats> batStatList = new ArrayList();
        String selectQuery = "SELECT  C.FIRSTBALLNO, C.STRIKERNAME, A.STRIKERNAME, IFNULL(A.WICKETHOW, ''), IFNULL(A.WICKETASSIST, ''), IFNULL(A.BOWLERNAME, ''), A.STRIKERRUNS, A.STRIKERBALLSFACED, A.RUNRATE FROM ( SELECT MIN(MINBALLNO) FIRSTBALLNO, D.STRIKERNAME FROM ( SELECT MIN(TOTALBALLNO) MINBALLNO, STRIKERNAME FROM MATCHSCOREBOARD WHERE TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY STRIKERNAME UNION SELECT MIN(TOTALBALLNO)+0.1 MINBALLNO, NONSTRIKERNAME FROM MATCHSCOREBOARD WHERE TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY NONSTRIKERNAME ) D GROUP BY STRIKERNAME ) C  LEFT OUTER JOIN ( SELECT TOTALBALLNO, STRIKERNAME, WICKETHOW, WICKETASSIST, BOWLERNAME, STRIKERRUNS, STRIKERBALLSFACED, Round((STRIKERRUNS *1.0 / STRIKERBALLSFACED *1.0)*100, 0) AS RUNRATE FROM MATCHSCOREBOARD WHERE ISWICKET = 1 AND ((WICKETHOW IS NULL) OR (WICKETHOW IS NOT NULL AND WICKETHOW NOT LIKE '%Non%')) AND MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? UNION SELECT TOTALBALLNO, NONSTRIKERNAME, WICKETHOW, WICKETASSIST, BOWLERNAME, NONSTRIKERRUNS, NONSTRIKERBALLSFACED, Round((NONSTRIKERRUNS *1.0 / NONSTRIKERBALLSFACED *1.0)*100, 0) AS 'R/R' FROM MATCHSCOREBOARD WHERE ISWICKET = 1 AND ((WICKETHOW IS NULL) OR (WICKETHOW IS NOT NULL AND WICKETHOW LIKE '%Non%')) AND MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? UNION SELECT MAX(TOTALBALLNO), STRIKERNAME, '', 'n.o.', '', MAX(STRIKERRUNS), MAX(STRIKERBALLSFACED), Round((MAX(STRIKERRUNS) *1.0 / MAX(STRIKERBALLSFACED) *1.0)*100, 0) AS 'R/R' FROM MATCHSCOREBOARD WHERE MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? AND STRIKERNAME NOT IN (SELECT STRIKERNAME FROM MATCHSCOREBOARD WHERE MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? AND ISWICKET = 1 AND WICKETHOW NOT LIKE '%Non%')  AND STRIKERNAME NOT IN (SELECT NONSTRIKERNAME FROM MATCHSCOREBOARD WHERE MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? AND ISWICKET = 1 AND WICKETHOW LIKE '%Non%')  GROUP BY TEAM, STRIKERNAME  ) A ON C.STRIKERNAME = A.STRIKERNAME";
        if (orderBy.trim().equalsIgnoreCase("")) {
            selectQuery = selectQuery + " ORDER BY C.FIRSTBALLNO";
        } else {
            selectQuery = selectQuery + " ORDER BY " + orderBy;
        }
        if (limit > 0) {
            selectQuery = selectQuery + " LIMIT " + limit;
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{team, matchID, inngNo, team, matchID, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            do {
                dtoBatsStats obj = new dtoBatsStats();
                obj.setFirstBallNo(cursor.isNull(0) ? 99999 : cursor.getInt(0));
                obj.setBatsmanName("" + cursor.getString(1));
                obj.setWicketHow(cursor.isNull(2) ? "" : "" + cursor.getString(3));
                obj.setWicketAssist(cursor.isNull(3) ? "" : "" + cursor.getString(4));
                obj.setBowlerName(cursor.isNull(4) ? "" : "" + cursor.getString(5));
                obj.setRuns(cursor.getInt(6));
                obj.setBalls(cursor.getInt(7));
                obj.setRunRate(cursor.getInt(8));
                batStatList.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return batStatList;
    }

    public List<dtoBatsStats> getBatsmanStatsForMatchSheet(String matchID, String team, String inngNo) {
        List<dtoBatsStats> batStatList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  C.STRIKERNAME, A.STRIKERNAME, IFNULL(B.RUNSBYBALL, ''), IFNULL(A.WICKETHOW, ''), IFNULL(A.WICKETASSIST, ''), IFNULL(A.BOWLERNAME, ''), A.STRIKERRUNS, A.STRIKERBALLSFACED, A.RUNRATE, SIXES.SIXES, FOURS.FOURS FROM ( SELECT MIN(MINBALLNO) FIRSTBALLNO, D.STRIKERNAME FROM ( SELECT MIN(TOTALBALLNO) MINBALLNO, STRIKERNAME FROM MATCHSCOREBOARD WHERE TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY STRIKERNAME UNION SELECT MIN(TOTALBALLNO)+0.1 MINBALLNO, NONSTRIKERNAME FROM MATCHSCOREBOARD WHERE TEAM = ? AND MATCHID LIKE ? AND INNGNO = ? GROUP BY NONSTRIKERNAME ) D GROUP BY STRIKERNAME ) C  LEFT OUTER JOIN ( SELECT TOTALBALLNO, STRIKERNAME, WICKETHOW, WICKETASSIST, BOWLERNAME, STRIKERRUNS, STRIKERBALLSFACED, Round((STRIKERRUNS *1.0 / STRIKERBALLSFACED *1.0)*100, 0) AS RUNRATE FROM MATCHSCOREBOARD WHERE ISWICKET = 1 AND ((WICKETHOW IS NULL) OR (WICKETHOW IS NOT NULL AND WICKETHOW NOT LIKE '%Non%')) AND MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? UNION SELECT TOTALBALLNO, NONSTRIKERNAME, WICKETHOW, WICKETASSIST, BOWLERNAME, NONSTRIKERRUNS, NONSTRIKERBALLSFACED, Round((NONSTRIKERRUNS *1.0 / NONSTRIKERBALLSFACED *1.0)*100, 0) AS 'R/R' FROM MATCHSCOREBOARD WHERE ISWICKET = 1 AND ((WICKETHOW IS NULL) OR (WICKETHOW IS NOT NULL AND WICKETHOW LIKE '%Non%')) AND MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? UNION SELECT MAX(TOTALBALLNO), STRIKERNAME, '', 'n.o.', '', MAX(STRIKERRUNS), MAX(STRIKERBALLSFACED), Round((MAX(STRIKERRUNS) *1.0 / MAX(STRIKERBALLSFACED) *1.0)*100, 0) AS 'R/R' FROM MATCHSCOREBOARD WHERE MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? AND STRIKERNAME NOT IN (SELECT STRIKERNAME FROM MATCHSCOREBOARD WHERE MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? AND ISWICKET = 1 AND WICKETHOW NOT LIKE '%Non%')  AND STRIKERNAME NOT IN (SELECT NONSTRIKERNAME FROM MATCHSCOREBOARD WHERE MATCHID LIKE ? AND TEAM = ? AND INNGNO = ? AND ISWICKET = 1 AND WICKETHOW LIKE '%Non%')  GROUP BY TEAM, STRIKERNAME  ) A ON C.STRIKERNAME = A.STRIKERNAME LEFT OUTER JOIN ( Select StrikerName, group_concat(Case When (IsNoBall = 1) Then Case When (RunScored <= 0) Then '' Else Case When (IsByes = 1 OR IsLegByes = 1) Then '' Else RunScored End End Else Case When (IsByes = 1 OR IsLegByes = 1) Then '-' Else Case When (RunScored <= 0) Then '-' Else RunScored End End End, ' ') AS RunsByBall From MatchScoreBoard  Where IsWide = 0 and MatchID LIKE ? AND INNGNO = ? Group by Strikername ) B ON A.STRIKERNAME = B.STRIKERNAME LEFT OUTER JOIN ( SELECT STRIKERNAME, COUNT(RUNSTHISBALL) SIXES FROM MATCHSCOREBOARD WHERE RUNSCORED = 6 AND ISBYES = 0 AND ISLEGBYES = 0 AND ISWIDE = 0  AND MATCHID LIKE ? AND INNGNO = ? GROUP BY STRIKERNAME ) SIXES ON SIXES.STRIKERNAME = A.STRIKERNAME LEFT OUTER JOIN ( SELECT STRIKERNAME, COUNT(RUNSTHISBALL) FOURS FROM MATCHSCOREBOARD WHERE RUNSCORED = 4 AND ISBYES = 0 AND ISLEGBYES = 0 AND ISWIDE = 0  AND MATCHID LIKE ? AND INNGNO = ? GROUP BY STRIKERNAME ) FOURS ON FOURS.STRIKERNAME = A.STRIKERNAME ORDER BY C.FIRSTBALLNO", new String[]{team, matchID, inngNo, team, matchID, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, inngNo, matchID, inngNo, matchID, inngNo});
        if (cursor.moveToFirst()) {
            do {
                dtoBatsStats obj = new dtoBatsStats();
                obj.setBatsmanName("" + cursor.getString(0));
                obj.setRunsByBall("" + cursor.getString(2));
                obj.setWicketHow("" + cursor.getString(3));
                obj.setWicketAssist("" + cursor.getString(4));
                obj.setBowlerName("" + cursor.getString(5));
                obj.setRuns(cursor.getInt(6));
                obj.setBalls(cursor.getInt(7));
                obj.setRunRate(cursor.getInt(8));
                obj.setSixes(cursor.getInt(9));
                obj.setFours(cursor.getInt(10));
                batStatList.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return batStatList;
    }

    public List<String> getAllMatchIDs() {
        List<String> matchList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MATCHID FROM MATCHMASTER", null);
        if (cursor.moveToFirst()) {
            do {
                matchList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return matchList;
    }

    public List<dtoMatchList> getAllMatchList() {
        List<dtoMatchList> matchList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MATCHID, MATCHDATETIME, TEAM1NAME || ' vs ' || TEAM2NAME || IFNULL(' @ ' || NULLIF(Venue, ''), '') As TEAMS,  CASE TossWonBy WHEN 'Team 1' THEN TEAM1NAME ELSE TEAM2NAME END || ' won the toss and opted to ' || OptedTo AS Toss,  CASE Overs WHEN '4479' THEN 'Unlimited' ELSE Overs END || ' overs match' AS Overs,  RESULT, UPPER(IFNULL(NULLIF(MATCHNAME, ''), 'PRACTICE MATCH')) FROM MATCHMASTER", null);
        if (cursor.moveToFirst()) {
            do {
                dtoMatchList match = new dtoMatchList();
                match.setMatchID(cursor.getString(0));
                match.setMatchDateTime(cursor.getString(1));
                match.setTeams(cursor.getString(2));
                match.setToss(cursor.getString(3));
                match.setOvers(cursor.getString(4));
                if (cursor.isNull(5)) {
                    match.setResult("");
                } else {
                    match.setResult(cursor.getString(5));
                }
                match.setMatchName("" + cursor.getString(6));
                matchList.add(match);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return matchList;
    }

    public dtoMatch getMatchMaster(String matchID) {
        int i = 0;
        dtoMatch match = new dtoMatch();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MatchID, MatchDateTime, Venue, Team1Name, Team2Name, TossWonBy, OptedTo, Overs, WideBall, WideReball, NoBall, NoballReball, LegByes, Byes, Lbw, WagonWheel, BallSpot, PresetTeams, WideRun, NoballRun, RestrictBPO, MaxBPO, BPO, Result, WwDot, WwWicket, MatchName, NoOfInngs, NoOfDays FROM MatchMaster WHERE MatchID LIKE ?", new String[]{matchID});
        if (cursor.moveToFirst()) {
            match.setMatchID("" + cursor.getString(0));
            match.setDateTime("" + cursor.getString(1));
            match.setVenue("" + cursor.getString(2));
            match.setTeam1Name("" + cursor.getString(3));
            match.setTeam2Name("" + cursor.getString(4));
            match.setTossWonBy("" + cursor.getString(5));
            match.setOptedTo("" + cursor.getString(6));
            match.setOvers(cursor.getInt(7));
            if (cursor.isNull(27)) {
                match.setNoOfInngs(1);
            } else {
                match.setNoOfInngs(cursor.getInt(27));
            }
            if (cursor.isNull(28)) {
                match.setNoOfDays(1);
            } else {
                match.setNoOfDays(cursor.getInt(28));
            }
            dtoAdditionalSettings settings = new dtoAdditionalSettings();
            settings.setWide(cursor.getInt(8));
            settings.setWideReball(cursor.getInt(9));
            settings.setNoball(cursor.getInt(10));
            settings.setNoballReball(cursor.getInt(11));
            settings.setLegbyes(cursor.getInt(12));
            settings.setByes(cursor.getInt(13));
            settings.setLbw(cursor.getInt(14));
            settings.setWagonWheel(cursor.getInt(15));
            settings.setBallSpot(cursor.getInt(16));
            settings.setPresetTeams(cursor.getInt(17));
            settings.setWideRun(cursor.getInt(18));
            settings.setNoballRun(cursor.getInt(19));
            settings.setRestrictBpo(cursor.getInt(20));
            settings.setMaxBpo(cursor.getInt(21));
            settings.setBpo(cursor.getInt(22));
            settings.setWwForDotBall(cursor.getInt(24));
            settings.setWwForWicket(cursor.getInt(25));
            match.setAdditionalSettings(settings);
            if (cursor.isNull(23)) {
                match.setMatchResult("");
            } else {
                match.setMatchResult("" + cursor.getString(23));
            }
            if (cursor.isNull(26)) {
                match.setMatchName("");
            } else {
                match.setMatchName("" + cursor.getString(26));
            }
        }
        cursor.close();
        cursor = db.rawQuery("SELECT MatchID, Team, TotalBallNo, OverNo, BallNo, StrikerName, NonStrikerName, BowlerName, IsWide, IsNoball, IsLegByes, IsByes, IsWicket, WicketHow, WicketAssist, RunScored, RunsThisBall, BallByBall, TotalScore, WicketCount, StrikerRuns, NonStrikerRuns, StrikerBallsFaced, NonStrikerBallsFaced, Target, TotalExtras, BallsThisOver, InngNo, DayNo, FEOD, FEOI, EOIDesc, IsPenalty FROM MatchScoreBoard WHERE MatchID LIKE ? AND TotalBallNo = (SELECT MAX(TotalBallNo) FROM MatchScoreBoard WHERE MatchID LIKE ?)", new String[]{matchID, matchID});
        if (cursor.moveToFirst()) {
            boolean z;
            dtoScoreBall ball = new dtoScoreBall();
            ball.setTeamNo(cursor.getString(1));
            ball.setTotalBallNo(cursor.getInt(2));
            ball.setOverNo(cursor.getInt(3));
            ball.setBallNo(cursor.getInt(4));
            ball.setStrikerName(cursor.getString(5));
            ball.setNonStrikerName(cursor.getString(6));
            ball.setBowlerName(cursor.getString(7));
            if (cursor.getInt(8) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsWide(z);
            if (cursor.getInt(9) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsNoball(z);
            if (cursor.getInt(10) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsLegByes(z);
            if (cursor.getInt(11) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsByes(z);
            if (cursor.getInt(12) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsWicket(z);
            ball.setWicketHow(cursor.getString(13));
            ball.setWicketAssist(cursor.getString(14));
            ball.setRunsScored(cursor.getInt(15));
            ball.setRunsThisBall(cursor.getInt(16));
            ball.setBallByBall(cursor.getString(17));
            ball.setTotalScore(cursor.getInt(18));
            ball.setWicketCount(cursor.getInt(19));
            ball.setStrikerRuns(cursor.getInt(20));
            ball.setNonStrikerRuns(cursor.getInt(21));
            ball.setBallsFacedStriker(cursor.getInt(22));
            ball.setBallsFacedNonStriker(cursor.getInt(23));
            ball.setTarget(cursor.getInt(24));
            ball.setTotalExtras(cursor.getInt(25));
            ball.setBallsThisOver(cursor.getInt(26));
            if (cursor.isNull(27)) {
                ball.setInningNo(1);
            } else {
                ball.setInningNo(cursor.getInt(27));
            }
            if (cursor.isNull(28)) {
                ball.setDayNo(1);
            } else {
                ball.setDayNo(cursor.getInt(28));
            }
            ball.setFlagEOD(cursor.isNull(29) ? 0 : cursor.getInt(29));
            ball.setFlagEOI(cursor.isNull(30) ? 0 : cursor.getInt(30));
            ball.setEOIDescription(cursor.getString(31));
            if (!cursor.isNull(32)) {
                i = cursor.getInt(32);
            }
            ball.setIsPenalty(i);
            match.setLastScoreball(ball);
        }
        match.setIsMatchReopened(true);
        cursor.close();
        db.close();
        return match;
    }

    public dtoScoreBall getLastScoreBall(String matchID) {
        int i = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MatchID, Team, TotalBallNo, OverNo, BallNo, StrikerName, NonStrikerName, BowlerName, IsWide, IsNoball, IsLegByes, IsByes, IsWicket, WicketHow, WicketAssist, RunScored, RunsThisBall, BallByBall, TotalScore, WicketCount, StrikerRuns, NonStrikerRuns, StrikerBallsFaced, NonStrikerBallsFaced, Target, TotalExtras, BallsThisOver, InngNo, DayNo, FEOD, FEOI, EOIDesc, IsPenalty FROM MatchScoreBoard WHERE MatchID LIKE ? AND TotalBallNo = (SELECT MAX(TotalBallNo) FROM MatchScoreBoard WHERE MatchID LIKE ?)", new String[]{matchID, matchID});
        dtoScoreBall ball = new dtoScoreBall();
        if (cursor.moveToFirst()) {
            boolean z;
            ball.setTeamNo(cursor.getString(1));
            ball.setTotalBallNo(cursor.getInt(2));
            ball.setOverNo(cursor.getInt(3));
            ball.setBallNo(cursor.getInt(4));
            ball.setStrikerName(cursor.getString(5));
            ball.setNonStrikerName(cursor.getString(6));
            ball.setBowlerName(cursor.getString(7));
            ball.setIsWide(cursor.getInt(8) == 1);
            if (cursor.getInt(9) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsNoball(z);
            if (cursor.getInt(10) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsLegByes(z);
            if (cursor.getInt(11) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsByes(z);
            if (cursor.getInt(12) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsWicket(z);
            ball.setWicketHow(cursor.getString(13));
            ball.setWicketAssist(cursor.getString(14));
            ball.setRunsScored(cursor.getInt(15));
            ball.setRunsThisBall(cursor.getInt(16));
            ball.setBallByBall(cursor.getString(17));
            ball.setTotalScore(cursor.getInt(18));
            ball.setWicketCount(cursor.getInt(19));
            ball.setStrikerRuns(cursor.getInt(20));
            ball.setNonStrikerRuns(cursor.getInt(21));
            ball.setBallsFacedStriker(cursor.getInt(22));
            ball.setBallsFacedNonStriker(cursor.getInt(23));
            ball.setTarget(cursor.getInt(24));
            ball.setTotalExtras(cursor.getInt(25));
            ball.setBallsThisOver(cursor.getInt(26));
            if (cursor.isNull(27)) {
                ball.setInningNo(1);
            } else {
                ball.setInningNo(cursor.getInt(27));
            }
            if (cursor.isNull(28)) {
                ball.setDayNo(1);
            } else {
                ball.setDayNo(cursor.getInt(28));
            }
            ball.setFlagEOD(cursor.isNull(29) ? 0 : cursor.getInt(29));
            ball.setFlagEOI(cursor.isNull(30) ? 0 : cursor.getInt(30));
            ball.setEOIDescription(cursor.getString(31));
            if (!cursor.isNull(32)) {
                i = cursor.getInt(32);
            }
            ball.setIsPenalty(i);
        } else {
            ball = null;
        }
        cursor.close();
        db.close();
        return ball;
    }

    public dtoMatchStats getMatchStats(String matchID, String team, String inngNo) {
        dtoMatchStats mStat = new dtoMatchStats();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT TEAM, OVERNO, BALLNO, TOTALSCORE, WICKETCOUNT, (IFNULL(W.WIDES, 0) + IFNULL(N.NOBALLS, 0) + IFNULL(LB.LEGBYES, 0) + IFNULL(B.BYES, 0) + IFNULL(P.PENALTY, 0)) TOTALEXTRAS, W.WIDES, N.NOBALLS, LB.LEGBYES, B.BYES, BALLSTHISOVER, EOIDESC, P.PENALTY FROM MATCHSCOREBOARD A  LEFT OUTER JOIN ( SELECT MATCHID, SUM(RUNSTHISBALL) WIDES FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND ISWIDE = 1 GROUP BY MATCHID, TEAM ) W ON A.MATCHID = W.MATCHID LEFT OUTER JOIN ( SELECT MATCHID, SUM(RUNSTHISBALL) + SUM(CASE WHEN ISBYES = 1 OR ISLEGBYES = 1 THEN RUNSCORED ELSE 0 END) - SUM(RUNSCORED) NOBALLS FROM MATCHSCOREBOARD  WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND ISNOBALL = 1 GROUP BY MATCHID, TEAM ) N ON A.MATCHID = N.MATCHID LEFT OUTER JOIN ( SELECT MATCHID, SUM(RUNSCORED) LEGBYES FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND ISLEGBYES = 1 AND ISWIDE = 0 AND ISNOBALL = 0 GROUP BY MATCHID, TEAM ) LB ON A.MATCHID = LB.MATCHID LEFT OUTER JOIN ( SELECT MATCHID, SUM(RUNSCORED) BYES FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND ISBYES = 1 AND ISWIDE = 0 AND ISNOBALL = 0 GROUP BY MATCHID, TEAM ) B ON A.MATCHID = B.MATCHID LEFT OUTER JOIN ( SELECT MATCHID, SUM(ISPENALTY) PENALTY FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? GROUP BY MATCHID, TEAM ) P ON A.MATCHID = P.MATCHID WHERE A.MATCHID = ? AND A.TOTALBALLNO IN (SELECT MAX(TOTALBALLNO) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND INNGNO = ? GROUP BY TEAM) AND A.TEAM = ? AND A.INNGNO = ? ORDER BY A.TOTALBALLNO", new String[]{matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, matchID, inngNo, team, inngNo});
        if (cursor.moveToFirst()) {
            mStat.setTeamNo(cursor.getInt(0));
            mStat.setOverNo(cursor.getInt(1));
            mStat.setBallNo(cursor.getInt(2));
            mStat.setTotalScore(cursor.getInt(3));
            mStat.setWicketCount(cursor.getInt(4));
            mStat.setTotalExtras(cursor.getInt(5));
            mStat.setWides(cursor.getInt(6));
            mStat.setNoballs(cursor.getInt(7));
            mStat.setLegByes(cursor.getInt(8));
            mStat.setByes(cursor.getInt(9));
            mStat.setBallsThisOver(cursor.getInt(10));
            mStat.setEOIDesc(cursor.isNull(11) ? "" : "" + cursor.getString(11));
            mStat.setPenalty(cursor.isNull(12) ? 0 : cursor.getInt(12));
        }
        cursor.close();
        db.close();
        return mStat;
    }

    public List<dtoWagonWheelDetail> getWagonWheelDetail(String matchID, String team, String inngNo, boolean includeDots, String playerName) {
        List<dtoWagonWheelDetail> wStat = new ArrayList();
        String selectQuery = "SELECT TOTALBALLNO, RUNSCORED, XCORD, YCORD, ISWICKET, TOTALEXTRAS FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ?";
        if (!includeDots) {
            selectQuery = selectQuery + " AND RUNSCORED != 0";
        }
        String[] parameters = new String[]{matchID, team, inngNo};
        if (!playerName.equalsIgnoreCase("ALL")) {
            selectQuery = selectQuery + " AND ISBYES = 0 AND ISLEGBYES = 0 AND ISWIDE = 0 AND STRIKERNAME LIKE ?";
            parameters = new String[]{matchID, team, inngNo, playerName};
        }
        selectQuery = selectQuery + " ORDER BY TOTALBALLNO";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, parameters);
        if (cursor.moveToFirst()) {
            do {
                dtoWagonWheelDetail detail = new dtoWagonWheelDetail();
                detail.setBallNo(cursor.getInt(0));
                detail.setRunsThisBall(cursor.getInt(1));
                detail.setXCord(cursor.getInt(2));
                detail.setYCord(cursor.getInt(3));
                detail.setIsWicket(cursor.getInt(4));
                detail.setTotalExtras(cursor.getInt(5));
                wStat.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return wStat;
    }

    public List<String> getWagonWheelTeamPlayers(String matchID, String team, String inngNo, boolean includeDots) {
        List<String> team1List = new ArrayList();
        String selectQuery = "SELECT STRIKERNAME FROM MATCHSCOREBOARD WHERE ISBYES = 0 AND ISLEGBYES = 0 AND ISWIDE = 0 AND MATCHID = ? AND TEAM = ? AND INNGNO = ?";
        if (!includeDots) {
            selectQuery = selectQuery + " AND RUNSCORED != 0";
        }
        selectQuery = selectQuery + " GROUP BY STRIKERNAME ORDER BY STRIKERNAME";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            do {
                team1List.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return team1List;
    }

    public String getFallOfWickets(String matchID, String team, String inngNo) {
        String fow = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT GROUP_CONCAT(WICKETCOUNT || '/' || TOTALSCORE || ' (' || CASE WHEN WICKETHOW LIKE '%non-striker%' THEN NONSTRIKERNAME ELSE STRIKERNAME END || '-' || OVERNO || '.' || BALLNO || ')', ' - ') FOW FROM MATCHSCOREBOARD A INNER JOIN ( SELECT MIN(TOTALBALLNO) AS TOTALBALLNO FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND WICKETCOUNT > 0 GROUP BY WICKETCOUNT ) B ON A.TOTALBALLNO = B.TOTALBALLNO WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ?", new String[]{matchID, team, inngNo, matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            fow = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return fow;
    }

    public List<dtoBallSpotDetail> getBallSpotDetail(String matchID, String team, String inngNo, String playerName) {
        List<dtoBallSpotDetail> wStat = new ArrayList();
        String selectQuery = "SELECT TOTALBALLNO, RUNSTHISBALL, BSXCORD, BSYCORD, ISWICKET, ISWIDE, ISNOBALL FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ?";
        String[] parameters = new String[]{matchID, team, inngNo};
        if (!playerName.equalsIgnoreCase("ALL")) {
            selectQuery = selectQuery + " AND BOWLERNAME LIKE ?";
            parameters = new String[]{matchID, team, inngNo, playerName};
        }
        selectQuery = selectQuery + " ORDER BY TOTALBALLNO";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, parameters);
        if (cursor.moveToFirst()) {
            do {
                dtoBallSpotDetail detail = new dtoBallSpotDetail();
                detail.setBallNo(cursor.getInt(0));
                detail.setRunsThisBall(cursor.getInt(1));
                detail.setBSXCord(cursor.getInt(2));
                detail.setBSYCord(cursor.getInt(3));
                detail.setIsWicket(cursor.getInt(4));
                detail.setIsWide(cursor.getInt(5));
                detail.setIsNoball(cursor.getInt(6));
                wStat.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return wStat;
    }

    public List<String> getBallSpotTeamPlayers(String matchID, String team, String inngNo) {
        List<String> team1List = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT BOWLERNAME FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ?ORDER BY BOWLERNAME", new String[]{matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            do {
                team1List.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return team1List;
    }

    public List<dtoRunsPerOver> getRunsPerOver(String matchID, String team, String inngNo) {
        List<dtoRunsPerOver> mStat = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT OVERNO, SUM(RUNSTHISBALL), SUM(ISWICKET) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? GROUP BY OVERNO ORDER BY OVERNO", new String[]{matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            do {
                dtoRunsPerOver detail = new dtoRunsPerOver();
                detail.setOverNo(cursor.getInt(0));
                detail.setRuns(cursor.getInt(1));
                detail.setWickets(cursor.getInt(2));
                mStat.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mStat;
    }

    public PartnershipStatsList getPartnershipData(String matchID, String team, String inngNo) {
        List<dtoPartnershipRawData> mStat = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT A.STRIKERNAME, A.NONSTRIKERNAME, SUM(A.RUNS) RUNS, SUM(A.EXTRAS) EXTRAS, SUM(A.BALLS) BALLS FROM ( SELECT STRIKERNAME, NONSTRIKERNAME, SUM(RUNSCORED) - SUM(CASE WHEN ISBYES = 1 OR ISLEGBYES = 1 THEN RUNSCORED ELSE 0 END) RUNS, 0 EXTRAS, COUNT(DISTINCT OVERNO || BALLNO) BALLS, MIN(TOTALBALLNO) O FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND ISWIDE = 0 GROUP BY STRIKERNAME, NONSTRIKERNAME UNION ALL SELECT STRIKERNAME, NONSTRIKERNAME, 0 RUNS, SUM(RUNSTHISBALL) EXTRAS, 0 BALLS, 999999 O FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND (ISWIDE = 1 OR ISLEGBYES = 1 OR ISBYES = 1) AND ISNOBALL = 0 GROUP BY STRIKERNAME, NONSTRIKERNAME UNION ALL SELECT STRIKERNAME, NONSTRIKERNAME, 0 RUNS, SUM(RUNSTHISBALL) + SUM(CASE WHEN ISBYES = 1 OR ISLEGBYES = 1 THEN RUNSCORED ELSE 0 END) - SUM(RUNSCORED) EXTRAS, 0 BALLS, 999999 O FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND ISNOBALL = 1 GROUP BY STRIKERNAME, NONSTRIKERNAME  UNION ALL SELECT STRIKERNAME, NONSTRIKERNAME, 0 RUNS, SUM(ISPENALTY) EXTRAS, 0 BALLS, 999999 O FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND ISPENALTY <> 0 GROUP BY STRIKERNAME, NONSTRIKERNAME ) A  GROUP BY A.STRIKERNAME, A.NONSTRIKERNAME ORDER BY MIN(A.O)", new String[]{matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo, matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            do {
                dtoPartnershipRawData detail = new dtoPartnershipRawData();
                detail.striker = cursor.getString(0);
                detail.nonstriker = cursor.getString(1);
                detail.runs = cursor.getInt(2);
                detail.extras = cursor.getInt(3);
                detail.balls = cursor.getInt(4);
                mStat.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        PartnershipStatsList result = new PartnershipStatsList();
        for (dtoPartnershipRawData d : mStat) {
            result.addRow(d);
        }
        return result;
    }

    public List<dtoRunsPerOver> getRunsRate(String matchID, String team, String inngNo) {
        List<dtoRunsPerOver> mStat = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT OVERNO, BALLNO, MAX(TOTALSCORE), SUM(ISWICKET) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? GROUP BY OVERNO, BALLNO ORDER BY OVERNO, BALLNO", new String[]{matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            do {
                dtoRunsPerOver detail = new dtoRunsPerOver();
                detail.setOverNo(cursor.getInt(0));
                detail.setBallNo(cursor.getInt(1));
                detail.setRuns(cursor.getInt(2));
                detail.setWickets(cursor.getInt(3));
                mStat.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mStat;
    }

    public List<dtoOverallBatsStat> getOverallBatStatsPlayerNames(String matchID, String teamName, String tournamentName, String filter) {
        teamName = teamName.toUpperCase(Locale.getDefault());
        tournamentName = tournamentName.toUpperCase(Locale.getDefault());
        List<dtoOverallBatsStat> list = new ArrayList();
        String selectQuery = "SELECT UPPER(TRIM(STRIKERNAME)) AS PLAYER FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE 1 = 1" + (!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "") + (!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "") + (!teamName.trim().equalsIgnoreCase("") ? " AND ((MSB.TEAM = 1 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 2 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))" : "") + (!filter.trim().equalsIgnoreCase("") ? " AND UPPER(STRIKERNAME) LIKE '%" + filter + "%'" : "") + " GROUP BY UPPER(TRIM(STRIKERNAME)) UNION SELECT UPPER(TRIM(NONSTRIKERNAME)) FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE 1 = 1" + (!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "") + (!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "") + (!teamName.trim().equalsIgnoreCase("") ? " AND ((MSB.TEAM = 1 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 2 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))" : "") + (!filter.trim().equalsIgnoreCase("") ? " AND UPPER(NONSTRIKERNAME) LIKE '%" + filter + "%'" : "") + " GROUP BY UPPER(TRIM(NONSTRIKERNAME))";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                dtoOverallBatsStat stat = new dtoOverallBatsStat();
                stat.setStrikerName(cursor.getString(0));
                stat.hasDetails(false);
                list.add(stat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<dtoOverallBatsStat> getOverallBatStats(String matchID, String teamName, String tournamentName, String filter) {
        return getOverallBatStatsQuery(matchID, teamName, tournamentName, filter, "");
    }

    public dtoOverallBatsStat getOverallBatStatsDetails(String matchID, String teamName, String tournamentName, String playerName) {
        List<dtoOverallBatsStat> list = getOverallBatStatsQuery(matchID, teamName, tournamentName, "", playerName);
        if (list.isEmpty()) {
            return null;
        }
        return (dtoOverallBatsStat) list.get(0);
    }

    public List<dtoOverallBatsStat> getOverallBatStatsQuery(String matchID, String teamName, String tournamentName, String filter, String playerName) {
        String str;
        List<dtoOverallBatsStat> list = new ArrayList();
        teamName = teamName.toUpperCase(Locale.getDefault());
        tournamentName = tournamentName.toUpperCase(Locale.getDefault());
        playerName = playerName.toUpperCase(Locale.getDefault());
        teamName = teamName.replace("'", "''");
        tournamentName = tournamentName.replace("'", "''");
        filter = filter.replace("'", "''");
        playerName = playerName.replace("'", "''");
        StringBuilder append = new StringBuilder().append("SELECT UPPER(C.PLAYER) PLAYER, COUNT(DISTINCT C.MATCHID) MATCHES, SUM(C.RUNS) RUNS, SUM(C.BALLS) BALLS, MAX(C.RUNS) HIGHSCORE, C.WKT STAR, COUNT(C.MATCHID) - SUM(C.WKT) NOTOUTS , D.SIXES, D.FOURS, D.THREES, D.TWOS, D.ONES, D.DOTS, COUNT(C.MATCHID) INNINGS, E.FIFTIES, F.TONS FROM ( SELECT B.PLAYER, B.MATCHID, B.INNGNO, MAX(TOTALBALLNO) TOTALBALLNO, MAX(B.RUNS) RUNS, MAX(B.BALLS) BALLS, MAX(B.WKT) WKT  FROM ( SELECT UPPER(TRIM(STRIKERNAME)) AS PLAYER, MSB.MATCHID, MSB.INNGNO, MAX(TOTALBALLNO) TOTALBALLNO, STRIKERRUNS RUNS, STRIKERBALLSFACED BALLS, CASE WHEN WICKETHOW LIKE '%non-striker%' THEN 0 ELSE ISWICKET END WKT FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE 1 = 1").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "").append(!teamName.trim().equalsIgnoreCase("") ? " AND ((MSB.TEAM = 1 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 2 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))" : "").append(" GROUP BY UPPER(TRIM(STRIKERNAME)), MSB.MATCHID, MSB.INNGNO UNION SELECT UPPER(TRIM(NONSTRIKERNAME)), MSB.MATCHID, MSB.INNGNO, MAX(TOTALBALLNO), NONSTRIKERRUNS RUNS, NONSTRIKERBALLSFACED BALLS, CASE WHEN WICKETHOW NOT LIKE '%non-striker%' THEN 0 ELSE ISWICKET END WKT FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE 1 = 1").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "");
        if (teamName.trim().equalsIgnoreCase("")) {
            str = "";
        } else {
            str = " AND ((MSB.TEAM = 1 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 2 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))";
        }
        append = append.append(str).append(" GROUP BY UPPER(TRIM(NONSTRIKERNAME)), MSB.MATCHID, MSB.INNGNO ) B GROUP BY B.PLAYER, B.MATCHID, B.INNGNO ) C LEFT OUTER JOIN ( SELECT UPPER(TRIM(STRIKERNAME)) PLAYER,  SUM(CASE RUNSCORED WHEN 6 THEN 1 ELSE 0 END) SIXES, SUM(CASE RUNSCORED WHEN 4 THEN 1 ELSE 0 END) FOURS, SUM(CASE RUNSCORED WHEN 3 THEN 1 ELSE 0 END) THREES, SUM(CASE RUNSCORED WHEN 2 THEN 1 ELSE 0 END) TWOS, SUM(CASE RUNSCORED WHEN 1 THEN 1 ELSE 0 END) ONES, SUM(CASE RUNSCORED WHEN 0 THEN 1 ELSE 0 END) - SUM(ISNOBALL) DOTS FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE ISBYES = 0 AND ISLEGBYES = 0 AND ISWIDE = 0").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "");
        if (teamName.trim().equalsIgnoreCase("")) {
            str = "";
        } else {
            str = " AND ((MSB.TEAM = 1 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 2 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))";
        }
        append = append.append(str).append(" GROUP BY UPPER(TRIM(STRIKERNAME)) ) D ON C.PLAYER = D.PLAYER LEFT OUTER JOIN ( SELECT A.STRIKERNAME PLAYER, COUNT(A.FIFTY) FIFTIES FROM ( SELECT UPPER(TRIM(MSB.STRIKERNAME)) STRIKERNAME, MAX(MSB.STRIKERRUNS) FIFTY FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE 1 = 1").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "");
        if (teamName.trim().equalsIgnoreCase("")) {
            str = "";
        } else {
            str = " AND ((MSB.TEAM = 1 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 2 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))";
        }
        append = append.append(str).append(" GROUP BY MSB.MATCHID, UPPER(TRIM(MSB.STRIKERNAME)), MSB.INNGNO HAVING MAX(MSB.STRIKERRUNS) >= 50 AND MAX(MSB.STRIKERRUNS) < 100 ) A GROUP BY A.STRIKERNAME ) E ON C.PLAYER = E.PLAYER LEFT OUTER JOIN ( SELECT A.STRIKERNAME PLAYER, COUNT(A.TON) TONS FROM ( SELECT UPPER(TRIM(MSB.STRIKERNAME)) STRIKERNAME, MAX(MSB.STRIKERRUNS) TON FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE 1 = 1").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "").append(!teamName.trim().equalsIgnoreCase("") ? " AND ((MSB.TEAM = 1 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 2 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))" : "").append(" GROUP BY MSB.MATCHID, UPPER(TRIM(MSB.STRIKERNAME)), MSB.INNGNO HAVING MAX(MSB.STRIKERRUNS) >= 100 ) A GROUP BY A.STRIKERNAME ) F ON C.PLAYER = F.PLAYER WHERE 1 = 1").append(!playerName.trim().equalsIgnoreCase("") ? " AND C.PLAYER = '" + playerName + "'" : "");
        str = (!playerName.trim().equalsIgnoreCase("") || filter.trim().equalsIgnoreCase("")) ? "" : " AND C.PLAYER LIKE '%" + filter + "%'";
        String selectQuery = append.append(str).append(" GROUP BY UPPER(C.PLAYER)").toString();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                dtoOverallBatsStat stat = new dtoOverallBatsStat();
                stat.setStrikerName(cursor.getString(0));
                stat.setMatches(cursor.getInt(1));
                stat.setRuns(cursor.getInt(2));
                stat.setBalls(cursor.getInt(3));
                append = new StringBuilder().append("").append(cursor.getString(4));
                str = (cursor.isNull(5) || cursor.getInt(5) != 0) ? "" : "<sup>*</sup>";
                stat.setHighScore(append.append(str).toString());
                stat.setNotOuts(cursor.getInt(6));
                stat.setSixes(cursor.getInt(7));
                stat.setFours(cursor.getInt(8));
                stat.setThrees(cursor.getInt(9));
                stat.setTwos(cursor.getInt(10));
                stat.setOnes(cursor.getInt(11));
                stat.setDots(cursor.getInt(12));
                stat.setInnings(cursor.getInt(13));
                stat.setFifties(cursor.isNull(14) ? 0 : cursor.getInt(14));
                stat.setTons(cursor.isNull(15) ? 0 : cursor.getInt(15));
                stat.hasDetails(true);
                stat.setShowDetails(true);
                list.add(stat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<dtoOverallBowlerStats> getOverallBowlerStatsPlayerNames(String matchID, String teamName, String tournamentName, String filter) {
        teamName = teamName.toUpperCase(Locale.getDefault());
        tournamentName = tournamentName.toUpperCase(Locale.getDefault());
        List<dtoOverallBowlerStats> list = new ArrayList();
        String selectQuery = "SELECT UPPER(TRIM(BOWLERNAME)) AS PLAYER FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE 1 = 1" + (!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "") + (!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "") + (!teamName.trim().equalsIgnoreCase("") ? " AND ((MSB.TEAM = 2 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 1 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))" : "") + (!filter.trim().equalsIgnoreCase("") ? " AND UPPER(TRIM(BOWLERNAME)) LIKE '%" + filter + "%'" : "") + " GROUP BY UPPER(TRIM(BOWLERNAME))";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                dtoOverallBowlerStats stat = new dtoOverallBowlerStats();
                stat.setBowlerName(cursor.getString(0));
                stat.hasDetails(false);
                list.add(stat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<dtoOverallBowlerStats> getOverallBowlerStats(String matchID, String teamName, String tournamentName, String filter) {
        return getOverallBowlerStatsQuery(matchID, teamName, tournamentName, filter, "");
    }

    public dtoOverallBowlerStats getOverallBowlerStatsDetails(String matchID, String teamName, String tournamentName, String playerName) {
        List<dtoOverallBowlerStats> list = getOverallBowlerStatsQuery(matchID, teamName, tournamentName, "", playerName);
        if (list.isEmpty()) {
            return null;
        }
        return (dtoOverallBowlerStats) list.get(0);
    }

    public List<dtoOverallBowlerStats> getOverallBowlerStatsQuery(String matchID, String teamName, String tournamentName, String filter, String playerName) {
        String str;
        List<dtoOverallBowlerStats> list = new ArrayList();
        teamName = teamName.toUpperCase(Locale.getDefault());
        tournamentName = tournamentName.toUpperCase(Locale.getDefault());
        playerName = playerName.toUpperCase(Locale.getDefault());
        teamName = teamName.replace("'", "''");
        tournamentName = tournamentName.replace("'", "''");
        filter = filter.replace("'", "''");
        playerName = playerName.replace("'", "''");
        StringBuilder append = new StringBuilder().append("SELECT B.BOWLERNAME, COUNT(DISTINCT B.MATCHID) MATCHES , SUM( B.CALCOVERS ) + (SUM(B.CALCBALLS) / MAX(B.BPO)) AS OVERS , SUM(B.CALCBALLS) % MAX(B.BPO) AS BALLS , SUM(B.RUNS) RUNS , SUM(B.WIDES) WIDES, SUM(B.NOBALLS) NOBALLS, SUM(B.WICKETS) WICKETS, SUM(B.MAIDEN) MAIDENS , D.DOTS, D.SIXES, D.FOURS , MIN(E.BESTFIG) BESTWKTS , COUNT(DISTINCT B.MATCHID+B.INNGNO) INNINGS , F.FIVEWKTHAUL FIVEWKTHAUL FROM ( SELECT UPPER(TRIM(A.BOWLERNAME)) BOWLERNAME, A.MATCHID, A.INNGNO, A.OVERNO, ( COUNT( A.BALLNO ) - SUM( A.ISWIDE ) - SUM( A.ISNOBALL )  ) / MAX( MM.BPO ) AS CALCOVERS, ( COUNT( A.BALLNO ) - SUM( A.ISWIDE ) - SUM( A.ISNOBALL )  ) % MAX( MM.BPO ) AS CALCBALLS, COUNT(A.BALLNO) BALLS, SUM(A.RUNSTHISBALL) TOTALRUNS, SUM(A.ISWIDE) WIDESBOWLED, SUM(A.ISNOBALL) NOBALLSBOWLED, SUM( CASE WHEN A.ISBYES = 0 AND A.ISLEGBYES = 0 THEN A.RUNSTHISBALL ELSE 0 END ) RUNS, SUM( CASE WHEN A.ISWIDE = 1 THEN A.RUNSTHISBALL ELSE 0 END ) WIDES, SUM( CASE WHEN A.ISNOBALL THEN A.RUNSTHISBALL - A.RUNSCORED ELSE 0 END ) NOBALLS, MAX(BPO) BPO, (SUM(A.ISWICKET) - SUM(CASE WHEN (A.ISWICKET = 1 AND A.WICKETHOW LIKE 'Run Out%') THEN 1 ELSE 0 END)) WICKETS, CASE WHEN SUM(A.RUNSTHISBALL) = 0 THEN 1 ELSE 0 END MAIDEN FROM MATCHSCOREBOARD A JOIN MATCHMASTER MM ON A.MATCHID = MM.MATCHID WHERE 1 = 1").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "");
        if (teamName.trim().equalsIgnoreCase("")) {
            str = "";
        } else {
            str = " AND ((A.TEAM = 2 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (A.TEAM = 1 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))";
        }
        append = append.append(str).append(" GROUP BY UPPER(TRIM(A.BOWLERNAME)), A.MATCHID, A.INNGNO, A.OVERNO ) B LEFT OUTER JOIN ( SELECT F.BOWLERNAME, MIN(F.BESTFIG) * -1 BESTFIG FROM ( SELECT A.MATCHID, A.INNGNO, UPPER(TRIM(A.BOWLERNAME)) BOWLERNAME ,CAST(((SUM(A.ISWICKET) - SUM(CASE WHEN (A.ISWICKET = 1 AND A.WICKETHOW LIKE 'Run Out%') THEN 1 ELSE 0 END)) || '.' || (999 - SUM(CASE WHEN A.ISBYES = 0 AND A.ISLEGBYES = 0 THEN A.RUNSTHISBALL ELSE 0 END))) AS DECIMAL) * -1 BESTFIG FROM MATCHSCOREBOARD A JOIN MATCHMASTER MM ON A.MATCHID = MM.MATCHID WHERE 1 = 1").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "");
        if (teamName.trim().equalsIgnoreCase("")) {
            str = "";
        } else {
            str = " AND ((A.TEAM = 2 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (A.TEAM = 1 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))";
        }
        append = append.append(str).append(" GROUP BY UPPER(TRIM(A.BOWLERNAME)), A.MATCHID, A.INNGNO ) F GROUP BY F.BOWLERNAME ) E ON B.BOWLERNAME = E.BOWLERNAME LEFT OUTER JOIN ( SELECT UPPER(TRIM(BOWLERNAME)) PLAYER,  SUM(CASE RUNSCORED WHEN 6 THEN 1 ELSE 0 END) SIXES, SUM(CASE RUNSCORED WHEN 4 THEN 1 ELSE 0 END) FOURS, SUM(CASE RUNSTHISBALL WHEN 0 THEN 1 ELSE 0 END) DOTS FROM MATCHSCOREBOARD MSB INNER JOIN MATCHMASTER MM ON MSB.MATCHID = MM.MATCHID WHERE ISBYES = 0 AND ISLEGBYES = 0 AND ISWIDE = 0").append(!matchID.trim().equalsIgnoreCase("") ? " AND MM.MATCHID = '" + matchID + "'" : "").append(!tournamentName.trim().equalsIgnoreCase("") ? " AND UPPER(IFNULL(MM.MATCHNAME, '')) = '" + tournamentName + "'" : "");
        if (teamName.trim().equalsIgnoreCase("")) {
            str = "";
        } else {
            str = " AND ((MSB.TEAM = 2 AND UPPER(MM.TEAM1NAME) = '" + teamName + "') OR (MSB.TEAM = 1 AND UPPER(MM.TEAM2NAME) = '" + teamName + "'))";
        }
        append = append.append(str).append(" GROUP BY UPPER(TRIM(BOWLERNAME)) ) D ON B.BOWLERNAME = D.PLAYER LEFT OUTER JOIN ( SELECT G.PLAYER, COUNT(FIVEWKTHAUL) FIVEWKTHAUL FROM ( SELECT UPPER(TRIM(BOWLERNAME)) PLAYER, (MATCHID || INNGNO) AS FIVEWKTHAUL FROM MATCHSCOREBOARD  GROUP BY UPPER(TRIM(BOWLERNAME)), MATCHID, TEAM, INNGNO  HAVING (SUM(ISWICKET) - SUM(CASE WHEN (ISWICKET = 1 AND WICKETHOW LIKE 'Run Out%') THEN 1 ELSE 0 END)) >= 5  ) G GROUP BY G.PLAYER ) F ON B.BOWLERNAME = F.PLAYER WHERE 1 = 1").append(!playerName.trim().equalsIgnoreCase("") ? " AND B.BOWLERNAME = '" + playerName + "'" : "");
        str = (!playerName.trim().equalsIgnoreCase("") || filter.trim().equalsIgnoreCase("")) ? "" : " AND B.BOWLERNAME LIKE '%" + filter + "%'";
        String selectQuery = append.append(str).append(" GROUP BY B.BOWLERNAME").toString();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                dtoOverallBowlerStats stat = new dtoOverallBowlerStats();
                stat.setBowlerName(cursor.getString(0));
                stat.setMatches(cursor.getInt(1));
                stat.setOvers(cursor.getInt(2));
                stat.setBalls(cursor.getInt(3));
                stat.setRuns(cursor.getInt(4));
                stat.setWides(cursor.getInt(5));
                stat.setNoballs(cursor.getInt(6));
                stat.setWickets(cursor.getInt(7));
                stat.setMaidens(cursor.getInt(8));
                stat.setDots(cursor.getInt(9));
                stat.setSixes(cursor.getInt(10));
                stat.setFours(cursor.getInt(11));
                stat.setBest(cursor.getDouble(12));
                stat.setInnings(cursor.getInt(13));
                stat.setFiveWktsHaul(cursor.isNull(14) ? 0 : cursor.getInt(14));
                stat.hasDetails(true);
                stat.setShowDetails(true);
                list.add(stat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public String getMatchAsXml(String matchID) {
        StringBuilder returnXml = new StringBuilder();
        String selectQuery = "SELECT * FROM MatchMaster WHERE MatchID = ?";
        String[] parameters = null;
        if (!matchID.trim().equalsIgnoreCase("")) {
            parameters = new String[]{matchID};
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, parameters);
        returnXml.append("<MatchMaster>");
        if (cursor.moveToFirst()) {
            returnXml.append(Utility.Tag(MATCH_DATETIME, "" + cursor.getString(cursor.getColumnIndex(MATCH_DATETIME))));
            returnXml.append(Utility.Tag(VENUE, "" + cursor.getString(cursor.getColumnIndex(VENUE))));
            returnXml.append(Utility.Tag(TEAM1_NAME, "" + cursor.getString(cursor.getColumnIndex(TEAM1_NAME))));
            returnXml.append(Utility.Tag(TEAM2_NAME, "" + cursor.getString(cursor.getColumnIndex(TEAM2_NAME))));
            returnXml.append(Utility.Tag(TOSS_WON_BY, "" + cursor.getString(cursor.getColumnIndex(TOSS_WON_BY))));
            returnXml.append(Utility.Tag(OPTED_TO, "" + cursor.getString(cursor.getColumnIndex(OPTED_TO))));
            returnXml.append(Utility.Tag(MATCH_OVERS, "" + cursor.getString(cursor.getColumnIndex(MATCH_OVERS))));
            returnXml.append(Utility.Tag(WIDE_BALL, "" + cursor.getString(cursor.getColumnIndex(WIDE_BALL))));
            returnXml.append(Utility.Tag(WIDE_BALL_REBALL, "" + cursor.getString(cursor.getColumnIndex(WIDE_BALL_REBALL))));
            returnXml.append(Utility.Tag(NO_BALL, "" + cursor.getString(cursor.getColumnIndex(NO_BALL))));
            returnXml.append(Utility.Tag(NO_BALL_REBALL, "" + cursor.getString(cursor.getColumnIndex(NO_BALL_REBALL))));
            returnXml.append(Utility.Tag(LEG_BYES, "" + cursor.getString(cursor.getColumnIndex(LEG_BYES))));
            returnXml.append(Utility.Tag(BYES, "" + cursor.getString(cursor.getColumnIndex(BYES))));
            returnXml.append(Utility.Tag(LBW, "" + cursor.getString(cursor.getColumnIndex(LBW))));
            returnXml.append(Utility.Tag(WAGON_WHEEL, "" + cursor.getString(cursor.getColumnIndex(WAGON_WHEEL))));
            returnXml.append(Utility.Tag(BALL_SPOT, "" + cursor.getString(cursor.getColumnIndex(BALL_SPOT))));
            returnXml.append(Utility.Tag(PRESET_TEAMS, "" + cursor.getString(cursor.getColumnIndex(PRESET_TEAMS))));
            returnXml.append(Utility.Tag(WIDE_RUN, "" + cursor.getString(cursor.getColumnIndex(WIDE_RUN))));
            returnXml.append(Utility.Tag(NOBALL_RUN, "" + cursor.getString(cursor.getColumnIndex(NOBALL_RUN))));
            returnXml.append(Utility.Tag(RESTRICT_BALLSPEROVER, "" + cursor.getString(cursor.getColumnIndex(RESTRICT_BALLSPEROVER))));
            returnXml.append(Utility.Tag(MAX_BALLSPEROVER, "" + cursor.getString(cursor.getColumnIndex(MAX_BALLSPEROVER))));
            returnXml.append(Utility.Tag(BALLSPEROVER, "" + cursor.getString(cursor.getColumnIndex(BALLSPEROVER))));
            returnXml.append(Utility.Tag(WW_FOR_DOT, "" + cursor.getString(cursor.getColumnIndex(WW_FOR_DOT))));
            returnXml.append(Utility.Tag(WW_FOR_WICKET, "" + cursor.getString(cursor.getColumnIndex(WW_FOR_WICKET))));
            returnXml.append(Utility.Tag(MATCH_NAME, "" + cursor.getString(cursor.getColumnIndex(MATCH_NAME))));
            returnXml.append(Utility.Tag(NO_OF_INNGS, "" + cursor.getString(cursor.getColumnIndex(NO_OF_INNGS))));
            returnXml.append(Utility.Tag(NO_OF_DAYS, "" + cursor.getString(cursor.getColumnIndex(NO_OF_DAYS))));
        }
        cursor.close();
        returnXml.append("</MatchMaster>");
        selectQuery = "SELECT * FROM MatchTeams WHERE MatchID = ? AND Team = ?";
        cursor = db.rawQuery(selectQuery, new String[]{matchID, "1"});
        returnXml.append("<Teams>");
        returnXml.append("<Team1>");
        if (cursor.moveToFirst()) {
            do {
                returnXml.append(Utility.Tag(PLAYER_NAME, "" + cursor.getString(cursor.getColumnIndex(PLAYER_NAME))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        returnXml.append("</Team1>");
        cursor = db.rawQuery(selectQuery, new String[]{matchID, "2"});
        returnXml.append("<Team2>");
        if (cursor.moveToFirst()) {
            do {
                returnXml.append(Utility.Tag(PLAYER_NAME, "" + cursor.getString(cursor.getColumnIndex(PLAYER_NAME))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        returnXml.append("</Team2>");
        returnXml.append("</Teams>");
        cursor = db.rawQuery("SELECT * FROM MatchScoreBoard WHERE MatchID = ?", new String[]{matchID});
        returnXml.append("<Scorecard>");
        if (cursor.moveToFirst()) {
            do {
                returnXml.append("<Ball>");
                returnXml.append(Utility.Tag(TEAM_NO, "" + cursor.getString(cursor.getColumnIndex(TEAM_NO))));
                returnXml.append(Utility.Tag(TOTAL_BALL_NO, "" + cursor.getString(cursor.getColumnIndex(TOTAL_BALL_NO))));
                returnXml.append(Utility.Tag(INNG_NO, "" + cursor.getString(cursor.getColumnIndex(INNG_NO))));
                returnXml.append(Utility.Tag(DAY_NO, "" + cursor.getString(cursor.getColumnIndex(DAY_NO))));
                returnXml.append(Utility.Tag(OVER_NO, "" + cursor.getString(cursor.getColumnIndex(OVER_NO))));
                returnXml.append(Utility.Tag(BALL_NO, "" + cursor.getString(cursor.getColumnIndex(BALL_NO))));
                returnXml.append(Utility.Tag(STRIKER_NAME, "" + cursor.getString(cursor.getColumnIndex(STRIKER_NAME))));
                returnXml.append(Utility.Tag(NONSTRIKER_NAME, "" + cursor.getString(cursor.getColumnIndex(NONSTRIKER_NAME))));
                returnXml.append(Utility.Tag(BOWLER_NAME, "" + cursor.getString(cursor.getColumnIndex(BOWLER_NAME))));
                returnXml.append(Utility.Tag(IS_WIDE, "" + cursor.getString(cursor.getColumnIndex(IS_WIDE))));
                returnXml.append(Utility.Tag(IS_NOBALL, "" + cursor.getString(cursor.getColumnIndex(IS_NOBALL))));
                returnXml.append(Utility.Tag(IS_LEGBYES, "" + cursor.getString(cursor.getColumnIndex(IS_LEGBYES))));
                returnXml.append(Utility.Tag(IS_BYES, "" + cursor.getString(cursor.getColumnIndex(IS_BYES))));
                returnXml.append(Utility.Tag(IS_WICKET, "" + cursor.getString(cursor.getColumnIndex(IS_WICKET))));
                returnXml.append(Utility.Tag(WICKET_HOW, "" + cursor.getString(cursor.getColumnIndex(WICKET_HOW))));
                returnXml.append(Utility.Tag(WICKET_ASSIST, "" + cursor.getString(cursor.getColumnIndex(WICKET_ASSIST))));
                returnXml.append(Utility.Tag(RUN_SCORED, "" + cursor.getString(cursor.getColumnIndex(RUN_SCORED))));
                returnXml.append(Utility.Tag(RUNS_THIS_BALL, "" + cursor.getString(cursor.getColumnIndex(RUNS_THIS_BALL))));
                returnXml.append(Utility.Tag(BALL_BY_BALL, "" + cursor.getString(cursor.getColumnIndex(BALL_BY_BALL))));
                returnXml.append(Utility.Tag(TOTAL_SCORE, "" + cursor.getString(cursor.getColumnIndex(TOTAL_SCORE))));
                returnXml.append(Utility.Tag(WICKET_COUNT, "" + cursor.getString(cursor.getColumnIndex(WICKET_COUNT))));
                returnXml.append(Utility.Tag(STRIKER_RUNS, "" + cursor.getString(cursor.getColumnIndex(STRIKER_RUNS))));
                returnXml.append(Utility.Tag(NONSTRIKER_RUNS, "" + cursor.getString(cursor.getColumnIndex(NONSTRIKER_RUNS))));
                returnXml.append(Utility.Tag(STRIKER_BALLS, "" + cursor.getString(cursor.getColumnIndex(STRIKER_BALLS))));
                returnXml.append(Utility.Tag(NONSTRIKER_BALLS, "" + cursor.getString(cursor.getColumnIndex(NONSTRIKER_BALLS))));
                returnXml.append(Utility.Tag(TARGET, "" + cursor.getString(cursor.getColumnIndex(TARGET))));
                returnXml.append(Utility.Tag(WW_X_CORD, "" + cursor.getString(cursor.getColumnIndex(WW_X_CORD))));
                returnXml.append(Utility.Tag(WW_Y_CORD, "" + cursor.getString(cursor.getColumnIndex(WW_Y_CORD))));
                returnXml.append(Utility.Tag(BS_X_CORD, "" + cursor.getString(cursor.getColumnIndex(BS_X_CORD))));
                returnXml.append(Utility.Tag(BS_Y_CORD, "" + cursor.getString(cursor.getColumnIndex(BS_Y_CORD))));
                returnXml.append(Utility.Tag(TOTAL_EXTRAS, "" + cursor.getString(cursor.getColumnIndex(TOTAL_EXTRAS))));
                returnXml.append(Utility.Tag(BALLS_THIS_OVER, "" + cursor.getString(cursor.getColumnIndex(BALLS_THIS_OVER))));
                returnXml.append(Utility.Tag(FLAG_EOD, "" + cursor.getString(cursor.getColumnIndex(FLAG_EOD))));
                returnXml.append(Utility.Tag(FLAG_EOI, "" + cursor.getString(cursor.getColumnIndex(FLAG_EOI))));
                returnXml.append(Utility.Tag(EOI_DESC, "" + cursor.getString(cursor.getColumnIndex(EOI_DESC))));
                returnXml.append("</Ball>");
            } while (cursor.moveToNext());
        }
        cursor.close();
        returnXml.append("</Scorecard>");
        db.close();
        return returnXml.toString();
    }

    public List<dtoMatch> createMatchMasterFromXml(StringBuilder xml) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new ByteArrayInputStream(xml.toString().getBytes()), null);
        String BCS = "BCS";
        String MATCH = "Match";
        String MATCH_MASTER = TABLE_MATCH_MASTER;
        List<dtoMatch> matches = new ArrayList();
        dtoMatch currentMatch = null;
        dtoAdditionalSettings currentAdditionalSettings = null;
        String matchID = "";
        boolean done = false;
        String tagName = "";
        for (int eventType = parser.getEventType(); eventType != 1 && !done; eventType = parser.next()) {
            tagName = parser.getName();
            switch (eventType) {
                case 2:
                    if (tagName.equals("BCS")) {
                    }
                    if (tagName.equals("Match")) {
                        currentMatch = new dtoMatch();
                        currentAdditionalSettings = new dtoAdditionalSettings();
                        currentMatch.setAdditionalSettings(currentAdditionalSettings);
                        currentMatch.setMatchID(parser.getAttributeValue(0).toString());
                    }
                    if (tagName.equals(TABLE_MATCH_MASTER)) {
                    }
                    if (tagName.equals(MATCH_DATETIME)) {
                        currentMatch.setDateTime(parser.nextText());
                    }
                    if (tagName.equals(VENUE)) {
                        currentMatch.setVenue(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(TEAM1_NAME)) {
                        currentMatch.setTeam1Name(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(TEAM2_NAME)) {
                        currentMatch.setTeam2Name(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(TOSS_WON_BY)) {
                        currentMatch.setTossWonBy(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(OPTED_TO)) {
                        currentMatch.setOptedTo(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(MATCH_OVERS)) {
                        currentMatch.setOvers(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WIDE_BALL)) {
                        currentAdditionalSettings.setWide(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WIDE_BALL_REBALL)) {
                        currentAdditionalSettings.setWideReball(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NO_BALL)) {
                        currentAdditionalSettings.setNoball(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NO_BALL_REBALL)) {
                        currentAdditionalSettings.setNoballReball(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(LEG_BYES)) {
                        currentAdditionalSettings.setLegbyes(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BYES)) {
                        currentAdditionalSettings.setByes(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(LBW)) {
                        currentAdditionalSettings.setLbw(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WAGON_WHEEL)) {
                        currentAdditionalSettings.setWagonWheel(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BALL_SPOT)) {
                        currentAdditionalSettings.setBallSpot(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(PRESET_TEAMS)) {
                        currentAdditionalSettings.setPresetTeams(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WIDE_RUN)) {
                        currentAdditionalSettings.setWideRun(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NOBALL_RUN)) {
                        currentAdditionalSettings.setNoballRun(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(RESTRICT_BALLSPEROVER)) {
                        currentAdditionalSettings.setRestrictBpo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(MAX_BALLSPEROVER)) {
                        currentAdditionalSettings.setMaxBpo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BALLSPEROVER)) {
                        currentAdditionalSettings.setBpo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WW_FOR_DOT)) {
                        currentAdditionalSettings.setWwForDotBall(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WW_FOR_WICKET)) {
                        currentAdditionalSettings.setWwForWicket(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(MATCH_NAME)) {
                        currentMatch.setMatchName(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(NO_OF_INNGS)) {
                        currentMatch.setNoOfInngs(Integer.parseInt(parser.nextText()));
                    }
                    if (!tagName.equals(NO_OF_DAYS)) {
                        break;
                    }
                    currentMatch.setNoOfDays(Integer.parseInt(parser.nextText()));
                    break;
                case 3:
                    if (tagName.equals("BCS")) {
                        done = true;
                    }
                    if (!tagName.equals("Match")) {
                        break;
                    }
                    matches.add(currentMatch);
                    break;
                default:
                    break;
            }
        }
        return matches;
    }

    public List<dtoMatch> createMatchFromXml(StringBuilder xml) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new ByteArrayInputStream(xml.toString().getBytes()), null);
        String BCS = "BCS";
        String MATCH = "Match";
        String MATCH_MASTER = TABLE_MATCH_MASTER;
        String BALL = "Ball";
        String TEAM1 = "Team1";
        String TEAM2 = "Team2";
        boolean isTeam1 = false;
        boolean isTeam2 = false;
        List<dtoMatch> matches = new ArrayList();
        dtoMatch currentMatch = null;
        dtoAdditionalSettings currentAdditionalSettings = null;
        dtoScoreBall scoreBall = null;
        List<dtoScoreBall> team1ScoreBalls = new ArrayList();
        List<dtoScoreBall> team2ScoreBalls = new ArrayList();
        String matchID = "";
        boolean done = false;
        String tagName = "";
        for (int eventType = parser.getEventType(); eventType != 1 && !done; eventType = parser.next()) {
            tagName = parser.getName();
            switch (eventType) {
                case 2:
                    String tempValue;
                    if (tagName.equals("BCS")) {
                    }
                    if (tagName.equals("Match")) {
                        currentMatch = new dtoMatch();
                        currentAdditionalSettings = new dtoAdditionalSettings();
                        currentMatch.setAdditionalSettings(currentAdditionalSettings);
                        team1ScoreBalls = new ArrayList();
                        team2ScoreBalls = new ArrayList();
                        currentMatch.setTeam1Scorecard(team1ScoreBalls);
                        currentMatch.setTeam2Scorecard(team2ScoreBalls);
                        currentMatch.setMatchID(parser.getAttributeValue(0).toString());
                    }
                    if (tagName.equals(TABLE_MATCH_MASTER)) {
                    }
                    if (tagName.equals(MATCH_DATETIME)) {
                        currentMatch.setDateTime(parser.nextText());
                    }
                    if (tagName.equals(VENUE)) {
                        currentMatch.setVenue(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(TEAM1_NAME)) {
                        currentMatch.setTeam1Name(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(TEAM2_NAME)) {
                        currentMatch.setTeam2Name(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(TOSS_WON_BY)) {
                        currentMatch.setTossWonBy(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(OPTED_TO)) {
                        currentMatch.setOptedTo(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(MATCH_OVERS)) {
                        currentMatch.setOvers(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WIDE_BALL)) {
                        currentAdditionalSettings.setWide(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WIDE_BALL_REBALL)) {
                        currentAdditionalSettings.setWideReball(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NO_BALL)) {
                        currentAdditionalSettings.setNoball(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NO_BALL_REBALL)) {
                        currentAdditionalSettings.setNoballReball(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(LEG_BYES)) {
                        currentAdditionalSettings.setLegbyes(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BYES)) {
                        currentAdditionalSettings.setByes(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(LBW)) {
                        currentAdditionalSettings.setLbw(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WAGON_WHEEL)) {
                        currentAdditionalSettings.setWagonWheel(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BALL_SPOT)) {
                        currentAdditionalSettings.setBallSpot(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(PRESET_TEAMS)) {
                        currentAdditionalSettings.setPresetTeams(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WIDE_RUN)) {
                        currentAdditionalSettings.setWideRun(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NOBALL_RUN)) {
                        currentAdditionalSettings.setNoballRun(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(RESTRICT_BALLSPEROVER)) {
                        currentAdditionalSettings.setRestrictBpo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(MAX_BALLSPEROVER)) {
                        currentAdditionalSettings.setMaxBpo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BALLSPEROVER)) {
                        currentAdditionalSettings.setBpo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WW_FOR_DOT)) {
                        currentAdditionalSettings.setWwForDotBall(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WW_FOR_WICKET)) {
                        currentAdditionalSettings.setWwForWicket(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(MATCH_NAME)) {
                        currentMatch.setMatchName(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(NO_OF_INNGS)) {
                        currentMatch.setNoOfInngs(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NO_OF_DAYS)) {
                        currentMatch.setNoOfDays(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals("Team1")) {
                        isTeam1 = true;
                        isTeam2 = false;
                    }
                    if (tagName.equals("Team2")) {
                        isTeam2 = true;
                        isTeam1 = false;
                    }
                    if (tagName.equals(PLAYER_NAME)) {
                        if (isTeam1) {
                            currentMatch.addTeam1Player(parser.nextText());
                        } else if (isTeam2) {
                            currentMatch.addTeam2Player(parser.nextText());
                        }
                    }
                    if (tagName.equals("Ball")) {
                        scoreBall = new dtoScoreBall();
                    }
                    if (tagName.equals(TEAM_NO)) {
                        scoreBall.setTeamNo(parser.nextText());
                    }
                    if (tagName.equals(TOTAL_BALL_NO)) {
                        scoreBall.setTotalBallNo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(INNG_NO)) {
                        scoreBall.setInningNo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(DAY_NO)) {
                        scoreBall.setDayNo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(OVER_NO)) {
                        scoreBall.setOverNo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BALL_NO)) {
                        scoreBall.setBallNo(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(STRIKER_NAME)) {
                        scoreBall.setStrikerName(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(NONSTRIKER_NAME)) {
                        scoreBall.setNonStrikerName(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(BOWLER_NAME)) {
                        scoreBall.setBowlerName(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(IS_WIDE)) {
                        scoreBall.setIsWide(parser.nextText().equalsIgnoreCase("1"));
                    }
                    if (tagName.equals(IS_NOBALL)) {
                        scoreBall.setIsNoball(parser.nextText().equalsIgnoreCase("1"));
                    }
                    if (tagName.equals(IS_LEGBYES)) {
                        scoreBall.setIsLegByes(parser.nextText().equalsIgnoreCase("1"));
                    }
                    if (tagName.equals(IS_BYES)) {
                        scoreBall.setIsByes(parser.nextText().equalsIgnoreCase("1"));
                    }
                    if (tagName.equals(IS_WICKET)) {
                        scoreBall.setIsWicket(parser.nextText().equalsIgnoreCase("1"));
                    }
                    if (tagName.equals(WICKET_HOW)) {
                        scoreBall.setWicketHow(Utility.fromHtml("" + parser.nextText()).toString());
                    }
                    if (tagName.equals(WICKET_ASSIST)) {
                        scoreBall.setWicketAssist(Utility.fromHtml("" + parser.nextText()).toString());
                    }
                    if (tagName.equals(RUN_SCORED)) {
                        scoreBall.setRunsScored(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(RUNS_THIS_BALL)) {
                        scoreBall.setRunsThisBall(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BALL_BY_BALL)) {
                        scoreBall.setBallByBall(parser.nextText());
                    }
                    if (tagName.equals(TOTAL_SCORE)) {
                        scoreBall.setTotalScore(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WICKET_COUNT)) {
                        scoreBall.setWicketCount(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(STRIKER_RUNS)) {
                        scoreBall.setStrikerRuns(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NONSTRIKER_RUNS)) {
                        scoreBall.setNonStrikerRuns(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(STRIKER_BALLS)) {
                        scoreBall.setBallsFacedStriker(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(NONSTRIKER_BALLS)) {
                        scoreBall.setBallsFacedNonStriker(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(TARGET)) {
                        scoreBall.setTarget(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WW_X_CORD)) {
                        scoreBall.setXCord(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WW_Y_CORD)) {
                        scoreBall.setYCord(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BS_X_CORD)) {
                        scoreBall.setbsXCord(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BS_Y_CORD)) {
                        scoreBall.setbsYCord(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(TOTAL_EXTRAS)) {
                        scoreBall.setTotalExtras(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(BALLS_THIS_OVER)) {
                        scoreBall.setBallsThisOver(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(FLAG_EOD)) {
                        tempValue = ("" + parser.nextText()).trim();
                        if (tempValue.equalsIgnoreCase("null") || tempValue.equalsIgnoreCase("")) {
                            scoreBall.setFlagEOD(0);
                        } else {
                            scoreBall.setFlagEOD(Integer.parseInt(tempValue));
                        }
                    }
                    if (tagName.equals(FLAG_EOI)) {
                        tempValue = ("" + parser.nextText()).trim();
                        if (tempValue.equalsIgnoreCase("null") || tempValue.equalsIgnoreCase("")) {
                            scoreBall.setFlagEOI(0);
                        } else {
                            scoreBall.setFlagEOI(Integer.parseInt(tempValue));
                        }
                    }
                    if (tagName.equals(EOI_DESC)) {
                        scoreBall.setEOIDescription(Utility.fromHtml("" + parser.nextText()).toString());
                    }
                    if (!tagName.equals(IS_PENALTY)) {
                        break;
                    }
                    scoreBall.setIsPenalty(Integer.parseInt(parser.nextText()));
                    break;
                case 3:
                    if (tagName.equals("BCS")) {
                        done = true;
                    }
                    if (tagName.equals("Ball")) {
                        if (scoreBall.getTeamNo().equalsIgnoreCase("1")) {
                            team1ScoreBalls.add(scoreBall);
                        } else if (scoreBall.getTeamNo().equalsIgnoreCase("2")) {
                            team2ScoreBalls.add(scoreBall);
                        }
                    }
                    if (!tagName.equals("Match")) {
                        break;
                    }
                    matches.add(currentMatch);
                    break;
                default:
                    break;
            }
        }
        return matches;
    }

    public String getTeamPlayersAsXml(String teamName) {
        StringBuilder returnXml = new StringBuilder();
        Iterator it = getManageTeamPlayersList(teamName).iterator();
        while (it.hasNext()) {
            dtoTeamPlayer player = (dtoTeamPlayer) it.next();
            returnXml.append("<Player>");
            returnXml.append(Utility.Tag(TEAM_NAME, player.getTeamName()));
            returnXml.append(Utility.Tag(PLAYER_NAME, player.getPlayerName()));
            returnXml.append(Utility.Tag(PLAYER_ORDER, "" + player.getPlayerOrder()));
            returnXml.append(Utility.Tag(HANDED_BAT, "" + player.getHandedBat()));
            returnXml.append(Utility.Tag(HANDED_BOWL, "" + player.getHandedBowl()));
            returnXml.append(Utility.Tag(WHAT_BOWLER, "" + player.getWhatBowler()));
            returnXml.append(Utility.Tag(IS_BATSMAN, "" + player.getIsBatsman()));
            returnXml.append(Utility.Tag(IS_BOWLER, "" + player.getIsBowler()));
            returnXml.append(Utility.Tag(IS_CAPTAIN, "" + player.getIsCaptain()));
            returnXml.append(Utility.Tag(IS_KEEPER, "" + player.getIsKeeper()));
            returnXml.append("</Player>");
        }
        return returnXml.toString();
    }

    public dtoTeamPlayerList createTeamFromXml(StringBuilder xml) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new ByteArrayInputStream(xml.toString().getBytes()), null);
        String BCS = "BCS";
        String TEAM = TEAM_NO;
        String PLAYER = "Player";
        dtoTeamPlayerList teams = new dtoTeamPlayerList();
        dtoTeamPlayer currentPlayer = new dtoTeamPlayer();
        boolean done = false;
        String tagName = "";
        for (int eventType = parser.getEventType(); eventType != 1 && !done; eventType = parser.next()) {
            tagName = parser.getName();
            switch (eventType) {
                case 2:
                    if (tagName.equals("BCS")) {
                    }
                    if (tagName.equals(TEAM_NO)) {
                    }
                    if (tagName.equals("Player")) {
                        currentPlayer = new dtoTeamPlayer();
                    }
                    if (tagName.equals(TEAM_NAME)) {
                        currentPlayer.setTeamName(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(PLAYER_NAME)) {
                        currentPlayer.setPlayerName(Utility.fromHtml(parser.nextText()).toString());
                    }
                    if (tagName.equals(PLAYER_ORDER)) {
                        currentPlayer.setPlayerOrder(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(HANDED_BAT)) {
                        currentPlayer.setHandedBat(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(HANDED_BOWL)) {
                        currentPlayer.setHandedBowl(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(WHAT_BOWLER)) {
                        currentPlayer.setWhatBowler(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(IS_BATSMAN)) {
                        currentPlayer.setIsBatsman(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(IS_BOWLER)) {
                        currentPlayer.setIsBowler(Integer.parseInt(parser.nextText()));
                    }
                    if (tagName.equals(IS_CAPTAIN)) {
                        currentPlayer.setIsCaptain(Integer.parseInt(parser.nextText()));
                    }
                    if (!tagName.equals(IS_KEEPER)) {
                        break;
                    }
                    currentPlayer.setIsKeeper(Integer.parseInt(parser.nextText()));
                    break;
                case 3:
                    if (tagName.equals("BCS")) {
                        done = true;
                    }
                    if (!tagName.equals("Player")) {
                        break;
                    }
                    teams.add(currentPlayer);
                    break;
                default:
                    break;
            }
        }
        return teams;
    }

    public boolean checkOversExists(String matchID, int editOvers) {
        int overs = 1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Max(OverNo) FROM MatchScoreBoard WHERE MatchID = ?", new String[]{matchID});
        if (cursor.moveToFirst()) {
            overs = 1 + cursor.getInt(0);
        }
        cursor.close();
        db.close();
        if (overs > editOvers) {
            return true;
        }
        return false;
    }

    public void updateMatchOvers(String matchID, int editOvers) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(MATCH_OVERS, Integer.valueOf(editOvers));
        db.update(TABLE_MATCH_MASTER, args, "MatchID = ?", new String[]{matchID});
        db.close();
    }

    public void updateMatchWagonWheelSetting(String matchID, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(WAGON_WHEEL, Integer.valueOf(value));
        db.update(TABLE_MATCH_MASTER, args, "MatchID = ?", new String[]{matchID});
        db.close();
    }

    public void updateMatchBallSpotSetting(String matchID, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(BALL_SPOT, Integer.valueOf(value));
        db.update(TABLE_MATCH_MASTER, args, "MatchID = ?", new String[]{matchID});
        db.close();
    }

    public void updateMatchResult(String matchID, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(MATCH_RESULT, value);
        db.update(TABLE_MATCH_MASTER, args, "MatchID = ?", new String[]{matchID});
        db.close();
    }

    public void clearMatchResult(String matchID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(MATCH_RESULT, "");
        db.update(TABLE_MATCH_MASTER, args, "MatchID = ?", new String[]{matchID});
        db.close();
    }

    public String getTeamScore(String matchID, String team, String inngNo) {
        String result = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(TOTALSCORE), SUM(ISWICKET) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ?", new String[]{matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            int score = cursor.getInt(0);
            result = "" + score + "/" + cursor.getInt(1);
        }
        cursor.close();
        db.close();
        return result;
    }

    public void replaceBatsman(String matchID, String team, String inng, String fromPlayer, String toPlayer) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT IFNULL(MAX(TOTALBALLNO), 0) + 1 FirstBallFaced from MATCHSCOREBOARD where MATCHID = ? AND TEAM = ? AND INNGNO = ? AND (STRIKERNAME NOT LIKE ? AND NONSTRIKERNAME NOT LIKE ?)", new String[]{matchID, team, inng, fromPlayer, fromPlayer});
        String fromBallToUpdate = "99999";
        if (cursor.moveToFirst()) {
            fromBallToUpdate = "" + cursor.getInt(0);
        }
        cursor.close();
        ContentValues args = new ContentValues();
        args.put(STRIKER_NAME, toPlayer);
        db.update(TABLE_MATCH_SCOREBOARD, args, "MATCHID = ? AND TEAM = ? AND INNGNO = ? AND STRIKERNAME LIKE ? AND TOTALBALLNO >= ?", new String[]{matchID, team, inng, fromPlayer, fromBallToUpdate});
        args = new ContentValues();
        args.put(NONSTRIKER_NAME, toPlayer);
        db.update(TABLE_MATCH_SCOREBOARD, args, "MATCHID = ? AND TEAM = ? AND INNGNO = ? AND NONSTRIKERNAME LIKE ? AND TOTALBALLNO >= ?", new String[]{matchID, team, inng, fromPlayer, fromBallToUpdate});
        db.close();
    }

    public void replaceBowler(String matchID, String team, String inng, String fromPlayer, String toPlayer) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT IFNULL(MAX(TOTALBALLNO), 0) + 1 FirstBallBowled from MATCHSCOREBOARD where MATCHID = ? AND TEAM = ? AND INNGNO = ? AND BOWLERNAME NOT LIKE ?", new String[]{matchID, team, inng, fromPlayer});
        String fromBallToUpdate = "99999";
        if (cursor.moveToFirst()) {
            fromBallToUpdate = "" + cursor.getInt(0);
        }
        cursor.close();
        ContentValues args = new ContentValues();
        args.put(BOWLER_NAME, toPlayer);
        db.update(TABLE_MATCH_SCOREBOARD, args, "MATCHID = ? AND TEAM = ? AND INNGNO = ? AND BOWLERNAME LIKE ? AND TOTALBALLNO >= ?", new String[]{matchID, team, inng, fromPlayer, fromBallToUpdate});
        db.close();
    }

    public void replaceBowlerForOver(String matchID, String team, String inngNo, int overNo, String fromPlayer, String toPlayer) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(BOWLER_NAME, toPlayer);
        db.update(TABLE_MATCH_SCOREBOARD, args, "MATCHID = ? AND TEAM = ? AND INNGNO = ? AND BOWLERNAME LIKE ? AND OVERNO = ?", new String[]{matchID, team, inngNo, fromPlayer, "" + overNo});
        db.close();
    }

    public dtoScoreBall getMatchBall(String matchID, String totalBallNo) {
        int i = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MatchID, Team, TotalBallNo, OverNo, BallNo, StrikerName, NonStrikerName, BowlerName, IsWide, IsNoball, IsLegByes, IsByes, IsWicket, WicketHow, WicketAssist, RunScored, RunsThisBall, BallByBall, TotalScore, WicketCount, StrikerRuns, NonStrikerRuns, StrikerBallsFaced, NonStrikerBallsFaced, Target, TotalExtras, BallsThisOver, InngNo, DayNo, FEOD, FEOI, EOIDesc, IsPenalty FROM MatchScoreBoard WHERE MatchID LIKE ? AND TotalBallNo = ?", new String[]{matchID, totalBallNo});
        dtoScoreBall ball = new dtoScoreBall();
        if (cursor.moveToFirst()) {
            boolean z;
            ball.setTeamNo(cursor.getString(1));
            ball.setTotalBallNo(cursor.getInt(2));
            ball.setOverNo(cursor.getInt(3));
            ball.setBallNo(cursor.getInt(4));
            ball.setStrikerName(cursor.getString(5));
            ball.setNonStrikerName(cursor.getString(6));
            ball.setBowlerName(cursor.getString(7));
            ball.setIsWide(cursor.getInt(8) == 1);
            if (cursor.getInt(9) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsNoball(z);
            if (cursor.getInt(10) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsLegByes(z);
            if (cursor.getInt(11) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsByes(z);
            if (cursor.getInt(12) == 1) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsWicket(z);
            if ((((ball.getIsByes() | ball.getIsLegByes()) | ball.getIsNoball()) | ball.getIsWide()) == 0) {
                z = true;
            } else {
                z = false;
            }
            ball.setIsStrike(z);
            ball.setWicketHow(cursor.getString(13));
            ball.setWicketAssist(cursor.getString(14));
            ball.setRunsScored(cursor.getInt(15));
            ball.setRunsThisBall(cursor.getInt(16));
            ball.setBallByBall(cursor.getString(17));
            ball.setTotalScore(cursor.getInt(18));
            ball.setWicketCount(cursor.getInt(19));
            ball.setStrikerRuns(cursor.getInt(20));
            ball.setNonStrikerRuns(cursor.getInt(21));
            ball.setBallsFacedStriker(cursor.getInt(22));
            ball.setBallsFacedNonStriker(cursor.getInt(23));
            ball.setTarget(cursor.getInt(24));
            ball.setTotalExtras(cursor.getInt(25));
            ball.setBallsThisOver(cursor.getInt(26));
            if (cursor.isNull(27)) {
                ball.setInningNo(1);
            } else {
                ball.setInningNo(cursor.getInt(27));
            }
            if (cursor.isNull(28)) {
                ball.setDayNo(1);
            } else {
                ball.setDayNo(cursor.getInt(28));
            }
            ball.setFlagEOD(cursor.isNull(29) ? 0 : cursor.getInt(29));
            ball.setFlagEOI(cursor.isNull(30) ? 0 : cursor.getInt(30));
            ball.setEOIDescription(cursor.getString(31));
            if (!cursor.isNull(32)) {
                i = cursor.getInt(32);
            }
            ball.setIsPenalty(i);
        }
        cursor.close();
        db.close();
        return ball;
    }

    public boolean updateMatchScoreBall(String matchID, dtoScoreBall ball) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STRIKER_NAME, ball.getStrikerName());
        values.put(NONSTRIKER_NAME, ball.getNonStrikerName());
        values.put(BOWLER_NAME, ball.getBowlerName());
        values.put(IS_WIDE, Boolean.valueOf(ball.getIsWide()));
        values.put(IS_NOBALL, Boolean.valueOf(ball.getIsNoball()));
        values.put(IS_LEGBYES, Boolean.valueOf(ball.getIsLegByes()));
        values.put(IS_BYES, Boolean.valueOf(ball.getIsByes()));
        values.put(RUN_SCORED, Integer.valueOf(ball.getRunsScored()));
        values.put(RUNS_THIS_BALL, Integer.valueOf(ball.getRunsThisBall()));
        values.put(BALL_BY_BALL, ball.getBallByBall());
        values.put(TOTAL_SCORE, Integer.valueOf(ball.getTotalScore()));
        values.put(STRIKER_RUNS, Integer.valueOf(ball.getStrikerRuns()));
        values.put(NONSTRIKER_RUNS, Integer.valueOf(ball.getNonStrikerRuns()));
        values.put(STRIKER_BALLS, Integer.valueOf(ball.getBallsFacedStriker()));
        values.put(NONSTRIKER_BALLS, Integer.valueOf(ball.getBallsFacedNonStriker()));
        values.put(TOTAL_EXTRAS, Integer.valueOf(ball.getTotalExtras()));
        values.put(BALLS_THIS_OVER, Integer.valueOf(ball.getBallsThisOver()));
        values.put(INNG_NO, Integer.valueOf(ball.getInningNo()));
        values.put(DAY_NO, Integer.valueOf(ball.getDayNo()));
        values.put(FLAG_EOD, Integer.valueOf(ball.getFlagEOD()));
        values.put(FLAG_EOI, Integer.valueOf(ball.getFlagEOI()));
        values.put(EOI_DESC, ball.getEOIDescription());
        values.put(IS_PENALTY, Integer.valueOf(ball.getIsPenalty()));
        values.put(WICKET_HOW, ball.getWicketHow());
        values.put(WICKET_ASSIST, ball.getWicketAssist());
        long retValue = (long) db.update(TABLE_MATCH_SCOREBOARD, values, "MATCHID = ? AND TOTALBALLNO = ?", new String[]{matchID, "" + ball.getTotalBallNo()});
        db.close();
        if (retValue < 0) {
            return true;
        }
        return false;
    }

    public void updateTarget(String matchID, int inngNo, String team, int totalScore) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TARGET, Integer.valueOf(totalScore));
        db.update(TABLE_MATCH_SCOREBOARD, values, "MATCHID = ? AND TEAM = ? AND INNGNO = ?", new String[]{matchID, team, "" + inngNo});
        db.close();
    }

    public void updateBallByBallForOver(String matchID, dtoScoreBall ball) {
        HashMap<Integer, String> balls = new HashMap();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT TotalBallNo, IsWide, IsNoball, IsLegByes, IsByes, IsWicket, RunScored, RunsThisBall, IsPenalty FROM MatchScoreBoard WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ? AND OVERNO = ? ORDER BY TOTALBALLNO", new String[]{matchID, ball.getTeamNo(), "" + ball.getInningNo(), "" + ball.getOverNo()});
        if (cursor.moveToFirst()) {
            do {
                String suffix = "";
                int score = cursor.getInt(6);
                if (cursor.getInt(1) == 1) {
                    suffix = "wd";
                }
                if (cursor.getInt(2) == 1) {
                    suffix = "nb";
                }
                if (cursor.getInt(3) == 1) {
                    suffix = "lb";
                }
                if (cursor.getInt(4) == 1) {
                    suffix = "b";
                }
                if (cursor.getInt(5) == 1) {
                    if (cursor.getInt(2) == 1 || cursor.getInt(1) == 1) {
                        suffix = suffix + "+W";
                    } else if ((cursor.getInt(3) == 1 || cursor.getInt(4) == 1) && score > 0) {
                        suffix = suffix + "+W";
                    } else {
                        suffix = "W";
                    }
                }
                if (score != 0) {
                    suffix = "" + score + suffix;
                } else if (suffix.trim().equalsIgnoreCase("")) {
                    suffix = "-";
                }
                balls.put(Integer.valueOf(cursor.getInt(0)), suffix.trim());
            } while (cursor.moveToNext());
        }
        cursor.close();
        SQLiteDatabase db = getWritableDatabase();
        String newBallText = "";
        for (Integer key : new TreeSet(balls.keySet())) {
            ContentValues values = new ContentValues();
            newBallText = (newBallText.trim().equalsIgnoreCase("") ? "" : newBallText + " ") + ((String) balls.get(key));
            values.put(BALL_BY_BALL, newBallText);
            db.update(TABLE_MATCH_SCOREBOARD, values, "MATCHID = ? AND TOTALBALLNO = ?", new String[]{matchID, "" + key});
        }
        db.close();
    }

    public boolean insertMatchScoreBall(String matchID, int bpo, dtoScoreBall ball, boolean duplicate) {
        long retValue;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE MatchScoreBoard SET TotalBallNo = TotalBallNo+1 WHERE MatchID = '" + matchID + "' AND " + TOTAL_BALL_NO + " >= " + ball.getTotalBallNo());
        db.execSQL("UPDATE MatchScoreBoard SET BallsThisOver = BallsThisOver+1 WHERE MatchID = '" + matchID + "' AND " + TOTAL_BALL_NO + " >= " + ball.getTotalBallNo() + " AND " + INNG_NO + " = " + ball.getInningNo() + " AND " + OVER_NO + " = " + ball.getOverNo());
        boolean hasRow = false;
        Cursor cursor = db.rawQuery("SELECT TotalBallNo FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TotalBallNo = ?", new String[]{matchID, "" + ball.getTotalBallNo()});
        if (cursor.moveToFirst()) {
            hasRow = true;
        }
        cursor.close();
        if (hasRow) {
            retValue = -1;
        } else {
            ContentValues values = new ContentValues();
            values.put(MATCH_ID, matchID);
            values.put(TEAM_NO, ball.getTeamNo());
            values.put(TOTAL_BALL_NO, Integer.valueOf(ball.getTotalBallNo()));
            values.put(OVER_NO, Integer.valueOf(ball.getOverNo()));
            values.put(BALL_NO, Integer.valueOf(ball.getBallNo()));
            values.put(STRIKER_NAME, ball.getStrikerName());
            values.put(NONSTRIKER_NAME, ball.getNonStrikerName());
            values.put(BALL_NO, Integer.valueOf(ball.getBallNo()));
            values.put(BOWLER_NAME, ball.getBowlerName());
            values.put(IS_WIDE, Boolean.valueOf(ball.getIsWide()));
            values.put(IS_NOBALL, Boolean.valueOf(ball.getIsNoball()));
            values.put(IS_LEGBYES, Boolean.valueOf(ball.getIsLegByes()));
            values.put(IS_BYES, Boolean.valueOf(ball.getIsByes()));
            values.put(IS_WICKET, Boolean.valueOf(ball.getIsWicket()));
            values.put(WICKET_HOW, ball.getWicketHow());
            values.put(WICKET_ASSIST, ball.getWicketAssist());
            values.put(RUN_SCORED, Integer.valueOf(ball.getRunsScored()));
            values.put(RUNS_THIS_BALL, Integer.valueOf(ball.getRunsThisBall()));
            values.put(BALL_BY_BALL, ball.getBallByBall());
            values.put(TOTAL_SCORE, Integer.valueOf(ball.getTotalScore()));
            values.put(WICKET_COUNT, Integer.valueOf(ball.getWicketCount()));
            values.put(STRIKER_RUNS, Integer.valueOf(ball.getStrikerRuns()));
            values.put(NONSTRIKER_RUNS, Integer.valueOf(ball.getNonStrikerRuns()));
            values.put(STRIKER_BALLS, Integer.valueOf(ball.getBallsFacedStriker()));
            values.put(NONSTRIKER_BALLS, Integer.valueOf(ball.getBallsFacedNonStriker()));
            values.put(TARGET, Integer.valueOf(ball.getTarget()));
            values.put(TOTAL_EXTRAS, Integer.valueOf(ball.getTotalExtras()));
            values.put(WW_X_CORD, Integer.valueOf(ball.getXCord()));
            values.put(WW_Y_CORD, Integer.valueOf(ball.getYCord()));
            values.put(BS_X_CORD, Integer.valueOf(ball.getbsXCord()));
            values.put(BS_Y_CORD, Integer.valueOf(ball.getbsYCord()));
            values.put(BALLS_THIS_OVER, Integer.valueOf(ball.getBallsThisOver()));
            values.put(INNG_NO, Integer.valueOf(ball.getInningNo()));
            values.put(DAY_NO, Integer.valueOf(ball.getDayNo()));
            values.put(FLAG_EOD, Integer.valueOf(ball.getFlagEOD()));
            values.put(FLAG_EOI, Integer.valueOf(ball.getFlagEOI()));
            values.put(EOI_DESC, ball.getEOIDescription());
            values.put(EOI_DESC, Integer.valueOf(ball.getIsPenalty()));
            retValue = db.insert(TABLE_MATCH_SCOREBOARD, null, values);
            if (!duplicate) {
                db.execSQL("UPDATE MatchScoreBoard SET BallNo = Min(BallNo+1, " + bpo + ") WHERE " + MATCH_ID + " = '" + matchID + "' AND " + TOTAL_BALL_NO + " >= " + ball.getTotalBallNo() + " AND " + INNG_NO + " = " + ball.getInningNo() + " AND " + OVER_NO + " = " + ball.getOverNo());
            }
            db.execSQL("UPDATE MatchScoreBoard SET StrikerBallsFaced = StrikerBallsFaced+1 WHERE MatchID = '" + matchID + "' AND " + TOTAL_BALL_NO + " >= " + ball.getTotalBallNo() + " AND " + INNG_NO + " = " + ball.getInningNo() + " AND " + STRIKER_NAME + " = '" + ball.getStrikerName() + "'");
        }
        db.close();
        if (retValue < 0) {
            return true;
        }
        return false;
    }

    public int getFirstTotalBallNoOfInnings(String matchID, String team, String inngNo) {
        int result = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(TOTALBALLNO) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND INNGNO = ?", new String[]{matchID, team, inngNo});
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return result;
    }

    public int getLastTotalBallNoOfInnings(String matchID, String team) {
        int result = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(TOTALBALLNO) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ?", new String[]{matchID, team});
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean checkDuplicateScoreBall(String matchID, String team, String overNo, String ballNo) {
        int result = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(TOTALBALLNO) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND OVERNO = ? AND BALLNO = ?", new String[]{matchID, team, overNo, ballNo});
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        if (result > 1) {
            return true;
        }
        return false;
    }

    public void deleteDuplicateScoreBall(String matchID, String team, String totalBallNo, String overNo) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MATCH_SCOREBOARD, "MatchID = ? AND Team = ? AND TotalBallNo = ?", new String[]{matchID, team, totalBallNo});
        db.execSQL("UPDATE MatchScoreBoard SET TotalBallNo = TotalBallNo-1 WHERE MatchID = '" + matchID + "' AND " + TOTAL_BALL_NO + " > " + totalBallNo);
        db.execSQL("UPDATE MatchScoreBoard SET BallsThisOver = BallsThisOver-1 WHERE MatchID = '" + matchID + "' AND " + TOTAL_BALL_NO + " >= " + totalBallNo + " AND " + OVER_NO + " = " + overNo);
        db.close();
    }

    public String getBallByBallText(String matchID, String team, String totalBallNo) {
        String result = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT BallByBall FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND TOTALBALLNO = ?", new String[]{matchID, team, totalBallNo});
        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }

    public String getBowlerForOver(String matchID, String team, String overNo) {
        String result = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT BOWLERNAME FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND OVERNO = ?", new String[]{matchID, team, overNo});
        if (cursor.moveToFirst()) {
            result = "" + cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }

    public void updateEndOfDayOnLastScoreBall(String matchID) {
        setFlagsOnLastScoreBall(matchID, 1, 1, "");
    }

    public void clearEndOfDayOnLastScoreBall(String matchID) {
        setFlagsOnLastScoreBall(matchID, 1, 0, "");
    }

    public void updateEndOfInngsOnLastScoreBall(String matchID, int value, String desc) {
        setFlagsOnLastScoreBall(matchID, 2, value, desc);
    }

    public void clearEndOfInngsOnLastScoreBall(String matchID, int value) {
        setFlagsOnLastScoreBall(matchID, 2, value, "");
    }

    private void setFlagsOnLastScoreBall(String matchID, int what, int value, String desc) {
        SQLiteDatabase db = getWritableDatabase();
        String strFilter = "MatchID = ? and TotalBallNo = (Select MAX(TotalBallNo) FROM MatchScoreBoard WHERE MatchID = ?)";
        ContentValues args = new ContentValues();
        if (what == 1) {
            args.put(FLAG_EOD, Integer.valueOf(value));
        } else {
            args.put(FLAG_EOI, Integer.valueOf(value));
            String str = EOI_DESC;
            if (value == 0) {
                desc = "";
            }
            args.put(str, desc);
        }
        db.update(TABLE_MATCH_SCOREBOARD, args, strFilter, new String[]{matchID, matchID});
        db.close();
    }

    public void setPenaltyOnLastScoreBall(String matchID, String penalty, String value, String ballByBall) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE MatchScoreBoard SET IsPenalty = ?, RunsThisBall = RunsThisBall+ ?, TotalExtras = TotalExtras+ ?, TotalScore = TotalScore+ ?, BallByBall = ?  WHERE MatchID = ? AND TotalBallNo = (Select MAX(TotalBallNo) FROM MatchScoreBoard WHERE MatchID = ?)", new String[]{penalty, value, value, value, ballByBall, matchID, matchID});
        db.close();
    }

    public int getPrevInningsScore(String matchID, String team, int inningNo) {
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(TotalScore) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? AND InngNo < ? AND TOTALBALLNO IN (SELECT MAX(TOTALBALLNO) FROM MATCHSCOREBOARD WHERE MATCHID = ? AND TEAM = ? GROUP BY MATCHID, TEAM, InngNo) GROUP BY MATCHID, TEAM", new String[]{matchID, team, "" + inningNo, matchID, team});
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return result;
    }

    public int[] getMaxOversOfMatch(String matchID) {
        int i = 0;
        int[] result = new int[3];
        for (int i2 = 0; i2 < result.length; i2++) {
            result[i2] = 0;
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(OverNo), MAX(InngNo), MAX(DayNo) OVERS FROM MATCHSCOREBOARD WHERE MatchID = ?", new String[]{matchID});
        if (cursor.moveToFirst()) {
            result[0] = cursor.isNull(0) ? 0 : cursor.getInt(0);
            result[1] = cursor.isNull(1) ? 0 : cursor.getInt(1);
            if (!cursor.isNull(2)) {
                i = cursor.getInt(2);
            }
            result[2] = i;
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<dtoBall> getOversList(String matchID, String team, String inngNo) {
        List<dtoBall> oversList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Team, InngNo, TotalBallNo, BowlerName, OverNo, BallNo,  Case When IsWide = 1 Then 'Wide' When IsNoball = 1 Then 'Noball'  When IsWicket = 1 Then 'Wicket, ' || TRIM(CASE WHEN WICKETHOW LIKE '' THEN 'bowled' ELSE WicketHow END || ' ' || IFNULL(NULLIF(WicketAssist, 'null'), '') || (CASE WHEN WICKETHOW LIKE 'c &' THEN ' b' ELSE '' END)) When IsByes = 1 Then 'Byes' When IsLegByes = 1 Then 'Leg Byes' Else '' End BallType, StrikerName, StrikerRuns, StrikerBallsFaced, NonStrikerName, NonStrikerRuns, NonStrikerBallsFaced, RunsThisBall From MatchScoreBoard Where MatchID = ? AND Team = ? AND InngNo = ? Order By TotalBallNo DESC", new String[]{matchID, team, inngNo});
        String groupTitle = "";
        if (cursor.moveToFirst()) {
            do {
                dtoBall ball = new dtoBall();
                ball.TeamNo = cursor.getInt(0);
                ball.InngNo = cursor.getInt(1);
                ball.TotalBallNo = cursor.getInt(2);
                ball.BowlerName = cursor.getString(3);
                ball.OverNo = cursor.getInt(4);
                ball.BallNo = cursor.getInt(5);
                ball.BallType = cursor.getString(6);
                ball.StrikerName = cursor.getString(7);
                ball.StrikerRuns = "" + cursor.getInt(8) + " (" + cursor.getInt(9) + ")";
                ball.NonStrikerName = cursor.getString(10);
                ball.NonStrikerRuns = "" + cursor.getInt(11) + " (" + cursor.getInt(12) + ")";
                ball.RunsThisBall = cursor.getInt(13);
                ball.GroupTitle = "";
                if (!groupTitle.equalsIgnoreCase(ball.getTitle())) {
                    groupTitle = ball.getTitle();
                    ball.GroupTitle = groupTitle;
                }
                oversList.add(ball);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return oversList;
    }
}
