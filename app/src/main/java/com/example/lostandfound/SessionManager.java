package com.example.lostandfound;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages the current logged-in user session in SharedPreferences.
 */
public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setLoggedIn(Context context, boolean loggedIn, String email, String name) {
        getPrefs(context).edit()
                .putBoolean(KEY_IS_LOGGED_IN, loggedIn)
                .putString(KEY_USER_EMAIL, email != null ? email.trim().toLowerCase() : "")
                .putString(KEY_USER_NAME, name != null ? name.trim() : "")
                .apply();
    }

    public static String getUserEmail(Context context) {
        return getPrefs(context).getString(KEY_USER_EMAIL, "");
    }

    public static String getUserName(Context context) {
        return getPrefs(context).getString(KEY_USER_NAME, "");
    }

    public static void logout(Context context) {
        getPrefs(context).edit().clear().apply();
    }
}
