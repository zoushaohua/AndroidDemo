package com.hik.dialyinterview.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {
    private static PreferenceUtil instance;
    private static Context context;

    private PreferenceUtil(Context context) {
        this.context = context;
    }

    public synchronized static PreferenceUtil getInstance(Context context) {
        if (instance == null)
            instance = new PreferenceUtil(context);
        return instance;
    }

    private SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public boolean getFirst() {
        return getSharedPreferences().getBoolean("first ", true);
    }

    public boolean setFirst(boolean flag) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean("first ", flag);
        return editor.commit();
    }

}
