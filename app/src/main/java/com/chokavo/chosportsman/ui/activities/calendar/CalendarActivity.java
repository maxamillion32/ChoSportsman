package com.chokavo.chosportsman.ui.activities.calendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.CalendarManager;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.NavigationDrawerActivity;
import com.chokavo.chosportsman.ui.fragments.NewEventFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by repitch on 10.03.16.
 */
public class CalendarActivity extends NavigationDrawerActivity {


    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };


    public static final int ADD_EVENT = 1;
    public static final int CHOOSE_ACCOUNT = 2;
    private FlexibleCalendarView mCalendarView;
    private Button mButton;
    private FloatingActionButton mFabAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_calendar, R.id.nav_calendar);

        mCalendarView = (FlexibleCalendarView) findViewById(R.id.month_view);
        mFabAddEvent = (FloatingActionButton) findViewById(R.id.fab_add_event);
        mCalendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, int direction) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, 1);
                Toast.makeText(CalendarActivity.this, cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " " + year, Toast.LENGTH_SHORT).show();
            }
        });

        mCalendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                if (year == 2015 && month == 3 && day == 25) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if (year == 2015 && month == 3 && day == 8) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if (year == 2015 && month == 3 && day == 5) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                return null;
            }
        });

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCalendarView.isShown()) {
                    mCalendarView.collapse();
                    mButton.setText(R.string.to_show);
                } else {
                    mCalendarView.expand();
                    mButton.setText(R.string.to_hide);
                }
            }
        });
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");
        mFabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFragment(new NewEventFragment(), NewEventFragment.class.getName());
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((this),
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    BaseActivity.MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return;
        }

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(DataManager.getInstance().getGoogleAccount());
        Log.e("","");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCredential != null) {
            refreshResults();
        }
    }

    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (AppUtils.isDeviceOnline()) {
//                new MakeRequestTask(mCredential).execute();
                CalendarManager.getInstance(this).getSportCalendar();
                Log.e("A", "execute async");
            } else {
                ImageSnackbar.make(mCalendarView, ImageSnackbar.TYPE_ERROR, "Нет интернет-соединения", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_EVENT:
                Log.e("","");
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        DataManager.getInstance().setAndSaveGoogleAccount(accountName);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    ImageSnackbar.make(mCalendarView, ImageSnackbar.TYPE_ERROR, "К сожалению, вы не выбрали аккаунт", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CALENDAR) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                CalendarManager.getInstance(CalendarActivity.this).getSportCalendar();
            } else {
                ImageSnackbar.make(mCalendarView, ImageSnackbar.TYPE_ERROR, "К сожалению, вы запретили доступ к календарю", Snackbar.LENGTH_LONG).show();
            }
        } else
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCOUNTS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCredential = GoogleAccountCredential.usingOAuth2(
                        getApplicationContext(), Arrays.asList(SCOPES))
                        .setBackOff(new ExponentialBackOff())
                        .setSelectedAccountName(DataManager.getInstance().getGoogleAccount());
                Log.e("","");
            } else {
                ImageSnackbar.make(mCalendarView, ImageSnackbar.TYPE_ERROR, "К сожалению, вы запретили доступ к аккаунтам", Snackbar.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public class CustomEvent implements Event {

        private int color;

        public CustomEvent(int color){
            this.color = color;
        }

        @Override
        public int getColor() {
            return color;
        }
    }

}
