package com.chokavo.chosportsman.models;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.chokavo.chosportsman.App;
import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<SportObjectDataRow> sportObjects;
    public VKApiUserFull vkUser;
    public Sportsman mSportsman;
    public boolean userSportsChosen;
    private List<SportType> mSportTypes = new ArrayList<>();
    private List<SportType> mUserSports = new ArrayList<>();

//    private List<SportKind> mSportKinds = new ArrayList<>();
    public SharedPreferences mPreferences;

    private String mGoogleAccount;
    public GoogleAccountCredential googleCredential;
    public String calendarGAPIid; // id for a google calendar на сервере гугл
    public long calendarCPid; // id for a google calendar в Content provider

    public me.everything.providers.android.calendar.Calendar calendarCP; // календарь, получаем из ContentProvider с помощью me.every
    public Calendar calendarGAPI; // календарь, который получаем из Google API
    public Event lastEvent; // последнее созданное событие

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

    public void setUserSports(List<SportType> sports, String key) {
        mUserSports = sports;
        Set<String> sportNames = new HashSet<>();
        for (SportType sport :
                sports) {
            sportNames.add(sport.getTitle());
        }
        mPreferences.edit().putStringSet(key, sportNames).apply();
    }

    public SportType getSportTypeByName(@NonNull CharSequence sportTypeChar) {
        if (sportTypeChar == null) return null;
        for (SportType curSportType: mSportTypes) {
            if (curSportType.getTitle().equals(sportTypeChar.toString())) {
                return curSportType;
            }
        }
        return null;
    }

    public CharSequence[] getSportTypesAsChars() {
        CharSequence[] chars = new CharSequence[mSportTypes.size()];
        int i = 0;
        for (SportType sportType: mSportTypes) {
            chars[i++] = sportType.getTitle();
        }
        return chars;
    }

    public List<SportType> getUserSports() {
        return mUserSports;
    }

    public List<SportType> loadUserSports(String key) {
        if (getUserSports().size() != 0)
            return getUserSports();
        Set<String> sportNames = mPreferences.getStringSet(key, null);
        if (sportNames == null)
            return new ArrayList<>();
        mUserSports.clear();
        for (SportType sport :
                mSportTypes) {
            if (sportNames.contains(sport.getTitle()))
                mUserSports.add(sport);
        }
        return mUserSports;
    }

    @Deprecated
    public void loadSports() {
        mSportTypes = new ArrayList<>();
        SportType football = new SportType("Футбол");
        SportType voleyball = new SportType("Волейбол");
        SportType hockey = new SportType("Хокей");
        SportType basketball = new SportType("Баскетбол");
        mSportTypes.add(football);
        mSportTypes.add(voleyball);
        mSportTypes.add(hockey);
        mSportTypes.add(basketball);
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

    public List<SportType> getSportTypes() {
        return mSportTypes;
    }

    public void setSportTypes(List<SportType> sportTypes) {
        mSportTypes = sportTypes;
    }
}
