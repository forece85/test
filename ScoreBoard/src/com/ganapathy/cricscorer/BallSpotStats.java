package com.ganapathy.cricscorer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BallSpotStats extends BaseActivity implements OnItemSelectedListener {
    private boolean _hiResImage = false;
    private int _lastSelectedInng = -1;
    private int _lastSelectedTeam = -1;
    private int fours;
    private int noballs;
    private int rest;
    private int sixes;
    private int wickets;
    private int wides;

    class C02111 implements OnItemSelectedListener {
        C02111() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (BallSpotStats.this._lastSelectedTeam != position) {
                BallSpotStats.this._lastSelectedTeam = position;
                if (BallSpotStats.this.getIntent().hasExtra("SelectedTeam")) {
                    BallSpotStats.this.getIntent().removeExtra("SelectedTeam");
                }
                BallSpotStats.this.getIntent().putExtra("SelectedTeam", position);
                BallSpotStats.this.populatePlayers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02122 implements OnItemSelectedListener {
        C02122() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (BallSpotStats.this._lastSelectedInng != position) {
                BallSpotStats.this._lastSelectedInng = position;
                if (BallSpotStats.this.getIntent().hasExtra("SelectedInng")) {
                    BallSpotStats.this.getIntent().removeExtra("SelectedInng");
                }
                BallSpotStats.this.getIntent().putExtra("SelectedInng", position);
                BallSpotStats.this.populatePlayers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_ball_spot_stats);
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
            initializeControls();
            populateSpinners();
            Spinner teamSpinner = (Spinner) findViewById(C0252R.id.spinnerTeams);
            Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
            Spinner spinPlayer = (Spinner) findViewById(C0252R.id.spinPlayers);
            spinPlayer.setOnItemSelectedListener(this);
            if (getIntent().hasExtra("SelectedTeam")) {
                teamSpinner.setSelection(getIntent().getExtras().getInt("SelectedTeam"));
            }
            if (getIntent().hasExtra("SelectedInng")) {
                inngSpinner.setSelection(getIntent().getExtras().getInt("SelectedInng"));
            }
            if (getIntent().hasExtra("SelectedPosition")) {
                spinPlayer.setSelection(getIntent().getExtras().getInt("SelectedPosition"));
            }
        } catch (Exception e) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0252R.menu.activity_ball_spot_stats, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(C0252R.id.menu_hiResBallSpot).setChecked(this._hiResImage);
        if (VERSION.SDK_INT < 14) {
            if (this._hiResImage) {
                menu.findItem(C0252R.id.menu_hiResBallSpot).setTitle(getString(C0252R.string.hiResText) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_hiResBallSpot).setTitle(getString(C0252R.string.hiResText));
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.menu_hiResBallSpot:
                this._hiResImage = !this._hiResImage;
                break;
            case C0252R.id.menu_send:
                sendImage();
                break;
            case C0252R.id.menu_close:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (getIntent().hasExtra("SelectedPosition")) {
            getIntent().removeExtra("SelectedPosition");
        }
        getIntent().putExtra("SelectedPosition", pos);
        try {
            updateBallSpot();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    public void onOptions(View view) {
        try {
            openOptionsMenu();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    protected void updateBallSpot() {
        ImageView imageView = (ImageView) findViewById(C0252R.id.imageBallSpot);
        Utility.plottedBitmap = plotBallSpot(((Spinner) findViewById(C0252R.id.spinPlayers)).getSelectedItem().toString());
        imageView.setImageBitmap(Utility.plottedBitmap);
        updateNumbers();
        Toast.makeText(getApplicationContext(), "Ball spot updated!", 0).show();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onBackPressed() {
        Utility.cleanupTempFiles(this);
        super.onBackPressed();
    }

    public void onBack(View view) {
        onBackPressed();
    }

    protected void sendImage() {
        String matchID = ((CricScorerApp) getApplication()).currentMatch.getMatchID();
        String playerName = ((Spinner) findViewById(C0252R.id.spinPlayers)).getSelectedItem().toString();
        String address = "" + Utility.GetMailIds(this);
        String subject = playerName + "'s Bowling spots in " + matchID;
        String emailtext = "Please check the attached Bowling spots for the Player " + playerName;
        try {
            if (Utility.plottedBitmap == null) {
                Utility.plottedBitmap = plotBallSpot(playerName);
            }
            String strFile = Utility.CreateJpegFromBitmap(this, Utility.plottedBitmap, "BallSpot.jpg", this._hiResImage);
            if (strFile != null && !strFile.equalsIgnoreCase("")) {
                Intent emailIntent = new Intent("android.intent.action.SEND");
                emailIntent.setType("image/jpeg");
                emailIntent.putExtra("android.intent.extra.EMAIL", new String[]{address});
                emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
                emailIntent.putExtra("android.intent.extra.TEXT", emailtext);
                File fileReport = new File(strFile);
                if (fileReport.exists()) {
                    emailIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(fileReport));
                }
                startActivity(Intent.createChooser(emailIntent, "Share Image"));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    private void initializeControls() {
        TextView text1s = (TextView) findViewById(C0252R.id.textOne);
        text1s.setBackgroundColor(SupportMenu.CATEGORY_MASK);
        text1s.setTextColor(-1);
        TextView text2s = (TextView) findViewById(C0252R.id.textTwo);
        text2s.setBackgroundColor(-65281);
        text2s.setTextColor(-1);
        TextView text3s = (TextView) findViewById(C0252R.id.textThree);
        text3s.setBackgroundColor(-16776961);
        text3s.setTextColor(-1);
        TextView text4s = (TextView) findViewById(C0252R.id.textFour);
        text4s.setBackgroundColor(Utility.getBallSpotLineColor(4));
        text4s.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        TextView text6s = (TextView) findViewById(C0252R.id.textSix);
        text6s.setBackgroundColor(Utility.getBallSpotLineColor(6));
        text6s.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        TextView textRest = (TextView) findViewById(C0252R.id.textRest);
        textRest.setBackgroundColor(Utility.getBallSpotLineColor(5));
        textRest.setTextColor(ViewCompat.MEASURED_STATE_MASK);
    }

    private void updateNumbers() {
        ((TextView) findViewById(C0252R.id.textOneValue)).setText("" + this.wickets);
        ((TextView) findViewById(C0252R.id.textTwoValue)).setText("" + this.wides);
        ((TextView) findViewById(C0252R.id.textThreeValue)).setText("" + this.noballs);
        ((TextView) findViewById(C0252R.id.textFourValue)).setText("" + this.fours);
        ((TextView) findViewById(C0252R.id.textSixValue)).setText("" + this.sixes);
        ((TextView) findViewById(C0252R.id.textRestValue)).setText("" + this.rest);
    }

    private void populateSpinners() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
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
        teamSpinner.setOnItemSelectedListener(new C02111());
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
        inngSpinner.setOnItemSelectedListener(new C02122());
        if (refMatch.getNoOfInngs() <= 1) {
            inngSpinner.setEnabled(false);
        }
    }

    public void populatePlayers() {
        String team;
        String inng;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
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
        List<String> teamPlayers = db.getBallSpotTeamPlayers(refMatch.getMatchID(), team, inng);
        db.close();
        List<String> strikerNames = new ArrayList();
        strikerNames.add("ALL");
        for (String player : teamPlayers) {
            strikerNames.add(player);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, 17367048, strikerNames);
        adapter1.setDropDownViewResource(17367049);
        ((Spinner) findViewById(C0252R.id.spinPlayers)).setAdapter(adapter1);
        ((Spinner) findViewById(C0252R.id.spinPlayers)).setSelection(0);
    }

    public void onOK(View view) {
        sendImage();
    }

    protected Bitmap plotBallSpot(String playerName) {
        String team;
        String inng;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
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
        List<dtoBallSpotDetail> ballSpotStat = db.getBallSpotDetail(refMatch.getMatchID(), team, inng, playerName);
        db.close();
        this.rest = 0;
        this.sixes = 0;
        this.fours = 0;
        this.noballs = 0;
        this.wides = 0;
        this.wickets = 0;
        for (dtoBallSpotDetail row : ballSpotStat) {
            if (row.getIsNoball() == 0 && row.getIsWicket() == 0 && row.getIsWide() == 0) {
                switch (row.getRunsThisBall()) {
                    case 4:
                        this.fours++;
                        break;
                    case 6:
                        this.sixes++;
                        break;
                    default:
                        this.rest++;
                        break;
                }
            }
            if (row.getIsWide() == 1) {
                this.wides++;
            }
            if (row.getIsNoball() == 1) {
                this.noballs++;
            }
            if (row.getIsWicket() == 1) {
                this.wickets++;
            }
        }
        if (playerName.equalsIgnoreCase("ALL")) {
            if (team.equalsIgnoreCase("1")) {
                playerName = refMatch.getTeam1Name();
            } else {
                playerName = refMatch.getTeam2Name();
            }
        }
        return Utility.PlotBallSpot(this, ballSpotStat, playerName);
    }
}
