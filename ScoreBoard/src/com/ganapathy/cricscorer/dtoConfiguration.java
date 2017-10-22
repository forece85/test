package com.ganapathy.cricscorer;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;

public class dtoConfiguration {
    private int inlineWwBs;
    private String mailIds;
    private int themeId;
    private int wdNbBowlerStats;

    public String getMailIds() {
        return this.mailIds;
    }

    public void setMailIds(String value) {
        this.mailIds = value;
    }

    @SuppressLint({"InlinedApi"})
    public int getThemeId() {
        if (this.themeId == Integer.MIN_VALUE) {
            if (VERSION.SDK_INT < 11) {
                this.themeId = 16973829;
            } else if (VERSION.SDK_INT < 14) {
                this.themeId = 16973931;
            } else if (VERSION.SDK_INT >= 14) {
                this.themeId = 16974120;
            }
        }
        return this.themeId;
    }

    public void setThemeId(int value) {
        this.themeId = value;
    }

    public int getWdNbBowlerStats() {
        return this.wdNbBowlerStats;
    }

    public void setWdNbBowlerStats(int value) {
        this.wdNbBowlerStats = value;
    }

    public int getInlineWwBs() {
        return this.inlineWwBs;
    }

    public void setInlineWwBs(int value) {
        this.inlineWwBs = value;
    }
}
