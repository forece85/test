package com.ganapathy.cricscorer;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WagonWheelStats extends BaseActivity implements OnItemSelectedListener {
    private boolean _hiResImage = false;
    private int _lastSelectedInng = -1;
    private int _lastSelectedTeam = -1;
    private int dots;
    private int fours;
    private int ones;
    private int rest;
    private Map<String, Boolean> showHideOptions = new HashMap();
    private int sixes;
    private int threes;
    private int twos;

    class C02731 implements OnItemSelectedListener {
        C02731() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (WagonWheelStats.this._lastSelectedTeam != position) {
                WagonWheelStats.this._lastSelectedTeam = position;
                if (WagonWheelStats.this.getIntent().hasExtra("SelectedTeam")) {
                    WagonWheelStats.this.getIntent().removeExtra("SelectedTeam");
                }
                WagonWheelStats.this.getIntent().putExtra("SelectedTeam", position);
                WagonWheelStats.this.populatePlayers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C02742 implements OnItemSelectedListener {
        C02742() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (WagonWheelStats.this._lastSelectedInng != position) {
                WagonWheelStats.this._lastSelectedInng = position;
                if (WagonWheelStats.this.getIntent().hasExtra("SelectedInng")) {
                    WagonWheelStats.this.getIntent().removeExtra("SelectedInng");
                }
                WagonWheelStats.this.getIntent().putExtra("SelectedInng", position);
                WagonWheelStats.this.populatePlayers();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_wagon_wheel_stats);
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
        getMenuInflater().inflate(C0252R.menu.activity_wagon_wheel_stats, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(C0252R.id.menu_hiResWagonWheel).setChecked(this._hiResImage);
        if (VERSION.SDK_INT < 14) {
            if (this._hiResImage) {
                menu.findItem(C0252R.id.menu_hiResWagonWheel).setTitle(getString(C0252R.string.hiResText) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_hiResWagonWheel).setTitle(getString(C0252R.string.hiResText));
            }
        }
        menu.findItem(C0252R.id.menu_plotDots).setChecked(((Boolean) this.showHideOptions.get("dots")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("dots")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotDots).setTitle(getString(C0252R.string.plotDots) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotDots).setTitle(getString(C0252R.string.plotDots));
            }
        }
        menu.findItem(C0252R.id.menu_plotOnes).setChecked(((Boolean) this.showHideOptions.get("ones")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("ones")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotOnes).setTitle(getString(C0252R.string.plotOnes) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotOnes).setTitle(getString(C0252R.string.plotOnes));
            }
        }
        menu.findItem(C0252R.id.menu_plotTwos).setChecked(((Boolean) this.showHideOptions.get("twos")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("twos")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotTwos).setTitle(getString(C0252R.string.plotTwos) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotTwos).setTitle(getString(C0252R.string.plotTwos));
            }
        }
        menu.findItem(C0252R.id.menu_plotThrees).setChecked(((Boolean) this.showHideOptions.get("threes")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("threes")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotThrees).setTitle(getString(C0252R.string.plotThrees) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotThrees).setTitle(getString(C0252R.string.plotThrees));
            }
        }
        menu.findItem(C0252R.id.menu_plotFours).setChecked(((Boolean) this.showHideOptions.get("fours")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("fours")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotFours).setTitle(getString(C0252R.string.plotFours) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotFours).setTitle(getString(C0252R.string.plotFours));
            }
        }
        menu.findItem(C0252R.id.menu_plotSixes).setChecked(((Boolean) this.showHideOptions.get("sixes")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("sixes")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotSixes).setTitle(getString(C0252R.string.plotSixes) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotSixes).setTitle(getString(C0252R.string.plotSixes));
            }
        }
        menu.findItem(C0252R.id.menu_plotWickets).setChecked(((Boolean) this.showHideOptions.get("wickets")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("wickets")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotWickets).setTitle(getString(C0252R.string.plotWickets) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotWickets).setTitle(getString(C0252R.string.plotWickets));
            }
        }
        menu.findItem(C0252R.id.menu_plotRest).setChecked(((Boolean) this.showHideOptions.get("rest")).booleanValue());
        if (VERSION.SDK_INT < 14) {
            if (((Boolean) this.showHideOptions.get("rest")).booleanValue()) {
                menu.findItem(C0252R.id.menu_plotRest).setTitle(getString(C0252R.string.plotRest) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_plotRest).setTitle(getString(C0252R.string.plotRest));
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean z = true;
        Map map;
        String str;
        switch (item.getItemId()) {
            case C0252R.id.menu_send:
                sendImage();
                break;
            case C0252R.id.menu_close:
                onBackPressed();
                break;
            case C0252R.id.menu_plotDots:
                boolean z2;
                map = this.showHideOptions;
                str = "dots";
                if (((Boolean) this.showHideOptions.get("dots")).booleanValue()) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                map.put(str, Boolean.valueOf(z2));
                updateWagonWheel();
                break;
            case C0252R.id.menu_plotOnes:
                map = this.showHideOptions;
                str = "ones";
                if (((Boolean) this.showHideOptions.get("ones")).booleanValue()) {
                    z = false;
                }
                map.put(str, Boolean.valueOf(z));
                updateWagonWheel();
                break;
            case C0252R.id.menu_plotTwos:
                map = this.showHideOptions;
                str = "twos";
                if (((Boolean) this.showHideOptions.get("twos")).booleanValue()) {
                    z = false;
                }
                map.put(str, Boolean.valueOf(z));
                updateWagonWheel();
                break;
            case C0252R.id.menu_plotThrees:
                map = this.showHideOptions;
                str = "threes";
                if (((Boolean) this.showHideOptions.get("threes")).booleanValue()) {
                    z = false;
                }
                map.put(str, Boolean.valueOf(z));
                updateWagonWheel();
                break;
            case C0252R.id.menu_plotFours:
                map = this.showHideOptions;
                str = "fours";
                if (((Boolean) this.showHideOptions.get("fours")).booleanValue()) {
                    z = false;
                }
                map.put(str, Boolean.valueOf(z));
                updateWagonWheel();
                break;
            case C0252R.id.menu_plotSixes:
                map = this.showHideOptions;
                str = "sixes";
                if (((Boolean) this.showHideOptions.get("sixes")).booleanValue()) {
                    z = false;
                }
                map.put(str, Boolean.valueOf(z));
                updateWagonWheel();
                break;
            case C0252R.id.menu_plotRest:
                map = this.showHideOptions;
                str = "rest";
                if (((Boolean) this.showHideOptions.get("rest")).booleanValue()) {
                    z = false;
                }
                map.put(str, Boolean.valueOf(z));
                updateWagonWheel();
                break;
            case C0252R.id.menu_plotWickets:
                map = this.showHideOptions;
                str = "wickets";
                if (((Boolean) this.showHideOptions.get("wickets")).booleanValue()) {
                    z = false;
                }
                map.put(str, Boolean.valueOf(z));
                updateWagonWheel();
                break;
            case C0252R.id.menu_hiResWagonWheel:
                if (this._hiResImage) {
                    z = false;
                }
                this._hiResImage = z;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (getIntent().hasExtra("SelectedPosition")) {
            getIntent().removeExtra("SelectedPosition");
        }
        getIntent().putExtra("SelectedPosition", pos);
        ImageView imageView = (ImageView) findViewById(C0252R.id.imageWagonWheel);
        try {
            updateWagonWheel();
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

    protected void updateWagonWheel() {
        ImageView imageView = (ImageView) findViewById(C0252R.id.imageWagonWheel);
        Utility.plottedBitmap = plotWagonWheel(((Spinner) findViewById(C0252R.id.spinPlayers)).getSelectedItem().toString());
        imageView.setImageBitmap(Utility.plottedBitmap);
        updateNumbers();
        Toast.makeText(getApplicationContext(), "Wagon wheel updated!", 0).show();
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
        Utility.showNotSupportedAlert(this);
    }

    private void initializeControls() {
        TextView text1s = (TextView) findViewById(C0252R.id.textOne);
        text1s.setBackgroundColor(Utility.getWagonWheelLineColor(1));
        text1s.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        ((TextView) findViewById(C0252R.id.textTwo)).setBackgroundColor(Utility.getWagonWheelLineColor(2));
        ((TextView) findViewById(C0252R.id.textThree)).setBackgroundColor(Utility.getWagonWheelLineColor(3));
        TextView text4s = (TextView) findViewById(C0252R.id.textFour);
        text4s.setBackgroundColor(Utility.getWagonWheelLineColor(4));
        text4s.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        ((TextView) findViewById(C0252R.id.textSix)).setBackgroundColor(Utility.getWagonWheelLineColor(6));
        TextView textRest = (TextView) findViewById(C0252R.id.textRest);
        textRest.setBackgroundColor(Utility.getWagonWheelLineColor(5));
        textRest.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        ((TextView) findViewById(C0252R.id.textDot)).setBackgroundColor(Utility.getWagonWheelLineColor(0));
        this.showHideOptions.put("dots", Boolean.valueOf(true));
        this.showHideOptions.put("ones", Boolean.valueOf(true));
        this.showHideOptions.put("twos", Boolean.valueOf(true));
        this.showHideOptions.put("threes", Boolean.valueOf(true));
        this.showHideOptions.put("fours", Boolean.valueOf(true));
        this.showHideOptions.put("sixes", Boolean.valueOf(true));
        this.showHideOptions.put("wickets", Boolean.valueOf(true));
        this.showHideOptions.put("rest", Boolean.valueOf(true));
    }

    private void updateNumbers() {
        ((TextView) findViewById(C0252R.id.textDotValue)).setText("" + this.dots);
        ((TextView) findViewById(C0252R.id.textOneValue)).setText("" + this.ones);
        ((TextView) findViewById(C0252R.id.textTwoValue)).setText("" + this.twos);
        ((TextView) findViewById(C0252R.id.textThreeValue)).setText("" + this.threes);
        ((TextView) findViewById(C0252R.id.textFourValue)).setText("" + this.fours);
        ((TextView) findViewById(C0252R.id.textSixValue)).setText("" + this.sixes);
        ((TextView) findViewById(C0252R.id.textRestValue)).setText("" + this.rest);
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
        teamSpinner.setOnItemSelectedListener(new C02731());
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
        inngSpinner.setOnItemSelectedListener(new C02742());
        if (refMatch.getNoOfInngs() <= 1) {
            inngSpinner.setEnabled(false);
        }
    }

    public void populatePlayers() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
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
        List<String> teamPlayers = db.getWagonWheelTeamPlayers(refMatch.getMatchID(), team, inng, settings.getWwForDotBall() == 1);
        db.close();
        this.showHideOptions.put("dots", Boolean.valueOf(settings.getWwForDotBall() == 1));
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

    protected Bitmap plotWagonWheel(String playerName) {
        boolean z = true;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
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
        String matchID = refMatch.getMatchID();
        if (settings.getWwForDotBall() != 1) {
            z = false;
        }
        List<dtoWagonWheelDetail> wagonStat = db.getWagonWheelDetail(matchID, team, inng, z, playerName);
        db.close();
        this.rest = 0;
        this.sixes = 0;
        this.fours = 0;
        this.threes = 0;
        this.twos = 0;
        this.ones = 0;
        this.dots = 0;
        for (dtoWagonWheelDetail row : wagonStat) {
            switch (row.getRunsThisBall()) {
                case 0:
                    this.dots++;
                    break;
                case 1:
                    this.ones++;
                    break;
                case 2:
                    this.twos++;
                    break;
                case 3:
                    this.threes++;
                    break;
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
        if (playerName.equalsIgnoreCase("ALL")) {
            if (team.equalsIgnoreCase("1")) {
                playerName = refMatch.getTeam1Name();
            } else {
                playerName = refMatch.getTeam2Name();
            }
        }
        return Utility.PlotWagonWheel(this, wagonStat, this.showHideOptions, playerName);
    }
}
