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

public class OverallBowlerStats extends BaseActivity {
    public boolean _firstTeamsLoad = true;
    private String _matchID;
    public boolean _showAll = false;
    private int _sort;
    protected List<dtoOverallBowlerStats> list;
    protected List<dtoTeam> teams;
    protected List<dtoTeam> tournaments;

    class C02491 implements OnItemSelectedListener {
        C02491() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
            if (OverallBowlerStats.this._firstTeamsLoad) {
                OverallBowlerStats.this._firstTeamsLoad = false;
            } else {
                OverallBowlerStats.this.populatePlayers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02502 implements OnItemSelectedListener {
        C02502() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
            OverallBowlerStats.this.populatePlayers();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02513 extends AsyncTask<String, Void, Void> {
        DatabaseHandler db = new DatabaseHandler(OverallBowlerStats.this.getApplicationContext());
        private ProgressDialog pd;

        C02513() {
        }

        protected void onPreExecute() {
            this.pd = new ProgressDialog(OverallBowlerStats.this);
            this.pd.setTitle("Calculating...");
            this.pd.setMessage("Please wait.");
            this.pd.setCancelable(false);
            this.pd.setIndeterminate(true);
            this.pd.show();
        }

        protected Void doInBackground(String... strings) {
            try {
                if (OverallBowlerStats.this._showAll) {
                    OverallBowlerStats.this.list = this.db.getOverallBowlerStats(strings[0], strings[1], strings[2], strings[3]);
                } else {
                    OverallBowlerStats.this.list = this.db.getOverallBowlerStatsPlayerNames(strings[0], strings[1], strings[2], strings[3]);
                }
                OverallBowlerStats.this._showAll = false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.db.close();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            this.pd.dismiss();
            if (OverallBowlerStats.this.list == null || OverallBowlerStats.this.list.isEmpty()) {
                ((TextView) OverallBowlerStats.this.findViewById(C0252R.id.noDataFoundText)).setVisibility(0);
            } else {
                ((TextView) OverallBowlerStats.this.findViewById(C0252R.id.noDataFoundText)).setVisibility(8);
                if (OverallBowlerStats.this._sort == 2) {
                    Collections.sort(OverallBowlerStats.this.list, new SortDescending());
                } else {
                    Collections.sort(OverallBowlerStats.this.list, new SortAscending());
                }
            }
            ((ListView) OverallBowlerStats.this.findViewById(C0252R.id.overallBowlersListView)).setAdapter(new OverallBowlerStatsAdapter(OverallBowlerStats.this, OverallBowlerStats.this.list));
        }
    }

    private class SortAscending implements Comparator<dtoOverallBowlerStats> {
        private SortAscending() {
        }

        public int compare(dtoOverallBowlerStats o1, dtoOverallBowlerStats o2) {
            return o1.getBowlerName().compareToIgnoreCase(o2.getBowlerName());
        }
    }

    private class SortDescending implements Comparator<dtoOverallBowlerStats> {
        private SortDescending() {
        }

        public int compare(dtoOverallBowlerStats o1, dtoOverallBowlerStats o2) {
            return o2.getBowlerName().compareToIgnoreCase(o1.getBowlerName());
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_overall_bowler_stats);
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
            screenTitle.setText(getString(C0252R.string.overallBowlerText));
            spinTeams.setVisibility(0);
            spinTournament.setVisibility(0);
            labelTeams.setVisibility(0);
            labelTournaments.setVisibility(0);
            populateSpinner();
        } else {
            screenTitle.setText(getString(C0252R.string.overallBowlerText));
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
        spinTeam.setOnItemSelectedListener(new C02491());
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
        spinTournament.setOnItemSelectedListener(new C02502());
    }

    public void asyncPopulatePlayers(String matchID, String teamName, String tournamentName, String searchText) {
        new C02513().execute(new String[]{matchID, teamName, tournamentName, searchText});
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

    public void onBowlerClick(View v) {
        dtoTeam item;
        Spinner spinTeam = (Spinner) findViewById(C0252R.id.spinTeams);
        Spinner spinTournament = (Spinner) findViewById(C0252R.id.spinTournaments);
        Button currentSelection = (Button) v;
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
            ListView mainListView = (ListView) findViewById(C0252R.id.overallBowlersListView);
            dtoOverallBowlerStats item2 = (dtoOverallBowlerStats) this.list.get(Integer.parseInt("" + currentSelection.getTag()));
            if (item2.hasDetails()) {
                item2.setShowDetails(!item2.getShowDetails());
                mainListView.invalidateViews();
                return;
            }
            DatabaseHandler db = new DatabaseHandler(this);
            dtoOverallBowlerStats stat = db.getOverallBowlerStatsDetails(this._matchID, teamName, tournamentName, item2.getBowlerName());
            db.close();
            item2.copy(stat);
            item2.hasDetails(true);
            item2.setShowDetails(true);
            mainListView.invalidateViews();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onShare(View view) {
        try {
            File fileReport = new File(Utility.CreateFileFromString(this, createOverallBowlerStatsReport(this._matchID), "BowlStats.html"));
            Intent browserIntent = new Intent(this, DisplayWebView.class);
            browserIntent.putExtra("title", getString(C0252R.string.overallBowlerText));
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

    protected String createOverallBowlerStatsReport(String matchID) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        String data = ((("" + "<html>") + Utility.createStyleSheet()) + "<body style=\"width:800px\">") + "<div class=\"CSSTableGenerator\">";
        if (this._matchID.trim().equalsIgnoreCase("")) {
            data = data + "<h3><u>OVERALL BOWLER STATISTICS</u></h3>";
        } else {
            data = (data + "<h3><u>BOWLER STATISTICS OF THE MATCH - " + refMatch.getMatchID() + "</u></h3>") + "<h3><u>MATCH BETWEEN " + refMatch.getTeam1Name() + " vs " + refMatch.getTeam2Name() + " - " + refMatch.getOvers() + " Overs</u></h3>";
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
        if (this.list.isEmpty()) {
            ((TextView) findViewById(C0252R.id.noDataFoundText)).setVisibility(0);
        } else {
            ((TextView) findViewById(C0252R.id.noDataFoundText)).setVisibility(8);
            if (this._sort == 2) {
                Collections.sort(this.list, new SortDescending());
            } else {
                Collections.sort(this.list, new SortAscending());
            }
        }
        String table = "<table>";
        if (!teamName.trim().equalsIgnoreCase("")) {
            table = table + "<tr><th colspan=\"15\"><div width=\"100%\">TEAM NAME: " + teamName + "</div></th></tr>";
        }
        table = table + "<tr><th>Bowler Name</th><th>Played Matches</th><th>Innings</th><th>Overs</th><th>Runs</th><th>Run Rate</th><th>Wickets</th><th>Average</th><th>Best</th><th>Maidens</th><th>Wide</th><th>Noball</th><th>6's</th><th>4's</th><th>0's</th></tr>";
        for (dtoOverallBowlerStats row : this.list) {
            if (row.hasDetails() && row.getShowDetails()) {
                table = (((((((((((((((table + "<tr><th>" + Utility.toDisplayCase(row.getBowlerName()) + "</th>") + "<td>" + row.getMatches() + "</td>") + "<td>" + row.getInnings() + "</td>") + "<td>" + row.getOvers() + "</td>") + "<td>" + row.getRuns() + "</td>") + "<td>" + row.getRunRate() + "</td>") + "<td>" + row.getWickets() + "</td>") + "<td>" + row.getAverage() + "</td>") + "<th>" + row.getBowlerBest() + "</th>") + "<td>" + row.getMaidens() + "</td>") + "<td>" + row.getWides() + "</td>") + "<td>" + row.getNoballs() + "</td>") + "<td>" + row.getSixes() + "</td>") + "<td>" + row.getFours() + "</td>") + "<td>" + row.getDots() + "</td>") + "</tr>";
            }
        }
        return table + "</table>";
    }
}
