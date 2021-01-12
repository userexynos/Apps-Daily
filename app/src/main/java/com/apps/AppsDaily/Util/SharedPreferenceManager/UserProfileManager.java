package com.apps.AppsDaily.Util.SharedPreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProfileManager {
    public static final String PREF_NAMA = "_APP_user";
    public static final String PREF_NOHP = "_APP_no_hp";
    public static final String PREF_JK = "_APP_jenis_kealmin";
    public static final String PREF_EMAIL = "_APP_email";
    public static final String PREF_JOB = "_APP_pekerjaan";
    public static final String PREF_ALAMAT = "_APP_alamat";
    public static final String PREF_PENDIDIKAN = "_APP_pendidikan";
    public static final String PREF_BOD = "_APP_BOD";
    public static final String PREF_PROVINSI = "_APP_provinsi";
    public static final String PREF_KOTA = "_APP_kota";
    public static final String PREF_PENDAPATAN = "_APP_pendapatan";
    public static final String PREF_HARGAHP = "_APP_hargahp";
    public static final String PREF_PREFERENSIHP = "_APP_preferensi";
    public static final String PREF_MERKHP = "_APP_merkhp";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public UserProfileManager(Context context) {
        sp = context.getSharedPreferences("_APP_USERS_PROFILES", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void save(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return sp.getString(key, null);
    }
    public void valRemove(String key) {
        editor.remove(key);
        editor.commit();
    }
}
