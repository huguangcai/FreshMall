package com.ysxsoft.freshmall.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    public static void saveSp(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.commit();
    }

    public static String getSp(Context context, String key) {
        SharedPreferences getSp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        String value = getSp.getString(key, "");
        return value;
    }

    public static void deleteSp(Context context) {
        SharedPreferences.Editor deleteSp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE).edit();
        deleteSp.clear();
        deleteSp.commit();
    }
}
