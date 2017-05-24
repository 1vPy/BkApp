package com.roy.bkapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class PreferencesHelper {
    private final static String SP_NAME = "BkApp";
    private final static int MODE = Context.MODE_PRIVATE;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final boolean DEFAULT_NIGHT_MODE = false;
    private static final int DEFAULT_CURRENT_ITEM = 0;

    public class Constants{
        public static final String SP_NIGHT_MODE = "night_mode";
        public static final String SP_CURRENT_ITEM = "current_item";
    }

    @Inject
    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, MODE);
        editor = preferences.edit();
    }

    public boolean getNightModeState() {
        return preferences.getBoolean(Constants.SP_NIGHT_MODE, DEFAULT_NIGHT_MODE);
    }

    public void setNightModeState(boolean state) {
        editor.putBoolean(Constants.SP_NIGHT_MODE, state).apply();
    }

    public int getCurrentItem() {
        return preferences.getInt(Constants.SP_CURRENT_ITEM, DEFAULT_CURRENT_ITEM);
    }

    public void setCurrentItem(int item) {
        editor.putInt(Constants.SP_CURRENT_ITEM, item).apply();
    }
}
