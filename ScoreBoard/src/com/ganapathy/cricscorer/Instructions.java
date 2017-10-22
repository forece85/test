package com.ganapathy.cricscorer;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Instructions extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_instructions);
            String versionName = "";
            try {
                versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            ((TextView) findViewById(C0252R.id.textInstructions)).setText("Best Cricket Scorer v" + versionName + " FREE Edition \n\nHow to use: \n--------------\n1. Create match -> Choose team and set additional settings. \n2. Choose Striker, Non-Striker and Bowler. \n3. Select appropriate action among Strike, Wide, Noball, etc., \n4. You can select Wide+Wicket, Noball+Byes, Noball+LegByes. \n5. Click on the Runs scored on that ball. Do not include runs for Wide or Noball. Those will be automatically added. \n6. Switch Bats will switch Striker and Non-Striker. \n7. Click Stats to view match statistics at any time. \n8. Send scorecard via Whatsapp, Bluetooth, Gmail etc., \n9. Features like Wagon wheel, Ball Spot etc., \n10. Runs per over, Run Rate compare graphs \nFor detailed instructions, pls visit below blogspot \n\n\nDesigned and Developed by: \n-----------------------------------------\nGanapathy Subramaniam. B\nAssisted by: \nFamily and Friends - who lend their time for this project.\n\nSPECIAL THANKS TO\n------------------------- \nCustomers who provided their valuable suggestions to improve BCS.\n\nHelp & Support \n------------------------- \nFor help and support visit: \nhttp://bestcricketscorer.blogspot.in/\nhttp://www.facebook.com/BestCricketScorer\nFor queries mail to:\nganapathy.android@gmail.com \n");
            setTitle(getText(C0252R.string.instructionsText));
        } catch (Exception e2) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void doRateApp(View view) {
        Intent rateAppIntent = getAppIntent();
        if (rateAppIntent != null) {
            startActivity(rateAppIntent);
        } else {
            Toast.makeText(this, "Couldn't launch the market, please open Market place and Rate the application. Thanks for your support.", 0).show();
        }
    }

    public Intent getAppIntent() {
        Intent i = new Intent("android.intent.action.VIEW");
        i.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
        if (isIntentAvailable(i)) {
            return i;
        }
        i.setData(Uri.parse("amzn://apps/android?p=" + getApplicationContext().getPackageName()));
        return !isIntentAvailable(i) ? null : i;
    }

    public boolean isIntentAvailable(Intent intent) {
        return getApplicationContext().getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }
}
