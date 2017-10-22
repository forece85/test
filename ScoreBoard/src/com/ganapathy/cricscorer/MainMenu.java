package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

public class MainMenu extends BaseActivity {
    private static final int REQUEST_WRITE_STORAGE = 112;

    class C02361 implements OnClickListener {
        C02361() {
        }

        public void onClick(DialogInterface dialog, int id) {
            MainMenu.this.makeRequest();
        }
    }

    class C02372 implements OnClickListener {
        C02372() {
        }

        public void onClick(DialogInterface dialog, int which) {
            MainMenu.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://market.android.com/details?id=com.ganapathy.best.cricket.scorer")));
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0252R.layout.activity_main_menu);
        checkPermission();
    }

    public void onResume() {
        super.onResume();
    }

    protected void checkPermission() {
        if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                Builder builder = new Builder(this);
                builder.setMessage("Access to storage is mandatory to create reports.").setTitle("Permission required");
                builder.setPositiveButton("OK", new C02361());
                builder.create().show();
                return;
            }
            makeRequest();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_WRITE_STORAGE);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE /*112*/:
                if (grantResults.length == 0 || grantResults[0] != 0) {
                    Toast.makeText(getApplicationContext(), "Permission has been denied by user. Please allow BCS to use storage in App Permissions!", 1).show();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        finish();
    }

    public boolean hideWindowTitle() {
        return true;
    }

    public void openMatch(View view) {
        if (!isFrequentClick()) {
            startActivity(new Intent(this, OpenMatch.class));
        }
    }

    public void onSetDefaultTeams(View view) {
        if (!isFrequentClick()) {
            startActivity(new Intent(this, TeamList.class));
        }
    }

    public void onConfiguration(View view) {
        if (!isFrequentClick()) {
            startActivity(new Intent(this, Configuration.class));
        }
    }

    public void onInstructions(View view) {
        if (!isFrequentClick()) {
            startActivity(new Intent(this, FAQ.class));
        }
    }

    public void onCredits(View view) {
        if (!isFrequentClick()) {
            startActivity(new Intent(this, Instructions.class));
        }
    }

    public void onOverallBatsStats(View view) {
        if (!isFrequentClick()) {
            Intent intent = new Intent(this, OverallBatsmanStats.class);
            intent.putExtra("MatchID", "");
            startActivity(intent);
        }
    }

    public void onOverallBowlerStats(View view) {
        if (!isFrequentClick()) {
            Intent intent = new Intent(this, OverallBowlerStats.class);
            intent.putExtra("MatchID", "");
            startActivity(intent);
        }
    }

    public void onArchive(View view) {
        if (!isFrequentClick()) {
            startActivity(new Intent(this, ArchiveExplorer.class));
        }
    }

    public void createNewMatch(View view) {
        if (!isFrequentClick()) {
            if (matchCountExceeds()) {
                new Builder(this).setTitle("MAX limit reached!").setMessage("You have already created 3 matches and reached FREE max limit. Please buy Best Cricket Scorer FULL or reinstall this FREE application to create new match!").setPositiveButton(C0252R.string.buyText, new C02372()).setNegativeButton(C0252R.string.laterText, null).show();
            } else {
                startActivity(new Intent(this, CreateMatch.class));
            }
        }
    }

    private boolean matchCountExceeds() {
        DatabaseHandler db = new DatabaseHandler(this);
        int matchCount = db.getMatchCount();
        db.close();
        return matchCount >= 3;
    }
}
