package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArchiveTeam extends BaseActivity {
    private String _storagePath = "";
    protected TeamListAdapter listAdapter;
    protected List<String> teamList;

    class C02091 implements OnItemClickListener {
        C02091() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ArchiveTeam.this.listAdapter.setSelectedPosition(position);
        }
    }

    class C02102 implements OnClickListener {
        C02102() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ArchiveTeam.this.doBackup();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_archive_team);
            this._storagePath = getIntent().getExtras().getString("StoragePath");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        try {
            populateTeams();
            Utility.CheckDirs(this._storagePath);
            setFileName(new File(this._storagePath + File.separator + "Archive"));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 0).show();
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void setFileName(File fileName) {
        EditText editFileName = (EditText) findViewById(C0252R.id.editFileName);
        editFileName.setText(fileName.getName());
        editFileName.setTag(fileName.getAbsolutePath().substring(0, fileName.getAbsolutePath().lastIndexOf("/")));
    }

    protected void populateTeams() {
        DatabaseHandler db = new DatabaseHandler(this);
        this.teamList = db.getManageTeamList();
        db.close();
        ListView teamListView = (ListView) findViewById(C0252R.id.mainListView);
        this.listAdapter = new TeamListAdapter(this, this.teamList, false);
        teamListView.setAdapter(this.listAdapter);
        teamListView.setOnItemClickListener(new C02091());
    }

    public void onBackup(View view) {
        if (this.listAdapter.getSelectedPositions().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please select one or more teams to archive!", 0).show();
            return;
        }
        EditText editFileName = (EditText) findViewById(C0252R.id.editFileName);
        String archiveFileName = editFileName.getText().toString().trim();
        String archiveFullFileName = editFileName.getTag().toString().trim() + "//" + archiveFileName + (archiveFileName.toLowerCase(Locale.US).endsWith(".btz") ? "" : ".btz");
        if (archiveFileName.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please provide archive file name to proceed!", 0).show();
            return;
        }
        try {
            if (new File(archiveFullFileName).exists()) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("Archive file already exist. Do you want to overwrite?");
                Utility.setMediumTitle(textView, getApplicationContext());
                new Builder(this).setTitle("Archive Team!").setView(textView).setIcon(17301543).setCancelable(false).setPositiveButton("Yes", new C02102()).setNegativeButton("No", null).show();
                return;
            }
            doBackup();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), 1).show();
        }
    }

    public void doBackup() {
        List<Integer> selectedPositions = this.listAdapter.getSelectedPositions();
        EditText editFileName = (EditText) findViewById(C0252R.id.editFileName);
        String archiveFileName = editFileName.getText().toString().trim();
        String archiveFullFileName = editFileName.getTag().toString().trim() + "//" + archiveFileName + (archiveFileName.toLowerCase(Locale.US).endsWith(".btz") ? "" : ".btz");
        List<String> teamIds = new ArrayList();
        for (Integer position : selectedPositions) {
            teamIds.add(this.listAdapter.getTeamName(position.intValue()));
        }
        try {
            if (!teamIds.isEmpty() && Utility.ArchiveTeam(getApplicationContext(), teamIds, this._storagePath, archiveFullFileName)) {
                Toast.makeText(getApplicationContext(), "Teams archived successfully in file " + archiveFileName + "!", 1).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), 1).show();
        }
    }
}
