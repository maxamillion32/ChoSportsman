package com.chokavo.chosportsman.ui.fragments.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.chokavo.chosportsman.Constants;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.CalendarManager;
import com.chokavo.chosportsman.calendar.GoogleCalendarAPI;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.activities.calendar.CalendarActivity;
import com.chokavo.chosportsman.ui.activities.calendar.CreateEventActivity;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.calendar.model.CalendarList;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.entity.SelectedDateItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.providers.android.calendar.Calendar;
import rx.Subscriber;

/**
 * Created by ilyapyavkin on 21.03.16.
 */
public class SportCalendarFragment extends BaseFragment {
    private TextView mOutputText, mTxtGoogleAccount, mTxtCalendarType, mTxtMonth;
    ProgressDialog mProgress;
    private Calendar mCalendar;
    private CircleImageView mBtnHideCalendar;
    private FlexibleCalendarView mCalendarView;
    private FloatingActionButton mFabAddEvent;

    @Override
    public String getFragmentTitle() {
        return "Календарь";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sport_calendar, container, false);
        initViews(rootView);
        // сначала посмотрим, если ли у нас calendar Cont Prov
        initCalendar();
        if (DataManager.getInstance().calendarCPid > 0) {
            workWithCalendarCP();
        } else if (DataManager.getInstance().calendarGAPIid != null) {
            workWithCalendarGAPI();
        } else {
            // вообще нет данных по календарю. Запускаем NoSportCalendar Fragment
            launchFragmentNoBackStack(new NoSportCalendarFragment(), NoSportCalendarFragment.getFragmentTag());
        }
        return rootView;
    }

    private void rotateArrowButton(boolean back) {
        Animation rotateAnim = AnimationUtils.loadAnimation(getActivity(), back ? R.anim.rotate_180_back : R.anim.rotate_180);
        mBtnHideCalendar.setAnimation(rotateAnim);
    }

    private void initViews(View rootView) {
        mCalendarView = (FlexibleCalendarView) rootView.findViewById(R.id.month_view);
        mBtnHideCalendar = (CircleImageView) rootView.findViewById(R.id.btn_hide_calendar);
        mOutputText = (TextView) rootView.findViewById(R.id.txt_output);
        mTxtGoogleAccount = (TextView) rootView.findViewById(R.id.txt_google_account);
        mTxtCalendarType = (TextView) rootView.findViewById(R.id.txt_calendar_type);
        mTxtMonth = (TextView) rootView.findViewById(R.id.txt_month);
        mFabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fab_add_event);

        mCalendarView.collapse();
        mBtnHideCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCalendarView.isShown()){
                    rotateArrowButton(true);
                    mCalendarView.collapse();
                }else{
                    rotateArrowButton(false);
                    mCalendarView.expand();
                }
            }
        });

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.progress_gapi));

        // Initialize credentials and service object.
        mTxtGoogleAccount.setText(DataManager.getInstance().getGoogleAccount());
        mFabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CreateEventActivity.class),
                        CreateEventActivity.REQUEST_CREATE_EVENT);
            }
        });

    }

    private void initCalendar() {
        SelectedDateItem dateItem = mCalendarView.getSelectedDateItem();
        updateMonthUI(dateItem.getYear(), dateItem.getMonth());

        mCalendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, int direction) {
                updateMonthUI(year, month);
            }
        });

        mCalendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                if (year == 2016 && month == 2 && day == 25) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if (year == 2016 && month == 2 && day == 8) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if (year == 2016 && month == 2 && day == 5) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                return null;
            }
        });
    }

    private void updateMonthUI(int year, int month) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_MONTH);
        mTxtMonth.setText(sdf.format(cal.getTime()));
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

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_AUTHORIZATION:
                if (resultCode != Activity.RESULT_OK) {
                    launchFragmentNoBackStack(new NoSportCalendarFragment(), NoSportCalendarFragment.getFragmentTag());
                }
                break;
            case CreateEventActivity.REQUEST_CREATE_EVENT:
                if (resultCode == Activity.RESULT_OK) {
                    ImageSnackbar.make(mBtnHideCalendar, ImageSnackbar.TYPE_SUCCESS,
                            String.format("Событие '%s' успешно создано", DataManager.getInstance().lastEvent.getSummary()),
                            Snackbar.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void workWithCalendarGAPI() {
        if (DataManager.getInstance().calendarGAPI == null) {
            // придется делать асинхронный запрос к серверу google
            mProgress.show();
            GoogleCalendarAPI.getCalendarGAPIbyId(new Subscriber<CalendarList>() {
                @Override
                public void onCompleted() {
                    mProgress.hide();
                    workWithCalendarGAPI();
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof UserRecoverableAuthIOException) {
                        startActivityForResult(
                                ((UserRecoverableAuthIOException) e).getIntent(),
                                REQUEST_AUTHORIZATION);
                    } else {
                        Log.e(CalendarActivity.class.getName(), "error: " + e.toString());
                        ImageSnackbar.make(getView(), "Ошибка! " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNext(CalendarList calendarList) {
                    // nothing here
                }
            }, DataManager.getInstance().calendarGAPIid);
            return;
        }
        mTxtCalendarType.setText(R.string.calendar_type_gapi);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("summary: %s\n", DataManager.getInstance().calendarGAPI.getSummary()));
        sb.append(String.format("desc: %s\n", DataManager.getInstance().calendarGAPI.getDescription()));
        sb.append(String.format("timezone: %s\n", DataManager.getInstance().calendarGAPI.getTimeZone()));
        mOutputText.setText(sb.toString());

    }

    private void workWithCalendarCP() {
        mTxtCalendarType.setText(R.string.calendar_type_cp);
        mCalendar = CalendarManager.getInstance(getActivity())
                .getSportCalendar();
        if (mCalendar == null) {
            mOutputText.setText("calendar is null");
        } else {
            mOutputText.setText(String.format("%s\n%s\n", mCalendar.accountName, mCalendar.displayName));

        }
    }

}