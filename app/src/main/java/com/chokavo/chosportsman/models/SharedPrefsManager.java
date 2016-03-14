package com.chokavo.chosportsman.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.chokavo.chosportsman.App;

import org.jetbrains.annotations.NonNls;

/**
 * Created by ilyapyavkin on 14.03.16.
 */
public final class SharedPrefsManager {

    @NonNls
    private final String PREFS = "PREFS";

    @NonNls
    private static final String GOOGLE_ACCOUNT = "GOOGLE_ACCOUNT";

    private static SharedPrefsManager manager;
    private SharedPreferences preferences;

    private SharedPrefsManager(Context context) {
        preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static SharedPrefsManager getInstance() {
        if (manager == null)
            manager = new SharedPrefsManager(App.getInstance());
        return manager;
    }

    /**
     * GOOGLE_ACCOUNT
     */

    public static void saveGoogleAccount() {
        getInstance().preferences
                .edit()
                .putString(GOOGLE_ACCOUNT, DataManager.getInstance().googleAccount)
                .commit();
    }

    public static void restoreGoogleAccount() {
        DataManager.getInstance().googleAccount = getInstance().preferences.getString(GOOGLE_ACCOUNT, null);
    }
}

