package com.chokavo.chosportsman.ui.fragments;


import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by repitch on 15.03.16.
 */

public abstract class BaseFragment extends Fragment {

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private static String TAG;
    protected boolean mShowBackBtn;

    public abstract String getFragmentTitle();

    public static String getFragmentTag() {
        return TAG;
    }

    public void launchFragment(Fragment fragment, String tag) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity)getActivity()).launchFragment(fragment, tag);
        } else {
            Log.e(BaseFragment.class.getName(), "launchFragment when activity is not an instance of BaseActivity");
        }
    }

    public void launchFragmentNoBackStack(Fragment fragment, String tag) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity)getActivity()).launchFragmentNoBackStack(fragment, tag);
        } else {
            Log.e(BaseFragment.class.getName(), "launchFragment when activity is not an instance of BaseActivity");
        }
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    protected boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    protected void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                getActivity(),
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}