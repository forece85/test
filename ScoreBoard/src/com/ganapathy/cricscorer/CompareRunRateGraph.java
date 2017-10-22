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
import java.util.ArrayList;
import java.util.Iterator;

public class CompareRunRateGraph {
    Activity activity;
    private ChartAttributes chartAttributes;
    protected float chart_height;
    protected float chart_width;
    protected int display_height;
    protected int display_width;
    private float no_of_x_negetive_unit;
    private float no_of_x_positive_unit;
    private float no_of_y_negetive_unit;
    private float no_of_y_positive_unit;
    protected float offset_bottom;
    protected float offset_left;
    protected float offset_right;
    protected float offset_top;
    protected Paint paint;
    private ArrayList<PathOnChart> paths;
    private float x_translate_factor;
    private float x_unit_length;
    protected float xbl;
    protected float xbr;
    protected float xtl;
    protected float xtr;
    private float y_translate_factor;
    private float y_unit_length;
    protected float ybl;
    protected float ybr;
    protected float ytl;
    protected float ytr;

    public CompareRunRateGraph(Activity activity, ArrayList<PathOnChart> paths, ChartAttributes chartAttributes) {
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
        this.chartAttributes = chartAttributes;
        this.paths = configurePaths(paths);
        this.paint = new Paint(65);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
    }

    private ArrayList<PathOnChart> configurePaths(ArrayList<PathOnChart> paths) {
        double minimum_x = 0.0d;
        double maximum_x = 0.0d;
        double minimum_y = 0.0d;
        double maximum_y = 0.0d;
        ArrayList<PathOnChart> paths_configured = paths;
        int bpo = this.chartAttributes.getBpo();
        Iterator it = paths_configured.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((PathOnChart) it.next()).points.iterator();
            while (it2.hasNext()) {
                PointOnChart pointOnChart = (PointOnChart) it2.next();
                if (((double) pointOnChart.f3x) < minimum_x) {
                    minimum_x = (double) pointOnChart.f3x;
                }
                if (((double) pointOnChart.f4y) < minimum_y) {
                    minimum_y = (double) pointOnChart.f4y;
                }
                if (((double) pointOnChart.f3x) > maximum_x) {
                    maximum_x = (double) (pointOnChart.f3x + ((float) bpo));
                }
                if (((double) pointOnChart.f4y) > maximum_y) {
                    maximum_y = (double) (pointOnChart.f4y + 10.0f);
                }
            }
        }
        this.no_of_x_negetive_unit = (float) Math.abs(minimum_x);
        this.no_of_x_positive_unit = (float) Math.abs(maximum_x);
        this.no_of_y_negetive_unit = (float) Math.abs(minimum_y);
        this.no_of_y_positive_unit = (float) Math.abs(maximum_y);
        this.x_unit_length = this.chart_width / (this.no_of_x_negetive_unit + this.no_of_x_positive_unit);
        this.y_unit_length = this.chart_height / (this.no_of_y_negetive_unit + this.no_of_y_positive_unit);
        this.y_translate_factor = ((float) this.display_height) - this.offset_bottom;
        this.x_translate_factor = (this.no_of_x_negetive_unit * this.x_unit_length) + this.offset_left;
        this.y_translate_factor = (((float) this.display_height) - (this.no_of_y_negetive_unit * this.y_unit_length)) - this.offset_bottom;
        it = paths_configured.iterator();
        while (it.hasNext()) {
            it2 = ((PathOnChart) it.next()).points.iterator();
            while (it2.hasNext()) {
                pointOnChart = (PointOnChart) it2.next();
                pointOnChart.f3x = this.x_translate_factor + (pointOnChart.f3x * this.x_unit_length);
                pointOnChart.f4y = this.y_translate_factor - (pointOnChart.f4y * this.y_unit_length);
            }
        }
        return paths;
    }

    protected Bitmap createGraph() {
        Bitmap bitmap = Bitmap.createBitmap(this.display_width, this.display_height, Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor(this.chartAttributes.backgroundColor));
        if (this.chartAttributes.gridVisible) {
            drawGrid(canvas);
        }
        drawAxis(canvas);
        drawAxisNames(canvas);
        drawLegends(canvas);
        drawAxisUnits(canvas);
        drawPhaths(canvas);
        drawPoints(canvas);
        return bitmap;
    }

    private void drawPhaths(Canvas canvas) {
        Iterator it = this.paths.iterator();
        while (it.hasNext()) {
            PathOnChart pathOnChart = (PathOnChart) it.next();
            if (pathOnChart.points.size() > 0) {
                this.paint.setColor(Color.parseColor(pathOnChart.attributes.pointColor));
                Path path = new Path();
                PointOnChart p1 = (PointOnChart) pathOnChart.points.get(0);
                path.moveTo(p1.f3x, p1.f4y);
                Iterator it2 = pathOnChart.points.iterator();
                while (it2.hasNext()) {
                    PointOnChart point = (PointOnChart) it2.next();
                    path.quadTo(p1.f3x, p1.f4y, point.f3x, point.f4y);
                    p1 = point;
                    path.lineTo(p1.f3x, p1.f4y);
                    this.paint.setColor(Color.parseColor(pathOnChart.attributes.pathColor));
                    this.paint.setStrokeWidth(pathOnChart.attributes.strokeWidthOfPath);
                    this.paint.setStyle(Style.STROKE);
                    canvas.drawPath(path, this.paint);
                }
            }
        }
    }

    private void drawLegends(Canvas canvas) {
        int i = 0;
        Iterator it = this.paths.iterator();
        while (it.hasNext()) {
            PathOnChart pathOnChart = (PathOnChart) it.next();
            this.paint.setColor(Color.parseColor(pathOnChart.attributes.pathColor));
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeWidth(pathOnChart.attributes.strokeWidthOfPath + 2.0f);
            canvas.drawText(pathOnChart.attributes.pathName, this.xtl + 5.0f, (this.ytl + 20.0f) + (((float) i) * 40.0f), this.paint);
            this.paint.setColor(-1);
            this.paint.setStrokeWidth(pathOnChart.attributes.strokeWidthOfPath);
            this.paint.setStyle(Style.FILL);
            canvas.drawText(pathOnChart.attributes.pathName, this.xtl + 5.0f, (this.ytl + 20.0f) + (((float) i) * 40.0f), this.paint);
            i++;
        }
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

    private void drawPoints(Canvas canvas) {
        Iterator it = this.paths.iterator();
        while (it.hasNext()) {
            PathOnChart path = (PathOnChart) it.next();
            Iterator it2 = path.points.iterator();
            while (it2.hasNext()) {
                PointOnChart point = (PointOnChart) it2.next();
                for (int i = 0; i < point.wickets; i++) {
                    this.paint.setColor(-1);
                    this.paint.setStyle(Style.FILL);
                    canvas.drawCircle(point.f3x, point.f4y - ((float) (i * 15)), path.attributes.radiusOfPoints, this.paint);
                    this.paint.setColor(Color.parseColor(path.attributes.pointColor));
                    this.paint.setStyle(Style.FILL);
                    canvas.drawCircle(point.f3x, point.f4y - ((float) (i * 15)), path.attributes.radiusOfPoints - 1.5f, this.paint);
                }
            }
        }
    }

    private void drawAxis(Canvas canvas) {
        this.paint.setColor(Color.parseColor(this.chartAttributes.axisColor));
        this.paint.setStrokeWidth(this.chartAttributes.axisStrokeWidth);
        Canvas canvas2 = canvas;
        canvas2.drawLine((this.no_of_x_negetive_unit * this.x_unit_length) + this.xtl, this.ytl, (this.no_of_x_negetive_unit * this.x_unit_length) + this.xbl, this.ybl, this.paint);
        canvas2 = canvas;
        canvas2.drawLine(this.xtl, (this.no_of_y_positive_unit * this.y_unit_length) + this.ytl, this.xtr, (this.no_of_y_positive_unit * this.y_unit_length) + this.ytr, this.paint);
    }

    private void drawAxisUnits(Canvas canvas) {
        this.paint.setColor(Color.parseColor(this.chartAttributes.axisNameColor));
        this.paint.setStrokeWidth(0.0f);
        int x_increment = 1;
        if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit >= 300.0f) {
            x_increment = 10;
        } else if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit > 200.0f) {
            x_increment = 5;
        } else if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit > 150.0f) {
            x_increment = 3;
        } else if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit > 80.0f) {
            x_increment = 2;
        }
        int i = 2;
        int j = 1;
        int k = 1;
        while (((float) i) <= (this.no_of_x_negetive_unit + this.no_of_x_positive_unit) + 1.0f) {
            if (this.xbl + ((((float) (i + 1)) * this.x_unit_length) - (this.paint.measureText("" + j) / 2.0f)) <= this.xbr && ((float) j) <= (this.no_of_x_positive_unit + 1.0f) / ((float) this.chartAttributes.getBpo())) {
                if (k == x_increment) {
                    canvas.drawText("" + j, (this.xbl + (((float) (i + 1)) * this.x_unit_length)) - (this.paint.measureText("" + j) / 2.0f), this.ybl + (this.offset_bottom * 0.3f), this.paint);
                    k = 1;
                } else {
                    k++;
                }
            }
            i += this.chartAttributes.getBpo();
            j++;
        }
        int y_increment = 5;
        if (this.no_of_y_negetive_unit + this.no_of_y_positive_unit > 150.0f) {
            y_increment = 20;
        } else if (this.no_of_y_negetive_unit + this.no_of_y_positive_unit > 50.0f) {
            y_increment = 10;
        }
        for (i = 0; ((float) i) < (this.no_of_y_negetive_unit + this.no_of_y_positive_unit) + 1.0f; i += y_increment) {
            if (this.ybl - (((float) i) * this.y_unit_length) > this.ytl) {
                canvas.drawText("" + i, this.xbl - (this.offset_left * 0.55f), this.ybl - (((float) i) * this.y_unit_length), this.paint);
            }
        }
    }

    private void drawGrid(Canvas canvas) {
        Canvas canvas2;
        this.paint.setColor(Color.parseColor(this.chartAttributes.getGridColor()));
        float max_stroke_width = this.chartAttributes.gridStrokeWidth;
        float gridStrokeWidth_x = max_stroke_width;
        float gridStrokeWidth_y = max_stroke_width;
        gridStrokeWidth_y = 10.0f / (this.no_of_y_negetive_unit + this.no_of_y_positive_unit);
        if (10.0f / (this.no_of_x_negetive_unit + this.no_of_x_positive_unit) > max_stroke_width) {
            gridStrokeWidth_x = max_stroke_width;
        }
        if (gridStrokeWidth_y > max_stroke_width) {
            gridStrokeWidth_y = max_stroke_width;
        }
        this.paint.setStrokeWidth(this.chartAttributes.gridStrokeWidth);
        int x_increment = 1;
        if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit >= 300.0f) {
            x_increment = 10;
        } else if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit > 200.0f) {
            x_increment = 5;
        } else if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit > 150.0f) {
            x_increment = 3;
        } else if (this.no_of_x_negetive_unit + this.no_of_x_positive_unit > 80.0f) {
            x_increment = 2;
        }
        int i = 0;
        int k = 0;
        while (((float) i) < (this.no_of_x_negetive_unit + this.no_of_x_positive_unit) + 1.0f) {
            if (k == x_increment) {
                canvas2 = canvas;
                canvas2.drawLine((((float) i) * this.x_unit_length) + this.xtl, this.ytl, (((float) i) * this.x_unit_length) + this.xbl, this.ybl, this.paint);
                k = 1;
            } else {
                k++;
            }
            i += this.chartAttributes.getBpo();
        }
        canvas2 = canvas;
        canvas2.drawLine(((this.no_of_x_negetive_unit + this.no_of_x_positive_unit) * this.x_unit_length) + this.xtl, this.ytl, ((this.no_of_x_negetive_unit + this.no_of_x_positive_unit) * this.x_unit_length) + this.xbl, this.ybl, this.paint);
        int y_increment = 5;
        if (this.no_of_y_negetive_unit + this.no_of_y_positive_unit > 150.0f) {
            y_increment = 20;
        } else if (this.no_of_y_negetive_unit + this.no_of_y_positive_unit > 50.0f) {
            y_increment = 10;
        }
        for (i = 0; ((float) i) < (this.no_of_y_negetive_unit + this.no_of_y_positive_unit) + 1.0f; i += y_increment) {
            canvas.drawLine(this.xbl, this.ybl - (((float) i) * this.y_unit_length), this.xbr, this.ybr - (((float) i) * this.y_unit_length), this.paint);
        }
        canvas.drawLine(this.xbl, this.ybl - ((this.no_of_y_negetive_unit + this.no_of_y_positive_unit) * this.y_unit_length), this.xbr, this.ybr - ((this.no_of_y_negetive_unit + this.no_of_y_positive_unit) * this.y_unit_length), this.paint);
    }
}
