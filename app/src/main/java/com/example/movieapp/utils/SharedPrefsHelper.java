package com.example.movieapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class for managing SharedPreferences
 */
public class SharedPrefsHelper {
    private static SharedPrefsHelper instance;
    private SharedPreferences sharedPreferences;
    
    private SharedPrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(
            Constants.PREF_NAME,
            Context.MODE_PRIVATE
        );
    }
    
    public static synchronized SharedPrefsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Save user session
     */
    public void saveUserSession(int userId, String name, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USER_NAME, name);
        editor.putString(Constants.KEY_USER_EMAIL, email);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
    
    /**
     * Clear user session (logout)
     */
    public void clearUserSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.KEY_USER_ID);
        editor.remove(Constants.KEY_USER_NAME);
        editor.remove(Constants.KEY_USER_EMAIL);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, false);
        editor.apply();
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * Get current user ID
     */
    public int getUserId() {
        return sharedPreferences.getInt(Constants.KEY_USER_ID, -1);
    }
    
    /**
     * Get current user name
     */
    public String getUserName() {
        return sharedPreferences.getString(Constants.KEY_USER_NAME, "");
    }
    
    /**
     * Get current user email
     */
    public String getUserEmail() {
        return sharedPreferences.getString(Constants.KEY_USER_EMAIL, "");
    }
}

