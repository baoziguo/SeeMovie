package com.baozi.movie.util;

import android.content.SharedPreferences;

import com.baozi.movie.BmobIMApplication;

/**
 * sp存储 by baozi
 */
public class PreferencesUtil {

    public static <T> void putPreferences(String key, T value) {
        SharedPreferences.Editor editor = BmobIMApplication.preferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    public static <T> T getPreferences(String key, T value) {
        Object o = null;
        if (value instanceof String) {
            o = BmobIMApplication.preferences.getString(key, value.toString());
        } else if (value instanceof Boolean) {
            o = BmobIMApplication.preferences.getBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            o = BmobIMApplication.preferences.getInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            o = BmobIMApplication.preferences.getFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            o = BmobIMApplication.preferences.getLong(key, ((Long) value).longValue());
        }
        T t = (T) o;
        return t;
    }
}
