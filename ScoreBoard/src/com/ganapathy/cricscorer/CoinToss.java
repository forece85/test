package com.ganapathy.cricscorer;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Calendar;

public class CoinToss extends BaseActivity implements AnimationListener {
    protected int count = 1;
    private ImageView image1;
    private ImageView image2;
    Flip3dAnimation rotation;
    Flip3dAnimation rotation1;

    class C02131 implements OnClickListener {
        C02131() {
        }

        public void onClick(View view) {
            CoinToss.this.applyRotation();
        }
    }

    class C02142 implements OnClickListener {
        C02142() {
        }

        public void onClick(View view) {
            CoinToss.this.applyRotation();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_coin_toss);
            this.image1 = (ImageView) findViewById(C0252R.id.ImageView01);
            this.image2 = (ImageView) findViewById(C0252R.id.ImageView02);
            this.image1.setVisibility(0);
            this.image2.setVisibility(0);
            this.image1.setOnClickListener(new C02131());
            this.image2.setOnClickListener(new C02142());
        } catch (Exception e) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void showResult() {
        float centerX = ((float) this.image1.getWidth()) / 2.0f;
        float centerY = ((float) this.image1.getHeight()) / 2.0f;
        if (Math.round(Math.ceil(Math.random() * 123.45d) * (((double) Calendar.getInstance().getTimeInMillis()) / 7.0d)) % 2 == 0) {
            this.image1.setVisibility(8);
            this.image2.setVisibility(0);
            this.rotation1 = new Flip3dAnimation(0.0f, 90.0f, centerX, centerY);
            this.rotation1.setDuration(100);
            this.rotation1.setInterpolator(new LinearInterpolator());
            this.image1.startAnimation(this.rotation1);
            Toast.makeText(getApplicationContext(), "TAILS", 1).show();
            return;
        }
        this.image1.setVisibility(0);
        this.image2.setVisibility(8);
        this.rotation1 = new Flip3dAnimation(0.0f, 90.0f, centerX, centerY);
        this.rotation1.setDuration(100);
        this.rotation1.setInterpolator(new LinearInterpolator());
        this.image2.startAnimation(this.rotation1);
        Toast.makeText(getApplicationContext(), "HEADS", 1).show();
    }

    private void applyRotation() {
        float centerX = ((float) this.image1.getWidth()) / 2.0f;
        float centerY = ((float) this.image1.getHeight()) / 2.0f;
        this.rotation = new Flip3dAnimation(-180.0f, 0.0f, centerX, centerY);
        this.rotation.setDuration(200);
        this.rotation.setRepeatCount(15);
        this.rotation.setRepeatMode(-1);
        this.rotation.setFillAfter(true);
        this.rotation.setInterpolator(new LinearInterpolator());
        this.rotation.setAnimationListener(this);
        this.image1.startAnimation(this.rotation);
        this.rotation1 = new Flip3dAnimation(-90.0f, 0.0f, centerX, centerY);
        this.rotation1.setDuration(200);
        this.rotation1.setRepeatCount(15);
        this.rotation1.setFillAfter(true);
        this.rotation1.setInterpolator(new LinearInterpolator());
        this.image2.startAnimation(this.rotation1);
    }

    public void onAnimationStart(Animation animation) {
    }

    public void onAnimationEnd(Animation animation) {
        showResult();
    }

    public void onAnimationRepeat(Animation animation) {
    }
}
