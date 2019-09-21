package com.example.myfilmandtvlist.reminder;

import android.content.Context;
import android.content.SharedPreferences;

public class ReminderPreference {
    private static final String PREFS_NAME = "user_pref";

    private static final String DAILY_STATUS = "daily";
    private static final String RELEASE_STATUS = "release";
    private static ReminderPreference INSTANCE;

    private final SharedPreferences mSharedPreferences;

    public ReminderPreference(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void setDailyStatus(boolean value) {
        // value = 0, remainder is off, and vice versa
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(DAILY_STATUS,value);
        editor.apply();
    }

    public void setReleaseStatus(boolean value) {
        // value = 0, remainder is off, and vice versa
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(RELEASE_STATUS,value);
        editor.apply();
    }

    public boolean getDailyRemainder() {
        if(true) {
            return mSharedPreferences.getBoolean(DAILY_STATUS, false);
        } else {
            return false;
        }
    }

    public boolean getReleaseRemainder() {
        if(true) {
            return mSharedPreferences.getBoolean(RELEASE_STATUS, false);
        } else {
            return false;
        }
    }



}
