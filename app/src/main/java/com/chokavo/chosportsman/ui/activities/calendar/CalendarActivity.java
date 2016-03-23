package com.chokavo.chosportsman.ui.activities.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.CalendarManager;
import com.chokavo.chosportsman.ui.activities.NavigationDrawerActivity;
import com.chokavo.chosportsman.ui.fragments.calendar.NoSportCalendarFragment;
import com.chokavo.chosportsman.ui.fragments.calendar.SportCalendarFragment;

public class CalendarActivity extends NavigationDrawerActivity {

    View mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_calendar2, R.id.nav_calendar);
        mContentFrame = findViewById(R.id.content_frame);
        launchCalendarFragment();
    }

    private void launchCalendarFragment() {
        if (CalendarManager.getInstance(this).haveGoogleCalendar()) {
            // google account есть
            launchFragmentNoBackStack(new SportCalendarFragment(), SportCalendarFragment.getFragmentTag());
        } else {
            // нету аккаунта или календаря
            launchFragmentNoBackStack(new NoSportCalendarFragment(), NoSportCalendarFragment.getFragmentTag());
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}