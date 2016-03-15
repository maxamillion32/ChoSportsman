package com.chokavo.chosportsman.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chokavo.chosportsman.R;

/**
 * Created by repitch on 15.03.16.
 */
public class NewEventFragment extends BaseFragment {
    @Override
    public String getFragmentTitle() {
        return "Новое событие";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_event, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
    }
}
