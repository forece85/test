package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class OpenMatch extends BaseActivity {
    private String _selectMatchDateTime = "";
    private String _selectedMatch = "";
    MatchListAdapter listAdapter;
    protected List<dtoMatchList> matchList;

    class C02441 implements OnItemClickListener {
        C02441() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            OpenMatch.this.listAdapter.setSelectedPosition(position);
            OpenMatch.this._selectedMatch = ((dtoMatchList) OpenMatch.this.matchList.get(position)).getMatchID();
            OpenMatch.this._selectMatchDateTime = ((dtoMatchList) OpenMatch.this.matchList.get(position)).getMatchDateTime();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_open_match);
            populateMatches();
            registerForContextMenu((Button) findViewById(C0252R.id.btnReports));
        } catch (Exception e) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(C0252R.menu.activity_open_match, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        switch (item.getItemId()) {
            case C0252R.id.miMatchSummary:
                onMatchSummary(null);
                return true;
            case C0252R.id.miMatchReport:
                onMatchReport(null);
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
            case C0252R.id.miRunsPerOver:
                startActivity(new Intent(this, RunsPerOver.class));
                return true;
            case C0252R.id.miInnsRunRate:
                Intent intent = new Intent(this, RunRate.class);
                intent.putExtra("FirstBatted", MatchHelper.getFirstBatted(refMatch));
                intent.putExtra("SecondBatted", MatchHelper.getSecondBatted(refMatch));
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void populateMatches() {
        DatabaseHandler db = new DatabaseHandler(this);
        this.matchList = db.getAllMatchList();
        db.close();
        Collections.sort(this.matchList, new MatchNameComparator());
        String matchName = "";
        for (dtoMatchList match : this.matchList) {
            if (matchName.equalsIgnoreCase(match.getMatchName())) {
                match.setMatchName("");
            } else {
                matchName = match.getMatchName();
            }
        }
        ListView mainListView = (ListView) findViewById(C0252R.id.mainListView);
        this.listAdapter = new MatchListAdapter(this, this.matchList);
        mainListView.setAdapter(this.listAdapter);
        mainListView.setOnItemClickListener(new C02441());
    }

    public void Refresh() {
        this._selectedMatch = "";
        this._selectMatchDateTime = "";
        populateMatches();
    }

    public void onOpenMatch(View view) {
        if (!isFrequentClick()) {
            if (this._selectedMatch == null || this._selectedMatch.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select a match to proceed!", 0).show();
            } else {
                setMatchDetails(this._selectedMatch);
            }
        }
    }

    public void onEditTeams(View view) {
        if (!isFrequentClick()) {
            if (this._selectedMatch == null || this._selectedMatch.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select a match to proceed!", 0).show();
                return;
            }
            DatabaseHandler db = new DatabaseHandler(this);
            ((CricScorerApp) getApplication()).currentMatch = db.getMatchMaster(this._selectedMatch);
            db.close();
            Intent intent = new Intent(this, EditTeams.class);
            intent.putExtra("SelectedMatch", this._selectedMatch);
            startActivity(intent);
            finish();
        }
    }

    public void onDeleteMatch(View view) {
        if (!isFrequentClick()) {
            Utility.showNotSupportedAlert(this);
        }
    }

    protected void setMatchDetails(String matchID) {
        DatabaseHandler db = new DatabaseHandler(this);
        ((CricScorerApp) getApplication()).currentMatch = db.getMatchMaster(matchID);
        db.close();
        startActivity(new Intent(this, Scorecard.class));
        finish();
    }

    public void onReports(View view) {
        if (!isFrequentClick()) {
            if (this._selectedMatch == null || this._selectedMatch.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select a match to proceed!", 0).show();
                return;
            }
            DatabaseHandler db = new DatabaseHandler(this);
            ((CricScorerApp) getApplication()).currentMatch = db.getMatchMaster(this._selectedMatch);
            db.close();
            MatchHelper.updateMatchSessions(((CricScorerApp) getApplication()).currentMatch);
            openContextMenu(view);
        }
    }

    public void onMatchSummary(View view) {
        try {
            Builder alertDialog = new Builder(this);
            final View convertView = getLayoutInflater().inflate(C0252R.layout.match_summary_view, null);
            alertDialog.setView(convertView);
            ((ImageButton) convertView.findViewById(C0252R.id.buttonSmsImage)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Utility.sendViaImage(OpenMatch.this, convertView, convertView.getHeight(), convertView.getWidth(), "InngsSummary.jpg", "Match Summary");
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
}
