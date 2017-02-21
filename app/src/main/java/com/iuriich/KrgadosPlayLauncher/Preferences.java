package com.iuriich.KrgadosPlayLauncher;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private static Preferences instance;

    private Preferences() {
        instance = this;
        App application = App.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public static Preferences getInstance() {
        if (instance == null) return new Preferences();
        else return instance;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    public Object getPref(Pref pref) {
        Class type = pref.getType();
        String key = pref.name();
        Object defaultVal = pref.getDefaultVal();

        if (type == String.class) {
            return sharedPreferences.getString(key, (String) defaultVal);
        } else if (type == Boolean.class) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultVal);
        } else if (type == Integer.class) {
            return sharedPreferences.getInt(key, (Integer) defaultVal);
        } else if (type == Long.class) {
            return sharedPreferences.getLong(key, (Long) defaultVal);
        } else if (type == Float.class) {
            return sharedPreferences.getFloat(key, (Float) defaultVal);
        }

        return null;
    }

    public void setPref(Pref pref, Object value) {
        String key = pref.name();
        if (value.getClass() != pref.getType()) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.apply();
    }

    public enum Pref {
        password(String.class, "00000"),
        enable_auto_launch(Boolean.class, true),
        appname(String.class, "");

        private Class type;
        private Object defaultVal;

        Pref(Class type, Object defaultVal) {
            this.type = type;
            this.defaultVal = defaultVal;
        }

        public Class getType() {
            return type;
        }

        public Object getDefaultVal() {
            return defaultVal;
        }
    }

}
