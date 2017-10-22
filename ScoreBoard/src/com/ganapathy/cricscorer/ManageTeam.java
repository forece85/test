package com.ganapathy.cricscorer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ManageTeam extends BaseActivity {
    private View _currentView;
    private boolean _itemSelectedForEdit = false;
    private String _previousTeam = "";
    private String _selectedTeam = "";
    private String _selectedTeamMember = "";
    protected dtoTeamPlayerList playerList;

    class C02403 implements OnClickListener {
        C02403() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ManageTeam.this.deleteTeamPlayer();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_manage_team);
        } catch (Exception e) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onResume() {
        super.onResume();
        try {
            Bundle extras = getIntent().getExtras();
            if (this._selectedTeam.equalsIgnoreCase("")) {
                this._selectedTeam = "" + extras.getString("SelectedTeam");
                this._previousTeam = this._selectedTeam;
            }
            if (this._selectedTeam.equalsIgnoreCase("")) {
                OnEditTeamName(null);
            } else {
                ((TextView) findViewById(C0252R.id.textTeamName)).setText(this._selectedTeam);
            }
            populatePlayers();
        } catch (Exception e) {
            finish();
        }
    }

    public void Refresh() {
        populatePlayers();
    }

    protected void populatePlayers() {
        if (this.playerList != null) {
            this.playerList.clear();
        }
        DatabaseHandler db = new DatabaseHandler(this);
        this.playerList = db.getManageTeamPlayersList(this._selectedTeam);
        db.close();
        ((ListView) findViewById(C0252R.id.editPlayersListView)).setAdapter(new EditTeamPlayersListAdapter(this, this.playerList, true, true, false));
        this._itemSelectedForEdit = false;
    }

    public void OnEditTeamName(View view) {
        if (this._itemSelectedForEdit) {
            Toast.makeText(getApplicationContext(), "Please complete previous action!", 0).show();
            return;
        }
        TextView child = (TextView) findViewById(C0252R.id.textTeamName);
        EditText edtChild = (EditText) findViewById(C0252R.id.editTeamName);
        Button btnChild1 = (Button) findViewById(C0252R.id.buttonEditTeamName);
        Button btnChild2 = (Button) findViewById(C0252R.id.saveTeamName);
        Button btnChild3 = (Button) findViewById(C0252R.id.cancelTeamName);
        this._selectedTeam = child.getText().toString().trim();
        edtChild.setText(this._selectedTeam);
        child.setVisibility(8);
        edtChild.setVisibility(0);
        btnChild1.setVisibility(8);
        btnChild2.setVisibility(0);
        btnChild3.setVisibility(0);
        this._itemSelectedForEdit = true;
        edtChild.requestFocus();
    }

    public void OnSaveTeamName(View view) {
        TextView child = (TextView) findViewById(C0252R.id.textTeamName);
        EditText edtChild = (EditText) findViewById(C0252R.id.editTeamName);
        Button btnChild1 = (Button) findViewById(C0252R.id.buttonEditTeamName);
        Button btnChild2 = (Button) findViewById(C0252R.id.saveTeamName);
        Button btnChild3 = (Button) findViewById(C0252R.id.cancelTeamName);
        String updatedTeamName = edtChild.getText().toString().trim();
        if (updatedTeamName.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Team name should not be empty!", 0).show();
            return;
        }
        child.setText(updatedTeamName);
        this._selectedTeam = updatedTeamName;
        if (!(this._previousTeam.equalsIgnoreCase("") || this._selectedTeam.equalsIgnoreCase("") || this._previousTeam.equalsIgnoreCase(this._selectedTeam))) {
            DatabaseHandler db = new DatabaseHandler(this);
            db.updateManageTeam(this._previousTeam, this._selectedTeam);
            db.close();
        }
        child.setVisibility(0);
        edtChild.setVisibility(8);
        btnChild1.setVisibility(0);
        btnChild2.setVisibility(8);
        btnChild3.setVisibility(8);
        this._itemSelectedForEdit = false;
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(edtChild.getWindowToken(), 0);
        ((EditText) findViewById(C0252R.id.newPlayerName)).requestFocus();
    }

    public void OnCancelTeamName(View view) {
        TextView child = (TextView) findViewById(C0252R.id.textTeamName);
        EditText edtChild = (EditText) findViewById(C0252R.id.editTeamName);
        Button btnChild1 = (Button) findViewById(C0252R.id.buttonEditTeamName);
        Button btnChild2 = (Button) findViewById(C0252R.id.saveTeamName);
        Button btnChild3 = (Button) findViewById(C0252R.id.cancelTeamName);
        child.setText(this._selectedTeam);
        child.setVisibility(0);
        edtChild.setVisibility(8);
        btnChild1.setVisibility(0);
        btnChild2.setVisibility(8);
        btnChild3.setVisibility(8);
        this._itemSelectedForEdit = false;
    }

    public void onAddPlayer(View view) {
        int i = 1;
        if (this._itemSelectedForEdit) {
            Toast.makeText(getApplicationContext(), "Please complete previous action!", 0).show();
        } else if (this._selectedTeam.equalsIgnoreCase("")) {
            toast = Toast.makeText(getApplicationContext(), "Team name should not be empty to proceed!", 1);
            toast.setGravity(17, 0, 0);
            toast.show();
        } else {
            EditText newPlayerName = (EditText) findViewById(C0252R.id.newPlayerName);
            String playerName = ("" + newPlayerName.getText().toString()).trim();
            if (!playerName.equalsIgnoreCase("")) {
                if (this.playerList.contains(playerName.trim())) {
                    toast = Toast.makeText(getApplicationContext(), "Player name already exists in this team!", 1);
                    toast.setGravity(17, 0, 0);
                    toast.show();
                    return;
                }
                int i2;
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(newPlayerName.getWindowToken(), 0);
                EditTeamPlayersListAdapter listAdapter = (EditTeamPlayersListAdapter) ((ListView) findViewById(C0252R.id.editPlayersListView)).getAdapter();
                dtoTeamPlayer player = new dtoTeamPlayer();
                player.setPlayerName(playerName);
                player.setHandedBat(((ToggleButton) findViewById(C0252R.id.toggleHandedBat)).isChecked() ? 1 : 0);
                if (((ToggleButton) findViewById(C0252R.id.toggleHandedBowl)).isChecked()) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                player.setHandedBowl(i2);
                if (((CheckBox) findViewById(C0252R.id.checkIsBatsman)).isChecked()) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                player.setIsBatsman(i2);
                if (((CheckBox) findViewById(C0252R.id.checkIsBowler)).isChecked()) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                player.setIsBowler(i2);
                if (((CheckBox) findViewById(C0252R.id.checkIsCaptain)).isChecked()) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                player.setIsCaptain(i2);
                if (((CheckBox) findViewById(C0252R.id.checkIsKeeper)).isChecked()) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                player.setIsKeeper(i2);
                player.setPlayerName(playerName.trim());
                player.setPlayerOrder(this.playerList.size());
                player.setTeamName(this._selectedTeam);
                if (!((ToggleButton) findViewById(C0252R.id.toggleWhatBowler)).isChecked()) {
                    i = 0;
                }
                player.setWhatBowler(i);
                this.playerList.add(player);
                listAdapter.notifyDataSetChanged();
                SaveTeam();
                resetNewPlayer();
                newPlayerName.requestFocus();
            }
        }
    }

    private void resetNewPlayer() {
        ((EditText) findViewById(C0252R.id.newPlayerName)).setText("");
        ((ToggleButton) findViewById(C0252R.id.toggleHandedBat)).setChecked(false);
        ((ToggleButton) findViewById(C0252R.id.toggleHandedBowl)).setChecked(false);
        ((ToggleButton) findViewById(C0252R.id.toggleWhatBowler)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.checkIsBatsman)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.checkIsBowler)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.checkIsKeeper)).setChecked(false);
        ((CheckBox) findViewById(C0252R.id.checkIsCaptain)).setChecked(false);
    }

    private void displayPopup(dtoTeamPlayer player) {
        Rect displayRectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C0252R.layout.popup_add_player, (ViewGroup) findViewById(C0252R.id.choosePopupLayout));
        layout.setMinimumWidth((int) (((float) displayRectangle.width()) * 0.9f));
        View textView = new TextView(layout.getContext());
        Utility.setMediumTitle(textView, layout.getContext());
        textView.setMinimumHeight(60);
        textView.setText(getApplicationContext().getResources().getString(C0252R.string.editPlayerText));
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
        final int playerOrder = player.getPlayerOrder();
        editPlayerNameBox.setText(player.getPlayerName());
        toggleHandedBatBox.setChecked(player.getHandedBat() == 1);
        toggleHandedBowlBox.setChecked(player.getHandedBowl() == 1);
        toggleWhatBowlerBox.setChecked(player.getWhatBowler() == 1);
        checkIsBatsmanBox.setChecked(player.getIsBatsman() == 1);
        checkIsBowlerBox.setChecked(player.getIsBowler() == 1);
        checkIsKeeperBox.setChecked(player.getIsKeeper() == 1);
        checkIsCaptainBox.setChecked(player.getIsCaptain() == 1);
        final AlertDialog alertDialog = builder.create();
        ((Button) layout.findViewById(C0252R.id.btnOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i;
                int i2 = 1;
                ((InputMethodManager) ManageTeam.this.getSystemService("input_method")).hideSoftInputFromWindow(editPlayerNameBox.getWindowToken(), 0);
                dtoTeamPlayer player = new dtoTeamPlayer();
                player.setPlayerName(editPlayerNameBox.getText().toString());
                player.setHandedBat(toggleHandedBatBox.isChecked() ? 1 : 0);
                if (toggleHandedBowlBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                player.setHandedBowl(i);
                if (toggleWhatBowlerBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                player.setWhatBowler(i);
                if (checkIsBatsmanBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                player.setIsBatsman(i);
                if (checkIsBowlerBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                player.setIsBowler(i);
                if (checkIsKeeperBox.isChecked()) {
                    i = 1;
                } else {
                    i = 0;
                }
                player.setIsKeeper(i);
                if (!checkIsCaptainBox.isChecked()) {
                    i2 = 0;
                }
                player.setIsCaptain(i2);
                player.setPlayerOrder(playerOrder);
                ManageTeam.this.UpdatePlayerList(player);
                alertDialog.dismiss();
                ManageTeam.this._itemSelectedForEdit = false;
            }
        });
        ((Button) layout.findViewById(C0252R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) ManageTeam.this.getSystemService("input_method")).hideSoftInputFromWindow(editPlayerNameBox.getWindowToken(), 0);
                alertDialog.dismiss();
                ManageTeam.this._itemSelectedForEdit = false;
            }
        });
        alertDialog.show();
        ((ViewGroup) layout.getParent()).setPadding(0, 0, 0, 0);
    }

    public void UpdatePlayerList(dtoTeamPlayer player) {
        if (!player.getPlayerName().trim().equalsIgnoreCase("")) {
            this.playerList.updatePlayer(player);
            ((EditTeamPlayersListAdapter) ((ListView) findViewById(C0252R.id.editPlayersListView)).getAdapter()).notifyDataSetChanged();
            SaveTeam();
        }
    }

    public void OnEditTeamMember(View view) {
        if (this._itemSelectedForEdit) {
            Toast.makeText(getApplicationContext(), "Please complete previous action!", 0).show();
        } else if (this._selectedTeam.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Team name should not be empty to proceed!", 1).show();
        } else {
            this._selectedTeamMember = "" + ((dtoTeamPlayer) this.playerList.get(((ListView) findViewById(C0252R.id.editPlayersListView)).getPositionForView(view))).getPlayerName();
            DatabaseHandler db = new DatabaseHandler(this);
            dtoTeamPlayer player = db.getManageTeamPlayer(this._selectedTeam, this._selectedTeamMember);
            db.close();
            if (player != null) {
                displayPopup(player);
                this._itemSelectedForEdit = true;
            }
        }
    }

    public void SaveTeam() {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteManageTeamPlayers(this._selectedTeam);
        db.addManageTeamPlayers(this.playerList);
        db.close();
    }

    public void OnDeleteTeamMember(View view) {
        this._currentView = view;
        if (this._itemSelectedForEdit) {
            Toast.makeText(getApplicationContext(), "Please complete previous action!", 0).show();
        } else {
            new Builder(this).setTitle("Delete player").setMessage("Are you sure to delete this player from this team? Note: Player stats will exists even player is deleted from this team.").setIcon(17301543).setCancelable(false).setPositiveButton("Yes", new C02403()).setNegativeButton("No", null).show();
        }
    }

    public void deleteTeamPlayer() {
        this._selectedTeamMember = ((dtoTeamPlayer) this.playerList.get(((ListView) findViewById(C0252R.id.editPlayersListView)).getPositionForView(this._currentView))).getPlayerName();
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteManageTeamPlayer(this._selectedTeam, this._selectedTeamMember);
        db.close();
        Refresh();
        this._selectedTeamMember = "";
        this._itemSelectedForEdit = false;
    }
}
