package com.ganapathy.cricscorer;

public class PathAttributes {
    String pathColor = "#0000FF";
    String pathName;
    String pointColor = "#FF0000";
    float radiusOfPoints = 3.0f;
    float strokeWidthOfPath = 3.0f;

    public String getPathColor() {
        return this.pathColor;
    }

    public void setPathColor(String pathColor) {
        this.pathColor = pathColor;
    }

    public String getPointColor() {
        return this.pointColor;
    }

    public void setPointColor(String pointColor) {
        this.pointColor = pointColor;
    }

    public String getPathName() {
        return this.pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public float getStrokeWidthOfPath() {
        return this.strokeWidthOfPath;
    }

    public void setStrokeWidthOfPath(float strokeWidthOfPath) {
        this.strokeWidthOfPath = strokeWidthOfPath;
    }

    public float getRadiusOfPoints() {
        return this.radiusOfPoints;
    }

    public void setRadiusOfPoints(float radiusOfPoints) {
        this.radiusOfPoints = radiusOfPoints;
    }
}
