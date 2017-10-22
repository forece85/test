package com.ganapathy.cricscorer;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    protected long lastClickTime = 0;

    public boolean hideWindowTitle() {
        return false;
    }

    public boolean makeFullScreen() {
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utility.getTheme(getApplicationContext()));
        if (hideWindowTitle()) {
            requestWindowFeature(1);
        }
        if (makeFullScreen()) {
            getWindow().setFlags(1024, 1024);
        }
        if (VERSION.SDK_INT > 13 && !hideWindowTitle()) {
            setActionBar();
        }
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @TargetApi(11)
    private void setActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return true;
    }

    public boolean isFrequentClick() {
        if (SystemClock.elapsedRealtime() - this.lastClickTime < 500) {
            return true;
        }
        this.lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }
}
