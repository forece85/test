package com.ganapathy.cricscorer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class ConfirmStartMatch extends BaseActivity {
    private boolean restrictBpo = false;

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_confirm_start_match);
            initializeControls();
        } catch (Exception e) {
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        try {
            if (this.restrictBpo) {
                ((TextView) findViewById(C0252R.id.warningRPOText)).setVisibility(0);
            } else {
                ((TextView) findViewById(C0252R.id.warningRPOText)).setVisibility(8);
            }
        } catch (Exception e) {
            finish();
        }
    }

    protected void initializeControls() {
        DatabaseHandler db = new DatabaseHandler(this);
        dtoAdditionalSettings settings = db.getDefaultAdditionalSettings();
        db.close();
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        ((TextView) findViewById(C0252R.id.textSettings)).setText(refMatch.getMatchID());
        TableLayout tblSettings = (TableLayout) findViewById(C0252R.id.tableSettings);
        tblSettings.addView(createTableRow(700, getString(C0252R.string.oversText) + ":", "" + (refMatch.getOvers() == 4479 ? getString(C0252R.string.unlimitedText) : Integer.valueOf(refMatch.getOvers()))), new LayoutParams(-1, -2));
        int startId = 700 + 2;
        if (refMatch.getNoOfInngs() > 1) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.inngsPerSideText) + ":", "" + refMatch.getNoOfInngs()), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (refMatch.getNoOfDays() > 1) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.noOfDaysText) + ":", "" + refMatch.getNoOfDays()), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getWide() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.wideText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getWideReball() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.wideReballText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getNoball() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.noballText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getNoballReball() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.noballReballText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getLegbyes() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.legByesText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getByes() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.byesText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getLbw() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.lbwText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getWagonWheel() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.wagonWheelText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getWwForDotBall() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.wwForDotBallText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getWwForWicket() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.wwForWicketText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getBallSpot() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.ballSpotText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getPresetTeams() == 0) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.presetTeamsText) + ":", getString(C0252R.string.noText)), new LayoutParams(-1, -2));
            startId += 2;
        }
        if (settings.getRestrictBpo() == 1) {
            tblSettings.addView(createTableRow(startId, getString(C0252R.string.restrictBallsText) + ":", getString(C0252R.string.yesText)), new LayoutParams(-1, -2));
            startId += 2;
            this.restrictBpo = true;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onBack(View view) {
        finish();
    }

    public void onYes(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        String matchId = refMatch.getMatchID();
        int runningNo = 1;
        while (db.checkMatchExists(refMatch.getMatchID())) {
            int runningNo2 = runningNo + 1;
            refMatch.setMatchID(matchId + "_" + runningNo);
            runningNo = runningNo2;
        }
        refMatch.setAdditionalSettings(db.getDefaultAdditionalSettings());
        dtoTeamPlayerList team1Players = refMatch.getTeam1Players();
        dtoTeamPlayerList team2Players = refMatch.getTeam2Players();
        int i = 0;
        while (i < team1Players.size()) {
            if (((dtoTeamPlayer) team1Players.get(i)).getIsChecked()) {
                i++;
            } else {
                team1Players.remove(i);
            }
        }
        i = 0;
        while (i < team2Players.size()) {
            if (((dtoTeamPlayer) team2Players.get(i)).getIsChecked()) {
                i++;
            } else {
                team2Players.remove(i);
            }
        }
        if (team1Players.size() <= 0) {
            refMatch.setTeam1Players(db.getMinimumTeam1Players());
        }
        if (team2Players.size() <= 0) {
            refMatch.setTeam2Players(db.getMinimumTeam2Players());
        }
        db.createMatch(refMatch);
        db.close();
        CreateMatch.hInstance.sendEmptyMessage(0);
        startActivity(new Intent(this, Scorecard.class));
        finish();
    }

    private TableRow createTableRow(int id, String caption, String text) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(-1, -2));
        tr.addView(createTextView(id, caption));
        tr.addView(createTextView(id + 1, text));
        return tr;
    }

    private TextView createTextView(int id, String value) {
        TextView txt = new TextView(this);
        txt.setId(id);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -2);
        layoutParams.setMargins(2, 2, 0, 2);
        txt.setLayoutParams(layoutParams);
        txt.setText(value);
        return txt;
    }
}
