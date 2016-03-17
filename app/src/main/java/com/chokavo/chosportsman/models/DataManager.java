package com.chokavo.chosportsman.models;

import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.List;

import com.google.api.services.calendar.model.Calendar;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<SportObjectDataRow> sportObjects;
    public VKApiUserFull vkUser;

    private String mGoogleAccount;
    public GoogleAccountCredential googleCredential;
    public String sportCalendarGAPIId; // id for a google calendar на сервере гугл
    public long sportCalendarContentProviderId; // id for a google calendar в Content provider

    public me.everything.providers.android.calendar.Calendar sportCalendar; // календарь, получаем из ContentProvider с помощью me.every
    public Calendar sportCalendarGAPI; // календарь, который получаем из Google API

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }

    public String getGoogleAccount() {
        if (mGoogleAccount == null) {
            SharedPrefsManager.restoreGoogleAccount();
        }
        return mGoogleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        mGoogleAccount = googleAccount;
        SharedPrefsManager.saveGoogleAccount();
    }


}
