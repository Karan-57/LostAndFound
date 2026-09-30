package com.example.lostandfound;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages the current joined campus code in SharedPreferences.
 */
public class CampusManager {

    private static final String PREF_NAME = "CampusSession";
    private static final String KEY_CAMPUS_CODE = "joined_campus_code";

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean hasJoinedCampus(Context context) {
        String code = getJoinedCampusCode(context);
        return code != null && !code.trim().isEmpty();
    }

    public static String getJoinedCampusCode(Context context) {
        return getPrefs(context).getString(KEY_CAMPUS_CODE, null);
    }

    public static void setJoinedCampusCode(Context context, String campusCode) {
        getPrefs(context).edit().putString(KEY_CAMPUS_CODE, campusCode.trim().toUpperCase()).apply();
    }

    public static void leaveCampus(Context context) {
        getPrefs(context).edit().remove(KEY_CAMPUS_CODE).apply();
    }
}
