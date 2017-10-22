package com.ganapathy.cricscorer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class SetTeams extends BaseActivity {
    private boolean _isOpened = false;
    private dtoTeamPlayer player;
    private EditTeamPlayersListAdapter team1Adapter;
    private dtoTeamPlayerList team1Players;
    private EditTeamPlayersListAdapter team2Adapter;
    private dtoTeamPlayerList team2Players;

    class C02651 implements OnItemClickListener {
        C02651() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            dtoTeamPlayer entry = (dtoTeamPlayer) SetTeams.this.team1Players.get(position);
            ImageView checked = (ImageView) view.findViewById(C0252R.id.checkPlayer);
            if (entry.getIsChecked()) {
                checked.setBackgroundResource(C0252R.drawable.blank_24);
                entry.setIsChecked(false);
            } else {
                checked.setBackgroundResource(C0252R.drawable.tick_ok);
                entry.setIsChecked(true);
            }
            SetTeams.this.updateSelectedPlayers();
        }
    }

    class C02662 implements OnItemLongClickListener {
        C02662() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            SetTeams.this.player = (dtoTeamPlayer) SetTeams.this.team1Players.get(position);
            SetTeams.this.displayPlayerPopup(1, SetTeams.this.getString(C0252R.string.editPlayerText));
            return true;
        }
    }

    class C02673 implements OnItemClickListener {
        C02673() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            dtoTeamPlayer entry = (dtoTeamPlayer) SetTeams.this.team2Players.get(position);
            ImageView checked = (ImageView) view.findViewById(C0252R.id.checkPlayer);
            if (entry.getIsChecked()) {
                checked.setBackgroundResource(C0252R.drawable.blank_24);
                entry.setIsChecked(false);
            } else {
                checked.setBackgroundResource(C0252R.drawable.tick_ok);
                entry.setIsChecked(true);
            }
            SetTeams.this.updateSelectedPlayers();
        }
    }

    class C02684 implements OnItemLongClickListener {
        C02684() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            SetTeams.this.player = (dtoTeamPlayer) SetTeams.this.team2Players.get(position);
            SetTeams.this.displayPlayerPopup(2, SetTeams.this.getString(C0252R.string.editPlayerText));
            return true;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_set_teams);
            if (!this._isOpened) {
                populateTeams();
                ((LinearLayout) findViewById(C0252R.id.MainLayout)).requestFocus();
                this._isOpened = true;
            }
        } catch (Exception e) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0252R.menu.activity_set_teams, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.loaddefaultteam1:
                loadSavedTeam1();
                break;
            case C0252R.id.loaddefaultteam2:
                loadSavedTeam2();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBack(View view) {
        finish();
    }

    public void onOptions(View view) {
        try {
            openOptionsMenu();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
        }
    }

    protected void populateTeams() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        ((TextView) findViewById(C0252R.id.team1Name)).setText(("" + refMatch.getTeam1Name()).toUpperCase(Locale.getDefault()));
        ((TextView) findViewById(C0252R.id.team2Name)).setText(("" + refMatch.getTeam2Name()).toUpperCase(Locale.getDefault()));
        this.team1Players = refMatch.getTeam1Players();
        this.team2Players = refMatch.getTeam2Players();
        ListView team1PlayersListView = (ListView) findViewById(C0252R.id.team1ListView);
        this.team1Adapter = new EditTeamPlayersListAdapter(this, this.team1Players, false, false, true);
        team1PlayersListView.setAdapter(this.team1Adapter);
        team1PlayersListView.setOnItemClickListener(new C02651());
        team1PlayersListView.setOnItemLongClickListener(new C02662());
        ListView team2PlayersListView = (ListView) findViewById(C0252R.id.team2ListView);
        this.team2Adapter = new EditTeamPlayersListAdapter(this, this.team2Players, false, false, true);
        team2PlayersListView.setAdapter(this.team2Adapter);
        team2PlayersListView.setOnItemClickListener(new C02673());
        team2PlayersListView.setOnItemLongClickListener(new C02684());
        updateSelectedPlayers();
    }

    public void updateSelectedPlayers() {
        Iterator it;
        int noSelected = 0;
        if (this.team1Players != null && this.team1Players.size() > 0) {
            it = this.team1Players.iterator();
            while (it.hasNext()) {
                if (((dtoTeamPlayer) it.next()).getIsChecked()) {
                    noSelected++;
                }
            }
        }
        ((Button) findViewById(C0252R.id.team1SelectedPlayers)).setText("(" + noSelected + " selected)");
        noSelected = 0;
        if (this.team2Players != null && this.team2Players.size() > 0) {
            it = this.team2Players.iterator();
            while (it.hasNext()) {
                if (((dtoTeamPlayer) it.next()).getIsChecked()) {
                    noSelected++;
                }
            }
        }
        ((Button) findViewById(C0252R.id.team2SelectedPlayers)).setText("(" + noSelected + " selected)");
    }

    public void onSync(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        DatabaseHandler db = new DatabaseHandler(this);
        try {
            if (("" + refMatch.getTeam1Name()).trim() != "") {
                db.deleteManageTeamPlayers(refMatch.getTeam1Name());
                db.addManageTeamPlayers(this.team1Players);
            }
            if (("" + refMatch.getTeam2Name()).trim() != "") {
                db.deleteManageTeamPlayers(refMatch.getTeam2Name());
                db.addManageTeamPlayers(this.team2Players);
            }
            Toast.makeText(getApplicationContext(), "Sync successful!", 0).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error! " + e.getMessage(), 0).show();
        } finally {
            db.close();
        }
    }

    public void loadSavedTeam1() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> team1DefaultPlayerNames = db.getAllTeam1Players();
        db.close();
        int i = 0;
        for (String playerName : team1DefaultPlayerNames) {
            this.player = new dtoTeamPlayer();
            this.player.setTeamName(((TextView) findViewById(C0252R.id.team1Name)).getText().toString());
            this.player.setPlayerName("" + playerName);
            int i2 = i + 1;
            this.player.setPlayerOrder(i);
            this.player.setIsChecked(true);
            this.team1Players.add(this.player);
            i = i2;
        }
        ((ListView) findViewById(C0252R.id.team1ListView)).invalidateViews();
        updateSelectedPlayers();
    }

    public void loadSavedTeam2() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> team2DefaultPlayerNames = db.getAllTeam2Players();
        db.close();
        int i = 0;
        for (String playerName : team2DefaultPlayerNames) {
            this.player = new dtoTeamPlayer();
            this.player.setTeamName(((TextView) findViewById(C0252R.id.team2Name)).getText().toString());
            this.player.setPlayerName("" + playerName);
            int i2 = i + 1;
            this.player.setPlayerOrder(i);
            this.player.setIsChecked(true);
            this.team2Players.add(this.player);
            i = i2;
        }
        ((ListView) findViewById(C0252R.id.team2ListView)).invalidateViews();
        updateSelectedPlayers();
    }

    public void onTeam1AddPlayer(View view) {
        if (("" + ((TextView) findViewById(C0252R.id.team1Name)).getText().toString()) == "") {
            Toast.makeText(getApplicationContext(), "Cannot add player for unnamed team!", 0).show();
            return;
        }
        this.player = new dtoTeamPlayer();
        this.player.setTeamName(((TextView) findViewById(C0252R.id.team1Name)).getText().toString());
        this.player.setPlayerName("");
        this.player.setPlayerOrder(this.team1Players.size());
        this.player.setIsChecked(true);
        displayPlayerPopup(1, getString(C0252R.string.newPlayerText));
    }

    public void updateTeam1Players() {
        if (this.team1Players.contains(this.player.getPlayerName())) {
            this.team1Players.updatePlayer(this.player);
        } else {
            this.team1Players.add(this.player);
        }
        ((ListView) findViewById(C0252R.id.team1ListView)).invalidateViews();
        updateSelectedPlayers();
    }

    public void onTeam2AddPlayer(View view) {
        String teamName = "" + ((TextView) findViewById(C0252R.id.team2Name)).getText().toString();
        if (teamName == "") {
            Toast.makeText(getApplicationContext(), "Cannot add player for unnamed team!", 0).show();
            return;
        }
        this.player = new dtoTeamPlayer();
        this.player.setTeamName(teamName);
        this.player.setPlayerName("");
        this.player.setPlayerOrder(this.team2Players.size());
        this.player.setIsChecked(true);
        displayPlayerPopup(2, getString(C0252R.string.newPlayerText));
    }

    public void updateTeam2Players() {
        if (this.team2Players.contains(this.player.getPlayerName())) {
            this.team2Players.updatePlayer(this.player);
        } else {
            this.team2Players.add(this.player);
        }
        ((ListView) findViewById(C0252R.id.team2ListView)).invalidateViews();
        updateSelectedPlayers();
    }

    private void displayPlayerPopup(int teamNo, String title) {
        Rect displayRectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C0252R.layout.popup_add_player, (ViewGroup) findViewById(C0252R.id.choosePopupLayout));
        layout.setMinimumWidth((int) (((float) displayRectangle.width()) * 0.9f));
        View textView = new TextView(layout.getContext());
        Utility.setMediumTitle(textView, layout.getContext());
        textView.setMinimumHeight(60);
        textView.setText(title);
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
        builder.setCancelable(false);
        final EditText editPlayerNameBox = (EditText) layout.findViewById(C0252R.id.newPlayerName);
        final ToggleButton toggleHandedBatBox = (ToggleButton) layout.findViewById(C0252R.id.toggleHandedBat);
        final ToggleButton toggleHandedBowlBox = (ToggleButton) layout.findViewById(C0252R.id.toggleHandedBowl);
        final ToggleButton toggleWhatBowlerBox = (ToggleButton) layout.findViewById(C0252R.id.toggleWhatBowler);
        final CheckBox checkIsBatsmanBox = (CheckBox) layout.findViewById(C0252R.id.checkIsBatsman);
        final CheckBox checkIsBowlerBox = (CheckBox) layout.findViewById(C0252R.id.checkIsBowler);
        final CheckBox checkIsKeeperBox = (CheckBox) layout.findViewById(C0252R.id.checkIsKeeper);
        final CheckBox checkIsCaptainBox = (CheckBox) layout.findViewById(C0252R.id.checkIsCaptain);
        final int playerOrder = this.player.getPlayerOrder();
        final String teamName = this.player.getTeamName();
        final int team = teamNo;
        editPlayerNameBox.setText(this.player.getPlayerName());
        toggleHandedBatBox.setChecked(this.player.getHandedBat() == 1);
        toggleHandedBowlBox.setChecked(this.player.getHandedBowl() == 1);
        toggleWhatBowlerBox.setChecked(this.player.getWhatBowler() == 1);
        checkIsBatsmanBox.setChecked(this.player.getIsBatsman() == 1);
        checkIsBowlerBox.setChecked(this.player.getIsBowler() == 1);
        checkIsKeeperBox.setChecked(this.player.getIsKeeper() == 1);
        checkIsCaptainBox.setChecked(this.player.getIsCaptain() == 1);
        final AlertDialog alertDialog = builder.create();
        ((Button) layout.findViewById(C0252R.id.btnOK)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int i;
                int i2 = 0;
                ((InputMethodManager) SetTeams.this.getSystemService("input_method")).hideSoftInputFromWindow(editPlayerNameBox.getWindowToken(), 0);
                SetTeams.this.player.setPlayerName(editPlayerNameBox.getText().toString());
                SetTeams.this.player.setHandedBat(toggleHandedBatBox.isChecked() ? 1 : 0);
                dtoTeamPlayer access$100 = SetTeams.this.player;
                if (toggleHandedBowlBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                access$100.setHandedBowl(i);
                access$100 = SetTeams.this.player;
                if (toggleWhatBowlerBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                access$100.setWhatBowler(i);
                access$100 = SetTeams.this.player;
                if (checkIsBatsmanBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                access$100.setIsBatsman(i);
                access$100 = SetTeams.this.player;
                if (checkIsBowlerBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                access$100.setIsBowler(i);
                access$100 = SetTeams.this.player;
                if (checkIsKeeperBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                access$100.setIsKeeper(i);
                dtoTeamPlayer access$1002 = SetTeams.this.player;
                if (checkIsCaptainBox.isChecked()) {
                    i2 = 1;
                }
                access$1002.setIsCaptain(i2);
                SetTeams.this.player.setPlayerOrder(playerOrder);
                SetTeams.this.player.setTeamName(teamName);
                if (team == 1) {
                    SetTeams.this.updateTeam1Players();
                } else {
                    SetTeams.this.updateTeam2Players();
                }
                alertDialog.dismiss();
            }
        });
        final AlertDialog alertDialog2 = alertDialog;
        ((Button) layout.findViewById(C0252R.id.btnClose)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) SetTeams.this.getSystemService("input_method")).hideSoftInputFromWindow(editPlayerNameBox.getWindowToken(), 0);
                alertDialog2.dismiss();
            }
        });
        alertDialog.show();
        ((ViewGroup) layout.getParent()).setPadding(0, 0, 0, 0);
    }
}
