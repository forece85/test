package com.ganapathy.cricscorer;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.util.List;

public class SetDefaultTeams extends BaseActivity {
    private boolean _isOpened = false;

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_set_default_teams);
            if (!this._isOpened) {
                initializeControls();
                this._isOpened = true;
            }
        } catch (Exception e) {
            finish();
        }
    }

    protected void initializeControls() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> team1Players = db.getAllTeam1Players();
        List<String> team2Players = db.getAllTeam2Players();
        db.close();
        if (team1Players.size() > 0) {
            if (team1Players.size() > 0) {
                ((EditText) findViewById(C0252R.id.T1P01)).setText("" + ((String) team1Players.get(0)));
            }
            if (team1Players.size() > 1) {
                ((EditText) findViewById(C0252R.id.T1P02)).setText("" + ((String) team1Players.get(1)));
            }
            if (team1Players.size() > 2) {
                ((EditText) findViewById(C0252R.id.T1P03)).setText("" + ((String) team1Players.get(2)));
            }
            if (team1Players.size() > 3) {
                ((EditText) findViewById(C0252R.id.T1P04)).setText("" + ((String) team1Players.get(3)));
            }
            if (team1Players.size() > 4) {
                ((EditText) findViewById(C0252R.id.T1P05)).setText("" + ((String) team1Players.get(4)));
            }
            if (team1Players.size() > 5) {
                ((EditText) findViewById(C0252R.id.T1P06)).setText("" + ((String) team1Players.get(5)));
            }
            if (team1Players.size() > 6) {
                ((EditText) findViewById(C0252R.id.T1P07)).setText("" + ((String) team1Players.get(6)));
            }
            if (team1Players.size() > 7) {
                ((EditText) findViewById(C0252R.id.T1P08)).setText("" + ((String) team1Players.get(7)));
            }
            if (team1Players.size() > 8) {
                ((EditText) findViewById(C0252R.id.T1P09)).setText("" + ((String) team1Players.get(8)));
            }
            if (team1Players.size() > 9) {
                ((EditText) findViewById(C0252R.id.T1P10)).setText("" + ((String) team1Players.get(9)));
            }
            if (team1Players.size() > 10) {
                ((EditText) findViewById(C0252R.id.T1P11)).setText("" + ((String) team1Players.get(10)));
            }
            if (team1Players.size() > 11) {
                ((EditText) findViewById(C0252R.id.T1P12)).setText("" + ((String) team1Players.get(11)));
            }
            if (team1Players.size() > 12) {
                ((EditText) findViewById(C0252R.id.T1P13)).setText("" + ((String) team1Players.get(12)));
            }
            if (team1Players.size() > 13) {
                ((EditText) findViewById(C0252R.id.T1P14)).setText("" + ((String) team1Players.get(13)));
            }
            if (team1Players.size() > 14) {
                ((EditText) findViewById(C0252R.id.T1P15)).setText("" + ((String) team1Players.get(14)));
            }
        }
        if (team2Players.size() > 0) {
            if (team2Players.size() > 0) {
                ((EditText) findViewById(C0252R.id.T2P01)).setText("" + ((String) team2Players.get(0)));
            }
            if (team2Players.size() > 1) {
                ((EditText) findViewById(C0252R.id.T2P02)).setText("" + ((String) team2Players.get(1)));
            }
            if (team2Players.size() > 2) {
                ((EditText) findViewById(C0252R.id.T2P03)).setText("" + ((String) team2Players.get(2)));
            }
            if (team2Players.size() > 3) {
                ((EditText) findViewById(C0252R.id.T2P04)).setText("" + ((String) team2Players.get(3)));
            }
            if (team2Players.size() > 4) {
                ((EditText) findViewById(C0252R.id.T2P05)).setText("" + ((String) team2Players.get(4)));
            }
            if (team2Players.size() > 5) {
                ((EditText) findViewById(C0252R.id.T2P06)).setText("" + ((String) team2Players.get(5)));
            }
            if (team2Players.size() > 6) {
                ((EditText) findViewById(C0252R.id.T2P07)).setText("" + ((String) team2Players.get(6)));
            }
            if (team2Players.size() > 7) {
                ((EditText) findViewById(C0252R.id.T2P08)).setText("" + ((String) team2Players.get(7)));
            }
            if (team2Players.size() > 8) {
                ((EditText) findViewById(C0252R.id.T2P09)).setText("" + ((String) team2Players.get(8)));
            }
            if (team2Players.size() > 9) {
                ((EditText) findViewById(C0252R.id.T2P10)).setText("" + ((String) team2Players.get(9)));
            }
            if (team2Players.size() > 10) {
                ((EditText) findViewById(C0252R.id.T2P11)).setText("" + ((String) team2Players.get(10)));
            }
            if (team2Players.size() > 11) {
                ((EditText) findViewById(C0252R.id.T2P12)).setText("" + ((String) team2Players.get(11)));
            }
            if (team2Players.size() > 12) {
                ((EditText) findViewById(C0252R.id.T2P13)).setText("" + ((String) team2Players.get(12)));
            }
            if (team2Players.size() > 13) {
                ((EditText) findViewById(C0252R.id.T2P14)).setText("" + ((String) team2Players.get(13)));
            }
            if (team2Players.size() > 14) {
                ((EditText) findViewById(C0252R.id.T2P15)).setText("" + ((String) team2Players.get(14)));
            }
        }
        ((LinearLayout) findViewById(C0252R.id.MainLayout)).requestFocus();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onBack(View view) {
        finish();
    }

    public void onClear(View view) {
        ((EditText) findViewById(C0252R.id.T1P01)).setText("");
        ((EditText) findViewById(C0252R.id.T1P02)).setText("");
        ((EditText) findViewById(C0252R.id.T1P03)).setText("");
        ((EditText) findViewById(C0252R.id.T1P04)).setText("");
        ((EditText) findViewById(C0252R.id.T1P05)).setText("");
        ((EditText) findViewById(C0252R.id.T1P06)).setText("");
        ((EditText) findViewById(C0252R.id.T1P07)).setText("");
        ((EditText) findViewById(C0252R.id.T1P08)).setText("");
        ((EditText) findViewById(C0252R.id.T1P09)).setText("");
        ((EditText) findViewById(C0252R.id.T1P10)).setText("");
        ((EditText) findViewById(C0252R.id.T1P11)).setText("");
        ((EditText) findViewById(C0252R.id.T1P12)).setText("");
        ((EditText) findViewById(C0252R.id.T1P13)).setText("");
        ((EditText) findViewById(C0252R.id.T1P14)).setText("");
        ((EditText) findViewById(C0252R.id.T1P15)).setText("");
        ((EditText) findViewById(C0252R.id.T2P01)).setText("");
        ((EditText) findViewById(C0252R.id.T2P02)).setText("");
        ((EditText) findViewById(C0252R.id.T2P03)).setText("");
        ((EditText) findViewById(C0252R.id.T2P04)).setText("");
        ((EditText) findViewById(C0252R.id.T2P05)).setText("");
        ((EditText) findViewById(C0252R.id.T2P06)).setText("");
        ((EditText) findViewById(C0252R.id.T2P07)).setText("");
        ((EditText) findViewById(C0252R.id.T2P08)).setText("");
        ((EditText) findViewById(C0252R.id.T2P09)).setText("");
        ((EditText) findViewById(C0252R.id.T2P10)).setText("");
        ((EditText) findViewById(C0252R.id.T2P11)).setText("");
        ((EditText) findViewById(C0252R.id.T2P12)).setText("");
        ((EditText) findViewById(C0252R.id.T2P13)).setText("");
        ((EditText) findViewById(C0252R.id.T2P14)).setText("");
        ((EditText) findViewById(C0252R.id.T2P15)).setText("");
        ((EditText) findViewById(C0252R.id.T1P01)).requestFocus();
    }

    public void onSave(View view) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteAllTeamPlayers();
        String playerName = "" + ((EditText) findViewById(C0252R.id.T1P01)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P02)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P03)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P04)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P05)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P06)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P07)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P08)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P09)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P10)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P11)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P12)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P13)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P14)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T1P15)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam1), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P01)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P02)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P03)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P04)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P05)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P06)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P07)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P08)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P09)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P10)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P11)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P12)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P13)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P14)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        playerName = "" + ((EditText) findViewById(C0252R.id.T2P15)).getText().toString();
        if (!playerName.trim().equalsIgnoreCase("")) {
            db.addTeamPlayer(getString(C0252R.string.dbTeam2), playerName);
        }
        db.close();
    }
}
