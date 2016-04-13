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
    /*@NonNls
    private static final String GOOGLE_ACCOUNT = "GOOGLE_ACCOUNT";

    public static void removeGoogleAccount() {
        DataManager.getInstance().setAndSaveGoogleAccount(null);
        DataManager.getInstance().googleCredential = null;
    }

    public static void saveGoogleAccount() {
        getInstance().preferences
                .edit()
                .putString(GOOGLE_ACCOUNT, DataManager.getInstance().getGoogleAccount())
                .commit();
    }*/

    /**
     * Sport calendar
     */
    @NonNls
    private static final String SPORT_CALENDAR_SERVER_ID = "SPORT_CALENDAR_SERVER_ID";

    public static void saveCalendarGAPIid() {
        getInstance().preferences
                .edit()
                .putString(SPORT_CALENDAR_SERVER_ID, DataManager.getInstance().calendarGAPIid)
                .commit();
    }

    public static void restoreCalendarGAPIid() {
        String str = getInstance().preferences.getString(SPORT_CALENDAR_SERVER_ID, null);
        DataManager.getInstance().calendarGAPIid = str;
    }

    public static void removeCalendarGAPIid() {
        DataManager.getInstance().calendarGAPIid = null;
        getInstance().preferences
                .edit()
                .putString(SPORT_CALENDAR_SERVER_ID, null)
                .commit();
    }

    @NonNls
    private static final String SPORT_CALENDAR_CONTENT_PROVIDER_ID = "SPORT_CALENDAR_CONTENT_PROVIDER_ID";

    public static void saveCalendarCPid() {
        getInstance().preferences
                .edit()
                .putLong(SPORT_CALENDAR_CONTENT_PROVIDER_ID, DataManager.getInstance().calendarCPid)
                .commit();
    }

    public static void removeCalendarCPid() {
        DataManager.getInstance().calendarCPid = -1;
        getInstance().preferences
                .edit()
                .putLong(SPORT_CALENDAR_CONTENT_PROVIDER_ID, -1)
                .commit();
    }

    public static void restoreCalendarCPid() {
        DataManager.getInstance().calendarCPid = getInstance().preferences.getLong(SPORT_CALENDAR_CONTENT_PROVIDER_ID, -1);
    }

    // sports
    private static final String USER_SPORTS_CHOSEN = "USER_SPORTS_CHOSEN";

    public static void restoreUserSportsChosen() {
        DataManager.getInstance().userSportsChosen = getInstance().preferences.getBoolean(USER_SPORTS_CHOSEN, false);
    }

    public static void saveUserSportsChosen() {
        getInstance().preferences
                .edit()
                .putBoolean(USER_SPORTS_CHOSEN, DataManager.getInstance().userSportsChosen)
                .commit();
    }


    private static final String USER_ID_ORMLITE = "USER_ID_ORMLITE";

    /**
     * Восстанавливает id пользователя в локальной базе данных ORMLite
     */
    public static void restoreUserIdORMLite() {
        DataManager.getInstance().userIdOTMLite = getInstance().preferences.getInt(USER_ID_ORMLITE, -1);
    }

    public static void saveUserIdORMLite() {
        getInstance().preferences
                .edit()
                .putInt(USER_ID_ORMLITE, DataManager.getInstance().userIdOTMLite)
                .commit();
    }

    public static void restoreAllData() {
        restoreUserIdORMLite();
//        restoreGoogleAccount();
        restoreCalendarGAPIid();
        restoreCalendarCPid();
        restoreUserSportsChosen();
    }
}

