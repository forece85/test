package com.ganapathy.cricscorer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;

public class RunsPerOverGraph {
    Activity activity;
    private float barxmax;
    private float barymax;
    private ChartAttributes chartAttributes;
    protected float chart_height;
    protected float chart_width;
    protected int display_height;
    protected int display_width;
    private float maxovers;
    private float maxruns;
    protected float offset_bottom;
    protected float offset_left;
    protected float offset_right;
    protected float offset_top;
    private String[] overlabels;
    protected Paint paint;
    private PathAttributes pathAttributes;
    private float[] runs;
    private float[] wickets;
    protected float xbl;
    protected float xbr;
    protected float xtl;
    protected float xtr;
    private float xunitvalue;
    protected float ybl;
    protected float ybr;
    protected float ytl;
    protected float ytr;
    private float yunitvalue;

    public RunsPerOverGraph(Activity activity, float[] runs, String[] overlabels, float[] wickets, ChartAttributes chartAttributes, PathAttributes pathAttributes) {
        this.activity = activity;
        if (Utility.isOrientationPortrait(activity)) {
            this.display_width = activity.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            this.display_height = activity.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        } else {
            this.display_width = activity.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            this.display_height = activity.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        }
        this.offset_top = ((float) this.display_height) * 0.15f;
        this.offset_bottom = ((float) this.display_height) * 0.15f;
        this.offset_left = ((float) this.display_width) * 0.15f;
        this.offset_right = ((float) this.display_width) * 0.02f;
        this.chart_width = (((float) this.display_width) - this.offset_right) - this.offset_left;
        this.chart_height = (((float) this.display_height) - this.offset_top) - this.offset_bottom;
        this.xtl = this.offset_left;
        this.ytl = this.offset_top;
        this.xtr = ((float) this.display_width) - this.offset_right;
        this.ytr = this.offset_top;
        this.xbl = this.offset_left;
        this.ybl = ((float) this.display_height) - this.offset_bottom;
        this.xbr = ((float) this.display_width) - this.offset_right;
        this.ybr = ((float) this.display_height) - this.offset_bottom;
        if (runs == null) {
            runs = new float[0];
        } else {
            this.runs = runs;
        }
        this.chartAttributes = chartAttributes;
        this.pathAttributes = pathAttributes;
        if (overlabels == null) {
            this.overlabels = new String[0];
        } else {
            this.overlabels = overlabels;
        }
        if (wickets == null) {
            this.wickets = new float[0];
        } else {
            this.wickets = wickets;
        }
        this.maxovers = (float) overlabels.length;
        this.maxruns = getRunsMax();
        this.barxmax = this.xbr - this.xbl;
        this.barymax = this.ybl - this.ytl;
        this.xunitvalue = this.barxmax / this.maxovers;
        this.yunitvalue = this.barymax / (this.maxruns + 3.0f);
        this.paint = new Paint(65);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
    }

    protected Bitmap createGraph() {
        Bitmap bitmap = Bitmap.createBitmap(this.display_width, this.display_height, Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor(this.chartAttributes.getBackgroundColor()));
        drawAxis(canvas);
        drawAxisNames(canvas);
        drawAxisPoints(canvas);
        drawGrid(canvas);
        drawPoints(canvas);
        return bitmap;
    }

    private void drawAxis(Canvas canvas) {
        this.paint.setColor(Color.parseColor(this.chartAttributes.getAxisColor()));
        this.paint.setStrokeWidth(2.0f);
        this.paint.setStyle(Style.STROKE);
        canvas.drawRect(this.xtl, this.ytl, this.xbr, this.ybr, this.paint);
    }

    private void drawAxisNames(Canvas canvas) {
        this.paint.setColor(Color.parseColor(this.chartAttributes.getAxisNameColor()));
        this.paint.setStyle(Style.FILL_AND_STROKE);
        this.paint.setStrokeWidth(0.0f);
        this.paint.setTextSize(this.activity.getApplicationContext().getResources().getDisplayMetrics().scaledDensity * 16.0f);
        Path path = new Path();
        path.addRect(new RectF(this.xbl - (this.offset_left * 0.75f), this.ybl, this.xtl - (this.offset_left * 0.75f), this.ytl), Direction.CCW);
        canvas.drawTextOnPath(this.chartAttributes.Y_unit_name, path, (((((float) this.display_height) - this.offset_top) - this.offset_bottom) - this.paint.measureText(this.chartAttributes.Y_unit_name)) * 0.5f, 0.0f, this.paint);
        path = new Path();
        path.addRect(new RectF(this.xbl, this.ybl + (this.offset_bottom * 0.6f), this.xbr, this.ybr + (this.offset_bottom * 0.6f)), Direction.CW);
        canvas.drawTextOnPath(this.chartAttributes.X_unit_name, path, (((((float) this.display_width) - this.offset_left) - this.offset_right) - this.paint.measureText(this.chartAttributes.X_unit_name)) * 0.5f, 0.0f, this.paint);
        path = new Path();
        path.addRect(new RectF(0.0f, this.offset_top * 0.6f, (float) this.display_width, this.offset_top * 0.6f), Direction.CW);
        canvas.drawTextOnPath(this.chartAttributes.Chart_title, path, (((float) this.display_width) * 0.5f) - (this.paint.measureText(this.chartAttributes.Chart_title) * 0.5f), 0.0f, this.paint);
        path = new Path();
        path.addRect(new RectF(0.0f, this.offset_top * 0.3f, (float) this.display_width, this.offset_top * 0.3f), Direction.CW);
        canvas.drawTextOnPath(this.chartAttributes.Chart_title2, path, (((float) this.display_width) * 0.5f) - (this.paint.measureText(this.chartAttributes.Chart_title2) * 0.5f), 0.0f, this.paint);
    }

    private void drawAxisPoints(Canvas canvas) {
        this.paint.setColor(Color.parseColor(this.chartAttributes.axisNameColor));
        this.paint.setStrokeWidth(1.0f);
        int x_increment = 1;
        if (this.overlabels.length > 50) {
            x_increment = 10;
        } else if (this.overlabels.length > 40) {
            x_increment = 5;
        } else if (this.overlabels.length > 24) {
            x_increment = 3;
        } else if (this.overlabels.length > 12) {
            x_increment = 2;
        }
        int i = 1;
        for (String overlabel : this.overlabels) {
            if (i % x_increment == 0) {
                canvas.drawText(overlabel, ((this.xbl + (((float) i) * this.xunitvalue)) - (this.xunitvalue / 2.0f)) - (this.paint.measureText(overlabel) / 2.0f), this.ybl + (this.offset_bottom * 0.3f), this.paint);
            }
            i++;
        }
        int y_increment = 1;
        if (this.maxruns >= 32.0f) {
            y_increment = 5;
        } else if (this.maxruns >= 26.0f) {
            y_increment = 4;
        } else if (this.maxruns >= 20.0f) {
            y_increment = 3;
        } else if (this.maxruns >= 12.0f) {
            y_increment = 2;
        }
        for (int j = 0; ((float) j) <= this.maxruns + 3.0f; j += y_increment) {
            canvas.drawText("" + j, this.xbl - (this.offset_left * 0.4f), this.ybl - (((float) j) * this.yunitvalue), this.paint);
        }
    }

    private void drawGrid(Canvas canvas) {
        this.paint.setColor(Color.parseColor(this.chartAttributes.getGridColor()));
        this.paint.setStrokeWidth(1.0f);
        int y_increment = 1;
        if (this.maxruns >= 32.0f) {
            y_increment = 5;
        } else if (this.maxruns >= 26.0f) {
            y_increment = 4;
        } else if (this.maxruns >= 20.0f) {
            y_increment = 3;
        } else if (this.maxruns >= 12.0f) {
            y_increment = 2;
        }
        for (int j = 0; ((float) j) <= this.maxruns + 3.0f; j += y_increment) {
            canvas.drawLine(this.xbl, this.ybl - (((float) j) * this.yunitvalue), this.xbr, this.ybr - (((float) j) * this.yunitvalue), this.paint);
        }
    }

    private void drawPoints(Canvas canvas) {
        this.paint.setStrokeWidth(2.0f);
        int i = 0;
        for (float value : this.runs) {
            this.paint.setColor(Color.parseColor("#FFFFFFFF"));
            canvas.drawRect(1.5f + (this.xbl + (((float) i) * this.xunitvalue)), this.ybl, (this.xbl + (((float) (i + 1)) * this.xunitvalue)) - 1.5f, this.ybl - (this.yunitvalue * value), this.paint);
            this.paint.setColor(Color.parseColor(this.pathAttributes.getPathColor()));
            canvas.drawRect(3.0f + (this.xbl + (((float) i) * this.xunitvalue)), this.ybl - 1.5f, (this.xbl + (((float) (i + 1)) * this.xunitvalue)) - 3.0f, 1.5f + (this.ybl - (this.yunitvalue * value)), this.paint);
            i++;
        }
        i = 1;
        float wicketRadius = this.xunitvalue / 4.0f;
        for (float value2 : this.wickets) {
            for (int j = 0; ((float) j) < value2; j++) {
                this.paint.setColor(-1);
                canvas.drawCircle((this.xbl + (((float) i) * this.xunitvalue)) - (this.xunitvalue / 2.0f), ((this.ybl - (this.runs[i - 1] * this.yunitvalue)) - ((((float) j) * wicketRadius) * 2.0f)) - wicketRadius, wicketRadius, this.paint);
                this.paint.setColor(Color.parseColor(this.pathAttributes.getPointColor()));
                canvas.drawCircle((this.xbl + (((float) i) * this.xunitvalue)) - (this.xunitvalue / 2.0f), ((this.ybl - (this.runs[i - 1] * this.yunitvalue)) - ((((float) j) * wicketRadius) * 2.0f)) - wicketRadius, wicketRadius - (0.2f * wicketRadius), this.paint);
            }
            i++;
        }
    }

    private float getRunsMax() {
        float largest = -2.14748365E9f;
        for (int i = 0; i < this.runs.length; i++) {
            if (this.runs[i] > largest) {
                largest = this.runs[i];
            }
        }
        return largest;
    }
}
