package com.chokavo.chosportsman.models;

import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<SportObjectDataRow> sportObjects;
    public VKApiUserFull vkUser;
    public String googleAccount;
    public GoogleAccountCredential googleCredential;
    public String sportCalendarServerId; // id for a google calendar на сервере гугл
    public long sportCalendarContentProviderId; // id for a google calendar в Content provider
    public Calendar sportCalendar; // sport calendar

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }
}
