package com.agileengine.leadandroidtesttask.todolist.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FirstStartHandler {

    public static final String PREF_KEY_FIRST_START = "isFirstStart";
    public static final String PREFERENCES_NAME = "firstStartPref";

    public static boolean handleFirstStart(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean isFirstStart = prefs.getBoolean(PREF_KEY_FIRST_START, true);
        if (isFirstStart) {
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putBoolean(PREF_KEY_FIRST_START, false);
            prefsEditor.commit();
        }
        return isFirstStart;
    }

    public static void resetFirstStart(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(PREF_KEY_FIRST_START, true);
        prefsEditor.commit();
    }
}