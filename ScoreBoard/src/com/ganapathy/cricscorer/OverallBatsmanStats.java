package com.ganapathy.cricscorer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class OverallBatsmanStats extends BaseActivity {
    public boolean _firstTeamsLoad = true;
    public String _matchID;
    public boolean _showAll = false;
    private int _sort;
    protected List<dtoOverallBatsStat> list;
    protected List<dtoTeam> teams;
    protected List<dtoTeam> tournaments;

    class C02461 implements OnItemSelectedListener {
        C02461() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
            if (OverallBatsmanStats.this._firstTeamsLoad) {
                OverallBatsmanStats.this._firstTeamsLoad = false;
            } else {
                OverallBatsmanStats.this.populatePlayers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02472 implements OnItemSelectedListener {
        C02472() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
            OverallBatsmanStats.this.populatePlayers();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02483 extends AsyncTask<String, Void, Void> {
        DatabaseHandler db = new DatabaseHandler(OverallBatsmanStats.this.getApplicationContext());
        private ProgressDialog pd;

        C02483() {
        }

        protected void onPreExecute() {
            this.pd = new ProgressDialog(OverallBatsmanStats.this);
            this.pd.setTitle("Calculating...");
            this.pd.setMessage("Please wait.");
            this.pd.setCancelable(false);
            this.pd.setIndeterminate(true);
            this.pd.show();
        }

        protected Void doInBackground(String... strings) {
            try {
                if (OverallBatsmanStats.this._showAll) {
                    OverallBatsmanStats.this.list = this.db.getOverallBatStats(strings[0], strings[1], strings[2], strings[3]);
                } else {
                    OverallBatsmanStats.this.list = this.db.getOverallBatStatsPlayerNames(strings[0], strings[1], strings[2], strings[3]);
                }
                OverallBatsmanStats.this._showAll = false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.db.close();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            this.pd.dismiss();
            if (OverallBatsmanStats.this.list == null || OverallBatsmanStats.this.list.isEmpty()) {
                ((TextView) OverallBatsmanStats.this.findViewById(C0252R.id.noDataFoundText)).setVisibility(0);
            } else {
                ((TextView) OverallBatsmanStats.this.findViewById(C0252R.id.noDataFoundText)).setVisibility(8);
                if (OverallBatsmanStats.this._sort == 2) {
                    Collections.sort(OverallBatsmanStats.this.list, new SortDescending());
                } else {
                    Collections.sort(OverallBatsmanStats.this.list, new SortAscending());
                }
            }
            ((ListView) OverallBatsmanStats.this.findViewById(C0252R.id.overallBatsListView)).setAdapter(new OverallBatStatsAdapter(OverallBatsmanStats.this, OverallBatsmanStats.this.list));
        }
    }

    private class SortAscending implements Comparator<dtoOverallBatsStat> {
        private SortAscending() {
        }

        public int compare(dtoOverallBatsStat o1, dtoOverallBatsStat o2) {
            return o1.getStrikerName().compareToIgnoreCase(o2.getStrikerName());
        }
    }

    private class SortDescending implements Comparator<dtoOverallBatsStat> {
        private SortDescending() {
        }

        public int compare(dtoOverallBatsStat o1, dtoOverallBatsStat o2) {
            return o2.getStrikerName().compareToIgnoreCase(o1.getStrikerName());
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_overall_batsman_stats);
            this._matchID = getIntent().getExtras().getString("MatchID");
            initializeControls();
        } catch (Exception e) {
            finish();
        }
    }

    public boolean hideWindowTitle() {
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onBackPressed() {
        Utility.cleanupTempFiles(this);
        super.onBackPressed();
    }

    protected void initializeControls() {
        Spinner spinTeams = (Spinner) findViewById(C0252R.id.spinTeams);
        Spinner spinTournament = (Spinner) findViewById(C0252R.id.spinTournaments);
        TextView labelTeams = (TextView) findViewById(C0252R.id.labelTeamsText);
        TextView labelTournaments = (TextView) findViewById(C0252R.id.labelTournameText);
        TextView screenTitle = (TextView) findViewById(C0252R.id.textScreenTitle);
        if (this._matchID.trim().equalsIgnoreCase("")) {
            screenTitle.setText(getString(C0252R.string.overallBatsmanText));
            spinTeams.setVisibility(0);
            spinTournament.setVisibility(0);
            labelTeams.setVisibility(0);
            labelTournaments.setVisibility(0);
            populateSpinner();
        } else {
            screenTitle.setText(getString(C0252R.string.overallBatsmanText));
            spinTeams.setVisibility(8);
            spinTournament.setVisibility(8);
            labelTeams.setVisibility(8);
            labelTournaments.setVisibility(8);
            populatePlayers();
        }
        this._sort = 1;
    }

    protected void populateSpinner() {
        DatabaseHandler db = new DatabaseHandler(this);
        this.teams = db.getAllTeams();
        this.tournaments = db.getAllTournamentNames();
        db.close();
        Spinner spinTeam = (Spinner) findViewById(C0252R.id.spinTeams);
        List<String> teamNames = new ArrayList();
        for (dtoTeam value : this.teams) {
            teamNames.add(value.getTeamName());
        }
        if (teamNames.isEmpty()) {
            spinTeam.setVisibility(8);
        } else {
            ArrayAdapter<String> adapter1 = new ArrayAdapter(this, 17367048, teamNames);
            adapter1.setDropDownViewResource(17367049);
            spinTeam.setAdapter(adapter1);
            spinTeam.setSelection(0);
        }
        spinTeam.setOnItemSelectedListener(new C02461());
        Spinner spinTournament = (Spinner) findViewById(C0252R.id.spinTournaments);
        List<String> tournamentNames = new ArrayList();
        for (dtoTeam value2 : this.tournaments) {
            tournamentNames.add(value2.getTeamName());
        }
        if (tournamentNames.isEmpty()) {
            spinTournament.setVisibility(8);
        } else {
            adapter1 = new ArrayAdapter(this, 17367048, tournamentNames);
            adapter1.setDropDownViewResource(17367049);
            spinTournament.setAdapter(adapter1);
            spinTournament.setSelection(0);
        }
        spinTournament.setOnItemSelectedListener(new C02472());
    }

    public void onStrikerClick(View v) {
        dtoTeam item;
        Spinner spinTeam = (Spinner) findViewById(C0252R.id.spinTeams);
        Spinner spinTournament = (Spinner) findViewById(C0252R.id.spinTournaments);
        String teamName = "";
        String tournamentName = "";
        if (spinTeam.getSelectedItemPosition() <= 0) {
            teamName = "";
        } else {
            item = (dtoTeam) this.teams.get(spinTeam.getSelectedItemPosition());
            if (item != null) {
                teamName = item.getTeamName();
            }
        }
        if (spinTournament.getSelectedItemPosition() <= 0) {
            tournamentName = "";
        } else {
            item = (dtoTeam) this.tournaments.get(spinTournament.getSelectedItemPosition());
            if (item != null) {
                tournamentName = item.getTeamName();
            }
        }
        try {
            ListView mainListView = (ListView) findViewById(C0252R.id.overallBatsListView);
            dtoOverallBatsStat item2 = (dtoOverallBatsStat) this.list.get(Integer.parseInt("" + ((Button) v).getTag()));
            if (item2.hasDetails()) {
                item2.setShowDetails(!item2.getShowDetails());
                mainListView.invalidateViews();
                return;
            }
            DatabaseHandler db = new DatabaseHandler(this);
            dtoOverallBatsStat stat = db.getOverallBatStatsDetails(this._matchID, teamName, tournamentName, item2.getStrikerName());
            db.close();
            item2.copy(stat);
            item2.hasDetails(true);
            item2.setShowDetails(true);
            mainListView.invalidateViews();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    public void asyncPopulatePlayers(String matchID, String teamName, String tournamentName, String searchText) {
        new C02483().execute(new String[]{matchID, teamName, tournamentName, searchText});
    }

    public void populatePlayers() {
        dtoTeam item;
        EditText searchText = (EditText) findViewById(C0252R.id.editSearch);
        Spinner spinTeam = (Spinner) findViewById(C0252R.id.spinTeams);
        Spinner spinTournament = (Spinner) findViewById(C0252R.id.spinTournaments);
        String teamName = "";
        String tournamentName = "";
        if (spinTeam.getSelectedItemPosition() <= 0) {
            teamName = "";
        } else {
            item = (dtoTeam) this.teams.get(spinTeam.getSelectedItemPosition());
            if (item != null) {
                teamName = item.getTeamName();
            }
        }
        if (spinTournament.getSelectedItemPosition() <= 0) {
            tournamentName = "";
        } else {
            item = (dtoTeam) this.tournaments.get(spinTournament.getSelectedItemPosition());
            if (item != null) {
                tournamentName = item.getTeamName();
            }
        }
        if (this._matchID == null || this._matchID.equalsIgnoreCase("")) {
            asyncPopulatePlayers("", teamName, tournamentName, searchText.getText().toString());
        } else {
            asyncPopulatePlayers(this._matchID, teamName, tournamentName, searchText.getText().toString());
        }
    }

    public void onSort(View view) {
        if (this._sort == 1) {
            this._sort = 2;
        } else {
            this._sort = 1;
        }
        populatePlayers();
        Toast.makeText(this, "Statistics updated", 0).show();
    }

    public void onSearch(View view) {
        EditText searchText = (EditText) findViewById(C0252R.id.editSearch);
        populatePlayers();
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onShare(View view) {
        try {
            File fileReport = new File(Utility.CreateFileFromString(this, createOverallBatStatsReport(this._matchID), "BatStats.html"));
            Intent browserIntent = new Intent(this, DisplayWebView.class);
            browserIntent.putExtra("title", getString(C0252R.string.overallBatsmanText));
            if (fileReport.exists()) {
                browserIntent.putExtra("url", Uri.fromFile(fileReport).toString());
            }
            startActivity(browserIntent);
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(), 1).show();
        }
    }

    public void onShowAll(View view) {
        this._showAll = true;
        populatePlayers();
    }

    protected String createOverallBatStatsReport(String matchID) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        String data = ((("" + "<html>") + Utility.createStyleSheet()) + "<body style=\"width:800px\">") + "<div class=\"CSSTableGenerator\">";
        if (this._matchID.trim().equalsIgnoreCase("")) {
            data = data + "<h3><u>OVERALL BATSMEN STATISTICS</u></h3>";
        } else {
            data = (data + "<h3><u>BATSMEN STATISTICS OF THE MATCH - " + refMatch.getMatchID() + "</u></h3>") + "<h3><u>MATCH BETWEEN " + refMatch.getTeam1Name() + " vs " + refMatch.getTeam2Name() + " - " + refMatch.getOvers() + " Overs</u></h3>";
        }
        return (((data + createStats()) + Utility.createFooter()) + "</div>") + "</body></html>";
    }

    protected String createStats() {
        Spinner spinTeam = (Spinner) findViewById(C0252R.id.spinTeams);
        String teamName = "";
        if (spinTeam.getSelectedItemPosition() > 0) {
            dtoTeam item = (dtoTeam) this.teams.get(spinTeam.getSelectedItemPosition());
            if (item != null) {
                teamName = item.getTeamName();
            }
        }
        String table = "<table>";
        if (!teamName.trim().equalsIgnoreCase("")) {
            table = table + "<tr><th colspan=\"16\"><div width=\"100%\">TEAM NAME: " + teamName + "</div></th></tr>";
        }
        table = table + "<tr><th>Player Name</th><th>Played Matches</th><th>Innings</th><th>Runs (Balls)</th><th>Average</th><th>Striker Rate</th><th>High Score</th><th>Not outs</th><th>6's</th><th>4's</th><th>3's</th><th>2's</th><th>1's</th><th>0's</th><th>50's</th><th>100's</th></tr>";
        for (dtoOverallBatsStat row : this.list) {
            if (row.hasDetails() && row.getShowDetails()) {
                table = ((((((((((((((((table + "<tr><th>" + row.getStrikerName().toUpperCase(Locale.getDefault()) + "</th>") + "<td>" + row.getMatches() + "</td>") + "<td>" + row.getInnings() + "</td>") + "<td>" + row.getRuns() + "(" + row.getBalls() + ")</td>") + "<td>" + row.getAverage() + "</td>") + "<td>" + row.getStrikeRate() + "</td>") + "<td>" + row.getHighScore() + "</td>") + "<td>" + row.getNotOuts() + "</td>") + "<td>" + row.getSixes() + "</td>") + "<td>" + row.getFours() + "</td>") + "<td>" + row.getThrees() + "</td>") + "<td>" + row.getTwos() + "</td>") + "<td>" + row.getOnes() + "</td>") + "<td>" + row.getDots() + "</td>") + "<td>" + row.getFifties() + "</td>") + "<td>" + row.getTons() + "</td>") + "</tr>";
            }
        }
        return table + "</table>";
    }
}
