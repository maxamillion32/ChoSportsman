package com.chokavo.chosportsman.ui.activities.teams;

import android.os.Bundle;
import android.view.View;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.CalendarManager;
import com.chokavo.chosportsman.ui.activities.NavigationDrawerActivity;
import com.chokavo.chosportsman.ui.fragments.calendar.NoSportCalendarFragment;
import com.chokavo.chosportsman.ui.fragments.calendar.SportCalendarFragment;
import com.chokavo.chosportsman.ui.fragments.teams.NoTeamFragment;

/**
 * Created by Дашицырен on 21.04.2016.
 */
public class TeamsActivity extends NavigationDrawerActivity {

    View mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_teams, R.id.nav_teams);
        mContentFrame = findViewById(R.id.content_frame);
        launchCalendarFragment();
    }

    private void launchCalendarFragment() {
        //TODO:
        if (false) {
            // user have teams

        } else {
            // no teams
            launchFragmentNoBackStack(new NoTeamFragment(), NoTeamFragment.getFragmentTag());
        }
    }

}
