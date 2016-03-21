package com.chokavo.chosportsman.ui.fragments.calendar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.CalendarManager;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;

import me.everything.providers.android.calendar.Calendar;

/**
 * Created by ilyapyavkin on 21.03.16.
 */
public class SportCalendarFragment extends BaseFragment {
    private TextView mOutputText, mTxtGoogleAccount;
    ProgressDialog mProgress;

    @Override
    public String getFragmentTitle() {
        return "Календарь";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sport_calendar, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        mOutputText = (TextView) rootView.findViewById(R.id.txt_output);
        mTxtGoogleAccount = (TextView) rootView.findViewById(R.id.txt_google_account);

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Calling Google Calendar API ...");

        // Initialize credentials and service object.
        mTxtGoogleAccount.setText(DataManager.getInstance().getGoogleAccount());

    }



    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    /*private void refreshResults() {
        GoogleAccountCredential googleAccountCredential = DataManager.getInstance().googleCredential;
        if (googleAccountCredential == null || googleAccountCredential.getSelectedAccountName() == null) {
//            chooseAccount();
        } else {
            if (DataManager.getInstance().calendarGAPIid != null) {
                // если календарь уже создан - отлично, нам даже не нужен доступ в интернет
                workWithSportCalendar();
            } else if (AppUtils.isDeviceOnline()) {
//                new MakeRequestTask(googleAccountCredential).execute();

                GoogleCalendarAPI.getCalendarGAPI(mSubscriberGetCalList);
            } else {
                mOutputText.setText("No network connection available.");
            }
        }
    }*/

    private Calendar mCalendar;

    private void workWithSportCalendar() {
        mCalendar = CalendarManager.getInstance(getActivity())
                .getSportCalendar();
        if (mCalendar == null) {
            mOutputText.setText("calendar is null");
        } else {
            mOutputText.setText(String.format("%s\n%s\n", mCalendar.accountName, mCalendar.displayName));

        }
    }

    private void showCalendarInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("summary: %s\n", DataManager.getInstance().calendarGAPI.getSummary()));
        sb.append(String.format("desc: %s\n", DataManager.getInstance().calendarGAPI.getDescription()));
        sb.append(String.format("timezone: %s\n", DataManager.getInstance().calendarGAPI.getTimeZone()));
        mOutputText.setText(sb.toString());
    }
}