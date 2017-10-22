package com.ganapathy.cricscorer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scorecard extends BaseActivity {
    private List<String> _allPlayerList;
    private int _ballNo = 0;
    private boolean _ballSpotEnabled = false;
    private int _ballsThisOver = 0;
    private int _choicesToDisplay = 0;
    private boolean _clearBallByBall = false;
    private int _dayNo = 1;
    private int _inningNo = 1;
    private int _isBatsmanOut = 0;
    private boolean _isMatchEnded = false;
    private int _lastBatsmanOut = 0;
    private String _lastNonStriker;
    private String _lastStriker;
    private int _originalTarget = -1;
    private int _overNo = 0;
    private int _penalty = 0;
    private boolean _popupBowler = false;
    private boolean _popupNonStriker = false;
    private boolean _popupStriker = false;
    private boolean _presetTeamsEnabled = false;
    private int _prevBatScore = -1;
    private int _prevBowlScore = -1;
    private int _retiredBatsmanBallNo = -1;
    private boolean _saving = false;
    private int _scoreLastBall = 0;
    private int _target = 0;
    private boolean _teamWon = false;
    private int _totalBallNo = 0;
    private int _totalScore = 0;
    private boolean _wagonWheelEnabled = false;
    private boolean _wagonWheelForDotBallEnabled = false;
    private boolean _wagonWheelForWicketEnabled = false;
    private int _wicketCount = 0;
    private boolean autoBowlSpell = false;
    private boolean autoSwitchBatsman = true;
    private boolean ballSpotDone = false;
    private int bsxCord;
    private int bsyCord;
    private dtoConfiguration config = new dtoConfiguration();
    private long lastClickTime = 0;
    private int lastScoreBall = 0;
    private boolean preventDblClick = true;
    private PlayerListAdapter team1Adapter;
    private PlayerListAdapter team2Adapter;
    private boolean wagonWheelDone = false;
    private int xCord;
    private int yCord;

    class C02649 implements OnClickListener {
        C02649() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_scorecard);
            initializeControls();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        registerForContextMenu((ImageButton) findViewById(C0252R.id.buttonUndo));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0252R.menu.activity_scorecard, menu);
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == C0252R.id.buttonUndo) {
            inflater.inflate(C0252R.menu.undo_context_menu, menu);
            menu.findItem(C0252R.id.miEndOfDay).setVisible(false);
            menu.findItem(C0252R.id.miEndOfInnings).setVisible(false);
            menu.findItem(C0252R.id.miPenalty).setVisible(false);
            if (Utility.isBitSet(this._choicesToDisplay, 0)) {
                menu.findItem(C0252R.id.miEndOfDay).setVisible(true);
            }
            if (Utility.isBitSet(this._choicesToDisplay, 1)) {
                menu.findItem(C0252R.id.miEndOfInnings).setVisible(true);
            }
            if (this._penalty != 0) {
                menu.findItem(C0252R.id.miPenalty).setVisible(true);
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.miEndOfDay:
                onUndo((int) C0252R.string.undoEndOfDayText);
                return true;
            case C0252R.id.miEndOfInnings:
                onUndo((int) C0252R.string.undoEndOfInngsText);
                return true;
            case C0252R.id.miPenalty:
                onUndo((int) C0252R.string.undoPenaltyText);
                return true;
            case C0252R.id.miUndoLastBall:
                onUndo((int) C0252R.string.undoText);
                return true;
            default:
                return super.onContextItemSelected(item);
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

    protected void initializeDefaults() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        this._totalBallNo = 0;
        this._overNo = 0;
        this._ballNo = 0;
        this._scoreLastBall = 0;
        this._totalScore = 0;
        this._target = 0;
        this._wicketCount = 0;
        this._ballsThisOver = 0;
        this._retiredBatsmanBallNo = -1;
        this._inningNo = 1;
        this._dayNo = 1;
        this._prevBatScore = -1;
        this._prevBowlScore = -1;
        this._penalty = 0;
        this._isMatchEnded = false;
        this._teamWon = false;
        this._popupBowler = false;
        this._popupStriker = false;
        this._popupNonStriker = false;
        refMatch.setCurrentSession(1);
        ((TextView) findViewById(C0252R.id.textOvers)).setText(getString(C0252R.string.defaultOversText));
        ((TextView) findViewById(C0252R.id.textScore)).setText(getString(C0252R.string.defaultScoreText));
        ((TextView) findViewById(C0252R.id.bucketTarget)).setText(getString(C0252R.string.run0));
        ((TextView) findViewById(C0252R.id.textCurrentRunRate)).setText(getString(C0252R.string.defaultOversText));
        ((TextView) findViewById(C0252R.id.textExtras)).setText(getString(C0252R.string.run0));
        ((TextView) findViewById(C0252R.id.strikerScore)).setText(getString(C0252R.string.run0));
        ((TextView) findViewById(C0252R.id.nonStrikerScore)).setText(getString(C0252R.string.run0));
        ((TextView) findViewById(C0252R.id.BallbyBall)).setText(getString(C0252R.string.spaceText));
        ((TextView) findViewById(C0252R.id.ballsStriker)).setText(getString(C0252R.string.run0));
        ((TextView) findViewById(C0252R.id.ballsNonStriker)).setText(getString(C0252R.string.run0));
        findViewById(C0252R.id.checkBatsCrossed).setVisibility(8);
    }

    protected void initializeControls() {
        boolean z;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        DatabaseHandler db = new DatabaseHandler(this);
        this.config = db.getConfiguration();
        db.close();
        initializeDefaults();
        this._wagonWheelEnabled = settings.getWagonWheel() == 1;
        if (settings.getWwForDotBall() == 1) {
            z = true;
        } else {
            z = false;
        }
        this._wagonWheelForDotBallEnabled = z;
        if (settings.getWwForWicket() == 1) {
            z = true;
        } else {
            z = false;
        }
        this._wagonWheelForWicketEnabled = z;
        if (settings.getBallSpot() == 1) {
            z = true;
        } else {
            z = false;
        }
        this._ballSpotEnabled = z;
        if (settings.getPresetTeams() == 1) {
            z = true;
        } else {
            z = false;
        }
        this._presetTeamsEnabled = z;
        this.autoSwitchBatsman = true;
        this.autoBowlSpell = false;
        if (settings.getRestrictBpo() == 1) {
            findViewById(C0252R.id.RpoOn).setVisibility(0);
        } else {
            findViewById(C0252R.id.RpoOn).setVisibility(8);
        }
        populateTeamAutoComplete();
        initializeSpinners();
        initializeExtras();
        findViewById(C0252R.id.bucketWicketAs).setVisibility(8);
        if (refMatch.getIsMatchReopened()) {
            reopenMatch();
            refMatch.setIsMatchReopened(false);
        }
        populateMatchTitle();
        populateTargetBox();
        setLastBatsmen();
        showHideDayButton();
        if (!this._teamWon) {
            showPlayerChangePopups();
        }
    }

    private void initializeBallPositions() {
        findViewById(C0252R.id.inlineBallPositions).setVisibility(8);
        findViewById(C0252R.id.labelBallPosition).setVisibility(8);
        findViewById(C0252R.id.labelBallSpot).setVisibility(8);
        findViewById(C0252R.id.wagonWheelGroup).setVisibility(8);
        findViewById(C0252R.id.ballSpotGroup).setVisibility(8);
        if (this.config.getInlineWwBs() == 1 && (this._wagonWheelEnabled || this._ballSpotEnabled)) {
            findViewById(C0252R.id.inlineBallPositions).setVisibility(0);
            if (this._wagonWheelEnabled) {
                findViewById(C0252R.id.labelBallPosition).setVisibility(0);
                findViewById(C0252R.id.wagonWheelGroup).setVisibility(0);
            }
            if (this._ballSpotEnabled) {
                findViewById(C0252R.id.labelBallSpot).setVisibility(0);
                findViewById(C0252R.id.ballSpotGroup).setVisibility(0);
            }
        }
        final ImageView wagonWheelImage = (ImageView) findViewById(C0252R.id.imageWagonWheel);
        final ImageView wwBallPointer = (ImageView) findViewById(C0252R.id.wwBallPointer);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int measurement = outMetrics.widthPixels < outMetrics.heightPixels ? outMetrics.widthPixels : outMetrics.heightPixels;
        LayoutParams params = new LayoutParams(measurement / 2, measurement / 2);
        params.addRule(14);
        wagonWheelImage.setLayoutParams(params);
        wagonWheelImage.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                LayoutParams params = (LayoutParams) wwBallPointer.getLayoutParams();
                switch (event.getAction()) {
                    case 1:
                        int imageLeft;
                        int imageTop;
                        int imageWidth;
                        int imageHeight;
                        wwBallPointer.setVisibility(0);
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        int minSize = wagonWheelImage.getWidth() < wagonWheelImage.getHeight() ? wagonWheelImage.getWidth() : wagonWheelImage.getHeight();
                        int maxSize = wagonWheelImage.getWidth() > wagonWheelImage.getHeight() ? wagonWheelImage.getWidth() : wagonWheelImage.getHeight();
                        if (wagonWheelImage.getWidth() > wagonWheelImage.getHeight()) {
                            imageLeft = (maxSize / 2) - (minSize / 2);
                            imageTop = 0;
                            imageWidth = minSize;
                            imageHeight = minSize;
                        } else {
                            imageLeft = 0;
                            imageTop = (maxSize / 2) - (minSize / 2);
                            imageWidth = minSize;
                            imageHeight = minSize;
                        }
                        if (x_cord < imageLeft) {
                            x_cord = imageLeft;
                        }
                        if (y_cord < imageTop) {
                            y_cord = imageTop;
                        }
                        if (x_cord > imageWidth + imageLeft) {
                            x_cord = imageWidth + imageLeft;
                        }
                        if (y_cord > imageHeight + imageTop) {
                            y_cord = imageHeight + imageTop;
                        }
                        params.leftMargin = (wagonWheelImage.getLeft() + x_cord) - 8;
                        params.topMargin = (wagonWheelImage.getTop() + y_cord) - 8;
                        wwBallPointer.setLayoutParams(new LayoutParams(new MarginLayoutParams(params)));
                        y_cord = (int) Math.round(((960.0d / ((double) imageHeight)) * 1.0d) * ((double) (y_cord - imageTop)));
                        Scorecard.this.xCord = (int) Math.round(((960.0d / ((double) imageWidth)) * 1.0d) * ((double) (x_cord - imageLeft)));
                        Scorecard.this.yCord = y_cord;
                        break;
                }
                return true;
            }
        });
        final ImageView ballSpotImage = (ImageView) findViewById(C0252R.id.imageBallSpot);
        final ImageView bsBallPointer = (ImageView) findViewById(C0252R.id.bsBallPointer);
        ballSpotImage.setLayoutParams(params);
        ballSpotImage.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                LayoutParams params = (LayoutParams) bsBallPointer.getLayoutParams();
                switch (event.getAction()) {
                    case 1:
                        int imageLeft;
                        int imageTop;
                        int imageWidth;
                        int imageHeight;
                        bsBallPointer.setVisibility(0);
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        int minSize = ballSpotImage.getWidth() < ballSpotImage.getHeight() ? ballSpotImage.getWidth() : ballSpotImage.getHeight();
                        int maxSize = ballSpotImage.getWidth() > ballSpotImage.getHeight() ? ballSpotImage.getWidth() : ballSpotImage.getHeight();
                        if (ballSpotImage.getWidth() > ballSpotImage.getHeight()) {
                            imageLeft = (maxSize / 2) - (minSize / 2);
                            imageTop = 0;
                            imageWidth = minSize;
                            imageHeight = minSize;
                        } else {
                            imageLeft = 0;
                            imageTop = (maxSize / 2) - (minSize / 2);
                            imageWidth = minSize;
                            imageHeight = minSize;
                        }
                        if (x_cord < imageLeft) {
                            x_cord = imageLeft;
                        }
                        if (y_cord < imageTop) {
                            y_cord = imageTop;
                        }
                        if (x_cord > imageWidth + imageLeft) {
                            x_cord = imageWidth + imageLeft;
                        }
                        if (y_cord > imageHeight + imageTop) {
                            y_cord = imageHeight + imageTop;
                        }
                        params.leftMargin = (wagonWheelImage.getLeft() + x_cord) - 8;
                        params.topMargin = (wagonWheelImage.getTop() + y_cord) - 8;
                        bsBallPointer.setLayoutParams(new LayoutParams(new MarginLayoutParams(params)));
                        y_cord = (int) Math.round(((960.0d / ((double) imageHeight)) * 1.0d) * ((double) (y_cord - imageTop)));
                        Scorecard.this.bsxCord = (int) Math.round(((960.0d / ((double) imageWidth)) * 1.0d) * ((double) (x_cord - imageLeft)));
                        Scorecard.this.bsyCord = y_cord;
                        break;
                }
                return true;
            }
        });
        wwBallPointer.setVisibility(8);
        bsBallPointer.setVisibility(8);
    }

    private void initializeExtras() {
        boolean z;
        boolean z2 = true;
        dtoAdditionalSettings addlSettings = ((CricScorerApp) getApplication()).currentMatch.getAdditionalSettings();
        findViewById(C0252R.id.wideCheckBox).setEnabled(addlSettings.getWide() == 1);
        View findViewById = findViewById(C0252R.id.noballCheckBox);
        if (addlSettings.getNoball() == 1) {
            z = true;
        } else {
            z = false;
        }
        findViewById.setEnabled(z);
        findViewById = findViewById(C0252R.id.legbyesCheckBox);
        if (addlSettings.getLegbyes() == 1) {
            z = true;
        } else {
            z = false;
        }
        findViewById.setEnabled(z);
        View findViewById2 = findViewById(C0252R.id.byesCheckBox);
        if (addlSettings.getByes() != 1) {
            z2 = false;
        }
        findViewById2.setEnabled(z2);
        initializeBallPositions();
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

    public void onStriker(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        TextView stkBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        String playerName = ((Button) view).getText().toString();
        String nonStrikerName = ((Button) findViewById(C0252R.id.buttonNonStriker)).getText().toString();
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        boolean canRetireOrReplace = true;
        if (lastBall == null) {
            canRetireOrReplace = Integer.parseInt(new StringBuilder().append("0").append(stkBalls.getText().toString()).toString()) > 0 && Integer.parseInt("0" + stkBalls.getText().toString()) > 0;
        } else if (lastBall.isFlagEOI()) {
            canRetireOrReplace = false;
        } else if (lastBall.getIsWicket() && ((this._lastBatsmanOut == 0 && this._isBatsmanOut == 1) || ((this._lastBatsmanOut == 1 && this._isBatsmanOut == 0) || this._isBatsmanOut == this._lastBatsmanOut))) {
            canRetireOrReplace = false;
        } else if (lastBall.getStrikerName().equalsIgnoreCase(playerName) || lastBall.getNonStrikerName().equalsIgnoreCase(playerName)) {
            canRetireOrReplace = true;
        } else if (lastBall.getStrikerRuns() + lastBall.getBallsFacedStriker() == 0) {
            canRetireOrReplace = false;
        }
        displayPopup((Button) view, C0252R.string.strikerText, 1, canRetireOrReplace, canRetireOrReplace);
    }

    public void onNonStriker(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        TextView nstkBalls = (TextView) findViewById(C0252R.id.ballsNonStriker);
        String playerName = ((Button) view).getText().toString();
        String strikerName = ((Button) findViewById(C0252R.id.buttonStriker)).getText().toString();
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        boolean canRetireOrReplace = true;
        if (lastBall == null) {
            canRetireOrReplace = Integer.parseInt(new StringBuilder().append("0").append(nstkBalls.getText().toString()).toString()) > 0 && Integer.parseInt("0" + nstkBalls.getText().toString()) > 0;
        } else if (lastBall.isFlagEOI()) {
            canRetireOrReplace = false;
        } else if (lastBall.getIsWicket() && ((this._lastBatsmanOut == 0 && this._isBatsmanOut == 2) || ((this._lastBatsmanOut == 2 && this._isBatsmanOut == 0) || this._isBatsmanOut == this._lastBatsmanOut))) {
            canRetireOrReplace = false;
        } else if (lastBall.getStrikerName().equalsIgnoreCase(playerName) || lastBall.getNonStrikerName().equalsIgnoreCase(playerName)) {
            canRetireOrReplace = true;
        } else if (lastBall.getNonStrikerRuns() + lastBall.getBallsFacedNonStriker() == 0) {
            canRetireOrReplace = false;
        }
        displayPopup((Button) view, C0252R.string.nonStrikerText, 1, canRetireOrReplace, canRetireOrReplace);
    }

    public void onBowler(View view) {
        boolean z;
        boolean z2 = false;
        Button button = (Button) view;
        if (this._ballsThisOver > 0) {
            z = true;
        } else {
            z = false;
        }
        if (this._ballsThisOver > 0) {
            z2 = true;
        }
        displayPopup(button, C0252R.string.bowlerText, 2, z, z2);
    }

    public void onWicketBy(View view) {
        displayPopup((Button) view, C0252R.string.playerText, 2, false, false);
    }

    public void showPlayerChangePopups() {
        if (this._popupBowler) {
            displayPopup((Button) findViewById(C0252R.id.buttonBowler), C0252R.string.bowlerText, 2, false, false);
            this._popupBowler = false;
        }
        if (this._popupStriker) {
            displayPopup((Button) findViewById(C0252R.id.buttonStriker), C0252R.string.strikerText, 1, false, false);
            this._popupStriker = false;
        }
        if (this._popupNonStriker) {
            displayPopup((Button) findViewById(C0252R.id.buttonNonStriker), C0252R.string.nonStrikerText, 1, false, false);
            this._popupNonStriker = false;
        }
    }

    private void displayPopup(Button target, int title, int batOrBowl, boolean canReplace, boolean canRetire) {
        int position;
        Rect displayRectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C0252R.layout.popup_choose_player, (ViewGroup) findViewById(C0252R.id.choosePopupLayout));
        layout.setMinimumWidth((int) (((float) displayRectangle.width()) * 0.9f));
        final TextView textTitle = new TextView(layout.getContext());
        if (canRetire) {
            textTitle.setText(getResources().getString(C0252R.string.retireText) + " " + getResources().getString(title));
        } else {
            textTitle.setText(getResources().getString(title));
        }
        if (VERSION.SDK_INT < 23) {
            Utility.setLargeTitle(textTitle, layout.getContext());
        } else {
            Utility.setLargeTitle(textTitle);
        }
        textTitle.setMinimumHeight(60);
        if (this._presetTeamsEnabled) {
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
                textTitle.setBackgroundResource(a.resourceId);
            } else {
                int windowBackground = a.data;
                layout.setBackgroundColor(windowBackground);
                textTitle.setBackgroundColor(windowBackground);
            }
        }
        Builder builder = new Builder(this);
        builder.setView(layout);
        builder.setCustomTitle(textTitle);
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
        final int i = title;
        final boolean z = canRetire;
        cbReplace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cbReplace.isChecked()) {
                    textTitle.setText(Scorecard.this.getResources().getString(C0252R.string.replaceText) + " " + Scorecard.this.getResources().getString(i));
                    Toast.makeText(Scorecard.this.getApplicationContext(), "Important Warning: Replacing player CANNOT be UNDONE!", 1).show();
                } else if (z) {
                    textTitle.setText(Scorecard.this.getResources().getString(C0252R.string.retireText) + " " + Scorecard.this.getResources().getString(i));
                } else {
                    textTitle.setText(Scorecard.this.getResources().getString(i));
                }
            }
        });
        final Button button = target;
        final boolean z2 = canReplace;
        final CheckBox checkBox = cbReplace;
        final int i2 = title;
        final boolean z3 = canRetire;
        ((Button) layout.findViewById(C0252R.id.btnOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String playerName = "";
                if (!textPlayer.getText().toString().trim().equals("")) {
                    playerName = textPlayer.getText().toString().trim();
                    if (!Scorecard.this._allPlayerList.contains(Utility.toDisplayCase(playerName))) {
                        Scorecard.this._allPlayerList.add(playerName);
                    }
                } else if (spinnerPlayer.getItemAtPosition(spinnerPlayer.getSelectedItemPosition()) != null) {
                    String name = ((PlayerListAdapter) spinnerPlayer.getAdapter()).getPlayerName(spinnerPlayer.getSelectedItemPosition());
                    if (name.trim().equalsIgnoreCase("")) {
                        Toast.makeText(Scorecard.this.getApplicationContext(), "Player name cannot be empty!", 0).show();
                        return;
                    }
                    playerName = "" + name.trim();
                }
                if (!button.getText().toString().trim().equalsIgnoreCase(playerName)) {
                    if (z2 && checkBox.isChecked()) {
                        if (i2 == C0252R.string.strikerText) {
                            Scorecard.this.replaceWithPlayer(playerName, 1);
                        } else if (i2 == C0252R.string.nonStrikerText) {
                            Scorecard.this.replaceWithPlayer(playerName, 2);
                        } else if (i2 == C0252R.string.bowlerText) {
                            Scorecard.this.replaceWithPlayer(playerName, 3);
                        }
                    }
                    if (z3) {
                        if (!checkBox.isChecked()) {
                            if (i2 == C0252R.string.strikerText) {
                                Scorecard.this.setBatsmanForRetiredOut(1, playerName);
                            } else if (i2 == C0252R.string.nonStrikerText) {
                                Scorecard.this.setBatsmanForRetiredOut(2, playerName);
                            } else if (i2 == C0252R.string.bowlerText) {
                                Scorecard.this.retireBowler(playerName);
                            }
                        }
                    } else if (!checkBox.isChecked()) {
                        if (i2 == C0252R.string.strikerText) {
                            Scorecard.this.getRetiredBatsmanDetails(1, playerName);
                        } else if (i2 == C0252R.string.nonStrikerText) {
                            Scorecard.this.getRetiredBatsmanDetails(2, playerName);
                        }
                    }
                    if (i2 == C0252R.string.strikerText) {
                        Scorecard.this.findViewById(C0252R.id.labelStriker).setBackgroundColor(0);
                        Scorecard.this._popupStriker = false;
                    } else if (i2 == C0252R.string.nonStrikerText) {
                        Scorecard.this.findViewById(C0252R.id.labelNonStriker).setBackgroundColor(0);
                        Scorecard.this._popupNonStriker = false;
                    } else if (i2 == C0252R.string.bowlerText) {
                        Scorecard.this.findViewById(C0252R.id.labelBowler).setBackgroundColor(0);
                        Scorecard.this._popupBowler = false;
                        if (Scorecard.this._ballsThisOver == 0) {
                            ((TextView) Scorecard.this.findViewById(C0252R.id.BallbyBall)).setText("");
                        }
                    }
                    if (Scorecard.this._isBatsmanOut != 0) {
                        Scorecard.this._lastBatsmanOut = Scorecard.this._isBatsmanOut;
                    }
                    Scorecard.this._isBatsmanOut = 0;
                }
                button.setText(playerName);
                ((InputMethodManager) Scorecard.this.getSystemService("input_method")).hideSoftInputFromWindow(textPlayer.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
        ((Button) layout.findViewById(C0252R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) Scorecard.this.getSystemService("input_method")).hideSoftInputFromWindow(textPlayer.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(C0252R.id.menu_dblclick).setChecked(this.preventDblClick);
        if (VERSION.SDK_INT < 14) {
            if (this.preventDblClick) {
                menu.findItem(C0252R.id.menu_dblclick).setTitle(getString(C0252R.string.preventDblClkText) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_dblclick).setTitle(getString(C0252R.string.preventDblClkText));
            }
        }
        menu.findItem(C0252R.id.menu_autoSwitchBats).setChecked(this.autoSwitchBatsman);
        if (VERSION.SDK_INT < 14) {
            if (this.autoSwitchBatsman) {
                menu.findItem(C0252R.id.menu_autoSwitchBats).setTitle(getString(C0252R.string.autoSwitchBatsmenText) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_autoSwitchBats).setTitle(getString(C0252R.string.autoSwitchBatsmenText));
            }
        }
        menu.findItem(C0252R.id.menu_autoBowlSpell).setChecked(this.autoBowlSpell);
        if (VERSION.SDK_INT < 14) {
            if (this.autoBowlSpell) {
                menu.findItem(C0252R.id.menu_autoBowlSpell).setTitle(getString(C0252R.string.autoBowlSpellText) + "  ✔");
            } else {
                menu.findItem(C0252R.id.menu_autoBowlSpell).setTitle(getString(C0252R.string.autoBowlSpellText));
            }
        }
        return true;
    }

    public void onBackPressed() {
        this._saving = true;
        saveMatch();
        this._saving = false;
        super.onBackPressed();
    }

    public void onPause() {
        this._saving = true;
        saveMatch();
        this._saving = false;
        super.onPause();
    }

    public void onStop() {
        this._saving = true;
        saveMatch();
        this._saving = false;
        super.onStop();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean z = true;
        switch (item.getItemId()) {
            case C0252R.id.menu_addtionalSettings:
                showAdditionalSettings();
                break;
            case C0252R.id.menu_overrideTarget:
                overrideTarget();
                break;
            case C0252R.id.menu_overrideOvers:
                overrideOvers();
                break;
            case C0252R.id.menu_dblclick:
                if (this.preventDblClick) {
                    z = false;
                }
                this.preventDblClick = z;
                break;
            case C0252R.id.menu_autoSwitchBats:
                if (this.autoSwitchBatsman) {
                    z = false;
                }
                this.autoSwitchBatsman = z;
                break;
            case C0252R.id.menu_autoBowlSpell:
                if (this.autoBowlSpell) {
                    z = false;
                }
                this.autoBowlSpell = z;
                break;
            case C0252R.id.menu_addPenalty:
                addPenalty();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAdditionalSettings() {
        Rect displayRectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C0252R.layout.popup_settings, (ViewGroup) findViewById(C0252R.id.mainLayout));
        layout.setMinimumWidth((int) (((float) displayRectangle.width()) * 0.9f));
        if (VERSION.SDK_INT < 11) {
            TypedValue a = new TypedValue();
            getTheme().resolveAttribute(16842836, a, true);
            if (a.type < 28 || a.type > 31) {
                layout.setBackgroundResource(a.resourceId);
            } else {
                layout.setBackgroundColor(a.data);
            }
        }
        Builder builder = new Builder(this);
        builder.setView(layout);
        builder.setTitle("Match Settings");
        final AlertDialog alertDialog = builder.create();
        final dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        final dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        final CheckBox cbWide = (CheckBox) layout.findViewById(C0252R.id.wideCheckBox);
        final CheckBox cbWideReball = (CheckBox) layout.findViewById(C0252R.id.wideReballCheckBox);
        final CheckBox cbNoball = (CheckBox) layout.findViewById(C0252R.id.noballCheckBox);
        final CheckBox cbNoballReball = (CheckBox) layout.findViewById(C0252R.id.noballReballCheckBox);
        final CheckBox cbLegByes = (CheckBox) layout.findViewById(C0252R.id.legByesCheckBox);
        final CheckBox cbByes = (CheckBox) layout.findViewById(C0252R.id.byesCheckBox);
        final CheckBox cbLbw = (CheckBox) layout.findViewById(C0252R.id.lbwCheckBox);
        final CheckBox cbWagonWheel = (CheckBox) layout.findViewById(C0252R.id.wagonWheelCheckBox);
        final CheckBox cbBallSpot = (CheckBox) layout.findViewById(C0252R.id.ballSpotCheckBox);
        final CheckBox cbPresetTeams = (CheckBox) layout.findViewById(C0252R.id.presetTeamCheckBox);
        final CheckBox cbWwForDotBall = (CheckBox) layout.findViewById(C0252R.id.wagonWheelForDotBallCheckBox);
        final CheckBox cbWwForWicket = (CheckBox) layout.findViewById(C0252R.id.wagonWheelForWicketCheckBox);
        cbWide.setChecked(settings.getWide() == 1);
        cbWideReball.setChecked(settings.getWideReball() == 1);
        cbNoball.setChecked(settings.getNoball() == 1);
        cbNoballReball.setChecked(settings.getNoballReball() == 1);
        cbLegByes.setChecked(settings.getLegbyes() == 1);
        cbByes.setChecked(settings.getByes() == 1);
        cbLbw.setChecked(settings.getLbw() == 1);
        cbWagonWheel.setChecked(settings.getWagonWheel() == 1);
        cbBallSpot.setChecked(settings.getBallSpot() == 1);
        cbPresetTeams.setChecked(settings.getPresetTeams() == 1);
        cbWwForDotBall.setChecked(settings.getWwForDotBall() == 1);
        cbWwForWicket.setChecked(settings.getWwForWicket() == 1);
        ((Button) layout.findViewById(C0252R.id.btnOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i;
                int i2 = 0;
                settings.setWide(cbWide.isChecked() ? 1 : 0);
                dtoAdditionalSettings com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbWideReball.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setWideReball(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbNoball.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setNoball(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbNoballReball.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setNoballReball(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbLegByes.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setLegbyes(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbByes.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setByes(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbLbw.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setLbw(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbWagonWheel.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setWagonWheel(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbBallSpot.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setBallSpot(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbPresetTeams.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setPresetTeams(i);
                com_ganapathy_cricscorer_dtoAdditionalSettings = settings;
                if (cbWwForDotBall.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings.setWwForDotBall(i);
                dtoAdditionalSettings com_ganapathy_cricscorer_dtoAdditionalSettings2 = settings;
                if (cbWwForWicket.isChecked()) {
                    i2 = 1;
                }
                com_ganapathy_cricscorer_dtoAdditionalSettings2.setWwForWicket(i2);
                try {
                    DatabaseHandler db = new DatabaseHandler(Scorecard.this.getApplicationContext());
                    db.updateMatchSettings(refMatch);
                    db.close();
                    Scorecard.this._wagonWheelEnabled = cbWagonWheel.isChecked();
                    Scorecard.this._wagonWheelForDotBallEnabled = cbWwForDotBall.isChecked();
                    Scorecard.this._wagonWheelForWicketEnabled = cbWwForWicket.isChecked();
                    Scorecard.this._ballSpotEnabled = cbBallSpot.isChecked();
                    Scorecard.this._presetTeamsEnabled = cbPresetTeams.isChecked();
                    Scorecard.this.initializeExtras();
                    Scorecard.this.populateWicketTypes();
                } catch (Exception e) {
                    Toast.makeText(Scorecard.this.getApplicationContext(), "Error: " + e.getMessage(), 1).show();
                }
                alertDialog.dismiss();
            }
        });
        final AlertDialog alertDialog2 = alertDialog;
        ((Button) layout.findViewById(C0252R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
        alertDialog.show();
    }

    private void retireBowler(String playerName) {
        dtoScoreBall lastBall = ((CricScorerApp) getApplication()).currentMatch.getLastScoreball();
        if (lastBall != null) {
            Toast toast = Toast.makeText(getApplicationContext(), lastBall.getBowlerName() + " Retired. Remaining balls will be bowled by " + playerName + ".", 1);
            toast.setGravity(17, 0, 0);
            toast.show();
        }
    }

    private void replaceWithPlayer(String playerName, int playerType) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        if (lastBall != null) {
            String fromPlayer = "";
            if (playerType == 1) {
                if (this._lastStriker.equalsIgnoreCase(lastBall.getStrikerName()) || this._lastStriker.equalsIgnoreCase(lastBall.getNonStrikerName())) {
                    fromPlayer = this._lastStriker;
                } else if (lastBall.getRunsScored() % 2 == 0) {
                    fromPlayer = lastBall.getStrikerName();
                } else {
                    fromPlayer = lastBall.getNonStrikerName();
                }
            } else if (playerType == 2) {
                if (this._lastNonStriker.equalsIgnoreCase(lastBall.getStrikerName()) || this._lastNonStriker.equalsIgnoreCase(lastBall.getNonStrikerName())) {
                    fromPlayer = this._lastNonStriker;
                } else if (lastBall.getRunsScored() % 2 == 0) {
                    fromPlayer = lastBall.getNonStrikerName();
                } else {
                    fromPlayer = lastBall.getStrikerName();
                }
            } else if (playerType == 3) {
                fromPlayer = lastBall.getBowlerName();
            }
            try {
                DatabaseHandler db = new DatabaseHandler(this);
                if (playerType == 1 || playerType == 2) {
                    db.replaceBatsman(refMatch.getMatchID(), lastBall.getTeamNo(), "" + lastBall.getInningNo(), fromPlayer, playerName);
                    setLastBatsmen();
                } else if (playerType == 3) {
                    db.replaceBowler(refMatch.getMatchID(), lastBall.getTeamNo(), "" + lastBall.getInningNo(), fromPlayer, playerName);
                }
                db.close();
                dtoScoreBall ball = Utility.getLastScoreBall(this, refMatch.getMatchID());
                if (ball != null) {
                    refMatch.setLastScoreball(ball);
                }
                Toast toast = Toast.makeText(getApplicationContext(), "Player " + fromPlayer + " is replaced with " + playerName, 1);
                toast.setGravity(17, 0, 0);
                toast.show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), 1).show();
            }
        }
    }

    private void addPenalty() {
        if (((CricScorerApp) getApplication()).currentMatch.getLastScoreball() == null) {
            Toast.makeText(getApplicationContext(), "Innings has not started yet to add penalty!", 1).show();
        } else if (this._penalty != 0) {
            Toast.makeText(getApplicationContext(), "Ball already has penalty. Please UNDO to update penalty!", 1).show();
        } else {
            Builder builder = new Builder(this);
            builder.setTitle("Penalty Runs");
            final EditText input = new EditText(this);
            input.setInputType(InputDeviceCompat.SOURCE_TOUCHSCREEN);
            input.setText("");
            input.setHint("+/- Runs. Ex. -5 or 5");
            input.setImeOptions(6);
            builder.setView(input);
            builder.setPositiveButton("Set", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int value = Integer.parseInt("" + input.getText().toString());
                        if (value == 0 || value > 9999 || value < -9999) {
                            Toast.makeText(Scorecard.this.getApplicationContext(), "Penalty runs should not be 0 and should be valid between -9999 and 9999!", 1).show();
                        } else {
                            Scorecard.this.updatePenalty(value, false);
                        }
                    } catch (Exception e) {
                        Toast.makeText(Scorecard.this.getApplicationContext(), "Please enter the valid penalty runs!", 1).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new C02649());
            builder.show();
        }
    }

    private void overrideTarget() {
        if (((CricScorerApp) getApplication()).currentMatch.isFinalButOneInnings()) {
            Toast.makeText(getApplicationContext(), "Target has not been set yet!", 0).show();
            return;
        }
        Builder builder = new Builder(this);
        builder.setTitle("Override Target");
        final EditText input = new EditText(this);
        input.setInputType(2);
        input.setText("" + this._target);
        builder.setView(input);
        builder.setPositiveButton("Override", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int value = Integer.parseInt("0" + input.getText().toString());
                    if (value <= 0) {
                        Toast.makeText(Scorecard.this.getApplicationContext(), "Target must be greater than 0 runs!", 1).show();
                        return;
                    }
                    if (Scorecard.this._originalTarget == -1) {
                        Scorecard.this._originalTarget = Scorecard.this._target;
                    }
                    Scorecard.this._target = value;
                    Scorecard.this.populateTargetBox();
                } catch (Exception e) {
                    Toast.makeText(Scorecard.this.getApplicationContext(), "Please enter the valid runs!", 1).show();
                }
            }
        });
        builder.setNeutralButton("Reset", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Scorecard.this._originalTarget > 0) {
                    input.setText("" + Scorecard.this._originalTarget);
                    Scorecard.this._target = Scorecard.this._originalTarget;
                    Scorecard.this.populateTargetBox();
                }
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void overrideOvers() {
        final dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        int totalOvers = refMatch.getOvers();
        Builder builder = new Builder(this);
        builder.setTitle("Update Overs");
        final EditText input = new EditText(this);
        input.setInputType(2);
        input.setText("" + totalOvers);
        builder.setView(input);
        builder.setPositiveButton("Override", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int calculateCurrentOvers = Scorecard.this._overNo;
                    if (Scorecard.this._ballNo > 0) {
                        calculateCurrentOvers++;
                    }
                    int value = Integer.parseInt("0" + input.getText().toString());
                    if (value <= 0 || value < calculateCurrentOvers) {
                        Toast.makeText(Scorecard.this.getApplicationContext(), String.format("Overs must be greater than %d!", new Object[]{Integer.valueOf(calculateCurrentOvers)}), 1).show();
                        return;
                    }
                    refMatch.setOvers(value);
                    Scorecard.this.updateOvers();
                    Scorecard.this.populateTargetBox();
                } catch (Exception e) {
                    Toast.makeText(Scorecard.this.getApplicationContext(), "Please enter the valid number!", 1).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void updatePenalty(int value, boolean isUndo) {
        String sBallByBall;
        int i;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        if (isUndo) {
            sBallByBall = lastBall.getBallByBall().length() > lastBall.getBallByBall().length() - lastBall.getBallByBall().lastIndexOf(40) ? lastBall.getBallByBall().substring(0, lastBall.getBallByBall().lastIndexOf(40)) : "";
        } else {
            sBallByBall = lastBall.getBallByBall() + (value >= 0 ? "(+" + value + ")" : "(" + value + ")");
        }
        if (isUndo) {
            i = 0;
        } else {
            i = value;
        }
        this._penalty = i;
        DatabaseHandler db = new DatabaseHandler(this);
        db.setPenaltyOnLastScoreBall(refMatch.getMatchID(), "" + this._penalty, "" + value, sBallByBall);
        db.close();
        refMatch.setIsMatchReopened(true);
        initializeControls();
    }

    public void updateOvers() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateMatchOvers(refMatch.getMatchID(), refMatch.getOvers());
        db.close();
        TextView textOver = (TextView) findViewById(C0252R.id.textOvers);
        if ((this._ballNo >= settings.getBpo() || isBallsRestrictedForOver(settings, this._ballsThisOver)) && this._overNo < refMatch.getOvers()) {
            this._ballNo = 0;
            this._ballsThisOver = 0;
            textOver.setText("" + this._overNo + ".0");
            this._clearBallByBall = true;
            if (this.autoSwitchBatsman) {
                switchBatsmen();
            }
            findViewById(C0252R.id.labelBowler).setBackgroundColor(SupportMenu.CATEGORY_MASK);
        }
        Toast.makeText(getApplicationContext(), String.format("Match has been revised to %d Overs", new Object[]{Integer.valueOf(refMatch.getOvers())}), 1).show();
    }

    private void populateMatchTitle() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        ((TextView) findViewById(C0252R.id.matchTitle)).setText(refMatch.getCurrentBattingTeamName() + (refMatch.getNoOfInngs() > 1 ? "  Innings-" + this._inningNo : " Innings") + (refMatch.getNoOfDays() > 1 ? "  Day-" + Math.min(this._dayNo, refMatch.getNoOfDays()) : "") + "  Match Inngs-" + refMatch.getCurrentSession());
    }

    private void reopenMatch() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        dtoScoreBall ball = Utility.getLastScoreBall(getApplicationContext(), refMatch.getMatchID());
        if (ball != null) {
            refMatch.setLastScoreball(ball);
        }
        if (ball != null) {
            this._clearBallByBall = false;
            findViewById(C0252R.id.labelStriker).setBackgroundColor(0);
            this._popupStriker = false;
            findViewById(C0252R.id.labelNonStriker).setBackgroundColor(0);
            this._popupNonStriker = false;
            findViewById(C0252R.id.labelBowler).setBackgroundColor(0);
            this._popupBowler = false;
            this._totalBallNo = ball.getTotalBallNo();
            this._overNo = ball.getOverNo();
            this._ballNo = ball.getBallNo();
            this._totalScore = ball.getTotalScore();
            this._target = ball.getTarget();
            this._wicketCount = ball.getWicketCount();
            this._ballsThisOver = ball.getBallsThisOver();
            this._inningNo = ball.getInningNo();
            this._dayNo = ball.getDayNo();
            this._penalty = ball.getIsPenalty();
            this._wagonWheelEnabled = settings.getWagonWheel() == 1;
            this._wagonWheelForDotBallEnabled = settings.getWwForDotBall() == 1;
            this._wagonWheelForWicketEnabled = settings.getWwForWicket() == 1;
            this._ballSpotEnabled = settings.getBallSpot() == 1;
            this._presetTeamsEnabled = settings.getPresetTeams() == 1;
            this._isMatchEnded = false;
            String _currentStriker = ball.getStrikerName();
            String _currentNonStriker = ball.getNonStrikerName();
            String _currentBowler = ball.getBowlerName();
            populateTeamAutoComplete();
            initializeSpinners();
            Button nonstrikerButton = (Button) findViewById(C0252R.id.buttonNonStriker);
            Button bowlerButton = (Button) findViewById(C0252R.id.buttonBowler);
            ((Button) findViewById(C0252R.id.buttonStriker)).setText(_currentStriker);
            nonstrikerButton.setText(_currentNonStriker);
            bowlerButton.setText(_currentBowler);
            if (this._ballNo == settings.getBpo() || isBallsRestrictedForOver(settings, ball.getBallsThisOver())) {
                this._overNo++;
                this._ballNo = 0;
                this._ballsThisOver = 0;
                this._clearBallByBall = true;
                findViewById(C0252R.id.labelBowler).setBackgroundColor(SupportMenu.CATEGORY_MASK);
                this._popupBowler = true;
            }
            ((TextView) findViewById(C0252R.id.textOvers)).setText("" + this._overNo + "." + this._ballNo);
            ((TextView) findViewById(C0252R.id.textScore)).setText("" + (this._prevBatScore > 0 ? this._prevBatScore + " & " : "") + this._totalScore + "/" + this._wicketCount);
            setTitle(getString(C0252R.string.title_activity_scorecard) + "-" + ((TextView) findViewById(C0252R.id.textScore)).getText().toString() + " in " + ((TextView) findViewById(C0252R.id.textOvers)).getText().toString());
            ((TextView) findViewById(C0252R.id.bucketTarget)).setText("" + this._target);
            ((TextView) findViewById(C0252R.id.textExtras)).setText("" + ball.getTotalExtras());
            ((TextView) findViewById(C0252R.id.BallbyBall)).setText(ball.getBallByBall());
            ((TextView) findViewById(C0252R.id.strikerScore)).setText("" + ball.getStrikerRuns());
            ((TextView) findViewById(C0252R.id.nonStrikerScore)).setText("" + ball.getNonStrikerRuns());
            ((TextView) findViewById(C0252R.id.ballsStriker)).setText("" + ball.getBallsFacedStriker());
            ((TextView) findViewById(C0252R.id.ballsNonStriker)).setText("" + ball.getBallsFacedNonStriker());
            this._isBatsmanOut = 0;
            if (ball.getIsWicket()) {
                showBatsmenOutToast(ball);
            }
            if (ball.getRunsScored() % 2 != 0 && this.autoSwitchBatsman) {
                switchBatsmen();
            }
            if (this._ballNo == 0 && this.autoSwitchBatsman) {
                switchBatsmen();
            }
            setLastBatsmen();
            ((TextView) findViewById(C0252R.id.textCurrentRunRate)).setText("" + String.format("%.2f", new Object[]{Double.valueOf((((double) this._totalScore) * 1.0d) / ((((double) ((this._overNo * settings.getBpo()) + this._ballNo)) * 1.0d) / ((double) settings.getBpo())))}));
            MatchHelper.updateMatchSessions(refMatch);
            if (ball.isFlagEOD()) {
                doEndOfDay(false);
            }
            if (ball.isFlagEOI()) {
                doEndOfInnings(false, "", ball.isFollowOnInnings());
            }
        }
    }

    protected boolean isBallsRestrictedForOver(dtoAdditionalSettings settings, int ballsThisOver) {
        if (settings.getRestrictBpo() != 1 || ballsThisOver < settings.getMaxBpo()) {
            return false;
        }
        return true;
    }

    private void populateTargetBox() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        if (this._dayNo <= refMatch.getNoOfDays()) {
            if (refMatch.getCurrentSession() <= 2) {
                this._prevBatScore = 0;
                this._prevBowlScore = 0;
            } else if (this._prevBatScore == -1 || this._prevBowlScore == -1) {
                DatabaseHandler db = new DatabaseHandler(this);
                this._prevBatScore = db.getPrevInningsScore(refMatch.getMatchID(), refMatch.getCurrentBattingTeamNo(), this._inningNo);
                this._prevBowlScore = db.getPrevInningsScore(refMatch.getMatchID(), refMatch.getCurrentBowlingTeamNo(), this._inningNo);
                db.close();
            }
            ((TextView) findViewById(C0252R.id.textScore)).setText("" + (this._prevBatScore > 0 ? this._prevBatScore + " & " : "") + this._totalScore + "/" + this._wicketCount);
            if (refMatch.isFirstInnings()) {
                ((Button) findViewById(C0252R.id.buttonEndOfInnings)).setText(C0252R.string.endOfInningsText);
                findViewById(C0252R.id.bucketTarget).setVisibility(8);
            } else if (refMatch.isFinalInnings()) {
                ((Button) findViewById(C0252R.id.buttonEndOfInnings)).setText(C0252R.string.endMatchText);
                findViewById(C0252R.id.bucketTarget).setVisibility(0);
                runsRemaining = ((this._prevBowlScore - this._prevBatScore) + this._target) - this._totalScore;
                int ballsRemaining = (refMatch.getOvers() * settings.getBpo()) - ((this._overNo * settings.getBpo()) + this._ballNo);
                double runRateReq = (((double) runsRemaining) * 1.0d) / ((((double) ballsRemaining) * 1.0d) / ((double) settings.getBpo()));
                calculatedText = "";
                int toWin = (this._prevBowlScore - this._prevBatScore) + this._target;
                if (toWin > 0) {
                    calculatedText = "Target: " + toWin;
                }
                if (refMatch.getNoOfInngs() == 1) {
                    calculatedText = calculatedText + " - Req.: " + runsRemaining + " from " + ballsRemaining + " -  R/R Req.: " + String.format("%.2f", new Object[]{Double.valueOf(runRateReq)});
                } else {
                    calculatedText = calculatedText + " - Req runs: " + runsRemaining;
                }
                if (runsRemaining <= 1 || ballsRemaining <= 0 || this._isMatchEnded) {
                    String wonText = getWinnerText();
                    if (!wonText.trim().equalsIgnoreCase("")) {
                        if (toWin < 0) {
                            calculatedText = wonText + " - " + Math.abs(toWin) + " runs and an innings";
                            updateMatchResult(calculatedText);
                        } else {
                            calculatedText = "Target: " + toWin + " - " + wonText;
                        }
                    }
                }
                ((TextView) findViewById(C0252R.id.bucketTarget)).setText(calculatedText);
            } else {
                ((Button) findViewById(C0252R.id.buttonEndOfInnings)).setText(C0252R.string.endOfInningsText);
                findViewById(C0252R.id.bucketTarget).setVisibility(0);
                if (refMatch.getCurrentSession() > 2) {
                    runsRemaining = (this._prevBowlScore - this._prevBatScore) - this._totalScore;
                } else {
                    runsRemaining = this._target - this._totalScore;
                }
                if (runsRemaining > 0) {
                    calculatedText = String.format("Trial by %d runs", new Object[]{Integer.valueOf(runsRemaining)});
                } else if (runsRemaining < 0) {
                    calculatedText = String.format("Lead by %d runs", new Object[]{Integer.valueOf(Math.abs(runsRemaining))});
                } else {
                    calculatedText = "Innings Scores level";
                }
                ((TextView) findViewById(C0252R.id.bucketTarget)).setText(calculatedText);
            }
        } else if (refMatch.getMatchResult().equalsIgnoreCase("")) {
            findViewById(C0252R.id.bucketTarget).setVisibility(0);
            ((TextView) findViewById(C0252R.id.bucketTarget)).setText("End of match days. Match Drawn!");
            updateMatchResult(getString(C0252R.string.matchDrawText));
        }
    }

    private void initializeSpinners() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> team1Players = db.getMatchTeamPlayers(refMatch.getMatchID(), getString(C0252R.string.dbTeam1));
        List<String> team2Players = db.getMatchTeamPlayers(refMatch.getMatchID(), getString(C0252R.string.dbTeam2));
        db.close();
        List<String> strikerNames = new ArrayList();
        if (refMatch.isTeam1Batting()) {
            for (String player : team1Players) {
                strikerNames.add(player);
            }
        } else {
            for (String player2 : team2Players) {
                strikerNames.add(player2);
            }
        }
        ((Button) findViewById(C0252R.id.buttonStriker)).setText("" + ((String) strikerNames.get(0)));
        if (strikerNames.size() > 1) {
            ((Button) findViewById(C0252R.id.buttonNonStriker)).setText("" + ((String) strikerNames.get(1)));
        } else {
            ((Button) findViewById(C0252R.id.buttonNonStriker)).setText("");
        }
        List<String> bowlerNames = new ArrayList();
        if (refMatch.isTeam1Batting()) {
            for (String player22 : team2Players) {
                bowlerNames.add(player22);
            }
        } else {
            for (String player222 : team1Players) {
                bowlerNames.add(player222);
            }
        }
        ((Button) findViewById(C0252R.id.buttonBowler)).setText("" + ((String) bowlerNames.get(0)));
        populateWicketTypes();
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
        ArrayAdapter<String> adapter5 = new ArrayAdapter(this, 17367048, filteredWicketTypes);
        adapter5.setDropDownViewResource(17367049);
        ((Spinner) findViewById(C0252R.id.spinWicketAs)).setAdapter(adapter5);
    }

    public void setBatsmanForRetiredOut(int who, String playerName) {
        int playerPosition = 0;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        TextView strikerRuns = (TextView) findViewById(C0252R.id.strikerScore);
        TextView strikerBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        TextView nonStrikerRuns = (TextView) findViewById(C0252R.id.nonStrikerScore);
        TextView nonStrikerBalls = (TextView) findViewById(C0252R.id.ballsNonStriker);
        dtoScoreBall ball = refMatch.getLastScoreball();
        if (ball != null) {
            if (who == 1) {
                if (ball.getStrikerName().equalsIgnoreCase(this._lastStriker)) {
                    playerPosition = 1;
                } else if (ball.getNonStrikerName().equalsIgnoreCase(this._lastStriker)) {
                    playerPosition = 2;
                }
            } else if (ball.getStrikerName().equalsIgnoreCase(this._lastNonStriker)) {
                playerPosition = 1;
            } else if (ball.getNonStrikerName().equalsIgnoreCase(this._lastNonStriker)) {
                playerPosition = 2;
            }
            Toast toast;
            if (who == 1) {
                updateRetiredOutBatsman(playerPosition);
                toast = Toast.makeText(this, this._lastStriker + " retired for " + (playerPosition == 1 ? ball.getStrikerRuns() : ball.getNonStrikerRuns()) + "(" + (playerPosition == 1 ? ball.getBallsFacedStriker() : ball.getBallsFacedNonStriker()) + ")", 0);
                toast.setGravity(17, 0, 0);
                toast.show();
            } else {
                int ballsFacedStriker;
                updateRetiredOutBatsman(playerPosition);
                StringBuilder append = new StringBuilder().append(this._lastNonStriker).append(" retired for ").append(playerPosition == 1 ? ball.getStrikerRuns() : ball.getNonStrikerRuns()).append("(");
                if (playerPosition == 1) {
                    ballsFacedStriker = ball.getBallsFacedStriker();
                } else {
                    ballsFacedStriker = ball.getBallsFacedNonStriker();
                }
                toast = Toast.makeText(this, append.append(ballsFacedStriker).append(")").toString(), 0);
                toast.setGravity(17, 0, 0);
                toast.show();
            }
            List<String> player = getRunsForBatsmanRetired(playerPosition, playerName, refMatch.getCurrentBattingTeamNo(), "" + ball.getInningNo());
            if (player.isEmpty()) {
                if (who == 1) {
                    strikerRuns.setText("0");
                    strikerBalls.setText("0");
                } else {
                    nonStrikerRuns.setText("0");
                    nonStrikerBalls.setText("0");
                }
                this._retiredBatsmanBallNo = -1;
                return;
            }
            if (who == 1) {
                strikerRuns.setText((CharSequence) player.get(0));
                strikerBalls.setText((CharSequence) player.get(1));
            } else {
                nonStrikerRuns.setText((CharSequence) player.get(0));
                nonStrikerBalls.setText((CharSequence) player.get(1));
            }
            this._retiredBatsmanBallNo = Integer.parseInt((String) player.get(2));
        }
    }

    public void getRetiredBatsmanDetails(int who, String playerName) {
        int playerPosition = 0;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        TextView strikerRuns = (TextView) findViewById(C0252R.id.strikerScore);
        TextView strikerBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        TextView nonStrikerRuns = (TextView) findViewById(C0252R.id.nonStrikerScore);
        TextView nonStrikerBalls = (TextView) findViewById(C0252R.id.ballsNonStriker);
        dtoScoreBall ball = refMatch.getLastScoreball();
        if (ball != null) {
            if (who == 1) {
                if (ball.getStrikerName().equalsIgnoreCase(this._lastStriker)) {
                    playerPosition = 1;
                } else if (ball.getNonStrikerName().equalsIgnoreCase(this._lastStriker)) {
                    playerPosition = 2;
                }
            } else if (ball.getStrikerName().equalsIgnoreCase(this._lastNonStriker)) {
                playerPosition = 1;
            } else if (ball.getNonStrikerName().equalsIgnoreCase(this._lastNonStriker)) {
                playerPosition = 2;
            }
            List<String> player = getRunsForBatsmanRetired(playerPosition, playerName, refMatch.getCurrentBattingTeamNo(), "" + (ball.getFlagEOI() == 1 ? ball.getInningNo() + 1 : ball.getInningNo()));
            if (player.isEmpty()) {
                if (who == 1) {
                    strikerRuns.setText("0");
                    strikerBalls.setText("0");
                } else {
                    nonStrikerRuns.setText("0");
                    nonStrikerBalls.setText("0");
                }
                this._retiredBatsmanBallNo = -1;
                return;
            }
            if (who == 1) {
                strikerRuns.setText((CharSequence) player.get(0));
                strikerBalls.setText((CharSequence) player.get(1));
            } else {
                nonStrikerRuns.setText((CharSequence) player.get(0));
                nonStrikerBalls.setText((CharSequence) player.get(1));
            }
            this._retiredBatsmanBallNo = Integer.parseInt((String) player.get(2));
        }
    }

    protected void UncheckAllBatStatus() {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.noballCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wicketCheckBox)).setChecked(false);
    }

    protected void showWicketBucket(boolean show) {
        int i;
        int i2 = 0;
        View findViewById = findViewById(C0252R.id.bucketWicketAs);
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        findViewById.setVisibility(i);
        findViewById = findViewById(C0252R.id.buttonSwitchBats);
        if (show) {
            i = 8;
        } else {
            i = 0;
        }
        findViewById.setVisibility(i);
        View findViewById2 = findViewById(C0252R.id.checkBatsCrossed);
        if (!show) {
            i2 = 8;
        }
        findViewById2.setVisibility(i2);
    }

    public void onStrike(View view) {
        UncheckAllBatStatus();
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(true);
        showWicketBucket(false);
    }

    public void onWide(View view) {
        UncheckAllBatStatus();
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(true);
        showWicketBucket(false);
    }

    public void onNoball(View view) {
        UncheckAllBatStatus();
        ((CheckBox) findViewById(C0252R.id.noballCheckBox)).setChecked(true);
        showWicketBucket(false);
    }

    public void onLegByes(View view) {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wicketCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setChecked(true);
        showWicketBucket(false);
    }

    public void onByes(View view) {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wideCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.legbyesCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wicketCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.byesCheckBox)).setChecked(true);
        showWicketBucket(false);
    }

    public void onWicket(View view) {
        ((CheckBox) findViewById(C0252R.id.strikeCheckBox)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.wicketCheckBox)).setChecked(true);
        ((Spinner) findViewById(C0252R.id.spinWicketAs)).setSelection(0);
        ((Button) findViewById(C0252R.id.buttonWicketBy)).setText("");
        showWicketBucket(true);
        ((CheckBox) findViewById(C0252R.id.checkBatsCrossed)).setChecked(false);
    }

    public void onRpoOn(View view) {
        Toast toast = Toast.makeText(this, "" + getString(C0252R.string.warningRPOText), 1);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public void endOfDay(View view) {
        this._saving = true;
        saveMatch();
        this._saving = false;
        if (this._dayNo <= ((CricScorerApp) getApplication()).currentMatch.getNoOfDays()) {
            new Builder(this).setIcon(17301543).setTitle(getString(C0252R.string.endOfDayText) + " " + this._dayNo).setMessage(C0252R.string.areYouSureText).setPositiveButton(C0252R.string.yesText, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Scorecard.this.doEndOfDay(true);
                }
            }).setNegativeButton(C0252R.string.noText, null).show();
        }
    }

    public void doEndOfDay(boolean userClicked) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        if (this._dayNo <= refMatch.getNoOfDays()) {
            this._dayNo++;
        }
        if (userClicked) {
            DatabaseHandler db = new DatabaseHandler(this);
            db.updateEndOfDayOnLastScoreBall(refMatch.getMatchID());
            db.close();
            refMatch.getLastScoreball().setFlagEOD(1);
        }
        if (this._dayNo > refMatch.getNoOfDays()) {
            populateTargetBox();
            showHideDayButton();
            return;
        }
        populateMatchTitle();
    }

    public void showHideDayButton() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        ImageButton dayButton = (ImageButton) findViewById(C0252R.id.buttonEndOfDay);
        if (refMatch.getNoOfDays() <= 1 || this._dayNo > refMatch.getNoOfDays()) {
            dayButton.setVisibility(8);
        } else {
            dayButton.setVisibility(0);
        }
    }

    public void endOfInnings(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        this._saving = true;
        saveMatch();
        this._saving = false;
        Toast toast;
        if (((TextView) findViewById(C0252R.id.bucketTarget)).getText().toString().contains("Won by")) {
            toast = Toast.makeText(this, "Match already WON!\nNote: You can revise Target or Overs from Options", 1);
            toast.setGravity(17, 0, 0);
            toast.show();
        } else if (this._dayNo > refMatch.getNoOfDays()) {
            toast = Toast.makeText(this, "Match days are over!\nNote: You can UNDO the End of Day using UNDO button.", 1);
            toast.setGravity(17, 0, 0);
            toast.show();
        } else {
            dtoScoreBall lastBall = refMatch.getLastScoreball();
            if (lastBall == null || lastBall.isFlagEOI()) {
                toast = Toast.makeText(this, "Innings already marked as Ended!\nNote: You can UNDO the End of innings using UNDO button.", 1);
                toast.setGravity(17, 0, 0);
                toast.show();
                return;
            }
            final List<CharSequence> items = new ArrayList();
            if (refMatch.getNoOfInngs() > 1) {
                items.add(getString(C0252R.string.declareText));
                items.add(getString(C0252R.string.allOutText));
                if (refMatch.getCurrentSession() == 2) {
                    items.add(getString(C0252R.string.allOutFollowOnText));
                }
            } else {
                items.add(getString(C0252R.string.oversUpText));
                items.add(getString(C0252R.string.allOutText));
            }
            items.add(getString(C0252R.string.cancelText));
            new Builder(this).setIcon(17301543).setTitle(getString(C0252R.string.endOfInningsHowText)).setCancelable(false).setItems((CharSequence[]) items.toArray(new CharSequence[items.size()]), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (((CharSequence) items.get(which)).toString().equalsIgnoreCase(Scorecard.this.getString(C0252R.string.declareText))) {
                        Scorecard.this.doEndOfInnings(true, ((CharSequence) items.get(which)).toString(), false);
                    } else if (((CharSequence) items.get(which)).toString().equalsIgnoreCase(Scorecard.this.getString(C0252R.string.allOutText))) {
                        Scorecard.this.doEndOfInnings(true, ((CharSequence) items.get(which)).toString(), false);
                    } else if (((CharSequence) items.get(which)).toString().equalsIgnoreCase(Scorecard.this.getString(C0252R.string.allOutFollowOnText))) {
                        Scorecard.this.doEndOfInnings(true, Scorecard.this.getString(C0252R.string.allOutText), true);
                    } else if (((CharSequence) items.get(which)).toString().equalsIgnoreCase(Scorecard.this.getString(C0252R.string.oversUpText))) {
                        Scorecard.this.doEndOfInnings(true, ((CharSequence) items.get(which)).toString(), false);
                    }
                }
            }).show();
        }
    }

    public void doEndOfInnings(boolean userClicked, String description, boolean followOn) {
        int flagEOI;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        if (lastBall.isFollowOnInnings()) {
            flagEOI = 3;
        } else {
            flagEOI = 1;
        }
        refMatch.setFollowOn(lastBall.isFollowOnInnings() | followOn);
        if (userClicked) {
            DatabaseHandler db = new DatabaseHandler(this);
            db.updateEndOfInngsOnLastScoreBall(refMatch.getMatchID(), flagEOI, description);
            db.close();
            lastBall.setFlagEOI(flagEOI);
        }
        if (!refMatch.isFinalInnings()) {
            refMatch.setCurrentSession(refMatch.getCurrentSession() + 1);
            this._inningNo = ((refMatch.getCurrentSession() - 1) / 2) + 1;
            findViewById(C0252R.id.labelStriker).setBackgroundColor(0);
            this._popupStriker = false;
            findViewById(C0252R.id.labelNonStriker).setBackgroundColor(0);
            this._popupNonStriker = false;
            findViewById(C0252R.id.labelBowler).setBackgroundColor(0);
            this._popupBowler = false;
            reInitializeScoreboard();
        } else if ((this._prevBowlScore - this._prevBatScore) + this._target <= this._totalScore || this._isMatchEnded) {
            showWinner();
        } else {
            showWinner(true);
        }
    }

    private void reInitializeScoreboard() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        int lastTotalBallNo = this._totalBallNo;
        int lastTotalScore = this._totalScore;
        int lastInningNo = this._inningNo;
        int lastDayNo = this._dayNo;
        int lastRunsRemaining = (this._prevBowlScore - this._prevBatScore) - this._totalScore;
        int lastCurrentSession = refMatch.getCurrentSession();
        initializeDefaults();
        this._totalBallNo = lastTotalBallNo;
        this._inningNo = lastInningNo;
        this._dayNo = lastDayNo;
        refMatch.setCurrentSession(lastCurrentSession);
        if (!(refMatch.isFinalInnings() && refMatch.getNoOfInngs() == 1) && (!refMatch.isFinalInnings() || refMatch.getNoOfInngs() <= 1 || lastRunsRemaining > 0)) {
            this._target = lastTotalScore;
        } else {
            this._target = lastTotalScore + 1;
        }
        refMatch.setTarget(this._target);
        populateTeamAutoComplete();
        initializeSpinners();
        populateTargetBox();
        populateMatchTitle();
    }

    public void showWinner(boolean matchEnded) {
        this._isMatchEnded = matchEnded;
        showWinner();
    }

    public String getWinnerText() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        this._teamWon = false;
        String wonText = "";
        int target = (this._prevBowlScore - this._prevBatScore) + this._target;
        if (refMatch.getNoOfInngs() > 1 && refMatch.getFollowOn() && refMatch.isFinalInnings() && target < 0 && this._totalScore == 0) {
            wonText = "Won by ";
            if (refMatch.getFirstBattedTeamNo().equals("1")) {
                wonText = wonText + refMatch.getTeam1Name();
            } else {
                wonText = wonText + refMatch.getTeam2Name();
            }
            this._teamWon = true;
            return wonText;
        } else if (this._totalScore == target - 1) {
            if (this._overNo < refMatch.getOvers()) {
                return "Scores level";
            }
            return "Match Tie";
        } else if (this._overNo < refMatch.getOvers() && !this._isMatchEnded && this._totalScore < target) {
            return wonText;
        } else {
            wonText = "Won by ";
            if (refMatch.getNoOfInngs() == 1) {
                if (target > this._totalScore) {
                    if (refMatch.getFirstBattedTeamNo().equals("1")) {
                        wonText = wonText + refMatch.getTeam1Name();
                    } else {
                        wonText = wonText + refMatch.getTeam2Name();
                    }
                } else if (refMatch.getFirstBattedTeamNo().equals("1")) {
                    wonText = wonText + refMatch.getTeam2Name();
                } else {
                    wonText = wonText + refMatch.getTeam1Name();
                }
            } else if (this._totalScore >= target) {
                if (refMatch.getFollowOn()) {
                    if (refMatch.getCurrentSession() == 2 || refMatch.getCurrentSession() == 3) {
                        if (refMatch.getFirstBattedTeamNo().equals("1")) {
                            wonText = wonText + refMatch.getTeam2Name();
                        } else {
                            wonText = wonText + refMatch.getTeam1Name();
                        }
                    } else if (refMatch.getFirstBattedTeamNo().equals("1")) {
                        wonText = wonText + refMatch.getTeam1Name();
                    } else {
                        wonText = wonText + refMatch.getTeam2Name();
                    }
                } else if (refMatch.getCurrentSession() == 2 || refMatch.getCurrentSession() == 4) {
                    if (refMatch.getFirstBattedTeamNo().equals("1")) {
                        wonText = wonText + refMatch.getTeam1Name();
                    } else {
                        wonText = wonText + refMatch.getTeam2Name();
                    }
                } else if (refMatch.getFirstBattedTeamNo().equals("1")) {
                    wonText = wonText + refMatch.getTeam2Name();
                } else {
                    wonText = wonText + refMatch.getTeam1Name();
                }
            } else if (refMatch.getCurrentSession() == 2 || refMatch.getCurrentSession() == 4) {
                if (refMatch.getFirstBattedTeamNo().equals("1")) {
                    wonText = wonText + refMatch.getTeam1Name();
                } else {
                    wonText = wonText + refMatch.getTeam2Name();
                }
            } else if (refMatch.getFirstBattedTeamNo().equals("1")) {
                wonText = wonText + refMatch.getTeam2Name();
            } else {
                wonText = wonText + refMatch.getTeam1Name();
            }
            this._teamWon = true;
            return wonText;
        }
    }

    public void showWinner() {
        if (!((CricScorerApp) getApplication()).currentMatch.isFirstInnings()) {
            String wonText = getWinnerText();
            if (!wonText.trim().equalsIgnoreCase("")) {
                Toast toast = Toast.makeText(this, wonText, 1);
                toast.setGravity(17, 0, 0);
                toast.show();
                populateTargetBox();
                updateMatchResult(wonText);
            }
        }
    }

    private void updateMatchResult(String result) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        refMatch.setMatchResult(result);
        this._teamWon = result.trim().equalsIgnoreCase("");
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateMatchResult(refMatch.getMatchID(), result);
        db.close();
    }

    private String getBowlerForOver(String team, String overNo) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        String result = "" + db.getBowlerForOver(refMatch.getMatchID(), team, overNo);
        db.close();
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addScoreBall(int r47) {
        /*
        r46 = this;
        r4 = 0;
        r5 = 0;
        r6 = "";
        r36 = r46.getApplication();
        r36 = (com.ganapathy.cricscorer.CricScorerApp) r36;
        r0 = r36;
        r0 = r0.currentMatch;
        r23 = r0;
        r25 = r23.getAdditionalSettings();
        r0 = r46;
        r0 = r0._overNo;
        r36 = r0;
        r37 = r23.getOvers();
        r0 = r36;
        r1 = r37;
        if (r0 >= r1) goto L_0x004c;
    L_0x0024:
        r36 = r23.isFinalInnings();
        if (r36 == 0) goto L_0x0061;
    L_0x002a:
        r0 = r46;
        r0 = r0._totalScore;
        r36 = r0;
        r0 = r46;
        r0 = r0._prevBowlScore;
        r37 = r0;
        r0 = r46;
        r0 = r0._prevBatScore;
        r38 = r0;
        r37 = r37 - r38;
        r0 = r46;
        r0 = r0._target;
        r38 = r0;
        r37 = r37 + r38;
        r0 = r36;
        r1 = r37;
        if (r0 < r1) goto L_0x0061;
    L_0x004c:
        r36 = "End of Innings";
        r37 = 0;
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r36 = android.widget.Toast.makeText(r0, r1, r2);
        r36.show();
        r46.showWinner();
    L_0x0060:
        return;
    L_0x0061:
        r0 = r46;
        r0 = r0._dayNo;
        r36 = r0;
        r37 = r23.getNoOfDays();
        r0 = r36;
        r1 = r37;
        if (r0 <= r1) goto L_0x0083;
    L_0x0071:
        r36 = "End of Match Days";
        r37 = 0;
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r36 = android.widget.Toast.makeText(r0, r1, r2);
        r36.show();
        goto L_0x0060;
    L_0x0083:
        r36 = 2131493082; // 0x7f0c00da float:1.8609634E38 double:1.053097506E-314;
        r0 = r46;
        r1 = r36;
        r27 = r0.findViewById(r1);
        r27 = (android.widget.TextView) r27;
        r36 = 2131493087; // 0x7f0c00df float:1.8609644E38 double:1.0530975086E-314;
        r0 = r46;
        r1 = r36;
        r19 = r0.findViewById(r1);
        r19 = (android.widget.TextView) r19;
        r36 = 2131493184; // 0x7f0c0140 float:1.860984E38 double:1.0530975566E-314;
        r0 = r46;
        r1 = r36;
        r16 = r0.findViewById(r1);
        r16 = (android.widget.TextView) r16;
        r36 = 2131493083; // 0x7f0c00db float:1.8609636E38 double:1.0530975067E-314;
        r0 = r46;
        r1 = r36;
        r26 = r0.findViewById(r1);
        r26 = (android.widget.TextView) r26;
        r36 = 2131493089; // 0x7f0c00e1 float:1.8609648E38 double:1.0530975096E-314;
        r0 = r46;
        r1 = r36;
        r18 = r0.findViewById(r1);
        r18 = (android.widget.TextView) r18;
        r36 = 2131493075; // 0x7f0c00d3 float:1.860962E38 double:1.0530975027E-314;
        r0 = r46;
        r1 = r36;
        r29 = r0.findViewById(r1);
        r29 = (android.widget.TextView) r29;
        r36 = 2131493110; // 0x7f0c00f6 float:1.860969E38 double:1.05309752E-314;
        r0 = r46;
        r1 = r36;
        r30 = r0.findViewById(r1);
        r30 = (android.widget.TextView) r30;
        r36 = 2131493231; // 0x7f0c016f float:1.8609936E38 double:1.05309758E-314;
        r0 = r46;
        r1 = r36;
        r8 = r0.findViewById(r1);
        r8 = (android.widget.TextView) r8;
        r36 = 2131493235; // 0x7f0c0173 float:1.8609944E38 double:1.053097582E-314;
        r0 = r46;
        r1 = r36;
        r24 = r0.findViewById(r1);
        r24 = (android.widget.TextView) r24;
        r36 = 2131493211; // 0x7f0c015b float:1.8609896E38 double:1.05309757E-314;
        r0 = r46;
        r1 = r36;
        r14 = r0.findViewById(r1);
        r14 = (android.widget.Button) r14;
        r36 = 2131493213; // 0x7f0c015d float:1.86099E38 double:1.053097571E-314;
        r0 = r46;
        r1 = r36;
        r13 = r0.findViewById(r1);
        r13 = (android.widget.Button) r13;
        r36 = 2131493105; // 0x7f0c00f1 float:1.860968E38 double:1.0530975175E-314;
        r0 = r46;
        r1 = r36;
        r12 = r0.findViewById(r1);
        r12 = (android.widget.Button) r12;
        r7 = new com.ganapathy.cricscorer.dtoScoreBall;
        r7.<init>();
        r0 = r46;
        r0 = r0._totalBallNo;
        r36 = r0;
        r36 = r36 + 1;
        r0 = r36;
        r1 = r46;
        r1._totalBallNo = r0;
        r0 = r46;
        r0 = r0._ballsThisOver;
        r36 = r0;
        r36 = r36 + 1;
        r0 = r36;
        r1 = r46;
        r1._ballsThisOver = r0;
        r0 = r46;
        r0 = r0._totalBallNo;
        r36 = r0;
        r0 = r36;
        r7.setTotalBallNo(r0);
        r0 = r46;
        r0 = r0._ballsThisOver;
        r36 = r0;
        r0 = r36;
        r7.setBallsThisOver(r0);
        r36 = r23.getCurrentBattingTeamNo();
        r0 = r36;
        r7.setTeamNo(r0);
        r0 = r46;
        r0 = r0._overNo;
        r36 = r0;
        r0 = r36;
        r7.setOverNo(r0);
        r0 = r46;
        r0 = r0._inningNo;
        r36 = r0;
        r0 = r36;
        r7.setInningNo(r0);
        r0 = r46;
        r0 = r0._dayNo;
        r36 = r0;
        r0 = r36;
        r7.setDayNo(r0);
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1._penalty = r0;
        r0 = r46;
        r0 = r0._penalty;
        r36 = r0;
        r0 = r36;
        r7.setIsPenalty(r0);
        r36 = r14.getText();
        r36 = r36.toString();
        r0 = r36;
        r7.setStrikerName(r0);
        r36 = r13.getText();
        r36 = r36.toString();
        r0 = r36;
        r7.setNonStrikerName(r0);
        r36 = r12.getText();
        r36 = r36.toString();
        r0 = r36;
        r7.setBowlerName(r0);
        r17 = r23.getLastScoreball();
        if (r17 == 0) goto L_0x0253;
    L_0x01c0:
        r36 = r17.getStrikerName();
        r37 = r7.getStrikerName();
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x01dc;
    L_0x01ce:
        r36 = r17.getStrikerName();
        r37 = r7.getNonStrikerName();
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 == 0) goto L_0x01f8;
    L_0x01dc:
        r36 = r17.getNonStrikerName();
        r37 = r7.getStrikerName();
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x0253;
    L_0x01ea:
        r36 = r17.getNonStrikerName();
        r37 = r7.getNonStrikerName();
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x0253;
    L_0x01f8:
        r36 = r17.getStrikerName();
        r37 = r7.getStrikerName();
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x0a27;
    L_0x0206:
        r36 = r17.getNonStrikerName();
        r37 = r7.getStrikerName();
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x0a27;
    L_0x0214:
        r20 = r7.getStrikerName();
    L_0x0218:
        r15 = new com.ganapathy.cricscorer.DatabaseHandler;
        r0 = r46;
        r15.<init>(r0);
        r36 = r23.getMatchID();
        r37 = r23.getCurrentBattingTeamNo();
        r38 = new java.lang.StringBuilder;
        r38.<init>();
        r39 = "";
        r38 = r38.append(r39);
        r39 = r17.getInningNo();
        r38 = r38.append(r39);
        r38 = r38.toString();
        r0 = r36;
        r1 = r20;
        r2 = r37;
        r3 = r38;
        r36 = r15.findRetiredOutBallNo(r0, r1, r2, r3);
        r0 = r36;
        r1 = r46;
        r1._retiredBatsmanBallNo = r0;
        r15.close();
    L_0x0253:
        r0 = r46;
        r0 = r0._retiredBatsmanBallNo;
        r36 = r0;
        if (r36 <= 0) goto L_0x0276;
    L_0x025b:
        r15 = new com.ganapathy.cricscorer.DatabaseHandler;
        r0 = r46;
        r15.<init>(r0);
        r36 = r23.getMatchID();
        r0 = r46;
        r0 = r0._retiredBatsmanBallNo;
        r37 = r0;
        r0 = r36;
        r1 = r37;
        r15.clearRetiredOutBatsman(r0, r1);
        r15.close();
    L_0x0276:
        r36 = 2131493092; // 0x7f0c00e4 float:1.8609654E38 double:1.053097511E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.CheckBox) r36;
        r36 = r36.isChecked();
        if (r36 == 0) goto L_0x0290;
    L_0x0289:
        r36 = 1;
        r0 = r36;
        r7.setIsStrike(r0);
    L_0x0290:
        r36 = 2131493093; // 0x7f0c00e5 float:1.8609656E38 double:1.0530975116E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.CheckBox) r36;
        r36 = r36.isChecked();
        if (r36 == 0) goto L_0x02bd;
    L_0x02a3:
        r36 = 1;
        r0 = r36;
        r7.setIsWide(r0);
        r6 = "wd";
        r4 = r25.getWideRun();
        r36 = r25.getWideReball();
        r37 = 1;
        r0 = r36;
        r1 = r37;
        if (r0 != r1) goto L_0x02bd;
    L_0x02bc:
        r5 = 1;
    L_0x02bd:
        r36 = 2131493094; // 0x7f0c00e6 float:1.8609658E38 double:1.053097512E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.CheckBox) r36;
        r36 = r36.isChecked();
        if (r36 == 0) goto L_0x02ea;
    L_0x02d0:
        r36 = 1;
        r0 = r36;
        r7.setIsNoball(r0);
        r6 = "nb";
        r4 = r25.getNoballRun();
        r36 = r25.getNoballReball();
        r37 = 1;
        r0 = r36;
        r1 = r37;
        if (r0 != r1) goto L_0x02ea;
    L_0x02e9:
        r5 = 1;
    L_0x02ea:
        r36 = 2131493095; // 0x7f0c00e7 float:1.860966E38 double:1.0530975126E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.CheckBox) r36;
        r36 = r36.isChecked();
        if (r36 == 0) goto L_0x030c;
    L_0x02fd:
        r36 = 1;
        r0 = r36;
        r7.setIsLegByes(r0);
        r36 = r7.getIsNoball();
        if (r36 != 0) goto L_0x030c;
    L_0x030a:
        r6 = "lb";
    L_0x030c:
        r36 = 2131493096; // 0x7f0c00e8 float:1.8609662E38 double:1.053097513E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.CheckBox) r36;
        r36 = r36.isChecked();
        if (r36 == 0) goto L_0x0334;
    L_0x031f:
        r36 = 1;
        r0 = r36;
        r7.setIsByes(r0);
        r36 = r7.getIsNoball();
        if (r36 != 0) goto L_0x0334;
    L_0x032c:
        r36 = r7.getIsWide();
        if (r36 != 0) goto L_0x0334;
    L_0x0332:
        r6 = "b";
    L_0x0334:
        r36 = 2131493216; // 0x7f0c0160 float:1.8609906E38 double:1.0530975724E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.CheckBox) r36;
        r36 = r36.isChecked();
        if (r36 == 0) goto L_0x041c;
    L_0x0347:
        r0 = r46;
        r0 = r0._wicketCount;
        r36 = r0;
        r36 = r36 + 1;
        r0 = r36;
        r1 = r46;
        r1._wicketCount = r0;
        r36 = 1;
        r0 = r36;
        r7.setIsWicket(r0);
        r36 = r7.getIsNoball();
        if (r36 != 0) goto L_0x0368;
    L_0x0362:
        r36 = r7.getIsWide();
        if (r36 == 0) goto L_0x0a2d;
    L_0x0368:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r0 = r36;
        r36 = r0.append(r6);
        r37 = "+W";
        r36 = r36.append(r37);
        r6 = r36.toString();
    L_0x037d:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r37 = r36.append(r37);
        r36 = 2131493099; // 0x7f0c00eb float:1.8609669E38 double:1.0530975146E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.Spinner) r36;
        r36 = r36.getSelectedItem();
        r36 = r36.toString();
        r0 = r37;
        r1 = r36;
        r36 = r0.append(r1);
        r33 = r36.toString();
        r36 = "bowled";
        r0 = r33;
        r1 = r36;
        r36 = r0.equalsIgnoreCase(r1);
        if (r36 == 0) goto L_0x03b7;
    L_0x03b5:
        r33 = "";
    L_0x03b7:
        r0 = r33;
        r7.setWicketHow(r0);
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r37 = r36.append(r37);
        r36 = 2131493100; // 0x7f0c00ec float:1.860967E38 double:1.053097515E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r36 = (android.widget.Button) r36;
        r36 = r36.getText();
        r36 = r36.toString();
        r0 = r37;
        r1 = r36;
        r36 = r0.append(r1);
        r34 = r36.toString();
        r36 = r33.trim();
        r37 = "c";
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 == 0) goto L_0x0417;
    L_0x03f4:
        r36 = r34.trim();
        r37 = r7.getBowlerName();
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x040e;
    L_0x0402:
        r36 = r34.trim();
        r37 = "";
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 == 0) goto L_0x0417;
    L_0x040e:
        r33 = "c &";
        r34 = "";
        r0 = r33;
        r7.setWicketHow(r0);
    L_0x0417:
        r0 = r34;
        r7.setWicketAssist(r0);
    L_0x041c:
        r0 = r46;
        r0 = r0._wicketCount;
        r36 = r0;
        r0 = r36;
        r7.setWicketCount(r0);
        r0 = r47;
        r7.setRunsScored(r0);
        r36 = r7.getIsStrike();
        if (r36 == 0) goto L_0x0a56;
    L_0x0432:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r38 = "0";
        r37 = r37.append(r38);
        r38 = r27.getText();
        r38 = r38.toString();
        r37 = r37.append(r38);
        r37 = r37.toString();
        r37 = java.lang.Integer.parseInt(r37);
        r37 = r37 + r47;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r27;
        r1 = r36;
        r0.setText(r1);
    L_0x046d:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "0";
        r36 = r36.append(r37);
        r37 = r27.getText();
        r37 = r37.toString();
        r36 = r36.append(r37);
        r36 = r36.toString();
        r36 = java.lang.Integer.parseInt(r36);
        r0 = r36;
        r7.setStrikerRuns(r0);
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "0";
        r36 = r36.append(r37);
        r37 = r19.getText();
        r37 = r37.toString();
        r36 = r36.append(r37);
        r36 = r36.toString();
        r36 = java.lang.Integer.parseInt(r36);
        r0 = r36;
        r7.setNonStrikerRuns(r0);
        r36 = r7.getIsStrike();
        if (r36 != 0) goto L_0x04f3;
    L_0x04bb:
        r36 = r7.getIsByes();
        if (r36 != 0) goto L_0x04c7;
    L_0x04c1:
        r36 = r7.getIsLegByes();
        if (r36 == 0) goto L_0x04d3;
    L_0x04c7:
        r36 = r7.getIsWide();
        if (r36 != 0) goto L_0x04d3;
    L_0x04cd:
        r36 = r7.getIsNoball();
        if (r36 == 0) goto L_0x04f3;
    L_0x04d3:
        r36 = r7.getIsNoball();
        if (r36 == 0) goto L_0x04e7;
    L_0x04d9:
        r36 = r7.getIsByes();
        if (r36 != 0) goto L_0x04e7;
    L_0x04df:
        r36 = r7.getIsLegByes();
        if (r36 != 0) goto L_0x04e7;
    L_0x04e5:
        if (r47 > 0) goto L_0x04f3;
    L_0x04e7:
        r36 = r7.getIsWicket();
        if (r36 == 0) goto L_0x051f;
    L_0x04ed:
        r36 = r7.getIsWide();
        if (r36 != 0) goto L_0x051f;
    L_0x04f3:
        r36 = r26.getText();
        r36 = r36.toString();
        r21 = java.lang.Integer.parseInt(r36);
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r21 = r21 + 1;
        r0 = r36;
        r1 = r21;
        r36 = r0.append(r1);
        r36 = r36.toString();
        r0 = r26;
        r1 = r36;
        r0.setText(r1);
    L_0x051f:
        r36 = r26.getText();
        r36 = r36.toString();
        r10 = java.lang.Integer.parseInt(r36);
        r36 = r18.getText();
        r36 = r36.toString();
        r9 = java.lang.Integer.parseInt(r36);
        r7.setBallsFacedStriker(r10);
        r7.setBallsFacedNonStriker(r9);
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1._isBatsmanOut = r0;
        r36 = r7.getIsWicket();
        if (r36 == 0) goto L_0x0550;
    L_0x054b:
        r0 = r46;
        r0.showBatsmenOutToast(r7);
    L_0x0550:
        r36 = r47 % 2;
        if (r36 == 0) goto L_0x0570;
    L_0x0554:
        r0 = r46;
        r0 = r0.autoSwitchBatsman;
        r36 = r0;
        if (r36 == 0) goto L_0x0570;
    L_0x055c:
        r46.switchBatsmen();
        r36 = "Batsmen switched.";
        r37 = 0;
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r31 = android.widget.Toast.makeText(r0, r1, r2);
        r31.show();
    L_0x0570:
        r0 = r46;
        r0 = r0._clearBallByBall;
        r36 = r0;
        if (r36 == 0) goto L_0x0587;
    L_0x0578:
        r36 = "";
        r0 = r36;
        r8.setText(r0);
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1._clearBallByBall = r0;
    L_0x0587:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = r8.getText();
        r37 = r37.toString();
        r36 = r36.append(r37);
        r37 = " ";
        r37 = r36.append(r37);
        if (r47 != 0) goto L_0x0bf2;
    L_0x05a6:
        r36 = r6.trim();
        r38 = "";
        r0 = r36;
        r1 = r38;
        r36 = r0.equalsIgnoreCase(r1);
        if (r36 == 0) goto L_0x0bee;
    L_0x05b6:
        r36 = "-";
    L_0x05b8:
        r0 = r37;
        r1 = r36;
        r36 = r0.append(r1);
        r0 = r36;
        r36 = r0.append(r6);
        r36 = r36.toString();
        r28 = com.ganapathy.cricscorer.Utility.trim(r36);
        r0 = r28;
        r8.setText(r0);
        r0 = r28;
        r7.setBallByBall(r0);
        r36 = r47 + r4;
        r0 = r36;
        r1 = r46;
        r1._scoreLastBall = r0;
        r0 = r46;
        r0 = r0._scoreLastBall;
        r36 = r0;
        r0 = r36;
        r7.setRunsThisBall(r0);
        r0 = r46;
        r0 = r0._totalScore;
        r36 = r0;
        r0 = r46;
        r0 = r0._scoreLastBall;
        r37 = r0;
        r36 = r36 + r37;
        r0 = r36;
        r1 = r46;
        r1._totalScore = r0;
        r0 = r46;
        r0 = r0._totalScore;
        r36 = r0;
        r0 = r36;
        r7.setTotalScore(r0);
        r0 = r46;
        r0 = r0._target;
        r36 = r0;
        r0 = r36;
        r7.setTarget(r0);
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "0";
        r36 = r36.append(r37);
        r37 = r16.getText();
        r37 = r37.toString();
        r36 = r36.append(r37);
        r36 = r36.toString();
        r36 = java.lang.Integer.parseInt(r36);
        r0 = r36;
        r7.setTotalExtras(r0);
        if (r5 != 0) goto L_0x0649;
    L_0x063b:
        r0 = r46;
        r0 = r0._ballNo;
        r36 = r0;
        r36 = r36 + 1;
        r0 = r36;
        r1 = r46;
        r1._ballNo = r0;
    L_0x0649:
        r0 = r46;
        r0 = r0._ballNo;
        r36 = r0;
        r0 = r36;
        r7.setBallNo(r0);
        r36 = r25.getWagonWheel();
        r37 = 1;
        r0 = r36;
        r1 = r37;
        if (r0 != r1) goto L_0x0676;
    L_0x0660:
        r0 = r46;
        r0 = r0.xCord;
        r36 = r0;
        r0 = r36;
        r7.setXCord(r0);
        r0 = r46;
        r0 = r0.yCord;
        r36 = r0;
        r0 = r36;
        r7.setYCord(r0);
    L_0x0676:
        r36 = r25.getBallSpot();
        r37 = 1;
        r0 = r36;
        r1 = r37;
        if (r0 != r1) goto L_0x0698;
    L_0x0682:
        r0 = r46;
        r0 = r0.bsxCord;
        r36 = r0;
        r0 = r36;
        r7.setbsXCord(r0);
        r0 = r46;
        r0 = r0.bsyCord;
        r36 = r0;
        r0 = r36;
        r7.setbsYCord(r0);
    L_0x0698:
        r36 = r23.getFollowOn();
        if (r36 == 0) goto L_0x06a5;
    L_0x069e:
        r36 = 2;
        r0 = r36;
        r7.setFlagEOI(r0);
    L_0x06a5:
        r0 = r23;
        r0.addScoreBall(r7);
        r0 = r23;
        r0.setLastScoreball(r7);
        r36 = r23.isFinalInnings();
        if (r36 == 0) goto L_0x06ca;
    L_0x06b5:
        r36 = r23.getMatchResult();
        r37 = "";
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x06ca;
    L_0x06c1:
        r36 = "";
        r0 = r46;
        r1 = r36;
        r0.updateMatchResult(r1);
    L_0x06ca:
        if (r5 != 0) goto L_0x070c;
    L_0x06cc:
        r0 = r46;
        r0 = r0._overNo;
        r36 = r0;
        r37 = r23.getOvers();
        r0 = r36;
        r1 = r37;
        if (r0 >= r1) goto L_0x070c;
    L_0x06dc:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r0 = r46;
        r0 = r0._overNo;
        r37 = r0;
        r36 = r36.append(r37);
        r37 = ".";
        r36 = r36.append(r37);
        r0 = r46;
        r0 = r0._ballNo;
        r37 = r0;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r29;
        r1 = r36;
        r0.setText(r1);
    L_0x070c:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r37 = r36.append(r37);
        r0 = r46;
        r0 = r0._prevBatScore;
        r36 = r0;
        if (r36 <= 0) goto L_0x0bf8;
    L_0x071f:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r0 = r46;
        r0 = r0._prevBatScore;
        r38 = r0;
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);
        r38 = " & ";
        r0 = r36;
        r1 = r38;
        r36 = r0.append(r1);
        r36 = r36.toString();
    L_0x0740:
        r0 = r37;
        r1 = r36;
        r36 = r0.append(r1);
        r0 = r46;
        r0 = r0._totalScore;
        r37 = r0;
        r36 = r36.append(r37);
        r37 = "/";
        r36 = r36.append(r37);
        r0 = r46;
        r0 = r0._wicketCount;
        r37 = r0;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r30;
        r1 = r36;
        r0.setText(r1);
        r36 = r23.isFinalInnings();
        if (r36 == 0) goto L_0x0776;
    L_0x0773:
        r46.showWinner();
    L_0x0776:
        if (r5 == 0) goto L_0x078a;
    L_0x0778:
        r0 = r46;
        r0 = r0._ballsThisOver;
        r36 = r0;
        r0 = r46;
        r1 = r25;
        r2 = r36;
        r36 = r0.isBallsRestrictedForOver(r1, r2);
        if (r36 == 0) goto L_0x08b6;
    L_0x078a:
        r0 = r46;
        r0 = r0._ballNo;
        r36 = r0;
        r37 = r25.getBpo();
        r0 = r36;
        r1 = r37;
        if (r0 >= r1) goto L_0x07ac;
    L_0x079a:
        r0 = r46;
        r0 = r0._ballsThisOver;
        r36 = r0;
        r0 = r46;
        r1 = r25;
        r2 = r36;
        r36 = r0.isBallsRestrictedForOver(r1, r2);
        if (r36 == 0) goto L_0x08b6;
    L_0x07ac:
        r0 = r46;
        r0 = r0._overNo;
        r36 = r0;
        r36 = r36 + 1;
        r0 = r36;
        r1 = r46;
        r1._overNo = r0;
        r0 = r46;
        r0 = r0._overNo;
        r36 = r0;
        r37 = r23.getOvers();
        r0 = r36;
        r1 = r37;
        if (r0 >= r1) goto L_0x0c66;
    L_0x07ca:
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1._ballNo = r0;
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1._ballsThisOver = r0;
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r0 = r46;
        r0 = r0._overNo;
        r37 = r0;
        r36 = r36.append(r37);
        r37 = ".0";
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r29;
        r1 = r36;
        r0.setText(r1);
        r36 = 1;
        r0 = r36;
        r1 = r46;
        r1._clearBallByBall = r0;
        r32 = "End of Over.";
        r0 = r46;
        r0 = r0.autoSwitchBatsman;
        r36 = r0;
        if (r36 == 0) goto L_0x0bfc;
    L_0x0812:
        r46.switchBatsmen();
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r0 = r36;
        r1 = r32;
        r36 = r0.append(r1);
        r37 = " Batsmen switched.";
        r36 = r36.append(r37);
        r32 = r36.toString();
    L_0x082c:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = r7.getTeamNo();
        r36 = r36.append(r37);
        r36 = r36.toString();
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r38 = "";
        r37 = r37.append(r38);
        r38 = r7.getOverNo();
        r38 = r38 + -1;
        r37 = r37.append(r38);
        r37 = r37.toString();
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r22 = r0.getBowlerForOver(r1, r2);
        r0 = r46;
        r0 = r0.autoBowlSpell;
        r36 = r0;
        if (r36 == 0) goto L_0x0c15;
    L_0x086e:
        r36 = r22.trim();
        r37 = "";
        r36 = r36.equalsIgnoreCase(r37);
        if (r36 != 0) goto L_0x0c15;
    L_0x087a:
        r0 = r22;
        r12.setText(r0);
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r0 = r36;
        r1 = r32;
        r36 = r0.append(r1);
        r37 = " Continuing bowling spell!";
        r36 = r36.append(r37);
        r36 = r36.toString();
        r37 = 0;
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r31 = android.widget.Toast.makeText(r0, r1, r2);
        r36 = 17;
        r37 = 0;
        r38 = 0;
        r0 = r31;
        r1 = r36;
        r2 = r37;
        r3 = r38;
        r0.setGravity(r1, r2, r3);
        r31.show();
    L_0x08b6:
        r0 = r46;
        r0 = r0._overNo;
        r36 = r0;
        r0 = r46;
        r0 = r0._ballNo;
        r37 = r0;
        r36 = r36 + r37;
        if (r36 <= 0) goto L_0x0cc9;
    L_0x08c6:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = "%.2f";
        r38 = 1;
        r0 = r38;
        r0 = new java.lang.Object[r0];
        r38 = r0;
        r39 = 0;
        r0 = r46;
        r0 = r0._totalScore;
        r40 = r0;
        r0 = r40;
        r0 = (double) r0;
        r40 = r0;
        r42 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r40 = r40 * r42;
        r0 = r46;
        r0 = r0._overNo;
        r42 = r0;
        r43 = r25.getBpo();
        r42 = r42 * r43;
        r0 = r46;
        r0 = r0._ballNo;
        r43 = r0;
        r42 = r42 + r43;
        r0 = r42;
        r0 = (double) r0;
        r42 = r0;
        r44 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r42 = r42 * r44;
        r44 = r25.getBpo();
        r0 = r44;
        r0 = (double) r0;
        r44 = r0;
        r42 = r42 / r44;
        r40 = r40 / r42;
        r40 = java.lang.Double.valueOf(r40);
        r38[r39] = r40;
        r37 = java.lang.String.format(r37, r38);
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r24;
        r1 = r36;
        r0.setText(r1);
    L_0x092f:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = 2131034419; // 0x7f050133 float:1.7679355E38 double:1.0528708965E-314;
        r0 = r46;
        r1 = r37;
        r37 = r0.getString(r1);
        r36 = r36.append(r37);
        r37 = "-";
        r36 = r36.append(r37);
        r0 = r46;
        r0 = r0._totalScore;
        r37 = r0;
        r36 = r36.append(r37);
        r37 = "/";
        r36 = r36.append(r37);
        r0 = r46;
        r0 = r0._wicketCount;
        r37 = r0;
        r36 = r36.append(r37);
        r37 = " in ";
        r36 = r36.append(r37);
        r37 = r29.getText();
        r37 = r37.toString();
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r46;
        r1 = r36;
        r0.setTitle(r1);
        r36 = 1;
        r0 = r36;
        r1 = r46;
        r1._saving = r0;
        r46.saveMatch();
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1._saving = r0;
        r46.populateTargetBox();
        r0 = r46;
        r0 = r0._retiredBatsmanBallNo;
        r36 = r0;
        if (r36 <= 0) goto L_0x09a6;
    L_0x099e:
        r36 = -1;
        r0 = r36;
        r1 = r46;
        r1._retiredBatsmanBallNo = r0;
    L_0x09a6:
        r0 = r46;
        r0 = r0.config;
        r36 = r0;
        r36 = r36.getInlineWwBs();
        r37 = 1;
        r0 = r36;
        r1 = r37;
        if (r0 != r1) goto L_0x0a0e;
    L_0x09b8:
        r0 = r46;
        r0 = r0._wagonWheelEnabled;
        r36 = r0;
        if (r36 == 0) goto L_0x09e2;
    L_0x09c0:
        r36 = 2131493221; // 0x7f0c0165 float:1.8609916E38 double:1.053097575E-314;
        r0 = r46;
        r1 = r36;
        r35 = r0.findViewById(r1);
        r35 = (android.widget.ImageView) r35;
        r36 = 8;
        r35.setVisibility(r36);
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1.xCord = r0;
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1.yCord = r0;
    L_0x09e2:
        r0 = r46;
        r0 = r0._ballSpotEnabled;
        r36 = r0;
        if (r36 == 0) goto L_0x0a0e;
    L_0x09ea:
        r36 = 2131493223; // 0x7f0c0167 float:1.860992E38 double:1.053097576E-314;
        r0 = r46;
        r1 = r36;
        r11 = r0.findViewById(r1);
        r11 = (android.widget.ImageView) r11;
        r36 = 8;
        r0 = r36;
        r11.setVisibility(r0);
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1.bsxCord = r0;
        r36 = 0;
        r0 = r36;
        r1 = r46;
        r1.bsyCord = r0;
    L_0x0a0e:
        r36 = 0;
        r0 = r46;
        r1 = r36;
        r0.onStrike(r1);
        r46.setLastBatsmen();
        r0 = r46;
        r0 = r0._teamWon;
        r36 = r0;
        if (r36 != 0) goto L_0x0060;
    L_0x0a22:
        r46.showPlayerChangePopups();
        goto L_0x0060;
    L_0x0a27:
        r20 = r7.getNonStrikerName();
        goto L_0x0218;
    L_0x0a2d:
        r36 = r7.getIsLegByes();
        if (r36 != 0) goto L_0x0a39;
    L_0x0a33:
        r36 = r7.getIsByes();
        if (r36 == 0) goto L_0x0a52;
    L_0x0a39:
        if (r47 <= 0) goto L_0x0a52;
    L_0x0a3b:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r0 = r36;
        r36 = r0.append(r6);
        r37 = "+W";
        r36 = r36.append(r37);
        r6 = r36.toString();
        goto L_0x037d;
    L_0x0a52:
        r6 = "W";
        goto L_0x037d;
    L_0x0a56:
        r36 = r7.getIsNoball();
        if (r36 != 0) goto L_0x0a62;
    L_0x0a5c:
        r36 = r7.getIsWide();
        if (r36 == 0) goto L_0x0b6a;
    L_0x0a62:
        if (r47 <= 0) goto L_0x0b2d;
    L_0x0a64:
        r36 = r7.getIsByes();
        if (r36 != 0) goto L_0x0a76;
    L_0x0a6a:
        r36 = r7.getIsLegByes();
        if (r36 != 0) goto L_0x0a76;
    L_0x0a70:
        r36 = r7.getIsWide();
        if (r36 == 0) goto L_0x0ab5;
    L_0x0a76:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = r47 + r4;
        r38 = new java.lang.StringBuilder;
        r38.<init>();
        r39 = "0";
        r38 = r38.append(r39);
        r39 = r16.getText();
        r39 = r39.toString();
        r38 = r38.append(r39);
        r38 = r38.toString();
        r38 = java.lang.Integer.parseInt(r38);
        r37 = r37 + r38;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r16;
        r1 = r36;
        r0.setText(r1);
        goto L_0x046d;
    L_0x0ab5:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r38 = "0";
        r37 = r37.append(r38);
        r38 = r27.getText();
        r38 = r38.toString();
        r37 = r37.append(r38);
        r37 = r37.toString();
        r37 = java.lang.Integer.parseInt(r37);
        r37 = r37 + r47;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r27;
        r1 = r36;
        r0.setText(r1);
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r38 = "0";
        r37 = r37.append(r38);
        r38 = r16.getText();
        r38 = r38.toString();
        r37 = r37.append(r38);
        r37 = r37.toString();
        r37 = java.lang.Integer.parseInt(r37);
        r37 = r37 + r4;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r16;
        r1 = r36;
        r0.setText(r1);
        goto L_0x046d;
    L_0x0b2d:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r38 = "0";
        r37 = r37.append(r38);
        r38 = r16.getText();
        r38 = r38.toString();
        r37 = r37.append(r38);
        r37 = r37.toString();
        r37 = java.lang.Integer.parseInt(r37);
        r37 = r37 + r4;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r16;
        r1 = r36;
        r0.setText(r1);
        goto L_0x046d;
    L_0x0b6a:
        r36 = r7.getIsWicket();
        if (r36 == 0) goto L_0x0baf;
    L_0x0b70:
        if (r47 <= 0) goto L_0x0baf;
    L_0x0b72:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = new java.lang.StringBuilder;
        r37.<init>();
        r38 = "0";
        r37 = r37.append(r38);
        r38 = r27.getText();
        r38 = r38.toString();
        r37 = r37.append(r38);
        r37 = r37.toString();
        r37 = java.lang.Integer.parseInt(r37);
        r37 = r37 + r47;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r27;
        r1 = r36;
        r0.setText(r1);
        goto L_0x046d;
    L_0x0baf:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r37 = r47 + r4;
        r38 = new java.lang.StringBuilder;
        r38.<init>();
        r39 = "0";
        r38 = r38.append(r39);
        r39 = r16.getText();
        r39 = r39.toString();
        r38 = r38.append(r39);
        r38 = r38.toString();
        r38 = java.lang.Integer.parseInt(r38);
        r37 = r37 + r38;
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r16;
        r1 = r36;
        r0.setText(r1);
        goto L_0x046d;
    L_0x0bee:
        r36 = "";
        goto L_0x05b8;
    L_0x0bf2:
        r36 = java.lang.Integer.valueOf(r47);
        goto L_0x05b8;
    L_0x0bf8:
        r36 = "";
        goto L_0x0740;
    L_0x0bfc:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r0 = r36;
        r1 = r32;
        r36 = r0.append(r1);
        r37 = " Batsmen NOT switched.";
        r36 = r36.append(r37);
        r32 = r36.toString();
        goto L_0x082c;
    L_0x0c15:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r0 = r36;
        r1 = r32;
        r36 = r0.append(r1);
        r37 = " Change bowler!";
        r36 = r36.append(r37);
        r36 = r36.toString();
        r37 = 0;
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r31 = android.widget.Toast.makeText(r0, r1, r2);
        r36 = 17;
        r37 = 0;
        r38 = 0;
        r0 = r31;
        r1 = r36;
        r2 = r37;
        r3 = r38;
        r0.setGravity(r1, r2, r3);
        r31.show();
        r36 = 2131493104; // 0x7f0c00f0 float:1.8609679E38 double:1.053097517E-314;
        r0 = r46;
        r1 = r36;
        r36 = r0.findViewById(r1);
        r37 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
        r36.setBackgroundColor(r37);
        r36 = 1;
        r0 = r36;
        r1 = r46;
        r1._popupBowler = r0;
        goto L_0x08b6;
    L_0x0c66:
        r36 = new java.lang.StringBuilder;
        r36.<init>();
        r37 = "";
        r36 = r36.append(r37);
        r0 = r46;
        r0 = r0._overNo;
        r37 = r0;
        r36 = r36.append(r37);
        r37 = ".0";
        r36 = r36.append(r37);
        r36 = r36.toString();
        r0 = r29;
        r1 = r36;
        r0.setText(r1);
        r36 = r23.getFollowOn();
        if (r36 == 0) goto L_0x0cc6;
    L_0x0c92:
        r36 = 3;
    L_0x0c94:
        r0 = r36;
        r7.setFlagEOI(r0);
        r36 = 0;
        r37 = 2131034299; // 0x7f0500bb float:1.7679112E38 double:1.052870837E-314;
        r0 = r46;
        r1 = r37;
        r37 = r0.getString(r1);
        r38 = 0;
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r3 = r38;
        r0.doEndOfInnings(r1, r2, r3);
        r36 = "End of Innings";
        r37 = 0;
        r0 = r46;
        r1 = r36;
        r2 = r37;
        r31 = android.widget.Toast.makeText(r0, r1, r2);
        r31.show();
        goto L_0x08b6;
    L_0x0cc6:
        r36 = 1;
        goto L_0x0c94;
    L_0x0cc9:
        r36 = "0.0";
        r0 = r24;
        r1 = r36;
        r0.setText(r1);
        goto L_0x092f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ganapathy.cricscorer.Scorecard.addScoreBall(int):void");
    }

    private void showBatsmenOutToast(dtoScoreBall ball) {
        String toastText;
        int whoIsOut;
        TextView stkScore = (TextView) findViewById(C0252R.id.strikerScore);
        TextView nstkScore = (TextView) findViewById(C0252R.id.nonStrikerScore);
        TextView stkBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        TextView nstkBalls = (TextView) findViewById(C0252R.id.ballsNonStriker);
        CheckBox batsCrossed = (CheckBox) findViewById(C0252R.id.checkBatsCrossed);
        if (ball.getWicketHow().equalsIgnoreCase("Retired (striker)")) {
            toastText = ball.getStrikerName() + " retired for " + ball.getStrikerRuns() + " in " + ball.getBallsFacedStriker() + " Balls.";
            whoIsOut = 1;
        } else if (ball.getWicketHow().equalsIgnoreCase("Retired (non-striker)")) {
            toastText = ball.getNonStrikerName() + " retired for " + ball.getNonStrikerRuns() + " in " + ball.getBallsFacedNonStriker() + " Balls.";
            whoIsOut = 2;
        } else if (ball.getWicketHow().equalsIgnoreCase("Run out (non-striker)")) {
            toastText = ball.getNonStrikerName() + " out for " + nstkScore.getText().toString() + " in " + nstkBalls.getText().toString() + " Balls.";
            whoIsOut = 2;
        } else {
            toastText = ball.getStrikerName() + " out for " + stkScore.getText().toString() + " in " + stkBalls.getText().toString() + " Balls.";
            whoIsOut = 1;
        }
        if (batsCrossed.isChecked()) {
            switchBatsmen();
            whoIsOut = whoIsOut == 1 ? 2 : 1;
        }
        if (whoIsOut == 1) {
            stkScore.setText(getString(C0252R.string.run0));
            stkBalls.setText(getString(C0252R.string.run0));
            findViewById(C0252R.id.labelStriker).setBackgroundColor(SupportMenu.CATEGORY_MASK);
            this._popupStriker = true;
            this._isBatsmanOut = 1;
        } else if (whoIsOut == 2) {
            nstkScore.setText(getString(C0252R.string.run0));
            nstkBalls.setText(getString(C0252R.string.run0));
            findViewById(C0252R.id.labelNonStriker).setBackgroundColor(SupportMenu.CATEGORY_MASK);
            this._popupNonStriker = true;
            this._isBatsmanOut = 2;
        }
        Toast toast = Toast.makeText(this, toastText, 0);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    private void switchBatsmen() {
        Button buttonStriker = (Button) findViewById(C0252R.id.buttonStriker);
        Button buttonNonStriker = (Button) findViewById(C0252R.id.buttonNonStriker);
        TextView labelStriker = (TextView) findViewById(C0252R.id.labelStriker);
        TextView labelNonStriker = (TextView) findViewById(C0252R.id.labelNonStriker);
        String _currentStriker = buttonStriker.getText().toString();
        buttonStriker.setText(buttonNonStriker.getText().toString());
        buttonNonStriker.setText(_currentStriker);
        if (this._isBatsmanOut == 1) {
            labelStriker.setBackgroundColor(0);
            this._popupStriker = false;
            labelNonStriker.setBackgroundColor(SupportMenu.CATEGORY_MASK);
            this._popupNonStriker = true;
            this._isBatsmanOut = 2;
        } else if (this._isBatsmanOut == 2) {
            labelStriker.setBackgroundColor(SupportMenu.CATEGORY_MASK);
            this._popupStriker = true;
            labelNonStriker.setBackgroundColor(0);
            this._popupNonStriker = false;
            this._isBatsmanOut = 1;
        } else {
            labelStriker.setBackgroundColor(0);
            this._popupStriker = false;
            labelNonStriker.setBackgroundColor(0);
            this._popupNonStriker = false;
        }
        TextView stkScore = (TextView) findViewById(C0252R.id.strikerScore);
        TextView nstkScore = (TextView) findViewById(C0252R.id.nonStrikerScore);
        String score1 = stkScore.getText().toString();
        stkScore.setText(nstkScore.getText().toString());
        nstkScore.setText(score1);
        TextView stkBalls = (TextView) findViewById(C0252R.id.ballsStriker);
        TextView nstkBalls = (TextView) findViewById(C0252R.id.ballsNonStriker);
        String balls1 = stkBalls.getText().toString();
        stkBalls.setText(nstkBalls.getText().toString());
        nstkBalls.setText(balls1);
    }

    public void ShowWagonWheelDialog() {
        Intent intent = new Intent(this, BallPosition.class);
        intent.addFlags(65536);
        intent.putExtra("Run", this.lastScoreBall);
        startActivityForResult(intent, 1);
        overridePendingTransition(0, 0);
    }

    public void ShowBallSpotDialog() {
        Intent intent = new Intent(this, BallSpot.class);
        intent.addFlags(65536);
        startActivityForResult(intent, 2);
        overridePendingTransition(0, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            this.wagonWheelDone = true;
            if (resultCode == -1) {
                this.xCord = Integer.parseInt(data.getStringExtra("xCord"));
                this.yCord = Integer.parseInt(data.getStringExtra("yCord"));
            }
        }
        if (requestCode == 2) {
            this.ballSpotDone = true;
            if (resultCode == -1) {
                this.bsxCord = Integer.parseInt(data.getStringExtra("bsxCord"));
                this.bsyCord = Integer.parseInt(data.getStringExtra("bsyCord"));
            }
        }
        if (requestCode == 1 || requestCode == 2) {
            if (!this._wagonWheelEnabled) {
                this.wagonWheelDone = true;
            }
            if (!this._ballSpotEnabled) {
                this.ballSpotDone = true;
            }
            if (this.ballSpotDone && this.wagonWheelDone) {
                addScoreBall(this.lastScoreBall);
            }
        }
        if (requestCode == 3) {
            dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
            dtoScoreBall ball = Utility.getLastScoreBall(getApplicationContext(), refMatch.getMatchID());
            if (ball != null) {
                refMatch.setLastScoreball(ball);
                if (ball.getIsWicket()) {
                    this._lastBatsmanOut = 0;
                }
                reopenMatch();
                populateTargetBox();
                setLastBatsmen();
                if (!this._teamWon) {
                    showPlayerChangePopups();
                }
            }
        }
    }

    public void onDotBall(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled && this._wagonWheelForDotBallEnabled && !((CheckBox) findViewById(C0252R.id.wicketCheckBox)).isChecked()) {
            this.lastScoreBall = 0;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else if (this._wagonWheelEnabled && this._wagonWheelForWicketEnabled && ((CheckBox) findViewById(C0252R.id.wicketCheckBox)).isChecked()) {
            this.lastScoreBall = 0;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 0;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(0);
        }
    }

    public void onOne(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled) {
            this.lastScoreBall = 1;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 1;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(1);
        }
    }

    public void onTwo(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled) {
            this.lastScoreBall = 2;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 2;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(2);
        }
    }

    public void onThree(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled) {
            this.lastScoreBall = 3;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 3;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(3);
        }
    }

    public void onFour(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled) {
            this.lastScoreBall = 4;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 4;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(4);
        }
    }

    public void onFive(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled) {
            this.lastScoreBall = 5;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 5;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(5);
        }
    }

    public void onSix(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled) {
            this.lastScoreBall = 6;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 6;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(6);
        }
    }

    public void onSeven(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        this.wagonWheelDone = false;
        this.ballSpotDone = false;
        if (this._wagonWheelEnabled) {
            this.lastScoreBall = 7;
            if (this.config.getInlineWwBs() == 0) {
                ShowWagonWheelDialog();
            } else {
                this.wagonWheelDone = true;
            }
        } else {
            this.wagonWheelDone = true;
        }
        if (this._ballSpotEnabled) {
            this.lastScoreBall = 7;
            if (this.config.getInlineWwBs() == 0) {
                ShowBallSpotDialog();
            } else {
                this.ballSpotDone = true;
            }
        } else {
            this.ballSpotDone = true;
        }
        if (this.wagonWheelDone && this.ballSpotDone) {
            addScoreBall(7);
        }
    }

    public void onSwitchBats(View view) {
        if (this.preventDblClick) {
            if (SystemClock.elapsedRealtime() - this.lastClickTime >= 1000) {
                this.lastClickTime = SystemClock.elapsedRealtime();
            } else {
                return;
            }
        }
        if (this._isBatsmanOut == 1) {
            this._isBatsmanOut = 2;
        } else if (this._isBatsmanOut == 2) {
            this._isBatsmanOut = 1;
        } else {
            if (this._isBatsmanOut != 0) {
                this._lastBatsmanOut = this._isBatsmanOut;
            }
            this._isBatsmanOut = 0;
        }
        switchBatsmen();
    }

    public void onBack(View view) {
        this._saving = true;
        saveMatch();
        this._saving = false;
        finish();
    }

    public void onOptions(View view) {
        try {
            openOptionsMenu();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    public void onSave(View view) {
        this._saving = true;
        saveMatch();
        this._saving = false;
        startActivity(new Intent(this, MatchStats.class));
    }

    public void onUndo(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoAdditionalSettings settings = refMatch.getAdditionalSettings();
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        if (lastBall == null) {
            Toast.makeText(getApplicationContext(), "Nothing to undo!", 0).show();
            return;
        }
        this._choicesToDisplay = 0;
        if (lastBall.isFlagEOD()) {
            this._choicesToDisplay = 1;
        }
        if (lastBall.isFlagEOI() && !(lastBall.getBallNo() == settings.getBpo() && lastBall.getOverNo() == refMatch.getOvers() - 1)) {
            this._choicesToDisplay = 2;
        }
        if (lastBall.isFlagEOD() && lastBall.isFlagEOI()) {
            this._choicesToDisplay = 3;
        }
        switch (this._choicesToDisplay + lastBall.getIsPenalty()) {
            case 0:
                onUndo((int) C0252R.string.undoText);
                return;
            default:
                openContextMenu(view);
                return;
        }
    }

    public void onUndo(final int undoText) {
        new Builder(this).setIcon(17301543).setTitle(undoText).setMessage(C0252R.string.areYouSureText).setPositiveButton(C0252R.string.yesText, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (undoText == C0252R.string.undoEndOfDayText) {
                    Scorecard.this.clearEndsFlag(1);
                } else if (undoText == C0252R.string.undoEndOfInngsText) {
                    Scorecard.this.clearEndsFlag(2);
                } else if (undoText == C0252R.string.undoPenaltyText) {
                    Scorecard.this.updatePenalty(Scorecard.this._penalty * -1, true);
                } else {
                    Scorecard.this.undoLastBall();
                }
            }
        }).setNegativeButton(C0252R.string.noText, null).show();
    }

    public void undoLastBall() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        String matchID = refMatch.getMatchID();
        if (this._totalBallNo >= 1) {
            this._saving = true;
            saveMatch();
            this._saving = false;
            DatabaseHandler db = new DatabaseHandler(this);
            db.deleteLastMatchScoreBall(matchID, this._totalBallNo);
            refMatch.setLastScoreball(null);
            int i = this._totalBallNo - 1;
            this._totalBallNo = i;
            if (i >= 1) {
                ((CricScorerApp) getApplication()).currentMatch = db.getMatchMaster(matchID);
            } else {
                refMatch.setLastScoreball(null);
            }
            db.close();
        }
        if (!refMatch.getMatchResult().equalsIgnoreCase("")) {
            updateMatchResult("");
        }
        initializeControls();
    }

    private void clearEndsFlag(int what) {
        int i = 2;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoScoreBall lastBall = refMatch.getLastScoreball();
        DatabaseHandler db = new DatabaseHandler(this);
        if (what == 1) {
            db.clearEndOfDayOnLastScoreBall(refMatch.getMatchID());
            lastBall.setFlagEOD(0);
            this._dayNo = lastBall.getDayNo();
            populateMatchTitle();
            populateTargetBox();
            showHideDayButton();
            if (refMatch.getMatchResult().equalsIgnoreCase(getString(C0252R.string.matchDrawText))) {
                updateMatchResult("");
            }
        } else if (what == 2) {
            db.clearEndOfInngsOnLastScoreBall(refMatch.getMatchID(), lastBall.isFollowOnInnings() ? 2 : 0);
            if (!lastBall.isFollowOnInnings()) {
                i = 0;
            }
            lastBall.setFlagEOI(i);
            if (!refMatch.getMatchResult().equalsIgnoreCase("")) {
                updateMatchResult("");
            }
            ((CricScorerApp) getApplication()).currentMatch = db.getMatchMaster(refMatch.getMatchID());
            initializeControls();
        }
        db.close();
    }

    public void onEditBall(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        dtoScoreBall ball = Utility.getLastScoreBall(this, refMatch.getMatchID());
        if (ball != null) {
            refMatch.setLastScoreball(ball);
            this._prevBatScore = -1;
            this._prevBowlScore = -1;
            Intent intent = new Intent(this, EditBall.class);
            intent.addFlags(65536);
            intent.putExtra("ballNo", ball.getTotalBallNo());
            startActivityForResult(intent, 3);
            overridePendingTransition(0, 0);
        }
    }

    protected void saveMatch() {
        if (this._saving) {
            dtoScoreBall firstBall;
            dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
            DatabaseHandler db = new DatabaseHandler(this);
            dtoMatch notSavedBalls = db.addMatchScoreBall(refMatch);
            db.close();
            refMatch.clearScorecard();
            if (!notSavedBalls.getTeam1Scorecard().isEmpty()) {
                firstBall = (dtoScoreBall) notSavedBalls.getTeam1Scorecard().get(0);
                refMatch.setTeam1Scorecard(notSavedBalls.getTeam1Scorecard());
                showAlert("Error has occured during saving the ball " + firstBall.getOverNo() + "." + firstBall.getBallNo() + ". Please undo the ball.");
            }
            if (!notSavedBalls.getTeam2Scorecard().isEmpty()) {
                firstBall = (dtoScoreBall) notSavedBalls.getTeam1Scorecard().get(0);
                refMatch.setTeam2Scorecard(notSavedBalls.getTeam2Scorecard());
                showAlert("Error has occured during saving the ball " + firstBall.getOverNo() + "." + firstBall.getBallNo() + ". Please undo the ball.");
                return;
            }
            return;
        }
        Toast.makeText(getApplicationContext(), "Already Saving the data. Please wait...", 1).show();
    }

    protected void updateRetiredOutBatsman(int who) {
        this._saving = true;
        saveMatch();
        this._saving = false;
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateRetiredOutBatsman(refMatch.getMatchID(), who);
        db.close();
    }

    protected List<String> getRunsForBatsmanRetired(int who, String playerName, String teamNo, String inngNo) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> player = db.getRunsForRetiredOutBatsman(refMatch.getMatchID(), playerName, teamNo, inngNo);
        db.close();
        return player;
    }

    private void setLastBatsmen() {
        Button buttonNonStriker = (Button) findViewById(C0252R.id.buttonNonStriker);
        this._lastStriker = ((Button) findViewById(C0252R.id.buttonStriker)).getText().toString();
        this._lastNonStriker = buttonNonStriker.getText().toString();
    }

    private void showAlert(String message) {
        Builder builder = new Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
