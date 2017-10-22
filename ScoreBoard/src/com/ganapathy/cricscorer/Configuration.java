package com.ganapathy.cricscorer;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class Configuration extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_configuration);
            initializeControls();
        } catch (Exception e) {
            finish();
        }
    }

    protected void initializeControls() {
        boolean z;
        boolean z2 = true;
        dtoConfiguration config = new dtoConfiguration();
        DatabaseHandler db = new DatabaseHandler(this);
        config = db.getConfiguration();
        db.close();
        String mailIds = config.getMailIds();
        if (mailIds != null) {
            ((EditText) findViewById(C0252R.id.editMailIds)).setText("" + mailIds);
        }
        int themeId = config.getThemeId();
        if (themeId == 16974120) {
            ((RadioButton) findViewById(C0252R.id.radio_Default)).setChecked(true);
        } else if (themeId == 16973836 || themeId == 16973934) {
            ((RadioButton) findViewById(C0252R.id.radio_Light)).setChecked(true);
        } else {
            ((RadioButton) findViewById(C0252R.id.radio_Dark)).setChecked(true);
        }
        CheckBox checkBox = (CheckBox) findViewById(C0252R.id.excludeToBowlerStats);
        if (config.getWdNbBowlerStats() == 1) {
            z = true;
        } else {
            z = false;
        }
        checkBox.setChecked(z);
        checkBox = (CheckBox) findViewById(C0252R.id.inlineBallPositionsCheckBox);
        if (config.getInlineWwBs() != 1) {
            z2 = false;
        }
        checkBox.setChecked(z2);
    }

    @SuppressLint({"InlinedApi"})
    public void onSave(View view) {
        int themeId;
        int value;
        dtoConfiguration config = new dtoConfiguration();
        config.setMailIds(((EditText) findViewById(C0252R.id.editMailIds)).getText().toString());
        if (((RadioButton) findViewById(C0252R.id.radio_Default)).isChecked()) {
            if (VERSION.SDK_INT < 14) {
                themeId = 16973832;
            } else {
                themeId = 16974120;
            }
        } else if (((RadioButton) findViewById(C0252R.id.radio_Light)).isChecked()) {
            if (VERSION.SDK_INT < 11) {
                themeId = 16973836;
            } else {
                themeId = 16973934;
            }
        } else if (VERSION.SDK_INT < 11) {
            themeId = 16973832;
        } else {
            themeId = 16973931;
        }
        config.setThemeId(themeId);
        if (((CheckBox) findViewById(C0252R.id.excludeToBowlerStats)).isChecked()) {
            value = 1;
        } else {
            value = 0;
        }
        config.setWdNbBowlerStats(value);
        if (((CheckBox) findViewById(C0252R.id.inlineBallPositionsCheckBox)).isChecked()) {
            value = 1;
        } else {
            value = 0;
        }
        config.setInlineWwBs(value);
        DatabaseHandler db = new DatabaseHandler(this);
        db.addConfiguration(config);
        db.close();
        finish();
    }

    public void onBack(View view) {
        finish();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case C0252R.id.radio_Default:
                if (checked) {
                    return;
                }
                break;
            case C0252R.id.radio_Light:
                break;
            case C0252R.id.radio_Dark:
                break;
            default:
                return;
        }
        if (checked) {
            return;
        }
        if (!checked) {
        }
    }
}
