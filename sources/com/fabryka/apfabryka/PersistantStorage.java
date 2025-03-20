package com.fabryka.apfabryka;

import android.content.Context;
import android.content.SharedPreferences;

public class PersistantStorage {
    public static final String STORAGE_NAME = "FabrykaSave";
    private static Context context = null;
    private static SharedPreferences.Editor editor = null;
    private static SharedPreferences settings = null;

    public static void init(Context cntxt) {
        context = cntxt;
    }

    private static void init() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(STORAGE_NAME, 0);
        settings = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public static void addProperty(String name, String value) {
        if (settings == null) {
            init();
        }
        editor.putString(name, value);
        editor.commit();
    }

    public static String getProperty(String name) {
        if (settings == null) {
            init();
        }
        if (settings.contains(name)) {
            return settings.getString(name, (String) null);
        }
        return "";
    }
}
