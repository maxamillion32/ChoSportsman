package com.chokavo.chosportsman.ui.fragments;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.chokavo.chosportsman.ui.activities.BaseActivity;

/**
 * Created by repitch on 15.03.16.
 */

public abstract class BaseFragment extends Fragment {

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
}