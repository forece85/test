package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.ganapathy.cricscorer.dtoArchiveFile.ArchiveFileIconType;
import com.nononsenseapps.filepicker.AbstractFilePickerActivity;
import com.nononsenseapps.filepicker.FilePickerActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class ArchiveExplorer extends BaseActivity {
    private static final int FOLDER_PICKER = 321;
    public String _currentStoragePath = "";
    public String _lastSelectedFileName = "";
    ArchiveFileAdapter fileAdapter;
    ArrayList<dtoArchiveFile> fileList;

    class C02052 implements OnItemClickListener {
        C02052() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ArchiveExplorer.this.fileAdapter.setSelectedPosition(position);
            ArchiveExplorer.this._lastSelectedFileName = ((dtoArchiveFile) ArchiveExplorer.this.fileList.get(position)).getFileName();
            Intent intent;
            if (((dtoArchiveFile) ArchiveExplorer.this.fileList.get(position)).getIconType() == ArchiveFileIconType.MatchAdd) {
                intent = new Intent(ArchiveExplorer.this.getApplicationContext(), ArchiveMatch.class);
                intent.putExtra("StoragePath", ArchiveExplorer.this._currentStoragePath);
                intent.putExtra("IsInternal", false);
                ArchiveExplorer.this.startActivity(intent);
            } else if (((dtoArchiveFile) ArchiveExplorer.this.fileList.get(position)).getIconType() == ArchiveFileIconType.TeamAdd) {
                intent = new Intent(ArchiveExplorer.this.getApplicationContext(), ArchiveTeam.class);
                intent.putExtra("StoragePath", ArchiveExplorer.this._currentStoragePath);
                intent.putExtra("IsInternal", false);
                ArchiveExplorer.this.startActivity(intent);
            } else {
                ArchiveExplorer.this.openContextMenu(view);
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0252R.layout.activity_archive_explorer);
    }

    public void onResume() {
        super.onResume();
        if (this._currentStoragePath == null || this._currentStoragePath.equalsIgnoreCase("")) {
            if (Utility.IsSDCardPresent()) {
                this._currentStoragePath = Environment.getExternalStorageDirectory().getPath();
            } else {
                this._currentStoragePath = getFilesDir().getPath();
            }
        }
        ((TextView) findViewById(C0252R.id.textStorage)).setText(this._currentStoragePath);
        populateFiles();
        registerForContextMenu((GridView) findViewById(C0252R.id.archiveGrid));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FOLDER_PICKER && resultCode == -1) {
            this._currentStoragePath = data.getData().getPath();
            populateFiles();
        }
    }

    public void onBrowse(View view) {
        Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
        i.putExtra(AbstractFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(AbstractFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(AbstractFilePickerActivity.EXTRA_MODE, 1);
        i.putExtra(AbstractFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, FOLDER_PICKER);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0252R.menu.archive_explorer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != C0252R.id.goToDefault) {
            return super.onOptionsItemSelected(item);
        }
        if (Utility.IsSDCardPresent()) {
            this._currentStoragePath = Environment.getExternalStorageDirectory().getPath();
        } else {
            this._currentStoragePath = getFilesDir().getPath();
        }
        populateFiles();
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == C0252R.id.archiveGrid) {
            ArchiveFileAdapter archiveAdapter = (ArchiveFileAdapter) ((GridView) v).getAdapter();
            if (archiveAdapter.getSelectedPosition() != -1) {
                ArchiveFileIconType iconType = archiveAdapter.getItemType(archiveAdapter.getSelectedPosition());
                if (iconType == ArchiveFileIconType.Match || iconType == ArchiveFileIconType.MatchRaw) {
                    inflater.inflate(C0252R.menu.archive_match_context_menu, menu);
                } else if (iconType == ArchiveFileIconType.Team) {
                    inflater.inflate(C0252R.menu.archive_team_context_menu, menu);
                }
                if (iconType == ArchiveFileIconType.Match) {
                    menu.findItem(C0252R.id.miConvertToZip).setVisible(false);
                    menu.findItem(C0252R.id.miListRawMatches).setVisible(false);
                    menu.findItem(C0252R.id.miShareSendRawMatch).setVisible(false);
                    menu.findItem(C0252R.id.miDeleteRaw).setVisible(false);
                } else if (iconType == ArchiveFileIconType.MatchRaw) {
                    menu.findItem(C0252R.id.miListMatches).setVisible(false);
                    menu.findItem(C0252R.id.miShareSendMatch).setVisible(false);
                    menu.findItem(C0252R.id.miDelete).setVisible(false);
                }
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.miConvertToZip:
                Intent intent = new Intent(getApplicationContext(), ConvertToZip.class);
                intent.putExtra("StoragePath", this._currentStoragePath);
                intent.putExtra("FileName", this._lastSelectedFileName);
                startActivity(intent);
                return true;
            case C0252R.id.miListRawMatches:
                Utility.showNotSupportedAlert(this);
                return true;
            case C0252R.id.miListMatches:
                Utility.showNotSupportedAlert(this);
                return true;
            case C0252R.id.miShareSendMatch:
                shareSendMatchFile(".bmz");
                return true;
            case C0252R.id.miShareSendRawMatch:
                shareSendMatchFile(".bcs");
                return true;
            case C0252R.id.miDelete:
                deleteArchiveFile(".bmz");
                return true;
            case C0252R.id.miDeleteRaw:
                deleteArchiveFile(".bcs");
                return true;
            case C0252R.id.miClose:
                return true;
            case C0252R.id.miListTeams:
                Utility.showNotSupportedAlert(this);
                return true;
            case C0252R.id.miShareSendTeam:
                shareSendMatchFile(".btz");
                return true;
            case C0252R.id.miDeleteTeam:
                deleteArchiveFile(".btz");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void shareSendMatchFile(String extension) {
        Intent shareIntent = new Intent("android.intent.action.SEND");
        File f = new File(this._currentStoragePath + File.separator + this._lastSelectedFileName + extension);
        if (f.exists()) {
            shareIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
            shareIntent.setType("application/zip");
            startActivity(Intent.createChooser(shareIntent, "Share/Send Archive"));
            return;
        }
        Toast.makeText(getApplicationContext(), "Oops! File doesn't exist!", 0).show();
    }

    public void deleteArchiveFile(String extension) {
        final String fileName = this._currentStoragePath + File.separator + this._lastSelectedFileName + extension;
        if (new File(fileName).exists()) {
            new Builder(this).setIcon(17301543).setTitle(C0252R.string.deleteText).setMessage(C0252R.string.areYouSureText).setPositiveButton(C0252R.string.yesText, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Utility.DeleteFileIfExists(fileName);
                    ArchiveExplorer.this.populateFiles();
                }
            }).setNegativeButton(C0252R.string.noText, null).show();
        } else {
            Toast.makeText(getApplicationContext(), "Oops! File doesn't exist!", 0).show();
        }
    }

    public void populateFiles() {
        this.fileList = new ArrayList();
        this.fileList.add(new dtoArchiveFile(ArchiveFileIconType.MatchAdd, "Archive Match"));
        this.fileList.add(new dtoArchiveFile(ArchiveFileIconType.TeamAdd, "Archive Team"));
        ((TextView) findViewById(C0252R.id.textStorage)).setText(this._currentStoragePath);
        File[] file = new File(this._currentStoragePath).listFiles();
        int i = 0;
        while (file != null && i < file.length) {
            if (file[i].getName().toLowerCase(Locale.US).endsWith(".bmz")) {
                this.fileList.add(new dtoArchiveFile(ArchiveFileIconType.Match, file[i].getName().split(".bmz")[0]));
            }
            if (file[i].getName().toLowerCase(Locale.US).endsWith(".bcs")) {
                this.fileList.add(new dtoArchiveFile(ArchiveFileIconType.MatchRaw, file[i].getName().split(".bcs")[0]));
            }
            if (file[i].getName().toLowerCase(Locale.US).endsWith(".btz")) {
                this.fileList.add(new dtoArchiveFile(ArchiveFileIconType.Team, file[i].getName().split(".btz")[0]));
            }
            i++;
        }
        GridView gridview = (GridView) findViewById(C0252R.id.archiveGrid);
        this.fileAdapter = new ArchiveFileAdapter(this, this.fileList);
        gridview.setAdapter(this.fileAdapter);
        gridview.setOnItemLongClickListener(null);
        gridview.setOnItemClickListener(new C02052());
    }
}
