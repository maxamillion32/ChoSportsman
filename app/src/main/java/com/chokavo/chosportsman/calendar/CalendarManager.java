package com.chokavo.chosportsman.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SharedPrefsManager;
import com.chokavo.chosportsman.ui.activities.BaseActivity;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.core.Data;


/**
 * Created by ilyapyavkin on 14.03.16.
 */
public class CalendarManager {
    private static final String ACCOUNT_TYPE_GOOGLE = "com.google";
    private static CalendarManager sCalendarManager;
    private Context mContext;

    public CalendarManager(Context context) {
        mContext = context;
    }

    public static CalendarManager getInstance(Context context) {
        sCalendarManager = new CalendarManager(context);
        return sCalendarManager;
    }

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                  // 3
            CalendarContract.Calendars.CALENDAR_COLOR,                           // 4
            CalendarContract.Calendars.NAME,                           // 5
            CalendarContract.Calendars.VISIBLE,                           // 6
            CalendarContract.Calendars._SYNC_ID,                           // 7
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_CALENDAR_COLOR_INDEX = 4;
    private static final int PROJECTION_NAME_INDEX = 5;
    private static final int PROJECTION_VISIBLE_INDEX = 6;
    private static final int PROJECTION_VISIBLE_SYNC_ID_INDEX = 7;

    public void addEvent(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI);
        intent.putExtra(Events.EVENT_COLOR, Color.RED);
        activity.startActivity(intent);
    }

    public boolean haveGoogleCalendar() {
        if (DataManager.getInstance().getGoogleCredential() == null) {
            return false;
        }
        if (DataManager.getInstance().calendarCPid <= 0l &&
                DataManager.getInstance().calendarGAPIid == null) {
            return false;
        }
        return true;
    }

    static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }

    public Calendar getSportCalendar() {
        if (DataManager.getInstance().mSportsman == null) {
            Log.e(CalendarManager.class.getName(), "user is null!");
            return null;
        }
        if (DataManager.getInstance().mSportsman.getGoogleAccount() == null) {
            Log.e(CalendarManager.class.getName(), "googleAccount is null!");
            return null;
        }
        if (DataManager.getInstance().calendarGAPIid == null) {
            Log.e(CalendarManager.class.getName(), "calendarGAPIid is null!");
            return null;
        }
        if (DataManager.getInstance().calendarCP != null) {
            return DataManager.getInstance().calendarCP;
        }
        // календаря нет, нужно вытаскивать по id
        SharedPrefsManager.removeCalendarCPid();
        CalendarProvider calendarProvider = new CalendarProvider(mContext);
        if (DataManager.getInstance().calendarCPid == -1) {
            // у нас есть id календаря на сервере, но нет id в content provider
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(((Activity) mContext),
                        new String[]{Manifest.permission.READ_CALENDAR},
                        BaseActivity.MY_PERMISSIONS_REQUEST_READ_CALENDAR);
                return null;
            }
            Data<Calendar> calendarsData = calendarProvider.getCalendars();
            List<Calendar> calendars = calendarsData.getList();
            String sportCalendarServerId = DataManager.getInstance().calendarGAPIid;
            for (Calendar calendar : calendars) {
                Log.e("", "");
                String ownerAccount = calendar.ownerAccount;
                String accountName = calendar.accountName;
                if (calendar.ownerAccount != null && calendar.ownerAccount.equals(DataManager.getInstance().calendarGAPIid)) {
                    // запоминаем местный id
                    DataManager.getInstance().calendarCPid = calendar.id;
                    SharedPrefsManager.saveCalendarCPid();
                    DataManager.getInstance().calendarCP = calendar;
                    break;
                }
            }
            if (DataManager.getInstance().calendarCPid == -1) {
                // данный календарь не найден в ContentProvider
                Log.e(CalendarManager.class.getName(), "Sport calendar id is not found in ContentProvider");
                // поэтому получим его через Google API
                // TODO getCalendarGAPI()
                return null;
            }
        } else {
            DataManager.getInstance().calendarCP = calendarProvider.getCalendar(DataManager.getInstance().calendarCPid);
        }
        return DataManager.getInstance().calendarCP;
    }

    /**
     * Totally removes secondary calendar
     * @param calendar
     */
    private int deleteCalendarEntry(Calendar calendar) {
        long calID = calendar.id;
        Uri baseUri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI, "ilyapya@gmail.com", ACCOUNT_TYPE_GOOGLE);
        int iNumRowsDeleted = 0;
        Uri calendarUri = ContentUris.withAppendedId(baseUri, calID);

        if (calendar.accountName == null || calendar.accountType == null) {
            ContentValues values = new ContentValues();
            // The new display name for the calendar
            values.put(CalendarContract.Calendars.ACCOUNT_NAME, "ilyapya@gmail.com");
            values.put(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE_GOOGLE);
            int rows = mContext.getContentResolver().update(calendarUri, values, null, null);
            Log.i("HELLO", "Rows updated: " + rows);
        }
        // теперь удаляем календарь
        iNumRowsDeleted = mContext.getContentResolver().delete(calendarUri, null, null);

        Log.i(CalendarManager.class.getName(), "Deleted " + iNumRowsDeleted + " calendar entry.");

        return iNumRowsDeleted;
    }

    public long getCalendarContentProviderId(String googleAccount, String sportCalendarServerId) {
        // Run query
        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

//        Uri uri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI, DataManager.getInstance().googleAccount, ACCOUNT_TYPE_GOOGLE);
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_NAME + " = ?))";
        String[] selectionArgs = {ACCOUNT_TYPE_GOOGLE,/* calendarGAPIid,*/ googleAccount};
// Submit the query and get a Cursor object back.

        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        long sportCalendarContentProviderId = -1;
        while (cur.moveToNext()) {
            long calID = 0;
            String calSyncId = null;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
            int color = 0;
            String name = null;
            short visible = 0;
            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            color = cur.getInt(PROJECTION_CALENDAR_COLOR_INDEX);
            name = cur.getString(PROJECTION_NAME_INDEX);
            visible = cur.getShort(PROJECTION_VISIBLE_INDEX);
            calSyncId = cur.getString(PROJECTION_VISIBLE_SYNC_ID_INDEX);
            Log.e("", "");

            if (ownerName.equals(sportCalendarServerId)){
                // это наш календарь
                sportCalendarContentProviderId = calID;
                break;
            }
        }
        return sportCalendarContentProviderId;
    }
}
