package com.apps.AppsDaily.Util.SharedPreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    public static final String PREF_NAME = "_APP_TOKENS";
    public static final String PREF_LASTSEND = "_APP_LASTSEND";
    public static final String PREF_KEY = "_APP_TOKEN_user";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public UserManager(Context context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void save(String token) {
        editor.putString(PREF_KEY, token);
        editor.commit();
    }

    public String get() {
        return sp.getString(PREF_KEY, null);
    }

    public void val() {
        editor.remove(PREF_KEY);
        editor.commit();
    }

    public void saveTime(String last) {
        editor.putString(PREF_LASTSEND, last);
        editor.commit();
    }

    public String getTime() {
        return sp.getString(PREF_LASTSEND, null);
    }

    public void valRemoveTime() {
        editor.remove(PREF_LASTSEND);
        editor.commit();
    }
}
