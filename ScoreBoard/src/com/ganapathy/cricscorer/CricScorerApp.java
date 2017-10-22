package com.ganapathy.cricscorer;

import android.app.Application;
import android.content.Context;

public class CricScorerApp extends Application {
    private static Context mContext;
    public dtoMatch currentMatch;

    public void onCreate() {
        super.onCreate();
        mContext = this;
        DatabaseHandler db = new DatabaseHandler(this);
        db.addDefaultAdditionalSettings();
        db.initializeDefaultTeams();
        db.close();
    }

    public static Context getContext() {
        return mContext;
    }
}
