package com.chokavo.chosportsman.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
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
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.calendar.CalendarActivity;


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
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_CALENDAR_COLOR_INDEX = 4;
    private static final int PROJECTION_NAME_INDEX = 5;
    private static final int PROJECTION_VISIBLE_INDEX = 6;

    public void addEvent(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI);
        intent.putExtra(Events.EVENT_COLOR, Color.RED);
        activity.startActivityForResult(intent, CalendarActivity.ADD_EVENT);
    }

    static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }

    public void testCalendar() {
        if (DataManager.getInstance().googleAccount == null) {
            Log.e(CalendarManager.class.getName(), "googleAccount is null!");
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((Activity) mContext),
                    new String[]{Manifest.permission.READ_CALENDAR},
                    BaseActivity.MY_PERMISSIONS_REQUEST_READ_CALENDAR);
            return;
        }
        // Run query
        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();
//        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Uri uri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI, DataManager.getInstance().googleAccount, ACCOUNT_TYPE_GOOGLE);
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND (" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?))";
       /* String[] selectionArgs = new String[]{"ilyapya@gmail.com", "com.google",
                "ilyapya@gmail.com"};*/
        /*String[] selectionArgs = new String[]{null, "com.google",
                null};*/
        String[] selectionArgs = {ACCOUNT_TYPE_GOOGLE, DataManager.getInstance().googleAccount};
// Submit the query and get a Cursor object back.

        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            long calID = 0;
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
            Log.e("", "");
            // Do something with the values...

        }
    }
}
