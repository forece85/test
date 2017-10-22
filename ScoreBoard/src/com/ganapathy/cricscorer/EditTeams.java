package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class EditTeams extends BaseActivity {
    private View _currentView;
    private boolean _itemSelectedForEdit = false;
    private int _maxNoOfDays = 0;
    private int _maxNoOfInngs = 0;
    private int _maxOvers = 0;
    private String _selectedMatch = "";
    private String _selectedTeam = "1";
    private String _selectedTeamMember = "";
    protected List<String> playerList;

    class C02311 implements OnClickListener {
        C02311() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EditTeams.this.UpdateTeamMember(true);
            EditTeams.this.Refresh();
        }
    }

    class C02322 implements OnClickListener {
        C02322() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EditTeams.this.UpdateTeamMember(false);
            EditTeams.this.Refresh();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_edit_teams);
            this._selectedMatch = getIntent().getExtras().getString("SelectedMatch");
            initializeControls();
        } catch (Exception e) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == C0252R.id.noOfInngs) {
            menu.setHeaderTitle(C0252R.string.inngsPerSideText);
            inflater.inflate(C0252R.menu.noinngs_context_menu, menu);
        } else if (v.getId() == C0252R.id.noOfDays) {
            menu.setHeaderTitle(C0252R.string.noOfDaysText);
            inflater.inflate(C0252R.menu.days_context_menu, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.miDayOne:
            case C0252R.id.miDayTwo:
            case C0252R.id.miDayThree:
            case C0252R.id.miDayFour:
            case C0252R.id.miDayFive:
            case C0252R.id.miDaySix:
                ((Button) findViewById(C0252R.id.noOfDays)).setText(item.getTitle());
                return true;
            case C0252R.id.miOne:
            case C0252R.id.miTwo:
                ((Button) findViewById(C0252R.id.noOfInngs)).setText(item.getTitle());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void Refresh() {
        populatePlayers();
    }

    private void initializeControls() {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        TextView textMatchId = (TextView) findViewById(C0252R.id.textMatchID);
        EditText team1Name = (EditText) findViewById(C0252R.id.editTeam1);
        EditText team2Name = (EditText) findViewById(C0252R.id.editTeam2);
        EditText editOvers = (EditText) findViewById(C0252R.id.editOvers);
        EditText editMatchDateTime = (EditText) findViewById(C0252R.id.editDateTime);
        EditText editMatchName = (EditText) findViewById(C0252R.id.editTournamentName);
        EditText editVenue = (EditText) findViewById(C0252R.id.editVenue);
        Button team1Button = (Button) findViewById(C0252R.id.team1Button);
        Button team2Button = (Button) findViewById(C0252R.id.team2Button);
        CheckBox unlimitedOvers = (CheckBox) findViewById(C0252R.id.checkConfigOvers);
        Button noOfInnings = (Button) findViewById(C0252R.id.noOfInngs);
        Button noOfDays = (Button) findViewById(C0252R.id.noOfDays);
        ((Button) findViewById(C0252R.id.expandTeams)).setText(getString(C0252R.string.editTeamsText) + " " + getString(C0252R.string.tapToEditText));
        textMatchId.setText(getString(C0252R.string.matchIDText) + " " + refMatch.getMatchID());
        team1Name.setText(refMatch.getTeam1Name());
        team2Name.setText(refMatch.getTeam2Name());
        editMatchDateTime.setText("" + refMatch.getDateTime());
        editMatchName.setText("" + refMatch.getMatchName());
        editVenue.setText("" + refMatch.getVenue());
        try {
            DatabaseHandler db = new DatabaseHandler(this);
            int[] result = db.getMaxOversOfMatch(this._selectedMatch);
            this._maxOvers = result[0];
            this._maxNoOfInngs = result[1];
            this._maxNoOfDays = result[2];
            db.close();
        } catch (Exception e) {
            this._maxOvers = 0;
        }
        if (refMatch.getOvers() == 4479) {
            editOvers.setVisibility(4);
            unlimitedOvers.setChecked(true);
        } else {
            editOvers.setVisibility(0);
            unlimitedOvers.setChecked(false);
        }
        editOvers.setText("" + refMatch.getOvers());
        team1Button.setText(refMatch.getTeam1Name());
        team2Button.setText(refMatch.getTeam2Name());
        noOfInnings.setText("" + refMatch.getNoOfInngs());
        noOfDays.setText("" + refMatch.getNoOfDays());
        this._selectedTeam = "1";
        populatePlayers();
        team1Button.setCompoundDrawablesWithIntrinsicBounds(C0252R.drawable.tick_ok, 0, 0, 0);
        team2Button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        centerTextButton(team1Button);
        centerTextButton(team2Button);
        registerForContextMenu(noOfInnings);
        registerForContextMenu(noOfDays);
    }

    public void changeOversConfig(View view) {
        EditText oversText = (EditText) findViewById(C0252R.id.editOvers);
        if (((CheckBox) findViewById(C0252R.id.checkConfigOvers)).isChecked()) {
            oversText.setVisibility(4);
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(oversText.getWindowToken(), 0);
            return;
        }
        if (oversText.getText().toString().equalsIgnoreCase("4479")) {
            oversText.setText("" + (this._maxOvers + 1));
        }
        oversText.setVisibility(0);
    }

    public void onNoOfInnings(View view) {
        openContextMenu(view);
    }

    public void onNoOfDays(View view) {
        openContextMenu(view);
    }

    public void onTeam1(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        this._selectedTeam = "1";
        Button team1Button = (Button) findViewById(C0252R.id.team1Button);
        Button team2Button = (Button) findViewById(C0252R.id.team2Button);
        TextView labelPlayers = (TextView) findViewById(C0252R.id.textPlayerListLabel);
        team1Button.setCompoundDrawablesWithIntrinsicBounds(C0252R.drawable.tick_ok, 0, 0, 0);
        team2Button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        centerTextButton(team1Button);
        centerTextButton(team2Button);
        labelPlayers.setText(refMatch.getTeam1Name() + " " + getString(C0252R.string.playersListText));
        populatePlayers();
    }

    public void onTeam2(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        this._selectedTeam = "2";
        Button team1Button = (Button) findViewById(C0252R.id.team1Button);
        Button team2Button = (Button) findViewById(C0252R.id.team2Button);
        TextView labelPlayers = (TextView) findViewById(C0252R.id.textPlayerListLabel);
        team2Button.setCompoundDrawablesWithIntrinsicBounds(C0252R.drawable.tick_ok, 0, 0, 0);
        team1Button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        centerTextButton(team1Button);
        centerTextButton(team2Button);
        labelPlayers.setText(refMatch.getTeam2Name() + " " + getString(C0252R.string.playersListText));
        populatePlayers();
    }

    private void centerTextButton(Button view) {
        Drawable[] drawables = view.getCompoundDrawables();
        if (drawables == null || drawables.length <= 0 || drawables[0] == null) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingLeft(), view.getPaddingBottom());
            return;
        }
        int width = drawables[0].getIntrinsicWidth();
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingLeft(), view.getPaddingBottom());
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight() + width, view.getPaddingBottom());
    }

    public void onExpandMatchDetails(View view) {
        LinearLayout matchDetails = (LinearLayout) findViewById(C0252R.id.editMatchDetails);
        LinearLayout matchTeams = (LinearLayout) findViewById(C0252R.id.editMatchTeams);
        Button buttonTeams = (Button) findViewById(C0252R.id.expandTeams);
        ((Button) findViewById(C0252R.id.expandDetails)).setText(getString(C0252R.string.editMatchDetailsText));
        buttonTeams.setText(getString(C0252R.string.editTeamsText) + " " + getString(C0252R.string.tapToEditText));
        matchDetails.setVisibility(0);
        matchTeams.setVisibility(8);
    }

    public void onExpandMatchTeams(View view) {
        LinearLayout matchDetails = (LinearLayout) findViewById(C0252R.id.editMatchDetails);
        LinearLayout matchTeams = (LinearLayout) findViewById(C0252R.id.editMatchTeams);
        Button buttonTeams = (Button) findViewById(C0252R.id.expandTeams);
        ((Button) findViewById(C0252R.id.expandDetails)).setText(getString(C0252R.string.editMatchDetailsText) + " " + getString(C0252R.string.tapToEditText));
        buttonTeams.setText(getString(C0252R.string.editTeamsText));
        matchDetails.setVisibility(8);
        matchTeams.setVisibility(0);
    }

    protected void populatePlayers() {
        if (this.playerList != null) {
            this.playerList.clear();
        }
        this._itemSelectedForEdit = false;
        DatabaseHandler db = new DatabaseHandler(this);
        this.playerList = db.getMatchTeamPlayers(this._selectedMatch, this._selectedTeam);
        db.close();
        ((ListView) findViewById(C0252R.id.editTeamsListView)).setAdapter(new EditTeamListAdapter(this, this.playerList));
    }

    public void onAddPlayer(View view) {
        if (!isFrequentClick()) {
            if (this._itemSelectedForEdit) {
                Toast.makeText(getApplicationContext(), "Please complete previous action!", 0).show();
                return;
            }
            EditText newPlayerName = (EditText) findViewById(C0252R.id.newPlayerName);
            String playerName = ("" + newPlayerName.getText().toString()).trim();
            if (!playerName.equalsIgnoreCase("")) {
                boolean exists = false;
                for (String player : this.playerList) {
                    if (player != null && player.trim().equalsIgnoreCase(playerName.trim())) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Player name already exists in this team!", 1);
                    toast.setGravity(17, 0, 0);
                    toast.show();
                    return;
                }
                EditTeamListAdapter listAdapter = (EditTeamListAdapter) ((ListView) findViewById(C0252R.id.editTeamsListView)).getAdapter();
                this.playerList.add(playerName.trim());
                listAdapter.notifyDataSetChanged();
                DatabaseHandler db = new DatabaseHandler(this);
                db.addPlayerIntoMatch(this._selectedMatch, this._selectedTeam, playerName.trim());
                db.close();
                newPlayerName.setText("");
            }
        }
    }

    public void OnSaveMatchDetails(View view) {
        if (!isFrequentClick()) {
            EditText team1Name = (EditText) findViewById(C0252R.id.editTeam1);
            EditText team2Name = (EditText) findViewById(C0252R.id.editTeam2);
            EditText editOvers = (EditText) findViewById(C0252R.id.editOvers);
            EditText editMatchDateTime = (EditText) findViewById(C0252R.id.editDateTime);
            EditText editMatchName = (EditText) findViewById(C0252R.id.editTournamentName);
            EditText editVenue = (EditText) findViewById(C0252R.id.editVenue);
            Button team1Button = (Button) findViewById(C0252R.id.team1Button);
            Button team2Button = (Button) findViewById(C0252R.id.team2Button);
            CheckBox unlimitedOvers = (CheckBox) findViewById(C0252R.id.checkConfigOvers);
            Button noOfInnings = (Button) findViewById(C0252R.id.noOfInngs);
            Button noOfDays = (Button) findViewById(C0252R.id.noOfDays);
            if (team1Name.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Team 1 name should not be empty!", 0).show();
            } else if (team2Name.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Team 2 name should not be empty!", 0).show();
            } else if (team1Name.getText().toString().trim().equalsIgnoreCase(team2Name.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "Team names should not be same!", 0).show();
            } else if (editMatchDateTime.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Match Date Time should not be empty!", 0).show();
            } else {
                if (unlimitedOvers.isChecked()) {
                    editOvers.setText("4479");
                }
                try {
                    int updatedOvers = Integer.parseInt("0" + editOvers.getText().toString().trim());
                    if (editOvers.getText().toString().trim().equalsIgnoreCase("") || updatedOvers < this._maxOvers) {
                        Toast.makeText(getApplicationContext(), "Overs should not be empty or less than " + this._maxOvers + "!", 0).show();
                        return;
                    }
                    if (Integer.parseInt(noOfInnings.getText().toString()) < this._maxNoOfInngs) {
                        Toast.makeText(getApplicationContext(), "Number of Innings cannot be less than " + this._maxNoOfInngs + "!", 0).show();
                        return;
                    }
                    if (Integer.parseInt(noOfDays.getText().toString()) < this._maxNoOfDays) {
                        Toast.makeText(getApplicationContext(), "Number of Days cannot be less than " + this._maxNoOfDays + "!", 0).show();
                        return;
                    }
                    DatabaseHandler db = new DatabaseHandler(this);
                    dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
                    refMatch.setDateTime(editMatchDateTime.getText().toString().trim());
                    refMatch.setTeam1Name(team1Name.getText().toString().trim());
                    refMatch.setTeam2Name(team2Name.getText().toString().trim());
                    refMatch.setVenue(editVenue.getText().toString().trim());
                    refMatch.setMatchName(editMatchName.getText().toString().trim());
                    refMatch.setOvers(updatedOvers);
                    refMatch.setNoOfInngs(Integer.parseInt(noOfInnings.getText().toString()));
                    refMatch.setNoOfDays(Integer.parseInt(noOfDays.getText().toString()));
                    db.updateMatchDetails(refMatch);
                    db.close();
                    team1Button.setText(refMatch.getTeam1Name());
                    team2Button.setText(refMatch.getTeam2Name());
                    Toast.makeText(getApplicationContext(), "Match details saved successfuly!", 0).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error saving match details. " + e.getMessage(), 0).show();
                }
            }
        }
    }

    public void OnEditTeamMember(View view) {
        if (!isFrequentClick()) {
            if (this._itemSelectedForEdit) {
                Toast.makeText(getApplicationContext(), "Please complete previous action!", 0).show();
                return;
            }
            LinearLayout vwParentRow = (LinearLayout) view.getParent();
            TextView child = (TextView) vwParentRow.getChildAt(0);
            EditText edtChild = (EditText) vwParentRow.getChildAt(1);
            Button btnChild1 = (Button) vwParentRow.getChildAt(2);
            Button btnChild2 = (Button) vwParentRow.getChildAt(3);
            Button btnChild3 = (Button) vwParentRow.getChildAt(4);
            this._selectedTeamMember = child.getText().toString();
            edtChild.setText(this._selectedTeamMember);
            child.setVisibility(8);
            edtChild.setVisibility(0);
            btnChild1.setVisibility(8);
            btnChild2.setVisibility(0);
            btnChild3.setVisibility(0);
            this._itemSelectedForEdit = true;
            edtChild.requestFocus();
        }
    }

    public void OnSaveTeamMember(View view) {
        if (!isFrequentClick()) {
            this._currentView = view;
            String updatedTeamMember = ((EditText) ((LinearLayout) view.getParent()).getChildAt(1)).getText().toString();
            if (updatedTeamMember.trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Player name should not be empty!", 0).show();
                return;
            }
            boolean exists = false;
            for (String item : this.playerList) {
                if (!item.trim().equalsIgnoreCase("") && item.trim().equalsIgnoreCase(updatedTeamMember.trim())) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                new Builder(this).setTitle("Player already Exists").setMessage("Do you want to merge the player " + this._selectedTeamMember + "?\nRemember this cannot be undone and affects all of his records in this match!\n\nTip:To swap players please use temporary names.").setIcon(17301543).setCancelable(false).setPositiveButton("Yes", new C02311()).setNegativeButton("No", null).show();
            } else {
                new Builder(this).setTitle("Rename Player").setMessage("Are you sure to rename the player " + this._selectedTeamMember + "?\nRemember this cannot be undone and affects all of his records in this match!").setIcon(17301543).setCancelable(false).setPositiveButton("Yes", new C02322()).setNegativeButton("No", null).show();
            }
        }
    }

    public void UpdateTeamMember(boolean playerExists) {
        if (!isFrequentClick()) {
            LinearLayout vwParentRow = (LinearLayout) this._currentView.getParent();
            TextView child = (TextView) vwParentRow.getChildAt(0);
            EditText edtChild = (EditText) vwParentRow.getChildAt(1);
            Button btnChild1 = (Button) vwParentRow.getChildAt(2);
            Button btnChild2 = (Button) vwParentRow.getChildAt(3);
            Button btnChild3 = (Button) vwParentRow.getChildAt(4);
            String updatedTeamMember = edtChild.getText().toString();
            child.setText(updatedTeamMember);
            ListView editTeamsListView = (ListView) findViewById(C0252R.id.editTeamsListView);
            EditTeamListAdapter listAdapter = (EditTeamListAdapter) editTeamsListView.getAdapter();
            int position = editTeamsListView.getPositionForView(this._currentView);
            this.playerList.remove(position);
            this.playerList.add(position, updatedTeamMember);
            listAdapter.notifyDataSetChanged();
            DatabaseHandler db = new DatabaseHandler(this);
            db.updateTeamMember(this._selectedMatch, this._selectedTeamMember, updatedTeamMember, this._selectedTeam, playerExists);
            db.close();
            child.setVisibility(0);
            edtChild.setVisibility(8);
            btnChild1.setVisibility(0);
            btnChild2.setVisibility(8);
            btnChild3.setVisibility(8);
            this._itemSelectedForEdit = false;
        }
    }

    public void OnCancelTeamMember(View view) {
        if (!isFrequentClick()) {
            LinearLayout vwParentRow = (LinearLayout) view.getParent();
            TextView child = (TextView) vwParentRow.getChildAt(0);
            EditText edtChild = (EditText) vwParentRow.getChildAt(1);
            Button btnChild1 = (Button) vwParentRow.getChildAt(2);
            Button btnChild2 = (Button) vwParentRow.getChildAt(3);
            Button btnChild3 = (Button) vwParentRow.getChildAt(4);
            child.setText(this._selectedTeamMember);
            this._selectedTeamMember = "";
            child.setVisibility(0);
            edtChild.setVisibility(8);
            btnChild1.setVisibility(0);
            btnChild2.setVisibility(8);
            btnChild3.setVisibility(8);
            this._itemSelectedForEdit = false;
        }
    }

    public void OnDeleteTeamMember(View view) {
    }
}
