package com.ganapathy.cricscorer;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AdditionalSettings extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_additional_settings);
            initializeControls();
        } catch (Exception e) {
            finish();
        }
    }

    protected void initializeControls() {
        boolean z = true;
        DatabaseHandler db = new DatabaseHandler(this);
        dtoAdditionalSettings addlSettings = db.getDefaultAdditionalSettings();
        db.close();
        if (addlSettings != null) {
            boolean z2;
            ((ToggleButton) findViewById(C0252R.id.Wide)).setChecked(addlSettings.getWide() == 1);
            ToggleButton toggleButton = (ToggleButton) findViewById(C0252R.id.WideReball);
            if (addlSettings.getWideReball() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.Noball);
            if (addlSettings.getNoball() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.NoballReball);
            if (addlSettings.getNoballReball() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.Legbyes);
            if (addlSettings.getLegbyes() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.Byes);
            if (addlSettings.getByes() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.Lbw);
            if (addlSettings.getLbw() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.WagonWheel);
            if (addlSettings.getWagonWheel() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.BallSpot);
            if (addlSettings.getBallSpot() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.PresetTeams);
            if (addlSettings.getPresetTeams() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.RestrictBPO);
            if (addlSettings.getRestrictBpo() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.WagonWheelForDotBall);
            if (addlSettings.getWwForDotBall() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            toggleButton = (ToggleButton) findViewById(C0252R.id.WagonWheelForWicket);
            if (addlSettings.getWwForWicket() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            toggleButton.setChecked(z2);
            EditText editText = (EditText) findViewById(C0252R.id.maxBallsPerOverEdit);
            if (addlSettings.getRestrictBpo() != 1) {
                z = false;
            }
            editText.setEnabled(z);
            ((EditText) findViewById(C0252R.id.maxBallsPerOverEdit)).setText("" + addlSettings.getMaxBpo());
            ((EditText) findViewById(C0252R.id.WideRun)).setText("" + addlSettings.getWideRun());
            ((EditText) findViewById(C0252R.id.NoballRun)).setText("" + addlSettings.getNoballRun());
            ((EditText) findViewById(C0252R.id.BallsPerOver)).setText("" + addlSettings.getBpo());
        }
        ((LinearLayout) findViewById(C0252R.id.focusLayout)).requestFocus();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onBack(View view) {
        finish();
    }

    public void OnRestrictBallsClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            ((EditText) findViewById(C0252R.id.maxBallsPerOverEdit)).setEnabled(true);
            ((EditText) findViewById(C0252R.id.maxBallsPerOverEdit)).requestFocus();
            return;
        }
        ((EditText) findViewById(C0252R.id.maxBallsPerOverEdit)).setEnabled(false);
        ((EditText) findViewById(C0252R.id.maxBallsPerOverEdit)).setText(getResources().getText(C0252R.string.eightBallsText));
    }

    public void onSave(View view) {
        int i;
        dtoAdditionalSettings addlSettings = new dtoAdditionalSettings();
        if (((ToggleButton) findViewById(C0252R.id.Wide)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setWide(i);
        if (((ToggleButton) findViewById(C0252R.id.WideReball)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setWideReball(i);
        if (((ToggleButton) findViewById(C0252R.id.Noball)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setNoball(i);
        if (((ToggleButton) findViewById(C0252R.id.NoballReball)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setNoballReball(i);
        if (((ToggleButton) findViewById(C0252R.id.Legbyes)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setLegbyes(i);
        if (((ToggleButton) findViewById(C0252R.id.Byes)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setByes(i);
        if (((ToggleButton) findViewById(C0252R.id.Lbw)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setLbw(i);
        if (((ToggleButton) findViewById(C0252R.id.WagonWheel)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setWagonWheel(i);
        if (((ToggleButton) findViewById(C0252R.id.BallSpot)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setBallSpot(i);
        if (((ToggleButton) findViewById(C0252R.id.PresetTeams)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setPresetTeams(i);
        if (((ToggleButton) findViewById(C0252R.id.RestrictBPO)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setRestrictBpo(i);
        if (((ToggleButton) findViewById(C0252R.id.WagonWheelForDotBall)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setWwForDotBall(i);
        if (((ToggleButton) findViewById(C0252R.id.WagonWheelForWicket)).isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        addlSettings.setWwForWicket(i);
        int maxBpo = Integer.parseInt("0" + ((EditText) findViewById(C0252R.id.maxBallsPerOverEdit)).getText());
        if (maxBpo <= 0) {
            maxBpo = 8;
        }
        addlSettings.setMaxBpo(maxBpo);
        addlSettings.setWideRun(Integer.parseInt("0" + ((EditText) findViewById(C0252R.id.WideRun)).getText()));
        addlSettings.setNoballRun(Integer.parseInt("0" + ((EditText) findViewById(C0252R.id.NoballRun)).getText()));
        int tempBpo = Integer.parseInt("0" + ((EditText) findViewById(C0252R.id.BallsPerOver)).getText());
        if (tempBpo < 1 || tempBpo > 20) {
            addlSettings.setBpo(6);
        } else {
            addlSettings.setBpo(tempBpo);
        }
        DatabaseHandler db = new DatabaseHandler(this);
        db.saveDefaultAdditionalSettings(addlSettings);
        db.close();
        ((CricScorerApp) getApplication()).currentMatch.setAdditionalSettings(addlSettings);
        Toast.makeText(this, "Additional Settings Saved", 0).show();
        finish();
    }
}
