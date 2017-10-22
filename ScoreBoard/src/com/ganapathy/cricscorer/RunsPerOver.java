package com.ganapathy.cricscorer;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class RunsPerOver extends BaseActivity {
    private boolean _hiResImage = false;
    private int _lastSelectedInng = -1;
    private int _lastSelectedTeam = -1;
    private RunsPerOverGraph graph = null;

    class C02541 implements OnItemSelectedListener {
        C02541() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (RunsPerOver.this._lastSelectedTeam != position) {
                RunsPerOver.this._lastSelectedTeam = position;
                if (RunsPerOver.this.getIntent().hasExtra("SelectedTeam")) {
                    RunsPerOver.this.getIntent().removeExtra("SelectedTeam");
                }
                RunsPerOver.this.getIntent().putExtra("SelectedTeam", position);
                RunsPerOver.this.plotGraph();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02552 implements OnItemSelectedListener {
        C02552() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (RunsPerOver.this._lastSelectedInng != position) {
                RunsPerOver.this._lastSelectedInng = position;
                if (RunsPerOver.this.getIntent().hasExtra("SelectedInng")) {
                    RunsPerOver.this.getIntent().removeExtra("SelectedInng");
                }
                RunsPerOver.this.getIntent().putExtra("SelectedInng", position);
                RunsPerOver.this.plotGraph();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_runs_per_over);
        } catch (Exception e) {
            finish();
        }
    }

    public boolean hideWindowTitle() {
        return true;
    }

    public boolean makeFullScreen() {
        return true;
    }

    public void onResume() {
        super.onResume();
        try {
            populateSpinners();
            Spinner teamSpinner = (Spinner) findViewById(C0252R.id.spinnerTeams);
            Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
            if (getIntent().hasExtra("SelectedTeam")) {
                teamSpinner.setSelection(getIntent().getExtras().getInt("SelectedTeam"));
            }
            if (getIntent().hasExtra("SelectedInng")) {
                inngSpinner.setSelection(getIntent().getExtras().getInt("SelectedInng"));
            }
            plotGraph();
        } catch (Exception e) {
            finish();
        }
    }

    public void onStop() {
        super.onStop();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0252R.menu.activity_runs_per_over, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(C0252R.id.menu_hiResRPO).setChecked(this._hiResImage);
        if (VERSION.SDK_INT < 14) {
            if (this._hiResImage) {
                menu.findItem(C0252R.id.menu_hiResRPO).setTitle(getString(C0252R.string.hiResText) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_hiResRPO).setTitle(getString(C0252R.string.hiResText));
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.menu_send:
                sendImage();
                break;
            case C0252R.id.menu_close:
                onBackPressed();
                break;
            case C0252R.id.menu_hiResRPO:
                this._hiResImage = !this._hiResImage;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Utility.cleanupTempFiles(this);
        super.onBackPressed();
    }

    public void onOptions(View view) {
        try {
            openOptionsMenu();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    public void onSend(View view) {
        sendImage();
    }

    public void onBack(View view) {
        onBackPressed();
    }

    protected void plotGraph() {
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
        DatabaseHandler db = new DatabaseHandler(this);
        List<dtoRunsPerOver> runsPerOver = db.getRunsPerOver(refMatch.getMatchID(), team, inng);
        db.close();
        if (runsPerOver.size() > 0) {
            int maxOvers = refMatch.getOvers() == 4479 ? Math.max(5, runsPerOver.size()) : refMatch.getOvers();
            if (runsPerOver.size() < maxOvers) {
                for (int blankOver = runsPerOver.size(); blankOver < maxOvers; blankOver++) {
                    runsPerOver.add(new dtoRunsPerOver(blankOver, 0, 0, 0));
                }
            }
            float[] values = generateValues(refMatch, runsPerOver);
            float[] wickets = generateWickets(refMatch, runsPerOver);
            String[] overLabels = generateOverLabels(refMatch, runsPerOver);
            ChartAttributes chartAttributes = getChartAttributes(refMatch, team, inng);
            PathAttributes pathAttributes = getPathAttributes(refMatch, team);
            ImageView graphImage = (ImageView) findViewById(C0252R.id.graphyLayout);
            graphImage.setBackgroundColor(Utility.getThemeBackColor(this));
            this.graph = new RunsPerOverGraph(this, values, overLabels, wickets, chartAttributes, pathAttributes);
            Utility.plottedBitmap = this.graph.createGraph();
            graphImage.setImageBitmap(Utility.plottedBitmap);
            return;
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Data not yet available!", 0);
        toast.setGravity(16, 0, 0);
        toast.show();
        finish();
    }

    private void populateSpinners() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String firstBattedTeamName = MatchHelper.getTeamName(refMatch, firstBatted);
        String secondBattedTeamName = MatchHelper.getTeamName(refMatch, MatchHelper.getSecondBatted(refMatch));
        Spinner teamSpinner = (Spinner) findViewById(C0252R.id.spinnerTeams);
        Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
        List<String> teams = new ArrayList();
        teams.add(firstBattedTeamName);
        if (refMatch.getCurrentSession() > 1) {
            teams.add(secondBattedTeamName);
        }
        ArrayAdapter<String> teamsAdapter = new ArrayAdapter(this, 17367048, teams);
        teamsAdapter.setDropDownViewResource(17367049);
        teamSpinner.setAdapter(teamsAdapter);
        if (firstBatted.equalsIgnoreCase(refMatch.getCurrentBattingTeamNo())) {
            teamSpinner.setSelection(0);
        } else {
            teamSpinner.setSelection(1);
        }
        teamSpinner.setOnItemSelectedListener(new C02541());
        List<String> innings = new ArrayList();
        innings.add("1st Inngs");
        if (refMatch.getNoOfInngs() > 1 && refMatch.getCurrentSession() > 2) {
            innings.add("2nd Inngs");
        }
        ArrayAdapter<String> inngsAdapter = new ArrayAdapter(this, 17367048, innings);
        inngsAdapter.setDropDownViewResource(17367049);
        inngSpinner.setAdapter(inngsAdapter);
        if (refMatch.getCurrentInningsNo() == 1) {
            inngSpinner.setSelection(0);
        } else {
            inngSpinner.setSelection(1);
        }
        inngSpinner.setOnItemSelectedListener(new C02552());
        if (refMatch.getNoOfInngs() <= 1) {
            inngSpinner.setEnabled(false);
        }
    }

    private float[] generateValues(dtoMatch refMatch, List<dtoRunsPerOver> runsPerOver) {
        float[] values = new float[runsPerOver.size()];
        for (int counter = 0; counter < runsPerOver.size(); counter++) {
            values[counter] = (float) ((dtoRunsPerOver) runsPerOver.get(counter)).getRuns();
        }
        return values;
    }

    private float[] generateWickets(dtoMatch refMatch, List<dtoRunsPerOver> runsPerOver) {
        float[] values = new float[runsPerOver.size()];
        for (int counter = 0; counter < runsPerOver.size(); counter++) {
            values[counter] = (float) ((dtoRunsPerOver) runsPerOver.get(counter)).getWickets();
        }
        return values;
    }

    private String[] generateOverLabels(dtoMatch refMatch, List<dtoRunsPerOver> runsPerOver) {
        String[] values = new String[runsPerOver.size()];
        for (int counter = 0; counter < runsPerOver.size(); counter++) {
            values[counter] = "" + (((dtoRunsPerOver) runsPerOver.get(counter)).getOverNo() + 1);
        }
        return values;
    }

    private ChartAttributes getChartAttributes(dtoMatch refMatch, String team, String inng) {
        dtoMatchStats matchStatTeam = MatchHelper.getMatchScore(getApplicationContext(), refMatch, team, inng);
        ChartAttributes chartAttributes = new ChartAttributes();
        try {
            chartAttributes.setBackgroundColor("#" + Integer.toHexString(Utility.getThemeBackColor(this)));
            String hexVal = Integer.toHexString(Utility.getThemeTextColor(this));
            chartAttributes.setAxisColor("#" + hexVal);
            chartAttributes.setAxisNameColor("#" + hexVal);
        } catch (Exception e) {
            chartAttributes.setBackgroundColor("#000000");
            chartAttributes.setAxisColor("#ffffffff");
            chartAttributes.setAxisNameColor("#ffffff");
        }
        chartAttributes.setGridColor("#bbbbbbbb");
        chartAttributes.setX_unit_name("Overs");
        chartAttributes.setY_unit_name("Runs");
        int cut = refMatch.getDateTime().indexOf(32);
        StringBuilder append = new StringBuilder().append("vs ").append(Utility.toDisplayCase(getOpponentTeamName(refMatch, team))).append(" on ");
        String dateTime = refMatch.getDateTime();
        if (cut <= 0) {
            cut = Math.min(8, refMatch.getDateTime().length());
        }
        chartAttributes.setChart_title(append.append(dateTime.substring(0, cut)).toString());
        chartAttributes.setChart_title2(Utility.toDisplayCase(getTeamName(refMatch, team)) + " - " + matchStatTeam.getTotalScore() + "/" + matchStatTeam.getWicketCount() + " in " + matchStatTeam.getOverNo() + "." + matchStatTeam.getBallNo() + " Overs");
        return chartAttributes;
    }

    private PathAttributes getPathAttributes(dtoMatch refMatch, String team) {
        PathAttributes pathAttributes = new PathAttributes();
        if (team.equalsIgnoreCase("1")) {
            pathAttributes.setPathColor("#ff0000ff");
            pathAttributes.setPointColor("#ffff0000");
        } else {
            pathAttributes.setPathColor("#ffff0000");
            pathAttributes.setPointColor("#ff0000ff");
        }
        return pathAttributes;
    }

    private String getTeamName(dtoMatch refMatch, String team) {
        if (team.equalsIgnoreCase("1")) {
            return refMatch.getTeam1Name();
        }
        return refMatch.getTeam2Name();
    }

    private String getOpponentTeamName(dtoMatch refMatch, String team) {
        if (team.equalsIgnoreCase("1")) {
            return refMatch.getTeam2Name();
        }
        return refMatch.getTeam1Name();
    }

    protected void sendImage() {
        Utility.showNotSupportedAlert(this);
    }
}
