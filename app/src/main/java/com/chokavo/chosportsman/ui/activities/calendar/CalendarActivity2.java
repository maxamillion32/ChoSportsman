package com.chokavo.chosportsman.ui.activities.calendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.CalendarManager;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.fragments.calendar.NoSportCalendarFragment;
import com.chokavo.chosportsman.ui.fragments.calendar.SportCalendarFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class CalendarActivity2 extends BaseActivity {

    View mContentFrame;

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar2);
        mContentFrame = findViewById(R.id.content_frame);

        if (CalendarManager.getInstance(this).haveGoogleCalendar()) {
            // google account есть
            launchFragment(new SportCalendarFragment(), SportCalendarFragment.getFragmentTag());
        } else {
            // нету аккаунта или календаря
            launchFragment(new NoSportCalendarFragment(), NoSportCalendarFragment.getFragmentTag());
        }
    }


    /**
     * Called whenever this activity is pushed to the foreground, such as after
     * a call to onCreate().
     */
    /*@Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            mOutputText.setText("Google Play Services required: " +
                    "after installing, close and relaunch this app.");
        }
    }*/

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
                        DataManager.getInstance().setAndSaveGoogleAccount(accountName);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    ImageSnackbar.make(mContentFrame, ImageSnackbar.TYPE_ERROR, "Account unspecified.", Snackbar.LENGTH_LONG).show();
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
                    ImageSnackbar.make(mContentFrame, ImageSnackbar.TYPE_ERROR, "К сожалению, вы запретили доступ к аккаунтам", Snackbar.LENGTH_LONG).show();
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