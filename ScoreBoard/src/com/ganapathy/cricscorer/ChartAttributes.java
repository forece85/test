package com.ganapathy.cricscorer;

public class ChartAttributes {
    String Chart_title = "Team A vs Team B";
    String Chart_title2 = "Compare Run Rate";
    String X_unit_name = "Overs";
    float X_unit_value = 1.0f;
    String Y_unit_name = "Runs";
    float Y_unit_value = 1.0f;
    String axisColor = "#FF000000";
    String axisNameColor = "#FF000000";
    public float axisStrokeWidth = 2.0f;
    String axisUnitColor = "#88555555";
    String backgroundColor = "#FFFFFFFF";
    int ballsPerOver = 6;
    String gridColor = "#FFDDDDDD";
    public float gridStrokeWidth = 1.0f;
    boolean gridVisible = true;

    public boolean isGridVisible() {
        return this.gridVisible;
    }

    public void setGridVisible(boolean gridVisible) {
        this.gridVisible = gridVisible;
    }

    public float getX_unit_value() {
        return this.X_unit_value;
    }

    public void setX_unit_value(float x_unit_value) {
        this.X_unit_value = x_unit_value;
    }

    public float getY_unit_value() {
        return this.Y_unit_value;
    }

    public void setY_unit_value(float y_unit_value) {
        this.Y_unit_value = y_unit_value;
    }

    public String getX_unit_name() {
        return this.X_unit_name;
    }

    public void setX_unit_name(String x_unit_name) {
        this.X_unit_name = x_unit_name;
    }

    public String getY_unit_name() {
        return this.Y_unit_name;
    }

    public void setY_unit_name(String y_unit_name) {
        this.Y_unit_name = y_unit_name;
    }

    public String getChart_title() {
        return this.Chart_title;
    }

    public void setChart_title(String chart_title) {
        this.Chart_title = chart_title;
    }

    public String getChart_title2() {
        return this.Chart_title2;
    }

    public void setChart_title2(String chart_title) {
        this.Chart_title2 = chart_title;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getAxisColor() {
        return this.axisColor;
    }

    public void setAxisColor(String axisColor) {
        this.axisColor = axisColor;
    }

    public String getAxisNameColor() {
        return this.axisNameColor;
    }

    public void setAxisNameColor(String axisNameColor) {
        this.axisNameColor = axisNameColor;
    }

    public String getGridColor() {
        return this.gridColor;
    }

    public void setGridColor(String gridColor) {
        this.gridColor = gridColor;
    }

    public int getBpo() {
        return this.ballsPerOver;
    }

    public void setBpo(int value) {
        this.ballsPerOver = value;
    }
}
