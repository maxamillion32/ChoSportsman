package com.chokavo.chosportsman.models;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chokavo.chosportsman.App;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.model.Calendar;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<SportObjectDataRow> sportObjects;
    public VKApiUserFull vkUser;
    private Set<SportKind> mSportKinds = new HashSet<>();
    private Set<SportKind> mUserSports = new HashSet<>();
    public SharedPreferences mPreferences;

    private String mGoogleAccount;
    public GoogleAccountCredential googleCredential;
    public String calendarGAPIid; // id for a google calendar на сервере гугл
    public long calendarCPid; // id for a google calendar в Content provider

    public me.everything.providers.android.calendar.Calendar calendarCP; // календарь, получаем из ContentProvider с помощью me.every
    public Calendar calendarGAPI; // календарь, который получаем из Google API

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }

    public void setVkUser(VKApiUserFull vkUser, String key) {
        this.vkUser = vkUser;
        mPreferences.edit().putInt(key, vkUser.getId()).apply();
    }

    public void setUserSports(Set<SportKind> sports, String key) {
        mUserSports = sports;
        Set<String> sportNames = new HashSet<>();
        for (SportKind sport :
                sports) {
            sportNames.add(sport.getName());
        }
        mPreferences.edit().putStringSet(key, sportNames).apply();
    }

    public Set<SportKind> getSportKinds() {
        return mSportKinds;
    }

    public Set<SportKind> getUserSports() {
        return mUserSports;
    }

    public Set<SportKind> loadUserSports(String key) {
        if (getUserSports().size() != 0)
            return getUserSports();
        Set<String> sportNames = mPreferences.getStringSet(key, null);
        if (sportNames == null)
            return new HashSet<>();
        mUserSports.clear();
        for (SportKind sport :
                mSportKinds) {
            if (sportNames.contains(sport.getName()))
                mUserSports.add(sport);
        }
        return mUserSports;
    }

    public void loadSports() {
        mSportKinds = new HashSet<>();
        SportKind football = new SportKind("Футбол");
        SportKind voleyball = new SportKind("Волейбол");
        SportKind hockey = new SportKind("Хокей");
        SportKind basketball = new SportKind("Баскетбол");
        mSportKinds.add(football);
        mSportKinds.add(voleyball);
        mSportKinds.add(hockey);
        mSportKinds.add(basketball);
    }

    public String getGoogleAccount() {
       /* if (mGoogleAccount == null) {
            SharedPrefsManager.restoreGoogleAccount();
        }*/
        return mGoogleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        mGoogleAccount = googleAccount;
    }

    public void setAndSaveGoogleAccount(String googleAccount) {
        setGoogleAccount(googleAccount);
        SharedPrefsManager.saveGoogleAccount();
    }


}
