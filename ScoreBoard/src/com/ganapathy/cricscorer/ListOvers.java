package com.ganapathy.cricscorer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ListOvers extends BaseActivity {
    private int _lastSelectedInng = -1;
    private int _lastSelectedTeam = -1;
    private int _selectedBall = -1;
    private boolean _suppress = true;
    protected ListOversAdapter listAdapter;
    protected List<dtoBall> oversList;

    class C02331 implements OnItemSelectedListener {
        C02331() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (ListOvers.this._lastSelectedTeam != position) {
                ListOvers.this._lastSelectedTeam = position;
                ListOvers.this.populateOvers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02342 implements OnItemSelectedListener {
        C02342() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (ListOvers.this._lastSelectedInng != position) {
                ListOvers.this._lastSelectedInng = position;
                ListOvers.this.populateOvers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02353 implements OnItemClickListener {
        C02353() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ListOvers.this.listAdapter.setSelectedPosition(position);
            ListOvers.this._selectedBall = ((dtoBall) ListOvers.this.oversList.get(position)).TotalBallNo;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_list_overs);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    public boolean hideWindowTitle() {
        return true;
    }

    public void onBackPressed() {
        setResult(0, new Intent());
        finish();
        overridePendingTransition(0, 0);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onOK(View view) {
        Intent resultData = new Intent();
        resultData.putExtra("currentBall", this._selectedBall);
        setResult(-1, resultData);
        finish();
        overridePendingTransition(0, 0);
    }

    public void onResume() {
        try {
            super.onResume();
            setContentView(C0252R.layout.activity_list_overs);
            this._suppress = true;
            initializeControls();
            this._suppress = false;
            populateOvers();
        } catch (Exception e) {
            finish();
        }
    }

    public void initializeControls() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        Spinner teamSpinner = (Spinner) findViewById(C0252R.id.spinnerTeams);
        Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
        ((TextView) findViewById(C0252R.id.instructionText)).setText("Select a ball and Click:");
        List<String> teams = new ArrayList();
        teams.add(MatchHelper.getTeamName(refMatch, MatchHelper.getFirstBatted(refMatch)));
        if (refMatch.getCurrentSession() > 1) {
            teams.add(MatchHelper.getTeamName(refMatch, MatchHelper.getSecondBatted(refMatch)));
        }
        ArrayAdapter<String> teamsAdapter = new ArrayAdapter(this, 17367048, teams);
        teamsAdapter.setDropDownViewResource(17367049);
        teamSpinner.setAdapter(teamsAdapter);
        teamSpinner.setOnItemSelectedListener(new C02331());
        List<String> innings = new ArrayList();
        innings.add("1st Inngs");
        if (refMatch.getNoOfInngs() > 1) {
            innings.add("2nd Inngs");
        }
        ArrayAdapter<String> inngsAdapter = new ArrayAdapter(this, 17367048, innings);
        inngsAdapter.setDropDownViewResource(17367049);
        inngSpinner.setAdapter(inngsAdapter);
        inngSpinner.setOnItemSelectedListener(new C02342());
        if (refMatch.getNoOfInngs() <= 1) {
            inngSpinner.setEnabled(false);
        }
    }

    public void populateOvers() {
        if (!this._suppress) {
            Spinner teamSpinner = (Spinner) findViewById(C0252R.id.spinnerTeams);
            Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
            dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
            dtoScoreBall lastBall = refMatch.getLastScoreball();
            if (this._lastSelectedTeam == -1 || this._lastSelectedInng == -1) {
                int i;
                this._suppress = true;
                if (lastBall.getTeamNo().equalsIgnoreCase("1")) {
                    i = 0;
                } else {
                    i = 1;
                }
                this._lastSelectedTeam = i;
                teamSpinner.setSelection(this._lastSelectedTeam);
                if (lastBall.getInningNo() == 1) {
                    i = 0;
                } else {
                    i = 1;
                }
                this._lastSelectedInng = i;
                inngSpinner.setSelection(this._lastSelectedInng);
                this._suppress = false;
            }
            DatabaseHandler db = new DatabaseHandler(this);
            this.oversList = db.getOversList(refMatch.getMatchID(), "" + (this._lastSelectedTeam + 1), "" + (this._lastSelectedInng + 1));
            db.close();
            if (this.oversList == null || this.oversList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Innings might not started yet!", 1).show();
            }
            ListView mainListView = (ListView) findViewById(C0252R.id.oversListView);
            this.listAdapter = new ListOversAdapter(this, this.oversList);
            mainListView.setAdapter(this.listAdapter);
            mainListView.setOnItemClickListener(new C02353());
        }
    }
}
