package com.ganapathy.cricscorer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EditBall extends BaseActivity {
    private List<String> _allPlayerList;
    private int _currentBallNo = 0;
    private dtoScoreBall _lastBall = null;
    private int _lastBallNo = 0;
    private dtoScoreBall currentBall = null;
    private PlayerListAdapter team1Adapter;
    private PlayerListAdapter team2Adapter;

    class C02221 implements OnItemSelectedListener {
        C02221() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
            if (EditBall.this.currentBall != null) {
                EditBall.this.refreshRuns();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02232 implements OnClickListener {
        C02232() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EditBall.this.insertBall(false);
        }
    }

    class C02243 implements OnClickListener {
        C02243() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EditBall.this.saveScorecard();
        }
    }

    class C02254 implements OnClickListener {
        C02254() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EditBall.this.asyncDeleteBallAndUpdate();
        }
    }

    class C02265 extends AsyncTask<Boolean, Void, Void> {
        private ProgressDialog pd;

        C02265() {
        }

        protected void onPreExecute() {
            this.pd = new ProgressDialog(EditBall.this);
            this.pd.setTitle("Updating Scorecard...");
            this.pd.setMessage("Please wait.. It may take a min...");
            this.pd.setCancelable(false);
            this.pd.setIndeterminate(true);
            this.pd.show();
        }

        protected Void doInBackground(Boolean... arg0) {
            try {
                EditBall.this.updateScorecardAfterDelete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            this.pd.dismiss();
            EditBall.this.disableEditables();
            EditBall.this._lastBall = EditBall.this.getLastMatchBall("" + EditBall.this.currentBall.getTeamNo());
            EditBall.this._lastBallNo = EditBall.this._lastBall.getTotalBallNo();
            EditBall.this.populateBall();
        }
    }

    class C02276 extends AsyncTask<Boolean, Void, Void> {
        private ProgressDialog pd;

        C02276() {
        }

        protected void onPreExecute() {
            this.pd = new ProgressDialog(EditBall.this);
            this.pd.setTitle("Updating Scorecard...");
            this.pd.setMessage("Please wait.. It may take a min...");
            this.pd.setCancelable(false);
            this.pd.setIndeterminate(true);
            this.pd.show();
        }

        protected Void doInBackground(Boolean... arg0) {
            try {
                EditBall.this.updateScorecard(Boolean.valueOf("" + arg0[0]).booleanValue(), Boolean.valueOf("" + arg0[1]).booleanValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            this.pd.dismiss();
            EditBall.this.disableEditables();
            EditBall.this._lastBall = EditBall.this.getLastMatchBall("" + EditBall.this.currentBall.getTeamNo());
            EditBall.this._lastBallNo = EditBall.this._lastBall.getTotalBallNo();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_edit_ball);
            initializeControls();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        registerForContextMenu((Button) findViewById(C0252R.id.buttonInsert));
        ((LinearLayout) findViewById(C0252R.id.focusLayout)).requestFocus();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        dtoAdditionalSettings settings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == C0252R.id.buttonInsert) {
            inflater.inflate(C0252R.menu.insertball_context_menu, menu);
            menu.findItem(C0252R.id.miInsert).setTitle("Insert (" + this.currentBall.getOverNo() + "." + (this.currentBall.getBallNo() + 1) + ") and move rest");
            menu.findItem(C0252R.id.miDuplicate).setTitle("Duplicate ball (" + this.currentBall.getOverNo() + "." + this.currentBall.getBallNo() + ")");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.miInsert:
                insertBall(false);
                return true;
            case C0252R.id.miDuplicate:
                insertBall(true);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onBackPressed() {
        setResult(0, new Intent());
        finish();
        overridePendingTransition(0, 0);
    }

    protected void initializeControls() {
        DatabaseHandler db;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoScoreBall ball = refMatch.getLastScoreball();
        if (ball.getOverNo() == 0 && ball.getBallNo() == 0) {
            db = new DatabaseHandler(this);
            ball = db.getLastScoreBall(refMatch.getMatchID());
            db.close();
        }
        this._currentBallNo = ball.getTotalBallNo();
        disableEditables();
        ((TextView) findViewById(C0252R.id.labelBowler)).setText(getString(C0252R.string.bowlerText) + " (edit to update)");
        ((TextView) findViewById(C0252R.id.extrasLabel)).setText(getString(C0252R.string.extrasText) + ": ");
        ((Spinner) findViewById(C0252R.id.spinRuns)).setOnItemSelectedListener(new C02221());
        try {
            this._lastBall = (dtoScoreBall) ball.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this._lastBallNo = ball.getTotalBallNo();
        populateWicketTypes();
        db = new DatabaseHandler(this);
        ball = db.getMatchBall(refMatch.getMatchID(), "" + this._currentBallNo);
        db.close();
        if (ball != null) {
            this._currentBallNo = ball.getTotalBallNo();
            populateBall();
        }
    }

    public void populateTeam1Spinner(int batOrBowl) {
        dtoTeamPlayerList strikerNames;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        if (refMatch.isTeam1Batting()) {
            strikerNames = db.getMatchTeamPlayersWithDetails(refMatch.getMatchID(), getString(C0252R.string.dbTeam1));
            strikerNames.add(0, new dtoTeamPlayer(refMatch.getTeam1Name(), ""));
        } else {
            strikerNames = db.getMatchTeamPlayersWithDetails(refMatch.getMatchID(), getString(C0252R.string.dbTeam2));
            strikerNames.add(0, new dtoTeamPlayer(refMatch.getTeam2Name(), ""));
        }
        db.close();
        if (strikerNames.size() > 0) {
            this.team1Adapter = new PlayerListAdapter(this, strikerNames, true, batOrBowl);
        }
    }

    public void populateTeam2Spinner(int batOrBowl) {
        dtoTeamPlayerList bowlerNames;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        if (refMatch.isTeam1Batting()) {
            bowlerNames = db.getMatchTeamPlayersWithDetails(refMatch.getMatchID(), getString(C0252R.string.dbTeam2));
            bowlerNames.add(0, new dtoTeamPlayer(refMatch.getTeam2Name(), ""));
        } else {
            bowlerNames = db.getMatchTeamPlayersWithDetails(refMatch.getMatchID(), getString(C0252R.string.dbTeam1));
            bowlerNames.add(0, new dtoTeamPlayer(refMatch.getTeam2Name(), ""));
        }
        db.close();
        if (bowlerNames.size() > 0) {
            this.team2Adapter = new PlayerListAdapter(this, bowlerNames, true, batOrBowl);
        }
    }

    public void populateTeamAutoComplete() {
        DatabaseHandler db = new DatabaseHandler(this);
        this._allPlayerList = db.getConsolidatedTeamPlayers();
        db.close();
    }

    private void populateWicketTypes() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        List<String> wicketTypes = new ArrayList(Arrays.asList(getResources().getStringArray(C0252R.array.wicketAs)));
        List<String> filteredWicketTypes = new ArrayList();
        dtoAdditionalSettings addlSettings = refMatch.getAdditionalSettings();
        for (String wicketType : wicketTypes) {
            if (!wicketType.equals("LBW")) {
                filteredWicketTypes.add(wicketType);
            } else if (addlSettings.getLbw() == 1) {
                filteredWicketTypes.add(wicketType);
            }
        }
        ArrayAdapter<String> wktTypeAdapter = new ArrayAdapter(this, 17367048, filteredWicketTypes);
        wktTypeAdapter.setDropDownViewResource(17367049);
        ((Spinner) findViewById(C0252R.id.spinWicketAs)).setAdapter(wktTypeAdapter);
    }

    protected dtoScoreBall getMatchBall(int ballNo) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        dtoScoreBall ball = db.getMatchBall(refMatch.getMatchID(), "" + ballNo);
        db.close();
        return ball;
    }

    protected dtoScoreBall getLastMatchBall(String team) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        dtoScoreBall ball = db.getMatchBall(refMatch.getMatchID(), "" + db.getLastTotalBallNoOfInnings(refMatch.getMatchID(), team));
        db.close();
        return ball;
    }

    private void populateMatchTitle(dtoScoreBall ball) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        ((TextView) findViewById(C0252R.id.matchTitle)).setText((ball.getTeamNo().equalsIgnoreCase("1") ? refMatch.getTeam1Name() : refMatch.getTeam2Name()) + (refMatch.getNoOfInngs() > 1 ? "  Innings-" + ball.getInningNo() : " Innings") + (refMatch.getNoOfDays() > 1 ? "  Day-" + Math.min(ball.getDayNo(), refMatch.getNoOfDays()) : ""));
    }

    protected void populateBall() {
        if (this._currentBallNo > 0 && this._currentBallNo <= this._lastBallNo) {
            dtoAdditionalSettings settings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
            dtoScoreBall ball = getMatchBall(this._currentBallNo);
            if (ball == null) {
                Toast.makeText(getApplicationContext(), "Current Ball returned NULL! Re-Edit the ball.", 0).show();
                disableEditables();
                return;
            }
            this.currentBall = ball;
            populateMatchTitle(ball);
            ((TextView) findViewById(C0252R.id.textOvers)).setText(ball.getOverNo() + "." + ball.getBallNo() + " (" + ball.getTotalBallNo() + ")");
            ((TextView) findViewById(C0252R.id.editStriker)).setText(ball.getStrikerName());
            ((TextView) findViewById(C0252R.id.strikerScore)).setText("" + ball.getStrikerRuns());
            ((TextView) findViewById(C0252R.id.ballsStriker)).setText("" + ball.getBallsFacedStriker());
            ((TextView) findViewById(C0252R.id.editNonStriker)).setText(ball.getNonStrikerName());
            ((TextView) findViewById(C0252R.id.nonStrikerScore)).setText("" + ball.getNonStrikerRuns());
            ((TextView) findViewById(C0252R.id.ballsNonStriker)).setText("" + ball.getBallsFacedNonStriker());
            ((Button) findViewById(C0252R.id.buttonBowler)).setText(ball.getBowlerName());
            ((Spinner) findViewById(C0252R.id.spinRuns)).setSelection(ball.getRunsScored());
            ((TextView) findViewById(C0252R.id.extraRuns)).setText("" + ball.getTotalExtras());
            ((TextView) findViewById(C0252R.id.textScore)).setText("" + ball.getTotalScore() + "/" + ball.getWicketCount());
            ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(ball.isStrike());
            ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(ball.getIsWide());
            ((CheckBox) findViewById(C0252R.id.noballCheckBox)).setChecked(ball.getIsNoball());
            ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setChecked(ball.getIsLegByes());
            ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setChecked(ball.getIsByes());
            disableEditables();
            if (!ball.getIsWicket()) {
                findViewById(C0252R.id.bucketWicketAs).setVisibility(8);
            }
            ((Button) findViewById(C0252R.id.buttonEdit)).setEnabled(true);
            ((Button) findViewById(C0252R.id.buttonInsert)).setEnabled(true);
            ((Button) findViewById(C0252R.id.buttonDelete)).setEnabled(true);
            if (this.currentBall.getBallNo() > settings.getMaxBpo()) {
                ((Button) findViewById(C0252R.id.buttonDelete)).setEnabled(true);
            }
            if (ball.getIsWicket()) {
                ((TextView) findViewById(C0252R.id.textComment)).setText("(Wicket ball)");
                findViewById(C0252R.id.bucketWicketAs).setVisibility(0);
                Spinner spinWicketAs = (Spinner) findViewById(C0252R.id.spinWicketAs);
                spinWicketAs.setSelection(((ArrayAdapter) spinWicketAs.getAdapter()).getPosition(ball.getWicketHow()));
                ((Button) findViewById(C0252R.id.buttonWicketBy)).setText(ball.getWicketAssist());
                ((Button) findViewById(C0252R.id.buttonEdit)).setEnabled(true);
                ((Button) findViewById(C0252R.id.buttonDelete)).setEnabled(false);
                if (ball.getBallsThisOver() == 1) {
                    ((Button) findViewById(C0252R.id.buttonInsert)).setEnabled(false);
                }
            } else if (ball.isStrike()) {
                ((TextView) findViewById(C0252R.id.textComment)).setText("(Strike ball)");
            } else if (ball.getIsWide() || ball.getIsNoball()) {
                ((TextView) findViewById(C0252R.id.textComment)).setText("(Extra ball)");
            }
            ((ImageButton) findViewById(C0252R.id.buttonSwitchBats)).setTag("false");
            ((ImageButton) findViewById(C0252R.id.buttonSwitchBats)).setImageResource(C0252R.drawable.switch_bats_disabled);
        }
    }

    public void onPreviousBall(View view) {
        if (this._currentBallNo > 1) {
            this._currentBallNo--;
            populateBall();
        }
    }

    public void onNextBall(View view) {
        if (this._currentBallNo < this._lastBallNo) {
            this._currentBallNo++;
            populateBall();
        }
    }

    public void onStrike(View view) {
        UncheckAllBatStatus();
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(true);
        ((TextView) findViewById(C0252R.id.textComment)).setText("(Strike ball)");
        refreshRuns();
    }

    public void onWide(View view) {
        UncheckAllBatStatus();
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(true);
        ((TextView) findViewById(C0252R.id.textComment)).setText("(Extra ball)");
        refreshRuns();
    }

    public void onNoball(View view) {
        UncheckAllBatStatus();
        ((CheckBox) findViewById(C0252R.id.noballCheckBox)).setChecked(true);
        ((TextView) findViewById(C0252R.id.textComment)).setText("(Extra ball)");
        refreshRuns();
    }

    public void onLegByes(View view) {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setChecked(true);
        ((TextView) findViewById(C0252R.id.textComment)).setText("(Strike ball)");
        refreshRuns();
    }

    public void onByes(View view) {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setChecked(true);
        if (((CheckBox) findViewById(C0252R.id.noballCheckBox)).isChecked()) {
            ((TextView) findViewById(C0252R.id.textComment)).setText("(Extra ball)");
        } else {
            ((TextView) findViewById(C0252R.id.textComment)).setText("(Strike ball)");
        }
        refreshRuns();
    }

    public void onSwitch(View view) {
        TextView labelStriker = (TextView) findViewById(C0252R.id.editStriker);
        TextView labelNonStriker = (TextView) findViewById(C0252R.id.editNonStriker);
        TextView labelStrikerScore = (TextView) findViewById(C0252R.id.strikerScore);
        TextView labelNonStrikerScore = (TextView) findViewById(C0252R.id.nonStrikerScore);
        TextView labelStrikerBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        TextView labelNonStrikerBalls = (TextView) findViewById(C0252R.id.ballsNonStriker);
        ImageButton switchImage = (ImageButton) findViewById(C0252R.id.buttonSwitchBats);
        if (switchImage.getTag() == null || switchImage.getTag().toString().equalsIgnoreCase("false")) {
            switchImage.setTag("true");
        } else {
            switchImage.setTag("false");
        }
        String strikerName = labelStriker.getText().toString();
        labelStriker.setText(labelNonStriker.getText().toString());
        labelNonStriker.setText(strikerName);
        if (switchImage.getTag().toString().equalsIgnoreCase("true")) {
            switchImage.setImageResource(C0252R.drawable.switch_bats_red);
            int currentRuns = Integer.parseInt("0" + ((Spinner) findViewById(C0252R.id.spinRuns)).getSelectedItem().toString());
            if (this.currentBall.isStrike()) {
                if (((CheckBox) findViewById(C0252R.id.strikeCheckBox)).isChecked()) {
                    labelStrikerScore.setText("" + (this.currentBall.getNonStrikerRuns() + currentRuns));
                    labelNonStrikerScore.setText("" + (this.currentBall.getStrikerRuns() - this.currentBall.getRunsScored()));
                    labelStrikerBalls.setText("" + (this.currentBall.getBallsFacedNonStriker() + 1));
                    labelNonStrikerBalls.setText("" + (this.currentBall.getBallsFacedStriker() - 1));
                    return;
                }
                labelStrikerScore.setText("" + this.currentBall.getNonStrikerRuns());
                labelNonStrikerScore.setText("" + (this.currentBall.getStrikerRuns() - this.currentBall.getRunsScored()));
                labelStrikerBalls.setText("" + this.currentBall.getBallsFacedNonStriker());
                labelNonStrikerBalls.setText("" + (this.currentBall.getBallsFacedStriker() - 1));
                return;
            } else if (((CheckBox) findViewById(C0252R.id.strikeCheckBox)).isChecked()) {
                labelStrikerScore.setText("" + (this.currentBall.getNonStrikerRuns() + currentRuns));
                labelNonStrikerScore.setText("" + this.currentBall.getStrikerRuns());
                labelStrikerBalls.setText("" + (this.currentBall.getBallsFacedNonStriker() + 1));
                labelNonStrikerBalls.setText("" + this.currentBall.getBallsFacedStriker());
                return;
            } else {
                labelStrikerScore.setText("" + this.currentBall.getNonStrikerRuns());
                labelNonStrikerScore.setText("" + this.currentBall.getStrikerRuns());
                labelStrikerBalls.setText("" + this.currentBall.getBallsFacedNonStriker());
                labelNonStrikerBalls.setText("" + this.currentBall.getBallsFacedStriker());
                return;
            }
        }
        switchImage.setImageResource(C0252R.drawable.switch_bats);
        labelStrikerScore.setText("" + ((this.currentBall.getStrikerRuns() - this.currentBall.getRunsScored()) + Integer.parseInt("0" + ((Spinner) findViewById(C0252R.id.spinRuns)).getSelectedItem().toString())));
        labelNonStrikerScore.setText("" + this.currentBall.getNonStrikerRuns());
        labelStrikerBalls.setText("" + this.currentBall.getBallsFacedStriker());
        labelNonStrikerBalls.setText("" + this.currentBall.getBallsFacedNonStriker());
    }

    protected void UncheckAllBatStatus() {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.noballCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setChecked(false);
    }

    protected void disableEditables() {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setEnabled(false);
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setEnabled(false);
        ((CheckBox) findViewById(C0252R.id.noballCheckBox)).setEnabled(false);
        ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setEnabled(false);
        ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setEnabled(false);
        ((CheckBox) findViewById(C0252R.id.updateRestCheckBox)).setEnabled(false);
        ((ImageButton) findViewById(C0252R.id.buttonSwitchBats)).setEnabled(false);
        ((ImageButton) findViewById(C0252R.id.buttonSwitchBats)).setImageResource(C0252R.drawable.switch_bats_disabled);
        ((Spinner) findViewById(C0252R.id.spinRuns)).setEnabled(false);
        ((Button) findViewById(C0252R.id.buttonSave)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonCancel)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonEdit)).setVisibility(0);
        ((Button) findViewById(C0252R.id.buttonInsert)).setVisibility(0);
        ((Button) findViewById(C0252R.id.buttonDelete)).setVisibility(0);
        ((Spinner) findViewById(C0252R.id.spinWicketAs)).setEnabled(false);
        ((Button) findViewById(C0252R.id.buttonWicketBy)).setEnabled(false);
        ((Button) findViewById(C0252R.id.buttonBowler)).setEnabled(false);
    }

    protected void enableEditables() {
        boolean z;
        dtoAdditionalSettings settings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setEnabled(settings.getWide() == 1);
        CheckBox checkBox = (CheckBox) findViewById(C0252R.id.wideCheckBox);
        if (settings.getWide() == 1) {
            z = true;
        } else {
            z = false;
        }
        checkBox.setEnabled(z);
        checkBox = (CheckBox) findViewById(C0252R.id.noballCheckBox);
        if (settings.getNoball() == 1) {
            z = true;
        } else {
            z = false;
        }
        checkBox.setEnabled(z);
        checkBox = (CheckBox) findViewById(C0252R.id.legbyesCheckBox);
        if (settings.getLegbyes() == 1) {
            z = true;
        } else {
            z = false;
        }
        checkBox.setEnabled(z);
        checkBox = (CheckBox) findViewById(C0252R.id.byesCheckBox);
        if (settings.getByes() == 1) {
            z = true;
        } else {
            z = false;
        }
        checkBox.setEnabled(z);
        ((ImageButton) findViewById(C0252R.id.buttonSwitchBats)).setEnabled(true);
        ((ImageButton) findViewById(C0252R.id.buttonSwitchBats)).setImageResource(C0252R.drawable.switch_bats);
        ((Spinner) findViewById(C0252R.id.spinRuns)).setEnabled(true);
        ((Button) findViewById(C0252R.id.buttonBowler)).setEnabled(true);
        ((Button) findViewById(C0252R.id.buttonEdit)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonInsert)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonDelete)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonSave)).setVisibility(0);
        ((Button) findViewById(C0252R.id.buttonCancel)).setVisibility(0);
    }

    protected void enableWicketEditables() {
        dtoAdditionalSettings settings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
        findViewById(C0252R.id.bucketWicketAs).setVisibility(0);
        ((Spinner) findViewById(C0252R.id.spinWicketAs)).setEnabled(true);
        ((Button) findViewById(C0252R.id.buttonWicketBy)).setEnabled(true);
        ((Button) findViewById(C0252R.id.buttonEdit)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonInsert)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonDelete)).setVisibility(8);
        ((Button) findViewById(C0252R.id.buttonSave)).setVisibility(0);
        ((Button) findViewById(C0252R.id.buttonCancel)).setVisibility(0);
    }

    protected void refreshRuns() {
        int strikerBalls;
        dtoAdditionalSettings settings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
        TextView labelStrikerScore = (TextView) findViewById(C0252R.id.strikerScore);
        TextView labelStrikerBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        TextView totalScore = (TextView) findViewById(C0252R.id.textScore);
        TextView totalExtras = (TextView) findViewById(C0252R.id.extraRuns);
        ImageButton switchImage = (ImageButton) findViewById(C0252R.id.buttonSwitchBats);
        int score = Integer.parseInt("0" + ((Spinner) findViewById(C0252R.id.spinRuns)).getSelectedItem().toString());
        int strikerRuns = switchImage.getTag().toString().equalsIgnoreCase("true") ? this.currentBall.getNonStrikerRuns() : this.currentBall.getStrikerRuns();
        if (switchImage.getTag().toString().equalsIgnoreCase("true")) {
            strikerBalls = this.currentBall.getBallsFacedNonStriker();
        } else {
            strikerBalls = this.currentBall.getBallsFacedStriker();
        }
        int calRuns = score - this.currentBall.getRunsScored();
        int extraRuns = this.currentBall.getTotalExtras();
        int calScore = 0;
        int calBalls = 0;
        if (((CheckBox) findViewById(C0252R.id.strikeCheckBox)).isChecked()) {
            calScore = score;
            if (!this.currentBall.getIsStrike()) {
                calRuns = score;
                calBalls = 0 + 1;
            }
            if (this.currentBall.getIsWide()) {
                extraRuns -= settings.getWideRun();
            } else if (this.currentBall.getIsNoball()) {
                extraRuns -= settings.getNoballRun();
            }
        } else if (((CheckBox) findViewById(C0252R.id.wideCheckBox)).isChecked()) {
            calScore = score + settings.getWideRun();
            if (this.currentBall.getIsStrike()) {
                calRuns = -this.currentBall.getRunsScored();
                extraRuns += settings.getWideRun() + score;
                calBalls = 0 - 1;
            }
        } else if (((CheckBox) findViewById(C0252R.id.noballCheckBox)).isChecked()) {
            calScore = score + settings.getNoballRun();
            if (this.currentBall.getIsStrike()) {
                calRuns = -this.currentBall.getRunsScored();
                extraRuns += settings.getNoballRun() + score;
                calBalls = 0 - 1;
            }
        } else if (((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).isChecked()) {
            calScore = score;
            if (this.currentBall.getIsWide() || this.currentBall.getIsNoball()) {
                if (!this.currentBall.getIsLegByes()) {
                    calBalls = 0 + 1;
                }
            } else if (this.currentBall.getIsStrike()) {
                calRuns = -this.currentBall.getRunsScored();
                extraRuns += score;
            }
        } else if (((CheckBox) findViewById(C0252R.id.byesCheckBox)).isChecked()) {
            calScore = score;
            calRuns = -this.currentBall.getRunsThisBall();
            if (this.currentBall.getIsWide() || this.currentBall.getIsNoball()) {
                if (!this.currentBall.getIsByes()) {
                    calBalls = 0 + 1;
                }
            } else if (this.currentBall.getIsStrike()) {
                calRuns = -this.currentBall.getRunsScored();
                extraRuns += score;
            }
        }
        labelStrikerScore.setText("" + (strikerRuns + calRuns));
        labelStrikerBalls.setText("" + (strikerBalls + calBalls));
        totalScore.setText("" + ((this.currentBall.getTotalScore() - this.currentBall.getRunsThisBall()) + calScore) + "/" + this.currentBall.getWicketCount());
        totalExtras.setText("" + extraRuns);
    }

    public void onEdit(View view) {
        if (this.currentBall.getIsWicket()) {
            enableWicketEditables();
        } else {
            enableEditables();
        }
    }

    public void onEditOver(View view) {
        displayPopup((Button) view, C0252R.string.bowlerText, 2, false);
    }

    public void onWicketBy(View view) {
        displayPopup((Button) view, C0252R.string.playerText, 2, false);
    }

    public void onInsert(View view) {
        if (this.currentBall.getBallNo() < ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings().getBpo()) {
            openContextMenu(view);
        } else {
            new Builder(this).setIcon(17301543).setTitle(getString(C0252R.string.insertText) + " (" + this.currentBall.getOverNo() + "." + this.currentBall.getBallNo() + ")").setMessage(C0252R.string.areYouSureText).setPositiveButton(C0252R.string.yesText, new C02232()).setNegativeButton(C0252R.string.noText, null).show();
        }
    }

    public void onCancel(View view) {
        disableEditables();
        populateBall();
    }

    public void insertBall(boolean duplicate) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        dtoScoreBall newBall = new dtoScoreBall();
        DatabaseHandler db = new DatabaseHandler(this);
        newBall = db.getMatchBall(refMatch.getMatchID(), "" + this.currentBall.getTotalBallNo());
        newBall.setTotalBallNo(newBall.getTotalBallNo() + 1);
        newBall.setIsByes(false);
        newBall.setIsLegByes(false);
        newBall.setIsNoball(false);
        newBall.setIsWicket(false);
        newBall.setIsWide(false);
        newBall.setIsStrike(true);
        newBall.setRunsThisBall(0);
        newBall.setRunsScored(0);
        newBall.setXCord(0);
        newBall.setYCord(0);
        newBall.setbsXCord(0);
        newBall.setbsYCord(0);
        db.insertMatchScoreBall(refMatch.getMatchID(), settings.getBpo(), newBall, duplicate);
        db.updateBallByBallForOver(refMatch.getMatchID(), newBall);
        db.close();
        this._lastBall = getLastMatchBall("" + this.currentBall.getTeamNo());
        this._lastBallNo = this._lastBall.getTotalBallNo();
        onNextBall(null);
    }

    public void onSave(View view) {
        new Builder(this).setIcon(17301543).setTitle(C0252R.string.saveText).setMessage(C0252R.string.areYouSureText).setPositiveButton(C0252R.string.yesText, new C02243()).setNegativeButton(C0252R.string.noText, null).show();
    }

    public void onDeleteDup(View view) {
        if (new DatabaseHandler(this).checkDuplicateScoreBall(((CricScorerApp) getApplication()).currentMatch.getMatchID(), this.currentBall.getTeamNo(), "0" + this.currentBall.getOverNo(), "0" + this.currentBall.getBallNo())) {
            new Builder(this).setIcon(17301543).setTitle(C0252R.string.deleteText).setMessage(C0252R.string.areYouSureText).setPositiveButton(C0252R.string.yesText, new C02254()).setNegativeButton(C0252R.string.noText, null).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "No duplicate ball found! Pls edit the ball if required.", 0).show();
        disableEditables();
    }

    public void asyncDeleteBallAndUpdate() {
        new C02265().execute(new Boolean[0]);
    }

    public void updateScorecardAfterDelete() {
        int i;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        int currentBallNo = this.currentBall.getTotalBallNo();
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteDuplicateScoreBall(refMatch.getMatchID(), "" + this.currentBall.getTeamNo(), "" + this.currentBall.getTotalBallNo(), "" + this.currentBall.getOverNo());
        this._lastBallNo--;
        int scoreDiff = this.currentBall.getRunsThisBall() * -1;
        int totalExtras = this.currentBall.getTotalExtras();
        if (this.currentBall.isStrike()) {
            i = 0;
        } else {
            i = (!this.currentBall.getIsNoball() ? this.currentBall.getRunsScored() : 0) + this.currentBall.getRunsThisBall();
        }
        int totalExtrasDiff = totalExtras - i;
        String player1Name = this.currentBall.getStrikerName();
        int player1ScoreDiff = this.currentBall.getRunsScored() * -1;
        int player1BallsDiff = (this.currentBall.isStrike() ? 1 : 0) * -1;
        boolean updateTarget = false;
        int lastTotalScore = 0;
        int lastInnings = -1;
        String lastTeam = "";
        ArrayList<dtoScoreBall> ballsToUpdate = new ArrayList();
        for (int bl = currentBallNo; bl <= this._lastBallNo; bl++) {
            dtoScoreBall ball = db.getMatchBall(refMatch.getMatchID(), "" + bl);
            if (ball == null) {
                break;
            }
            if (this.currentBall.getInningNo() != ball.getInningNo() || this.currentBall.getTeamNo() != ball.getTeamNo()) {
                if (this.currentBall.getInningNo() == ball.getInningNo() && this.currentBall.getTeamNo() != ball.getTeamNo()) {
                    updateTarget = true;
                    lastInnings = ball.getInningNo();
                    lastTeam = ball.getTeamNo();
                    break;
                }
            }
            lastTotalScore = ball.getTotalScore() + scoreDiff;
            ball.setTotalScore(lastTotalScore);
            ball.setTotalExtras(ball.getTotalExtras() + totalExtrasDiff);
            if (player1Name.equalsIgnoreCase(ball.getStrikerName())) {
                ball.setStrikerRuns(ball.getStrikerRuns() + player1ScoreDiff);
                ball.setBallsFacedStriker(ball.getBallsFacedStriker() + player1BallsDiff);
            } else if (player1Name.equalsIgnoreCase(ball.getNonStrikerName())) {
                ball.setNonStrikerRuns(ball.getNonStrikerRuns() + player1ScoreDiff);
                ball.setBallsFacedNonStriker(ball.getBallsFacedNonStriker() + player1BallsDiff);
            }
            ballsToUpdate.add(ball);
        }
        Iterator it = ballsToUpdate.iterator();
        while (it.hasNext()) {
            db.updateMatchScoreBall(refMatch.getMatchID(), (dtoScoreBall) it.next());
        }
        if (updateTarget) {
            db.updateTarget(refMatch.getMatchID(), lastInnings, lastTeam, lastTotalScore);
        }
        db.updateBallByBallForOver(refMatch.getMatchID(), this.currentBall);
        db.close();
    }

    protected void saveScorecard() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        if (this.currentBall == null) {
            Toast.makeText(getApplicationContext(), "Current Ball returned NULL! Re-Edit the ball.", 0).show();
            disableEditables();
            return;
        }
        TextView labelStriker = (TextView) findViewById(C0252R.id.editStriker);
        TextView labelNonStriker = (TextView) findViewById(C0252R.id.editNonStriker);
        TextView labelStrikerScore = (TextView) findViewById(C0252R.id.strikerScore);
        TextView labelNonStrikerScore = (TextView) findViewById(C0252R.id.nonStrikerScore);
        TextView labelStrikerBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        TextView labelNonStrikerBalls = (TextView) findViewById(C0252R.id.ballsNonStriker);
        Button buttonBowler = (Button) findViewById(C0252R.id.buttonBowler);
        String previousBowler = "";
        dtoScoreBall previousBall = null;
        try {
            if (this._currentBallNo > 1) {
                previousBall = getMatchBall(this._currentBallNo - 1);
                if (!this.currentBall.getTeamNo().equalsIgnoreCase(previousBall.getTeamNo())) {
                    previousBall = null;
                }
            }
            dtoScoreBall ball = this.currentBall;
            previousBowler = ball.getBowlerName();
            boolean _hasReball = false;
            if (ball.getIsWicket()) {
                ball.setWicketHow("" + ((Spinner) findViewById(C0252R.id.spinWicketAs)).getSelectedItem().toString());
                ball.setWicketAssist("" + ((Button) findViewById(C0252R.id.buttonWicketBy)).getText().toString());
            } else {
                int totalExtras;
                int score = Integer.parseInt("0" + ((Spinner) findViewById(C0252R.id.spinRuns)).getSelectedItem().toString());
                String _suffix = "";
                int _extraRun = 0;
                ball.setIsStrike(false);
                ball.setIsWide(false);
                ball.setIsNoball(false);
                ball.setIsLegByes(false);
                ball.setIsByes(false);
                if (((CheckBox) findViewById(C0252R.id.strikeCheckBox)).isChecked()) {
                    ball.setIsStrike(true);
                }
                if (((CheckBox) findViewById(C0252R.id.wideCheckBox)).isChecked()) {
                    ball.setIsWide(true);
                    _suffix = "wd";
                    _extraRun = settings.getWideRun();
                    if (settings.getWideReball() == 1) {
                        _hasReball = true;
                    }
                }
                if (((CheckBox) findViewById(C0252R.id.noballCheckBox)).isChecked()) {
                    ball.setIsNoball(true);
                    _suffix = "nb";
                    _extraRun = settings.getNoballRun();
                    if (settings.getNoballReball() == 1) {
                        _hasReball = true;
                    }
                }
                if (((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).isChecked()) {
                    ball.setIsLegByes(true);
                    if (!ball.getIsNoball()) {
                        _suffix = "lb";
                    }
                }
                if (((CheckBox) findViewById(C0252R.id.byesCheckBox)).isChecked()) {
                    ball.setIsByes(true);
                    if (!(ball.getIsNoball() || ball.getIsWide())) {
                        _suffix = "b";
                    }
                }
                ball.setRunsScored(score);
                ball.setRunsThisBall(score + _extraRun);
                ball.setTotalScore(previousBall != null ? (previousBall.getTotalScore() + score) + _extraRun : score + _extraRun);
                ball.setTarget(this.currentBall.getTarget());
                StringBuilder append = new StringBuilder().append("0");
                if (previousBall != null) {
                    totalExtras = (_hasReball ? score : 0) + (previousBall.getTotalExtras() + _extraRun);
                } else {
                    totalExtras = (_hasReball ? score : 0) + _extraRun;
                }
                ball.setTotalExtras(Integer.parseInt(append.append(totalExtras).toString()));
                ball.setStrikerName(labelStriker.getText().toString());
                ball.setNonStrikerName(labelNonStriker.getText().toString());
                ball.setBowlerName(buttonBowler.getText().toString());
                ball.setStrikerRuns(Integer.parseInt("0" + labelStrikerScore.getText().toString()));
                ball.setNonStrikerRuns(Integer.parseInt("0" + labelNonStrikerScore.getText().toString()));
                ball.setBallsFacedStriker(Integer.parseInt("0" + labelStrikerBalls.getText().toString()));
                ball.setBallsFacedNonStriker(Integer.parseInt("0" + labelNonStrikerBalls.getText().toString()));
                String ballByBallText = "";
                Object valueOf;
                if ((previousBall == null && this.currentBall.getBallNo() == 1) || (previousBall != null && previousBall.getBallNo() == 6 && this.currentBall.getBallNo() == 1)) {
                    append = new StringBuilder().append("");
                    valueOf = score == 0 ? _suffix.trim().equalsIgnoreCase("") ? "-" : "" : Integer.valueOf(score);
                    ballByBallText = append.append(valueOf).append(_suffix).toString().trim();
                } else {
                    append = new StringBuilder().append("").append(previousBall.getBallByBall().toString()).append(" ");
                    valueOf = score == 0 ? _suffix.trim().equalsIgnoreCase("") ? "-" : "" : Integer.valueOf(score);
                    ballByBallText = append.append(valueOf).append(_suffix).toString().trim();
                }
                ball.setBallByBall(ballByBallText);
            }
            boolean updateRest = false;
            if (((CheckBox) findViewById(C0252R.id.updateRestCheckBox)).isChecked()) {
                updateRest = true;
            }
            asyncUpdateBalls(_hasReball, updateRest);
            if (!previousBowler.equalsIgnoreCase(buttonBowler.getText().toString())) {
                DatabaseHandler db = new DatabaseHandler(this);
                db.replaceBowlerForOver(refMatch.getMatchID(), ball.getTeamNo(), "" + ball.getInningNo(), ball.getOverNo(), previousBowler, buttonBowler.getText().toString());
                db.close();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), 1).show();
        }
    }

    public void asyncUpdateBalls(boolean hasReball, boolean updateRest) {
        new C02276().execute(new Boolean[]{Boolean.valueOf(hasReball), Boolean.valueOf(updateRest)});
    }

    public void updateScorecard(boolean hasReball, boolean updateRest) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        int currentBallNo = this.currentBall.getTotalBallNo();
        DatabaseHandler db = new DatabaseHandler(this);
        if (this._lastBallNo == currentBallNo || this.currentBall.getIsWicket()) {
            db.updateMatchScoreBall(refMatch.getMatchID(), this.currentBall);
            return;
        }
        int player1ScoreDiff;
        int player1BallsDiff;
        int player2ScoreDiff;
        int player2BallsDiff;
        dtoScoreBall old = db.getMatchBall(refMatch.getMatchID(), "" + currentBallNo);
        db.updateMatchScoreBall(refMatch.getMatchID(), this.currentBall);
        String ballByBallText = this.currentBall.getBallByBall().trim();
        if (hasReball) {
            try {
                dtoScoreBall extraBall = (dtoScoreBall) this.currentBall.clone();
                extraBall.setIsByes(false);
                extraBall.setIsLegByes(false);
                extraBall.setIsNoball(false);
                extraBall.setIsWicket(false);
                extraBall.setIsWide(false);
                extraBall.setIsStrike(true);
                extraBall.setRunsThisBall(0);
                extraBall.setRunsScored(0);
                extraBall.setXCord(0);
                extraBall.setYCord(0);
                extraBall.setbsXCord(0);
                extraBall.setbsYCord(0);
                extraBall.setTotalBallNo(this.currentBall.getTotalBallNo() + 1);
                extraBall.setBallsFacedStriker(this.currentBall.getBallsFacedStriker() + 1);
                ballByBallText = ballByBallText + " -";
                extraBall.setBallByBall(ballByBallText);
                db.insertMatchScoreBall(refMatch.getMatchID(), settings.getBpo(), extraBall, true);
                currentBallNo++;
                this._lastBallNo++;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        int scoreDiff = this.currentBall.getTotalScore() - old.getTotalScore();
        int totalExtrasDiff = this.currentBall.getTotalExtras() - old.getTotalExtras();
        String player1Name = this.currentBall.getStrikerName();
        String player2Name = this.currentBall.getNonStrikerName();
        boolean ballByBallReq = true;
        if (player1Name.equalsIgnoreCase(old.getStrikerName())) {
            player1ScoreDiff = this.currentBall.getStrikerRuns() - old.getStrikerRuns();
            player1BallsDiff = (this.currentBall.getBallsFacedStriker() - old.getBallsFacedStriker()) + (hasReball ? 1 : 0);
            player2ScoreDiff = this.currentBall.getNonStrikerRuns() - old.getNonStrikerRuns();
            player2BallsDiff = this.currentBall.getBallsFacedNonStriker() - old.getBallsFacedNonStriker();
        } else {
            player1ScoreDiff = this.currentBall.getStrikerRuns() - old.getNonStrikerRuns();
            player1BallsDiff = this.currentBall.getBallsFacedStriker() - old.getBallsFacedNonStriker();
            player2ScoreDiff = this.currentBall.getNonStrikerRuns() - old.getStrikerRuns();
            player2BallsDiff = (this.currentBall.getBallsFacedNonStriker() - old.getBallsFacedStriker()) + (hasReball ? 1 : 0);
        }
        boolean updateTarget = false;
        int lastTotalScore = 0;
        int lastInnings = -1;
        String lastTeam = "";
        ArrayList<dtoScoreBall> ballsToUpdate = new ArrayList();
        for (int bl = currentBallNo + 1; bl <= this._lastBallNo; bl++) {
            dtoScoreBall ball = db.getMatchBall(refMatch.getMatchID(), "" + bl);
            if (ball == null) {
                break;
            }
            if (this.currentBall.getInningNo() != ball.getInningNo() || !this.currentBall.getTeamNo().equalsIgnoreCase(ball.getTeamNo())) {
                if (this.currentBall.getInningNo() == ball.getInningNo() && !this.currentBall.getTeamNo().equalsIgnoreCase(ball.getTeamNo())) {
                    updateTarget = true;
                    lastInnings = ball.getInningNo();
                    lastTeam = ball.getTeamNo();
                    break;
                }
            }
            lastTotalScore = ball.getTotalScore() + scoreDiff;
            ball.setTotalScore(lastTotalScore);
            ball.setTotalExtras(ball.getTotalExtras() + totalExtrasDiff);
            if (player1Name.equalsIgnoreCase(ball.getStrikerName())) {
                ball.setStrikerRuns(ball.getStrikerRuns() + player1ScoreDiff);
                ball.setBallsFacedStriker(ball.getBallsFacedStriker() + player1BallsDiff);
            } else if (player1Name.equalsIgnoreCase(ball.getNonStrikerName())) {
                ball.setNonStrikerRuns(ball.getNonStrikerRuns() + player1ScoreDiff);
                ball.setBallsFacedNonStriker(ball.getBallsFacedNonStriker() + player1BallsDiff);
            }
            if (player2Name.equalsIgnoreCase(ball.getStrikerName())) {
                ball.setStrikerRuns(ball.getStrikerRuns() + player2ScoreDiff);
                ball.setBallsFacedStriker(ball.getBallsFacedStriker() + player2BallsDiff);
            } else if (player2Name.equalsIgnoreCase(ball.getNonStrikerName())) {
                ball.setNonStrikerRuns(ball.getNonStrikerRuns() + player2ScoreDiff);
                ball.setBallsFacedNonStriker(ball.getBallsFacedNonStriker() + player2BallsDiff);
            }
            if (ball.getOverNo() != this.currentBall.getOverNo()) {
                ballByBallReq = false;
            }
            if (ballByBallReq) {
                String lastBallByBallText = ball.getBallByBall();
                if (lastBallByBallText.trim().equalsIgnoreCase("")) {
                    ball.setBallByBall(ballByBallText + " -");
                } else {
                    ball.setBallByBall((ballByBallText + lastBallByBallText.substring(lastBallByBallText.lastIndexOf(" "))).trim());
                }
            }
            ballsToUpdate.add(ball);
        }
        Iterator it = ballsToUpdate.iterator();
        while (it.hasNext()) {
            db.updateMatchScoreBall(refMatch.getMatchID(), (dtoScoreBall) it.next());
        }
        if (updateTarget) {
            db.updateTarget(refMatch.getMatchID(), lastInnings, lastTeam, lastTotalScore);
        }
        db.close();
    }

    public void onListBalls(View v) {
        Intent intent = new Intent(this, ListOvers.class);
        intent.addFlags(65536);
        startActivityForResult(intent, 1);
        overridePendingTransition(0, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                this._currentBallNo = data.getExtras().getInt("currentBall");
                if (this._currentBallNo <= 0) {
                    this._currentBallNo = this._lastBallNo;
                }
                populateBall();
            }
            if (resultCode != 0) {
            }
        }
    }

    private void displayPopup(Button target, int title, int batOrBowl, boolean canReplace) {
        int position;
        dtoAdditionalSettings settings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
        Rect displayRectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C0252R.layout.popup_choose_player, (ViewGroup) findViewById(C0252R.id.choosePopupLayout));
        layout.setMinimumWidth((int) (((float) displayRectangle.width()) * 0.9f));
        View textView = new TextView(layout.getContext());
        textView.setText(getResources().getString(title));
        if (VERSION.SDK_INT < 23) {
            Utility.setLargeTitle(textView, layout.getContext());
        } else {
            Utility.setLargeTitle(textView);
        }
        textView.setMinimumHeight(60);
        if (settings.getPresetTeams() == 1) {
            layout.findViewById(C0252R.id.editPlayer).setVisibility(8);
            layout.findViewById(C0252R.id.editPlayerLabel).setVisibility(8);
        }
        if (!canReplace) {
            layout.findViewById(C0252R.id.replacePlayer).setVisibility(4);
        }
        if (VERSION.SDK_INT < 11) {
            TypedValue a = new TypedValue();
            getTheme().resolveAttribute(16842836, a, true);
            if (a.type < 28 || a.type > 31) {
                layout.setBackgroundResource(a.resourceId);
                textView.setBackgroundResource(a.resourceId);
            } else {
                int windowBackground = a.data;
                layout.setBackgroundColor(windowBackground);
                textView.setBackgroundColor(windowBackground);
            }
        }
        Builder builder = new Builder(this);
        builder.setView(layout);
        builder.setCustomTitle(textView);
        final AlertDialog alertDialog = builder.create();
        final AutoCompleteTextView textPlayer = (AutoCompleteTextView) layout.findViewById(C0252R.id.editPlayer);
        final Spinner spinnerPlayer = (Spinner) layout.findViewById(C0252R.id.spinnerPlayers);
        String value = "" + target.getText().toString().trim();
        if (batOrBowl == 1) {
            this.team1Adapter = null;
            populateTeam1Spinner(batOrBowl);
            spinnerPlayer.setAdapter(this.team1Adapter);
            position = this.team1Adapter.getPosition(Utility.toDisplayCase(value));
        } else {
            this.team2Adapter = null;
            populateTeam2Spinner(batOrBowl);
            spinnerPlayer.setAdapter(this.team2Adapter);
            position = this.team2Adapter.getPosition(Utility.toDisplayCase(value));
        }
        if (position >= 0) {
            spinnerPlayer.setSelection(position);
        } else {
            textPlayer.setText("");
            textPlayer.append(value);
        }
        if (this._allPlayerList == null) {
            populateTeamAutoComplete();
        }
        if (this._allPlayerList.size() > 0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, 17367048, this._allPlayerList);
            arrayAdapter.setDropDownViewResource(17367049);
            textPlayer.setAdapter(arrayAdapter);
        }
        final CheckBox cbReplace = (CheckBox) layout.findViewById(C0252R.id.replacePlayer);
        final View view = textView;
        final int i = title;
        cbReplace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cbReplace.isChecked()) {
                    view.setText(EditBall.this.getResources().getString(C0252R.string.replaceText) + " " + EditBall.this.getResources().getString(i));
                    Toast.makeText(EditBall.this.getApplicationContext(), "Important Warning: Replacing player CANNOT be UNDONE!", 1).show();
                    return;
                }
                view.setText(EditBall.this.getResources().getString(i));
            }
        });
        final Button button = target;
        ((Button) layout.findViewById(C0252R.id.btnOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String playerName = "";
                if (!textPlayer.getText().toString().trim().equals("")) {
                    playerName = textPlayer.getText().toString().trim();
                    if (!EditBall.this._allPlayerList.contains(Utility.toDisplayCase(playerName))) {
                        EditBall.this._allPlayerList.add(playerName);
                    }
                } else if (spinnerPlayer.getItemAtPosition(spinnerPlayer.getSelectedItemPosition()) != null) {
                    String name = ((PlayerListAdapter) spinnerPlayer.getAdapter()).getPlayerName(spinnerPlayer.getSelectedItemPosition());
                    if (name.trim().equalsIgnoreCase("")) {
                        Toast.makeText(EditBall.this.getApplicationContext(), "Player name cannot be empty!", 0).show();
                        return;
                    }
                    playerName = "" + name.trim();
                }
                button.setText(playerName);
                ((InputMethodManager) EditBall.this.getSystemService("input_method")).hideSoftInputFromWindow(textPlayer.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
        ((Button) layout.findViewById(C0252R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) EditBall.this.getSystemService("input_method")).hideSoftInputFromWindow(textPlayer.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
