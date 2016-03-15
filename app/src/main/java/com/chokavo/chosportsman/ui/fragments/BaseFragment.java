package com.chokavo.chosportsman.ui.fragments;


import android.support.v4.app.Fragment;

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
}