package com.chokavo.chosportsman.calendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SharedPrefsManager;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.calendar.model.CalendarList;

import me.everything.providers.android.calendar.Calendar;
import rx.Subscriber;

public class CalendarActivity2 extends Activity {
    private TextView mOutputText;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        activityLayout.addView(mOutputText);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");

        setContentView(activityLayout);

        // Initialize credentials and service object.
        SharedPrefsManager.removeGoogleAccount();
        SharedPrefsManager.restoreGoogleAccount();

    }


    /**
     * Called whenever this activity is pushed to the foreground, such as after
     * a call to onCreate().
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            mOutputText.setText("Google Play Services required: " +
                    "after installing, close and relaunch this app.");
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
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        DataManager.getInstance().googleCredential.setSelectedAccountName(accountName);
                        DataManager.getInstance().googleAccount = accountName;
                        SharedPrefsManager.saveGoogleAccount();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    mOutputText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        GoogleAccountCredential googleAccountCredential = DataManager.getInstance().googleCredential;
        if (googleAccountCredential == null || googleAccountCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (DataManager.getInstance().sportCalendarServerId != null) {
                // если календарь уже создан - отлично, нам даже не нужен доступ в интернет
                workWithSportCalendar();
            } else if (AppUtils.isDeviceOnline()) {
//                new MakeRequestTask(googleAccountCredential).execute();

                Subscriber<CalendarList> subscriber = new Subscriber<CalendarList>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted: ");
                        // мы сохранили CalendarId в DataManager
                        workWithSportCalendar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UserRecoverableAuthIOException) {
                            startActivityForResult(
                                    ((UserRecoverableAuthIOException) e).getIntent(),
                                    CalendarActivity2.REQUEST_AUTHORIZATION);
                        } else {
                            Log.e(CalendarActivity2.class.getName(), "error: " + e.toString());
                            ImageSnackbar.make(mOutputText, "Ошибка! " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(CalendarList calendarList) {
                        System.out.println("onNext: " + calendarList.size());
                    }
                };
                GoogleCalendarAPI.getCalendarList(subscriber);

//                GoogleCalendarAPI.createCalendar(googleAccountCredential);
            } else {
                mOutputText.setText("No network connection available.");
            }
        }
    }

    private Calendar mCalendar;

    private void workWithSportCalendar() {
        mCalendar = CalendarManager.getInstance(CalendarActivity2.this)
                .getSportCalendar();
        if (mCalendar == null) {
            mOutputText.setText("calendar is null");
        } else {
            mOutputText.setText(String.format("%s\n%s\n", mCalendar.accountName, mCalendar.displayName));

        }
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {

        if (ActivityCompat.checkSelfPermission(CalendarActivity2.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((CalendarActivity2.this),
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    BaseActivity.MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return;
        }
        startActivityForResult(
                DataManager.getInstance().googleCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case BaseActivity.MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseAccount();
                } else {
                    ImageSnackbar.make(mOutputText, ImageSnackbar.TYPE_ERROR, "К сожалению, вы запретили доступ к аккаунтам", Snackbar.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                CalendarActivity2.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}