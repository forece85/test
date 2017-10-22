package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MatchStats extends BaseActivity {
    private int _lastSelectedInng = -1;
    private int _lastSelectedTeam = -1;
    private boolean _suppress = true;

    class C02411 implements OnItemSelectedListener {
        C02411() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (MatchStats.this._lastSelectedTeam != position) {
                MatchStats.this._lastSelectedTeam = position;
                if (MatchStats.this.getIntent().hasExtra("SelectedTeam")) {
                    MatchStats.this.getIntent().removeExtra("SelectedTeam");
                }
                MatchStats.this.getIntent().putExtra("SelectedTeam", position);
                MatchStats.this.populateMatchSummary();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02422 implements OnItemSelectedListener {
        C02422() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (MatchStats.this._lastSelectedInng != position) {
                MatchStats.this._lastSelectedInng = position;
                if (MatchStats.this.getIntent().hasExtra("SelectedInng")) {
                    MatchStats.this.getIntent().removeExtra("SelectedInng");
                }
                MatchStats.this.getIntent().putExtra("SelectedInng", position);
                MatchStats.this.populateMatchSummary();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (savedInstanceState != null) {
                String matchId = savedInstanceState.getString("MATCH_ID");
                if (((CricScorerApp) getApplication()).currentMatch == null) {
                    DatabaseHandler db = new DatabaseHandler(this);
                    ((CricScorerApp) getApplication()).currentMatch = db.getMatchMaster(matchId);
                    db.close();
                }
            }
        } catch (Exception e) {
            finish();
        }
    }

    public boolean hideWindowTitle() {
        return true;
    }

    public void onResume() {
        try {
            super.onResume();
            setContentView(C0252R.layout.activity_match_stats);
            this._suppress = true;
            initializeControls();
            this._suppress = false;
            populateMatchSummary();
            registerForContextMenu((ImageButton) findViewById(C0252R.id.buttonGraphStats));
            registerForContextMenu((ImageButton) findViewById(C0252R.id.buttonReportStats));
            registerForContextMenu((ImageButton) findViewById(C0252R.id.buttonSmsImage));
        } catch (Exception e) {
            finish();
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("MATCH_ID", ((CricScorerApp) getApplication()).currentMatch.getMatchID());
        super.onSaveInstanceState(savedInstanceState);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onBackPressed() {
        Utility.cleanupTempFiles(this);
        super.onBackPressed();
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void initializeControls() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        Spinner teamSpinner = (Spinner) findViewById(C0252R.id.spinnerTeams);
        Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
        List<String> teams = new ArrayList();
        teams.add(MatchHelper.getTeamName(refMatch, MatchHelper.getFirstBatted(refMatch)));
        if (refMatch.getCurrentSession() > 1) {
            teams.add(MatchHelper.getTeamName(refMatch, MatchHelper.getSecondBatted(refMatch)));
        }
        ArrayAdapter<String> teamsAdapter = new ArrayAdapter(this, 17367048, teams);
        teamsAdapter.setDropDownViewResource(17367049);
        teamSpinner.setAdapter(teamsAdapter);
        if (getIntent().hasExtra("SelectedTeam")) {
            teamSpinner.setSelection(getIntent().getExtras().getInt("SelectedTeam"));
        } else if (MatchHelper.getFirstBatted(refMatch).equalsIgnoreCase(getBattingTeam())) {
            teamSpinner.setSelection(0);
            getIntent().putExtra("SelectedTeam", 0);
        } else {
            teamSpinner.setSelection(1);
            getIntent().putExtra("SelectedTeam", 1);
        }
        teamSpinner.setOnItemSelectedListener(new C02411());
        List<String> innings = new ArrayList();
        innings.add("1st Inngs");
        if (refMatch.getNoOfInngs() > 1 && refMatch.getCurrentSession() > 2) {
            innings.add("2nd Inngs");
        }
        ArrayAdapter<String> inngsAdapter = new ArrayAdapter(this, 17367048, innings);
        inngsAdapter.setDropDownViewResource(17367049);
        inngSpinner.setAdapter(inngsAdapter);
        if (getIntent().hasExtra("SelectedInng")) {
            inngSpinner.setSelection(getIntent().getExtras().getInt("SelectedInng"));
        } else if (getCurrentInngsNo().equalsIgnoreCase("1")) {
            inngSpinner.setSelection(0);
            getIntent().putExtra("SelectedInng", 0);
        } else {
            inngSpinner.setSelection(1);
            getIntent().putExtra("SelectedInng", 1);
        }
        inngSpinner.setOnItemSelectedListener(new C02422());
        if (refMatch.getNoOfInngs() <= 1) {
            inngSpinner.setEnabled(false);
        }
    }

    protected String getCurrentInngsNo() {
        return "" + ((CricScorerApp) getApplication()).currentMatch.getCurrentInningsNo();
    }

    public void populateMatchSummary() {
        if (!this._suppress) {
            dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
            Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
            String team = "";
            String inng = "1";
            if (((Spinner) findViewById(C0252R.id.spinnerTeams)).getSelectedItemPosition() == 0) {
                team = MatchHelper.getFirstBatted(refMatch);
            } else {
                team = MatchHelper.getSecondBatted(refMatch);
            }
            if (inngSpinner.getSelectedItemPosition() == 0) {
                inng = "1";
            } else {
                inng = "2";
            }
            clearTables();
            StringBuilder append = new StringBuilder().append(MatchHelper.getTeamName(refMatch, team)).append(" - ");
            String str = refMatch.getNoOfInngs() > 1 ? inng.equalsIgnoreCase("1") ? "1st " : "2nd " : "";
            ((TextView) findViewById(C0252R.id.titleMatchSummary)).setText(append.append(str).append("Innings").toString());
            populateScoreDetails(team, inng);
            populateBatsmenStats(team, inng);
            populateBowlerStats(team, inng);
            populateFow(team, inng);
            populatePartnershipStats(team, inng);
        }
    }

    public void onGraphs(View view) {
        openContextMenu(view);
    }

    public void onReports(View view) {
        openContextMenu(view);
    }

    public void onSms(View view) {
        if (Utility.IsSDCardPresent()) {
            openContextMenu(view);
        } else {
            sendViaText();
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        dtoAdditionalSettings settings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == C0252R.id.buttonReportStats) {
            inflater.inflate(C0252R.menu.reports_context_menu, menu);
        } else if (v.getId() == C0252R.id.buttonGraphStats) {
            inflater.inflate(C0252R.menu.graph_context_menu, menu);
            if (settings.getWagonWheel() == 0) {
                menu.findItem(C0252R.id.miFirstInnsWagonWheel).setVisible(false);
            }
            if (settings.getBallSpot() == 0) {
                menu.findItem(C0252R.id.miFirstInnsBallSpot).setVisible(false);
            }
        } else if (v.getId() == C0252R.id.buttonSmsImage) {
            inflater.inflate(C0252R.menu.sms_context_menu, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        Intent intent;
        switch (item.getItemId()) {
            case C0252R.id.miMatchSummary:
                onMatchSummary(null);
                return true;
            case C0252R.id.miClassicScorecard:
                onClassicScorecard("450");
                return true;
            case C0252R.id.miClassicScorecardPrintView:
                onClassicScorecard("800");
                return true;
            case C0252R.id.miBallByBall:
                onBallByBallReport();
                return true;
            case C0252R.id.miInnsRunRate:
                intent = new Intent(this, RunRate.class);
                intent.putExtra("FirstBatted", MatchHelper.getFirstBatted(refMatch));
                intent.putExtra("SecondBatted", MatchHelper.getSecondBatted(refMatch));
                startActivity(intent);
                return true;
            case C0252R.id.miFirstInnsWagonWheel:
                startActivity(new Intent(this, WagonWheelStats.class));
                return true;
            case C0252R.id.miFirstInnsBallSpot:
                startActivity(new Intent(this, BallSpotStats.class));
                return true;
            case C0252R.id.miFirstInnsRunsPerOver:
                startActivity(new Intent(this, RunsPerOver.class));
                return true;
            case C0252R.id.miOverallBatStats:
                intent = new Intent(this, OverallBatsmanStats.class);
                intent.putExtra("MatchID", refMatch.getMatchID());
                startActivity(intent);
                return true;
            case C0252R.id.miOverallBowlStats:
                intent = new Intent(this, OverallBowlerStats.class);
                intent.putExtra("MatchID", refMatch.getMatchID());
                startActivity(intent);
                return true;
            case C0252R.id.miSendScoreText:
                sendViaText();
                return true;
            case C0252R.id.miSendScoreImage:
                sendViaImage();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void clearTables() {
        TableLayout tblBatsmenStats = (TableLayout) findViewById(C0252R.id.tableBatsmenStats);
        tblBatsmenStats.removeViews(1, tblBatsmenStats.getChildCount() - 1);
        TableLayout tblBowlerStats = (TableLayout) findViewById(C0252R.id.tableBowlerStats);
        tblBowlerStats.removeViews(1, tblBowlerStats.getChildCount() - 1);
        TableLayout tblPartnershipStats = (TableLayout) findViewById(C0252R.id.tablePartnershipStats);
        tblPartnershipStats.removeViews(1, tblPartnershipStats.getChildCount() - 1);
    }

    protected String getBattingTeam() {
        return ((CricScorerApp) getApplication()).currentMatch.getCurrentBattingTeamNo();
    }

    protected String getBowlingTeam() {
        return ((CricScorerApp) getApplication()).currentMatch.getCurrentBowlingTeamNo();
    }

    protected void populateFow(String team, String inng) {
        ((TextView) findViewById(C0252R.id.textFow)).setText(MatchStatsHelper.getFOW(this, ((CricScorerApp) getApplication()).currentMatch, team, inng));
    }

    protected void populateScoreDetails(String team, String inngNo) {
        TextView textOvers = (TextView) findViewById(C0252R.id.textOvers);
        TextView textExtras = (TextView) findViewById(C0252R.id.textExtras);
        TextView textScore = (TextView) findViewById(C0252R.id.textScore);
        TextView textScoreDesc = (TextView) findViewById(C0252R.id.textScoreDesc);
        dtoMatchStats matchStatTeam = MatchHelper.getMatchScore(getApplicationContext(), ((CricScorerApp) getApplication()).currentMatch, team, inngNo);
        textOvers.setText("" + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo());
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
        textExtras.setText(extraText);
        textScore.setText("" + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount());
        textScoreDesc.setText(("" + matchStatTeam.getEOIDesc()).trim());
    }

    protected void populateBatsmenStats(String battingTeam, String inng) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        TableLayout tblBatsmenStats = (TableLayout) findViewById(C0252R.id.tableBatsmenStats);
        DatabaseHandler db = new DatabaseHandler(this);
        List<dtoBatsStats> batsmenStat = db.getBatsmanStats(refMatch.getMatchID(), battingTeam, inng);
        db.close();
        TextView textOvers = (TextView) findViewById(C0252R.id.textOvers);
        int startId = 800;
        for (dtoBatsStats row : batsmenStat) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(-1, -2));
            if (row.getBowlerName().trim().equalsIgnoreCase("") && row.getWicketAssist().trim().equalsIgnoreCase("")) {
                row.setWicketAssist("n.o.");
            }
            int notoutColor = 0;
            if (row.getWicketAssist().contains("n.o.")) {
                notoutColor = Utility.getColorInverse(textOvers.getCurrentTextColor());
            }
            int i = startId + 1;
            tr.addView(createTextView(startId, "" + row.getBatsmanName(), notoutColor));
            if (row.getWicketHow().contains("Run out")) {
                startId = i + 1;
                tr.addView(createTextView(i, "Run out " + row.getWicketAssist(), notoutColor));
            } else if (row.getWicketHow().contains("Retired")) {
                startId = i + 1;
                tr.addView(createTextView(i, "Retired", notoutColor));
            } else if (row.getWicketHow().contains("hit") || row.getWicketHow().contains("timed") || row.getWicketHow().contains("handled") || row.getWicketHow().contains("obstruct") || row.getWicketAssist().contains("n.o.")) {
                startId = i + 1;
                tr.addView(createTextView(i, "" + row.getWicketHow(), notoutColor));
            } else {
                startId = i + 1;
                tr.addView(createTextView(i, ("" + row.getWicketHow() + " " + row.getWicketAssist() + " b " + row.getBowlerName()).trim(), notoutColor));
            }
            i = startId + 1;
            tr.addView(createTextView(startId, "" + row.getRuns() + " (" + row.getBalls() + ")", notoutColor, 0.5f));
            startId = i + 1;
            tr.addView(createTextView(i, "" + row.getRunRate(), notoutColor, 0.3f));
            tblBatsmenStats.addView(tr, new TableLayout.LayoutParams(-1, -2));
        }
    }

    protected void populateBowlerStats(String bowlingTeam, String inngNo) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        TableLayout tblBowlerStats = (TableLayout) findViewById(C0252R.id.tableBowlerStats);
        DatabaseHandler db = new DatabaseHandler(this);
        List<dtoBowlerStat> bowlerStat = db.getBowlerStats(refMatch.getMatchID(), bowlingTeam, inngNo);
        db.close();
        int startId = 900;
        for (dtoBowlerStat row : bowlerStat) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(-1, -2));
            int i = startId + 1;
            tr.addView(createTextView(startId, "" + row.getBowlerName(), 0));
            if (settings.getRestrictBpo() == 1) {
                startId = i + 1;
                tr.addView(createTextView(i, "" + row.getCalculatedOvers() + "." + row.getCalculatedBalls(), 0, 0.3f));
            } else {
                startId = i + 1;
                tr.addView(createTextView(i, "" + row.getOvers() + "." + row.getBalls(), 0, 0.3f));
            }
            i = startId + 1;
            tr.addView(createTextView(startId, "" + row.getRuns(), 0, 0.3f));
            startId = i + 1;
            tr.addView(createTextView(i, "" + row.getWickets(), 0, 0.3f));
            i = startId + 1;
            tr.addView(createTextView(startId, "" + row.getWides(), 0, 0.2f));
            startId = i + 1;
            tr.addView(createTextView(i, "" + row.getNoballs(), 0, 0.2f));
            i = startId + 1;
            tr.addView(createTextView(startId, "" + row.getRunRate(), 0, 0.3f));
            tblBowlerStats.addView(tr, new TableLayout.LayoutParams(-1, -2));
            startId = i;
        }
    }

    protected void populatePartnershipStats(String battingTeam, String inng) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        TableLayout tblPartnershipStats = (TableLayout) findViewById(C0252R.id.tablePartnershipStats);
        DatabaseHandler db = new DatabaseHandler(this);
        PartnershipStatsList statList = db.getPartnershipData(refMatch.getMatchID(), battingTeam, inng);
        db.close();
        int startId = 700;
        Iterator it = statList.iterator();
        while (it.hasNext()) {
            dtoPartnershipStats row = (dtoPartnershipStats) it.next();
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(-1, -2));
            int i = startId + 1;
            tr.addView(createTextView(startId, "" + row.striker1 + " (" + row.striker1Runs + ")", 0));
            startId = i + 1;
            tr.addView(createTextView(i, "" + (((row.striker1Runs + row.striker2Runs) + row.striker1Extras) + row.striker2Extras) + " (" + (row.striker1Balls + row.striker2Balls) + ")", 0, 0.5f));
            i = startId + 1;
            tr.addView(createTextView(startId, "" + row.striker2 + " (" + row.striker2Runs + ")", 0));
            startId = i + 1;
            tr.addView(createTextView(i, "" + (row.striker1Extras + row.striker2Extras), 0, 0.0f));
            tblPartnershipStats.addView(tr, new TableLayout.LayoutParams(-1, -2));
        }
    }

    protected TextView createTextView(int id, String value, int backColor) {
        return createTextView(id, value, backColor, 1.0f);
    }

    protected TextView createTextView(int id, String value, int backColor, int textColor) {
        return createTextView(id, value, backColor, 1.0f, textColor);
    }

    protected TextView createTextView(int id, String value, int backColor, float weight) {
        return createTextView(id, value, backColor, weight, -99999);
    }

    protected TextView createTextView(int id, String value, int backColor, float weight, int textColor) {
        TextView txt = new TextView(this);
        txt.setId(id);
        txt.setBackgroundColor(backColor);
        if (textColor != -99999) {
            txt.setTextColor(textColor);
        }
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.setMargins(2, 2, 0, 2);
        if (((double) weight) > 0.0d) {
            layoutParams.width = 0;
            layoutParams.weight = weight;
        }
        txt.setLayoutParams(layoutParams);
        if (value.equalsIgnoreCase("")) {
            value = " ";
        }
        txt.setText(value);
        return txt;
    }

    public void onMatchSummary(View view) {
        try {
            Builder alertDialog = new Builder(this);
            final View convertView = getLayoutInflater().inflate(C0252R.layout.match_summary_view, null);
            alertDialog.setView(convertView);
            ((ImageButton) convertView.findViewById(C0252R.id.buttonSmsImage)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Utility.sendViaImage(MatchStats.this, convertView, convertView.getHeight(), convertView.getWidth(), "InngsSummary.jpg", "Match Summary");
                }
            });
            ((ListView) convertView.findViewById(C0252R.id.mainListView)).setAdapter(new MatchSummaryListAdapter(this, MatchSummary.populateMatchSummaryDetails(this, convertView)));
            alertDialog.show();
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(), 1).show();
        }
    }

    public void onMatchReport(View view) {
        try {
            File fileReport = new File(Utility.CreateFileFromString(this, MatchStatsHelper.createMatchSummary(this, ((CricScorerApp) getApplication()).currentMatch), "Scorecard.html"));
            Intent browserIntent = new Intent(this, DisplayWebView.class);
            browserIntent.putExtra("title", getString(C0252R.string.scorecardText));
            if (fileReport.exists()) {
                browserIntent.putExtra("url", Uri.fromFile(fileReport).toString());
            }
            startActivity(browserIntent);
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(), 1).show();
        }
    }

    public void onBallByBallReport() {
        try {
            File fileReport = new File(Utility.CreateFileFromString(this, MatchStatsHelper.createBallByBallReport(this, ((CricScorerApp) getApplication()).currentMatch), "BallByBallReport.html"));
            Intent browserIntent = new Intent(this, DisplayWebView.class);
            browserIntent.putExtra("title", "Ball by Ball Report");
            if (fileReport.exists()) {
                browserIntent.putExtra("url", Uri.fromFile(fileReport).toString());
            }
            startActivity(browserIntent);
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(), 1).show();
        }
    }

    public void onClassicScorecard(String width) {
        try {
            File fileReport = new File(Utility.CreateFileFromString(this, MatchStatsHelper.createClassicScorecard(this, ((CricScorerApp) getApplication()).currentMatch, width), "ClassicScorecard.html"));
            Intent browserIntent = new Intent(this, DisplayWebView.class);
            browserIntent.putExtra("title", "Classic Scorecard");
            if (fileReport.exists()) {
                browserIntent.putExtra("url", Uri.fromFile(fileReport).toString());
            }
            startActivity(browserIntent);
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(), 1).show();
        }
    }

    public void sendViaText() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String secondBatted = MatchHelper.getSecondBatted(refMatch);
        dtoMatchStats matchStatTeam = MatchHelper.getMatchScore(getApplicationContext(), refMatch, firstBatted, "1");
        String dataToSend = ((MatchHelper.getTeamName(refMatch, firstBatted) + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings") + "\n" + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount() + " (" + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo() + " overs)") + ", Ext: " + matchStatTeam.getTotalExtras();
        if (refMatch.getCurrentSession() > 1) {
            matchStatTeam = MatchHelper.getMatchScore(getApplicationContext(), refMatch, secondBatted, "1");
            dataToSend = ((dataToSend + "\n" + MatchHelper.getTeamName(refMatch, secondBatted) + " - " + (refMatch.getNoOfInngs() > 1 ? "1st " : "") + "Innings") + "\n" + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount() + " (" + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo() + " overs)") + ", Ext: " + matchStatTeam.getTotalExtras();
        }
        if (refMatch.getNoOfInngs() > 1 && refMatch.getCurrentSession() > 2) {
            String thirdBatted;
            if (refMatch.getFollowOn()) {
                thirdBatted = secondBatted;
            } else {
                thirdBatted = firstBatted;
            }
            matchStatTeam = MatchHelper.getMatchScore(getApplicationContext(), refMatch, thirdBatted, "2");
            dataToSend = ((dataToSend + "\n" + MatchHelper.getTeamName(refMatch, thirdBatted) + " - 2nd Innings" + (refMatch.getFollowOn() ? "(FOLLOW ON)" : "")) + "\n" + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount() + " (" + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo() + " overs)") + ", Ext: " + matchStatTeam.getTotalExtras();
            if (refMatch.getCurrentSession() > 3) {
                String lastBatted;
                if (refMatch.getFollowOn()) {
                    lastBatted = firstBatted;
                } else {
                    lastBatted = secondBatted;
                }
                matchStatTeam = MatchHelper.getMatchScore(getApplicationContext(), refMatch, lastBatted, "2");
                dataToSend = ((dataToSend + "\n" + MatchHelper.getTeamName(refMatch, lastBatted) + " - 2nd Innings") + "\n" + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount() + " (" + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo() + " overs)") + ", Ext: " + matchStatTeam.getTotalExtras();
            }
        }
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        if (lastBall != null) {
            boolean strikerNotout = true;
            boolean nonStrikerNotout = true;
            if (lastBall.getIsWicket()) {
                if (lastBall.getWicketHow().contains("Non-Striker")) {
                    nonStrikerNotout = false;
                } else {
                    strikerNotout = false;
                }
            }
            dataToSend = (dataToSend + "\n" + lastBall.getStrikerName() + " - " + lastBall.getStrikerRuns() + "(" + lastBall.getBallsFacedStriker() + ")" + (strikerNotout ? "*" : "")) + "\n" + lastBall.getNonStrikerName() + " - " + lastBall.getNonStrikerRuns() + "(" + lastBall.getBallsFacedNonStriker() + ")" + (nonStrikerNotout ? "*" : "");
            if (matchStatTeam.getWicketCount() > 0) {
                String fow = MatchStatsHelper.getFOW(this, refMatch, lastBall.getTeamNo(), "" + lastBall.getInningNo());
                int lastIndex = fow.lastIndexOf(" - ");
                if (lastIndex > 0) {
                    fow = fow.substring(lastIndex);
                }
                StringBuilder append = new StringBuilder().append(dataToSend).append("\nLast Wicket");
                if (!fow.startsWith(" - ")) {
                    fow = " - " + fow;
                }
                dataToSend = append.append(fow).toString();
            }
        }
        if (!refMatch.getMatchResult().equalsIgnoreCase("")) {
            dataToSend = dataToSend + "\n" + refMatch.getMatchResult();
        }
        Intent smsIntent = new Intent("android.intent.action.SEND");
        smsIntent.setType("text/plain");
        smsIntent.putExtra("android.intent.extra.TEXT", dataToSend);
        startActivity(Intent.createChooser(smsIntent, "SMS Score"));
    }

    public void sendViaImage() {
        ScrollView z = (ScrollView) findViewById(C0252R.id.scrollView1);
        Utility.sendViaImage(this, z, z.getChildAt(0).getHeight(), z.getChildAt(0).getWidth(), "InningsScore.jpg", "Send Score");
    }
}
