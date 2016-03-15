package com.chokavo.chosportsman.calendar;
import android.util.Log;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.models.DataManager;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.model.Calendar;

import java.io.IOException;

/**
 * Created by repitch on 15.03.16.
 */
public class GoogleCalendarAPI {

    public static final String CALENDAR_SUMMARY = "choSportsman";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory sJsonFactory =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport sHttpTransport = AndroidHttp.newCompatibleTransport();

    private static String createCalendarSync(GoogleAccountCredential credential) {
        if (DataManager.getInstance().calendarId != null) {
            Log.e(GoogleCalendarAPI.class.getName(), "CalendarId is already in DataManager");
            return DataManager.getInstance().calendarId;
        }
        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                sHttpTransport, sJsonFactory, credential)
                .setApplicationName(AppUtils.getApplicationName())
                .build();

        // Create a new calendar
        Calendar calendar = new Calendar();
        calendar.setSummary(CALENDAR_SUMMARY);

        // Insert the new calendar
        Calendar createdCalendar = null;
        try {
            createdCalendar = service.calendars().insert(calendar).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        DataManager.getInstance().calendarId = createdCalendar.getId();
        System.out.println(createdCalendar.getId());
        return DataManager.getInstance().calendarId;
    }
}

