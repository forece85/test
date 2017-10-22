package com.ganapathy.cricscorer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class BallSpot extends BaseActivity implements OnTouchListener {
    private ImageView ballPointer;
    private int bsx_cord;
    private int bsy_cord;
    private ImageView imageView;

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_ball_spot);
            this.imageView = (ImageView) findViewById(C0252R.id.imageBallSpot);
            this.imageView.setOnTouchListener(this);
            this.ballPointer = (ImageView) findViewById(C0252R.id.imageBallSpotPointer);
        } catch (Exception e) {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onTouch(View v, MotionEvent event) {
        LayoutParams params = (LayoutParams) this.ballPointer.getLayoutParams();
        switch (event.getAction()) {
            case 0:
            case 2:
                int imageLeft;
                int imageTop;
                int imageWidth;
                int imageHeight;
                this.bsx_cord = (int) event.getX();
                this.bsy_cord = (int) event.getY();
                int minSize = this.imageView.getWidth() < this.imageView.getHeight() ? this.imageView.getWidth() : this.imageView.getHeight();
                int maxSize = this.imageView.getWidth() > this.imageView.getHeight() ? this.imageView.getWidth() : this.imageView.getHeight();
                if (this.imageView.getWidth() > this.imageView.getHeight()) {
                    imageLeft = (maxSize / 2) - (minSize / 2);
                    imageTop = 0;
                    imageWidth = minSize;
                    imageHeight = minSize;
                } else {
                    imageLeft = 0;
                    imageTop = (maxSize / 2) - (minSize / 2);
                    imageWidth = minSize;
                    imageHeight = minSize;
                }
                if (this.bsx_cord < imageLeft) {
                    this.bsx_cord = imageLeft;
                }
                if (this.bsy_cord < imageTop) {
                    this.bsy_cord = imageTop;
                }
                if (this.bsx_cord > imageWidth + imageLeft) {
                    this.bsx_cord = imageWidth + imageLeft;
                }
                if (this.bsy_cord > imageHeight + imageTop) {
                    this.bsy_cord = imageHeight + imageTop;
                }
                params.leftMargin = (this.bsx_cord + this.imageView.getLeft()) - 8;
                params.topMargin = (this.bsy_cord + this.imageView.getTop()) - 8;
                this.ballPointer.setLayoutParams(new LayoutParams(new MarginLayoutParams(params)));
                this.bsx_cord = (int) Math.round(((960.0d / ((double) imageWidth)) * 1.0d) * ((double) (this.bsx_cord - imageLeft)));
                this.bsy_cord = (int) Math.round(((960.0d / ((double) imageHeight)) * 1.0d) * ((double) (this.bsy_cord - imageTop)));
                break;
            case 1:
                v.performClick();
                break;
        }
        return true;
    }

    public void onBackPressed() {
        onOK(null);
    }

    public void onOK(View view) {
        this.imageView.destroyDrawingCache();
        if (this.bsx_cord == 0 && this.bsy_cord == 0) {
            this.bsx_cord = 480;
            this.bsy_cord = 480;
        }
        Intent resultData = new Intent();
        resultData.putExtra("bsxCord", "" + this.bsx_cord);
        resultData.putExtra("bsyCord", "" + this.bsy_cord);
        setResult(-1, resultData);
        finish();
        overridePendingTransition(0, 0);
    }
}
