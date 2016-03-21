package com.chokavo.chosportsman.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.chokavo.chosportsman.App;
import com.chokavo.chosportsman.calendar.GoogleCalendarAPI;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import org.jetbrains.annotations.NonNls;

import java.util.Arrays;

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
    @NonNls
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
    }

    public static void restoreGoogleAccount() {
        DataManager.getInstance().setGoogleAccount(getInstance().preferences.getString(GOOGLE_ACCOUNT, null));
        GoogleAccountCredential googleCredential = GoogleAccountCredential.usingOAuth2(
                App.getInstance(), Arrays.asList(GoogleCalendarAPI.SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(DataManager.getInstance().getGoogleAccount());
        DataManager.getInstance().googleCredential = googleCredential;
    }

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
}

