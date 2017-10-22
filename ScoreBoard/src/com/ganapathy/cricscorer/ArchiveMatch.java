package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ArchiveMatch extends BaseActivity {
    public boolean _isSuccess = false;
    private String _storagePath = "";
    MatchListAdapter listAdapter;
    protected List<dtoMatchList> matchList;
    protected List<dtoMatch> matchesToRestore = null;

    class C02061 implements OnItemClickListener {
        C02061() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ArchiveMatch.this.listAdapter.setSelectedPosition(position);
        }
    }

    class C02072 implements OnClickListener {
        C02072() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ArchiveMatch.this.doBackup();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_archive_match);
            this._storagePath = getIntent().getExtras().getString("StoragePath");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        try {
            loadExistingMatchList();
            populateMatches();
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

    protected void loadExistingMatchList() {
        DatabaseHandler db = new DatabaseHandler(this);
        this.matchList = db.getAllMatchList();
        db.close();
        Collections.sort(this.matchList, new MatchNameComparator());
        String matchName = "";
        for (dtoMatchList match : this.matchList) {
            if (matchName.equalsIgnoreCase(match.getMatchName())) {
                match.setMatchName("");
            } else {
                matchName = match.getMatchName();
            }
        }
    }

    protected void populateMatches() {
        ListView mainListView = (ListView) findViewById(C0252R.id.mainListView);
        this.listAdapter = new MatchListAdapter(this, this.matchList, false);
        mainListView.setAdapter(this.listAdapter);
        mainListView.setOnItemClickListener(new C02061());
    }

    public void onBackup(View view) {
        EditText editFileName = (EditText) findViewById(C0252R.id.editFileName);
        String archiveFileName = editFileName.getText().toString().trim();
        String archiveFullFileName = editFileName.getTag().toString().trim() + "//" + archiveFileName + (archiveFileName.toLowerCase(Locale.US).endsWith(".bmz") ? "" : ".bmz");
        if (archiveFileName.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please provide archive file name to proceed!", 0).show();
            return;
        }
        try {
            if (new File(archiveFullFileName).exists()) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("Archive file already exist. Do you want to overwrite?");
                Utility.setMediumTitle(textView, getApplicationContext());
                new Builder(this).setTitle("Archive Match!").setView(textView).setIcon(17301543).setCancelable(false).setPositiveButton("Yes", new C02072()).setNegativeButton("No", null).show();
                return;
            }
            doBackup();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), 1).show();
        }
    }

    public void doBackup() {
        List<Integer> selectedPositions = this.listAdapter.getSelectedPositions();
        if (selectedPositions.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please select one or more matches to archive!", 0).show();
            return;
        }
        EditText editFileName = (EditText) findViewById(C0252R.id.editFileName);
        final String archiveFileName = editFileName.getText().toString().trim();
        final String archiveFullFileName = editFileName.getTag().toString().trim() + "//" + archiveFileName + (archiveFileName.toLowerCase(Locale.US).endsWith(".bmz") ? "" : ".bmz");
        if (archiveFileName.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please provide archive file name to proceed!", 0).show();
            return;
        }
        final List<String> matchIds = new ArrayList();
        for (Integer position : selectedPositions) {
            matchIds.add(this.listAdapter.getMatchID(position.intValue()));
        }
        this._isSuccess = false;
        try {
            if (!matchIds.isEmpty()) {
                new AsyncTask<Boolean, Void, Void>() {
                    private ProgressDialog pd;

                    protected void onPreExecute() {
                        this.pd = new ProgressDialog(ArchiveMatch.this);
                        this.pd.setTitle("Archiving matches...");
                        this.pd.setMessage("Please wait.");
                        this.pd.setCancelable(false);
                        this.pd.setIndeterminate(true);
                        this.pd.show();
                    }

                    protected Void doInBackground(Boolean... arg0) {
                        try {
                            ArchiveMatch.this._isSuccess = Utility.ArchiveMatch(ArchiveMatch.this.getApplicationContext(), matchIds, ArchiveMatch.this._storagePath, archiveFullFileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    protected void onPostExecute(Void result) {
                        if (this.pd.isShowing()) {
                            this.pd.dismiss();
                        }
                        if (ArchiveMatch.this._isSuccess) {
                            Toast.makeText(ArchiveMatch.this.getApplicationContext(), "Matches archived successfully in file " + archiveFileName + "!", 1).show();
                        } else {
                            Toast.makeText(ArchiveMatch.this.getApplicationContext(), "Archive terminated! Matches NOT archived in file " + archiveFileName + "!", 1).show();
                        }
                    }
                }.execute(new Boolean[0]);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), 1).show();
        }
    }

    public void onLoadList(View view) {
    }

    protected List<dtoMatchList> buildMatchList() {
        List<dtoMatchList> matchList = new ArrayList();
        for (dtoMatch match : this.matchesToRestore) {
            dtoMatchList list = new dtoMatchList();
            list.setMatchID(match.getMatchID());
            list.setMatchDateTime(match.getDateTime());
            list.setOvers("Overs " + (match.getOvers() == 4479 ? "Unlimited" : Integer.valueOf(match.getOvers())));
            list.setTeams("" + match.getTeam1Name() + " vs " + match.getTeam2Name() + (new StringBuilder().append("").append(match.getVenue()).toString().trim().equalsIgnoreCase("") ? "" : " at " + match.getVenue()));
            list.setToss("Toss won by " + (match.getTossWonBy().equalsIgnoreCase("Team1") ? match.getTeam1Name() : match.getTeam2Name()));
            matchList.add(list);
        }
        return matchList;
    }
}
