package com.chokavo.chosportsman.models;

import android.support.annotation.NonNull;

import com.chokavo.chosportsman.App;
import com.chokavo.chosportsman.calendar.GoogleCalendarAPI;
import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.chokavo.chosportsman.ormlite.models.SCalendar;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<SportObjectDataRow> sportObjects;
    public VKApiUserFull vkUser;
    public Sportsman mSportsman;
    public boolean userSportsChosen;
    private List<SSportType> mSportTypes = new ArrayList<>();
    private List<SSportType> mUserSports = new ArrayList<>();
    public List<Event> eventlist = new ArrayList<>();

//    private List<SportKind> mSportKinds = new ArrayList<>();


    public int userIdOTMLite; // id текущего юзера в локальной бд
    private String mGoogleAccount;
    private GoogleAccountCredential googleCredential;
    public SCalendar mSCalendar; // scalendar
    public String calendarGAPIid; // id for a google calendar на сервере гугл
    public long calendarCPid; // id for a google calendar в Content provider

    public me.everything.providers.android.calendar.Calendar calendarCP; // календарь, получаем из ContentProvider с помощью me.every
    public Calendar calendarGAPI; // календарь, который получаем из Google API
    public Event lastEvent; // последнее созданное событие
    public Event currentEvent; // текущее событие - для которого смотрим DetailEventFragment

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }

    public void setUserSports(List<SSportType> sports) {
        mUserSports = sports;
    }

    public SSportType getSportTypeByName(@NonNull CharSequence sportTypeChar) {
        if (sportTypeChar == null) return null;
        for (SSportType curSportType: mSportTypes) {
            if (curSportType.getTitle().equals(sportTypeChar.toString())) {
                return curSportType;
            }
        }
        return null;
    }

    public CharSequence[] getSportTypesAsChars() {
        CharSequence[] chars = new CharSequence[mSportTypes.size()];
        int i = 0;
        for (SSportType sportType: mSportTypes) {
            chars[i++] = sportType.getTitle();
        }
        return chars;
    }

    public List<SSportType> getUserSports() {
        return mUserSports;
    }

    @Deprecated
    public void loadSports() {
        mSportTypes = new ArrayList<>();
        SSportType football = new SSportType("Футбол");
        SSportType voleyball = new SSportType("Волейбол");
        SSportType hockey = new SSportType("Хокей");
        SSportType basketball = new SSportType("Баскетбол");
        mSportTypes.add(football);
        mSportTypes.add(voleyball);
        mSportTypes.add(hockey);
        mSportTypes.add(basketball);
    }

   /* public void setDeleteUserSports(boolean isDeleteAvailable){
        for (SSportType sport :
                mUserSports) {
            sport.setChecked(isDeleteAvailable);
        }
    }*/

    public String getGoogleAccount() {
        return mGoogleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        mGoogleAccount = googleAccount;
    }

    /*public void setAndSaveGoogleAccount(String googleAccount) {
        setGoogleAccount(googleAccount);
        SharedPrefsManager.saveGoogleAccount();
    }*/

    public List<SSportType> getSportTypes() {
        return mSportTypes;
    }

    public void setSportTypes(List<SSportType> sportTypes) {
        mSportTypes = sportTypes;
    }

    public GoogleAccountCredential getGoogleCredential() {
        if (googleCredential == null || googleCredential.getSelectedAccountName() == null ||
                googleCredential.getSelectedAccountName().isEmpty()) {
            googleCredential = GoogleAccountCredential.usingOAuth2(
                    App.getInstance(), Arrays.asList(GoogleCalendarAPI.SCOPES))
                    .setBackOff(new ExponentialBackOff())
                    .setSelectedAccountName(DataManager.getInstance().mSportsman.getGoogleAccount());
        }
        return googleCredential;
    }
}
