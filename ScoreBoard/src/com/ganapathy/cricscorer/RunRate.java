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

public class RunRate extends BaseActivity {
    private boolean _hiResImage = false;
    private int _lastSelectedInng = -1;
    private CompareRunRateGraph graph = null;

    class C02531 implements OnItemSelectedListener {
        C02531() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (RunRate.this._lastSelectedInng != position) {
                RunRate.this._lastSelectedInng = position;
                if (RunRate.this.getIntent().hasExtra("SelectedInng")) {
                    RunRate.this.getIntent().removeExtra("SelectedInng");
                }
                RunRate.this.getIntent().putExtra("SelectedInng", position);
                RunRate.this.plotGraph();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_run_rate);
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
            Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
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
        getMenuInflater().inflate(C0252R.menu.activity_run_rate, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(C0252R.id.menu_hiResRunRate).setChecked(this._hiResImage);
        if (VERSION.SDK_INT < 14) {
            if (this._hiResImage) {
                menu.findItem(C0252R.id.menu_hiResRunRate).setTitle(getString(C0252R.string.hiResText) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_hiResRunRate).setTitle(getString(C0252R.string.hiResText));
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
            case C0252R.id.menu_hiResRunRate:
                this._hiResImage = !this._hiResImage;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Utility.cleanupTempFiles(this);
        super.onBackPressed();
    }

    public void onSettings(View view) {
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

    private void populateSpinners() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        String firstBattedTeamName = MatchHelper.getTeamName(refMatch, MatchHelper.getFirstBatted(refMatch));
        String secondBattedTeamName = MatchHelper.getTeamName(refMatch, MatchHelper.getSecondBatted(refMatch));
        Spinner inngSpinner = (Spinner) findViewById(C0252R.id.spinnerInnings);
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
        inngSpinner.setOnItemSelectedListener(new C02531());
        if (refMatch.getNoOfInngs() <= 1) {
            inngSpinner.setEnabled(false);
        }
    }

    protected void plotGraph() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String secondBatted = MatchHelper.getSecondBatted(refMatch);
        String inng = "1";
        if (((Spinner) findViewById(C0252R.id.spinnerInnings)).getSelectedItemPosition() == 0) {
            inng = "1";
        } else {
            inng = "2";
        }
        DatabaseHandler db = new DatabaseHandler(this);
        List<dtoRunsPerOver> runRate1stInngs = db.getRunsRate(refMatch.getMatchID(), firstBatted, inng);
        List<dtoRunsPerOver> runRate2ndInngs = db.getRunsRate(refMatch.getMatchID(), secondBatted, inng);
        db.close();
        if (runRate1stInngs.size() > 0) {
            ArrayList<PathOnChart> paths = getPaths(refMatch, inng, generateData(refMatch, runRate1stInngs), generateData(refMatch, runRate2ndInngs));
            ChartAttributes chartAttributes = getChartAttributes(refMatch, inng);
            ImageView graphImage = (ImageView) findViewById(C0252R.id.graphyLayout);
            graphImage.setBackgroundColor(Utility.getThemeBackColor(this));
            this.graph = new CompareRunRateGraph(this, paths, chartAttributes);
            Utility.plottedBitmap = this.graph.createGraph();
            graphImage.setImageBitmap(Utility.plottedBitmap);
            return;
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Data not yet available!", 0);
        toast.setGravity(16, 0, 0);
        toast.show();
        finish();
    }

    private ArrayList<PointOnChart> generateData(dtoMatch refMatch, List<dtoRunsPerOver> runRateInngs) {
        ArrayList<PointOnChart> points = new ArrayList();
        for (int counter = 0; counter < runRateInngs.size(); counter++) {
            dtoRunsPerOver row = (dtoRunsPerOver) runRateInngs.get(counter);
            points.add(new PointOnChart((float) (((row.getOverNo() * refMatch.getAdditionalSettings().getBpo()) + row.getBallNo()) - 1), (float) row.getRuns(), row.getWickets()));
        }
        return points;
    }

    private ArrayList<PathOnChart> getPaths(dtoMatch refMatch, String inng, ArrayList<PointOnChart> points1, ArrayList<PointOnChart> points2) {
        String firstBatted = MatchHelper.getFirstBatted(refMatch);
        String firstBattedTeamName = MatchHelper.getTeamName(refMatch, firstBatted);
        String secondBatted = MatchHelper.getSecondBatted(refMatch);
        String secondBattedTeamName = MatchHelper.getTeamName(refMatch, secondBatted);
        DatabaseHandler db = new DatabaseHandler(this);
        String team1Score = db.getTeamScore(refMatch.getMatchID(), firstBatted, inng);
        String team2Score = db.getTeamScore(refMatch.getMatchID(), secondBatted, inng);
        db.close();
        PathAttributes pathAttributes1 = new PathAttributes();
        pathAttributes1.setPointColor("#00FFFF");
        pathAttributes1.setPathColor("#0000FF");
        pathAttributes1.setStrokeWidthOfPath(4.0f);
        pathAttributes1.setRadiusOfPoints(8.0f);
        pathAttributes1.setPathName(firstBattedTeamName + " - " + team1Score);
        PathOnChart path1 = new PathOnChart(points1, pathAttributes1);
        PathAttributes pathAttributes2 = new PathAttributes();
        pathAttributes2.setPointColor("#FFFF00");
        pathAttributes2.setPathColor("#FF0000");
        pathAttributes2.setStrokeWidthOfPath(4.0f);
        pathAttributes2.setRadiusOfPoints(8.0f);
        pathAttributes2.setPathName(secondBattedTeamName + " - " + team2Score);
        PathOnChart path2 = new PathOnChart(points2, pathAttributes2);
        ArrayList<PathOnChart> paths = new ArrayList();
        paths.add(path1);
        paths.add(path2);
        return paths;
    }

    private ChartAttributes getChartAttributes(dtoMatch refMatch, String inng) {
        ChartAttributes chartAttributes = new ChartAttributes();
        try {
            chartAttributes.setBackgroundColor("#" + Integer.toHexString(Utility.getThemeBackColor(this)));
            String hexVal = Integer.toHexString(Utility.getThemeTextColor(this));
            chartAttributes.setAxisColor("#" + hexVal);
            chartAttributes.setAxisNameColor("#" + hexVal);
        } catch (Exception e) {
            chartAttributes.setBackgroundColor("#000000");
            chartAttributes.setAxisColor("#ffffffff");
            chartAttributes.setAxisNameColor("#ffffffff");
        }
        chartAttributes.setGridColor("#bbbbbbbb");
        chartAttributes.setX_unit_name("Overs");
        chartAttributes.setY_unit_name("Runs");
        int cut = refMatch.getDateTime().indexOf(32);
        StringBuilder append = new StringBuilder().append("Compare Run Rate");
        String str = refMatch.getNoOfInngs() > 1 ? inng.equals("1") ? " - 1st Inngs" : " - 2nd Inngs" : "";
        chartAttributes.setChart_title2(append.append(str).toString());
        StringBuilder append2 = new StringBuilder().append(refMatch.getOvers() == 4479 ? "Unlimited" : "" + refMatch.getOvers()).append(" Overs on ");
        String dateTime = refMatch.getDateTime();
        if (cut <= 0) {
            cut = Math.min(8, refMatch.getDateTime().length());
        }
        chartAttributes.setChart_title(append2.append(dateTime.substring(0, cut)).append(refMatch.getVenue().trim().equalsIgnoreCase("") ? "" : ", " + refMatch.getVenue().trim()).toString());
        chartAttributes.setBpo(refMatch.getAdditionalSettings().getBpo());
        return chartAttributes;
    }

    protected void sendImage() {
        Utility.showNotSupportedAlert(this);
    }
}
