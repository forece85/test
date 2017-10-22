package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import java.util.List;

public class TeamList extends BaseActivity {
    private View _currentView;
    private String _selectedTeam = "";
    EditTeamListAdapter listAdapter;
    protected List<String> teamList;

    class C02711 implements OnClickListener {
        C02711() {
        }

        public void onClick(DialogInterface dialog, int which) {
            TeamList.this.deleteTeam();
            TeamList.this.Refresh();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_team_list);
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
            populateTeams();
        } catch (Exception e) {
            finish();
        }
    }

    protected void populateTeams() {
        DatabaseHandler db = new DatabaseHandler(this);
        this.teamList = db.getManageTeamList();
        db.close();
        ListView teamListView = (ListView) findViewById(C0252R.id.teamListView);
        this.listAdapter = new EditTeamListAdapter(this, this.teamList, true);
        teamListView.setAdapter(this.listAdapter);
    }

    public void Refresh() {
        this._selectedTeam = "";
        populateTeams();
    }

    public void onAddTeam(View view) {
        Intent intent = new Intent(this, ManageTeam.class);
        intent.putExtra("SelectedTeam", "");
        startActivity(intent);
    }

    public void onDefaultTeams(View view) {
        startActivity(new Intent(this, SetDefaultTeams.class));
    }

    public void OnEditTeamMember(View view) {
        this._selectedTeam = (String) this.teamList.get(((ListView) findViewById(C0252R.id.teamListView)).getPositionForView(view));
        if (this._selectedTeam != null && !this._selectedTeam.equalsIgnoreCase("")) {
            Intent intent = new Intent(this, ManageTeam.class);
            intent.putExtra("SelectedTeam", this._selectedTeam);
            startActivity(intent);
        }
    }

    public void OnDeleteTeamMember(View view) {
        this._currentView = view;
        new Builder(this).setTitle("Delete Team").setMessage("Are you sure to delete this team? Note: Player stats will exists even team is deleted.").setIcon(17301543).setCancelable(false).setPositiveButton("Yes", new C02711()).setNegativeButton("No", null).show();
    }

    public void deleteTeam() {
        ListView teamsListView = (ListView) findViewById(C0252R.id.teamListView);
        EditTeamListAdapter listAdapter = (EditTeamListAdapter) teamsListView.getAdapter();
        int position = teamsListView.getPositionForView(this._currentView);
        this._selectedTeam = "" + ((String) this.teamList.get(position));
        this.teamList.remove(position);
        listAdapter.notifyDataSetChanged();
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteManageTeamPlayers(this._selectedTeam);
        db.close();
        this._selectedTeam = "";
    }
}
