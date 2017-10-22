package com.ganapathy.cricscorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class BallPosition extends BaseActivity implements OnTouchListener {
    ImageView ballPointer;
    int centerX;
    int centerY;
    ImageView imageView;
    int lineColor = -1;
    int x_cord;
    int y_cord;

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_ball_position);
            this.lineColor = getLineColor(getIntent().getExtras().getInt("Run"));
            this.imageView = (ImageView) findViewById(C0252R.id.imageWagonWheel);
            this.imageView.setOnTouchListener(this);
            this.ballPointer = (ImageView) findViewById(C0252R.id.imageBallPointer);
        } catch (Exception e) {
            finish();
        }
    }

    private int getLineColor(int run) {
        switch (run) {
            case 1:
                return -1;
            case 2:
                return -65281;
            case 3:
                return -16776961;
            case 4:
                return InputDeviceCompat.SOURCE_ANY;
            case 6:
                return SupportMenu.CATEGORY_MASK;
            default:
                return -16711681;
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
                this.x_cord = (int) event.getX();
                this.y_cord = (int) event.getY();
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
                if (this.x_cord < imageLeft) {
                    this.x_cord = imageLeft;
                }
                if (this.y_cord < imageTop) {
                    this.y_cord = imageTop;
                }
                if (this.x_cord > imageWidth + imageLeft) {
                    this.x_cord = imageWidth + imageLeft;
                }
                if (this.y_cord > imageHeight + imageTop) {
                    this.y_cord = imageHeight + imageTop;
                }
                this.centerX = ((int) Math.round((((double) imageWidth) * 1.0d) / 2.0d)) + imageLeft;
                this.centerY = ((int) Math.round((((double) imageHeight) * 1.0d) / 2.0d)) + imageTop;
                params.leftMargin = (this.x_cord + this.imageView.getLeft()) - 8;
                params.topMargin = (this.y_cord + this.imageView.getTop()) - 8;
                this.ballPointer.setLayoutParams(new LayoutParams(new MarginLayoutParams(params)));
                this.x_cord = (int) Math.round(((960.0d / ((double) imageWidth)) * 1.0d) * ((double) (this.x_cord - imageLeft)));
                this.y_cord = (int) Math.round(((960.0d / ((double) imageHeight)) * 1.0d) * ((double) (this.y_cord - imageTop)));
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
        if (this.x_cord == 0 && this.y_cord == 0) {
            this.x_cord = 480;
            this.y_cord = 480;
        }
        Intent resultData = new Intent();
        resultData.putExtra("xCord", "" + this.x_cord);
        resultData.putExtra("yCord", "" + this.y_cord);
        setResult(-1, resultData);
        finish();
        overridePendingTransition(0, 0);
    }
}
