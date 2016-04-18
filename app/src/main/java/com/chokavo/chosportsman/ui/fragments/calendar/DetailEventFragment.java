package com.chokavo.chosportsman.ui.fragments.calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chokavo.chosportsman.App;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ormlite.models.SEvent;
import com.chokavo.chosportsman.ui.activities.calendar.DetailEventActivity;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.widgets.NewEventRowView;

/**
 * Created by Дашицырен on 17.04.2016.
 */
public class DetailEventFragment extends BaseFragment {

    private NewEventRowView mRowSportType, mRowEventType;
    private LinearLayout mRowLocation;
    private TextView mTxtDataLine1, mTxtDataLine2, mTxtRepeat, mTextLocation;
    private RecyclerView mRvReminders;

    private SEvent mSEvent;

    public static DetailEventFragment newInstance(Bundle args) {
        DetailEventFragment fragment = new DetailEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getFragmentTitle() {
        return "Мое событие";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // args
        if (getArguments() != null) {
            mSEvent = (SEvent) getArguments().getSerializable(DetailEventActivity.EXTRA_EVENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_event, container, false);
        initViews(rootView);
        showDetailEvent();
        initActions();
        ((DetailEventActivity) getActivity()).setToolbarTitle(getFragmentTitle());
        return rootView;
    }


    private void initViews(View rootView) {
        mRowSportType = (NewEventRowView) rootView.findViewById(R.id.row_sport_type);
        mRowEventType = (NewEventRowView) rootView.findViewById(R.id.row_event_type);
        mRowLocation = (LinearLayout) rootView.findViewById(R.id.row_event_location);
        mTextLocation = (TextView) rootView.findViewById(R.id.text_location);
        mTxtDataLine1 = (TextView) rootView.findViewById(R.id.text_data_line1);
        mTxtDataLine2 = (TextView) rootView.findViewById(R.id.text_data_line2);
        mTxtRepeat = (TextView) rootView.findViewById(R.id.text_repeat);

        mRvReminders = (RecyclerView) rootView.findViewById(R.id.rv_reminders);
    }

    private void showDetailEvent() {
        //TODO:
    }

    private void initActions() {
        mRowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:open Google maps and parse place
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + mTextLocation.getText());

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareEvent();
                return true;
            case R.id.action_delete:
                deleteEvent();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteEvent() {
        //TODO:
    }

    private void shareEvent() {
        //TODO:
    }
}
