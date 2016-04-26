package com.chokavo.chosportsman.ui.fragments.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.CalendarManager;
import com.chokavo.chosportsman.calendar.CalendarX;
import com.chokavo.chosportsman.calendar.GoogleCalendarAPI;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.network.RFManager;
import com.chokavo.chosportsman.ormlite.models.SCalendar;
import com.chokavo.chosportsman.ui.activities.calendar.CalendarActivity;
import com.chokavo.chosportsman.ui.activities.calendar.EditEventActivity;
import com.chokavo.chosportsman.ui.activities.calendar.DetailEventActivity;
import com.chokavo.chosportsman.ui.adapters.DayEventAdapter;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by ilyapyavkin on 21.03.16.
 */
public class SportCalendarFragment extends BaseFragment {
    ProgressDialog mProgress;
    private Calendar mCalendar;
    private ImageView mBtnHideCalendar;
    private ExpandableRelativeLayout mExpandCalendarWrap;
    private MaterialCalendarView mCalendarView;
    private FloatingActionButton mFabAddEvent;
    private TextView mTxtCurrentDay;
    private LinearLayout mWrapCurrentDay, mWrapNoEvent;
    private RecyclerView mRvDayEvents;

    @Override
    public String getFragmentTitle() {
        return "Календарь";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sport_calendar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                showCalendarInfo();
                return true;
            case R.id.action_refresh:
                refreshCalendar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshCalendar() {
        workWithCalendarGAPI();
    }

    private void showCalendarInfo() {
        MaterialDialog dialogInfo = new MaterialDialog.Builder(getActivity())
                .content(GoogleCalendarAPI.getReadableCalendarInfo(getActivity()))
                .positiveText(R.string.ok)
                .show();
    }

    private void rotateArrowButton(boolean back) {
        Animation rotateAnim = AnimationUtils.loadAnimation(getActivity(), back ? R.anim.rotate_180_back : R.anim.rotate_180);
        mBtnHideCalendar.setAnimation(rotateAnim);
    }

    private void collapseCalendar() {
        rotateArrowButton(true);
        mExpandCalendarWrap.collapse();
    }

    private void expandCalendar() {
        rotateArrowButton(false);
        mExpandCalendarWrap.expand();
    }

    private void toggleCalendar() {
        if (mExpandCalendarWrap.isExpanded()) {
            collapseCalendar();
        } else {
            expandCalendar();
        }
    }

    private void initViews(View rootView) {
        mCalendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendar_view);
        mExpandCalendarWrap = (ExpandableRelativeLayout) rootView.findViewById(R.id.expand_calendar_wrap);
        mBtnHideCalendar = (ImageView) rootView.findViewById(R.id.btn_hide_calendar);
        mWrapCurrentDay = (LinearLayout) rootView.findViewById(R.id.wrap_current_day);
        mWrapNoEvent = (LinearLayout) rootView.findViewById(R.id.wrap_no_event);
        mFabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fab_add_event);
        mTxtCurrentDay = (TextView) rootView.findViewById(R.id.txt_current_day);
        mRvDayEvents = (RecyclerView) rootView.findViewById(R.id.rv_day_events);
        mRvDayEvents.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBtnHideCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCalendar();
            }
        });

        mWrapNoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // для текущей даты откроем окно создания события
                Intent intent = new Intent(getActivity(), EditEventActivity.class);
                Date extraDate;
                if (mCalendarView.getSelectedDate() == null ||
                        DateUtils.isToday(mCalendarView.getSelectedDate().getDate().getTime())) {
                    extraDate = java.util.Calendar.getInstance().getTime();
                } else {
                    extraDate = mCalendarView.getSelectedDate().getDate();
                }
                intent.putExtra(EditEventActivity.EXTRA_DATE, extraDate);
                startActivityForResult(intent, EditEventActivity.REQUEST_CREATE_EVENT);
            }
        });

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.progress_gapi));

        // Initialize credentials and service object.
        mFabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), EditEventActivity.class),
                        EditEventActivity.REQUEST_CREATE_EVENT);
            }
        });

    }

    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(8, color));
        }
    }


    HashSet<CalendarDay> eventDays = new HashSet<>();

    private void initCalendar() {
        // выбранная дата - сегодня
        Date today = java.util.Calendar.getInstance().getTime();
        mCalendarView.setSelectedDate(today);
        selectDate(today);
        mCalendarView.addDecorator(new EventDecorator(Color.RED, eventDays));
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectDate(date.getDate());
                collapseCalendar();
            }
        });
    }

    public static java.util.Calendar eventDateTimeToCalendar(EventDateTime eventDateTime) {
        if (eventDateTime == null) {
            return null;
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        if (eventDateTime.getDate() != null) {
            cal.setTimeInMillis(eventDateTime.getDate().getValue());
        } else {
            cal.setTimeInMillis(eventDateTime.getDateTime().getValue());
        }
        return cal;
    }

    private void setEventDays(List<Event> eventList) {
        eventDays = new HashSet<>();
        for (Event event: eventList) {
            java.util.Calendar start = eventDateTimeToCalendar(event.getStart());
            eventDays.add(CalendarDay.from(start));
        }
        mCalendarView.addDecorator(new EventDecorator(Color.RED, eventDays));
    }

    private void selectDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.current_day_format),
                getResources().getConfiguration().locale);
        mTxtCurrentDay.setText(dateFormat.format(date));
        List<Event> dateEvents = getDateEvents(date);
        if (!dateEvents.isEmpty()) {
            mRvDayEvents.swapAdapter(new DayEventAdapter(dateEvents, new DayEventAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Event event) {
                    DataManager.getInstance().currentEvent = event;
                    Intent intent = new Intent(getActivity(), DetailEventActivity.class);
                    startActivity(intent);
                }
            }), true);
            mWrapNoEvent.setVisibility(View.GONE);
        } else {
            // пусто
            mRvDayEvents.swapAdapter(new DayEventAdapter(null,null), true);
            mWrapNoEvent.setVisibility(View.VISIBLE);
        }
    }

    private List<Event> getDateEvents(Date date) {
        java.util.Calendar calDay = java.util.Calendar.getInstance();
        calDay.setTime(date);
        List<Event> dateEvents = new ArrayList<>();
        for (Event event: DataManager.getInstance().eventlist) {
            if (event.getStart() == null) continue;
            DateTime eDate = event.getStart().getDate();
            DateTime eDateTime = event.getStart().getDateTime();
            java.util.Calendar startCal = java.util.Calendar.getInstance();
            if (eDate != null) {
                // full day
                startCal.setTimeInMillis(eDate.getValue());
            } else {
                // datetime
                startCal.setTimeInMillis(eDateTime.getValue());
            }
            if (CalendarX.sameDay(startCal, calDay)) {
                Log.e("sport", "this date!$ "+date.toString());
                dateEvents.add(event);
            }
        }
        return dateEvents;
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
            case EditEventActivity.REQUEST_CREATE_EVENT:
                if (resultCode == Activity.RESULT_OK) {
                    workWithCalendarGAPI();
                    ImageSnackbar.make(mBtnHideCalendar, ImageSnackbar.TYPE_SUCCESS,
                            String.format("Событие '%s' успешно создано", DataManager.getInstance().lastEvent.getSummary()),
                            Snackbar.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void workWithCalendarGAPI() {
        /*int userId = DataManager.getInstance().mSportsman.getServerId();
        String googleAPIid = DataManager.getInstance().calendarGAPIid;
        RFManager.createCalendar(userId, googleAPIid, new Callback<SCalendar>() {
            @Override
            public void onResponse(Call<SCalendar> call, Response<SCalendar> response) {
                SCalendar serverCal = response.body();
                Log.e("","");
            }

            @Override
            public void onFailure(Call<SCalendar> call, Throwable t) {
                Log.e("CreateCalendar", "onFailure: "+t.getLocalizedMessage());
            }
        });*/

        if (DataManager.getInstance().calendarGAPI == null) {
            // придется делать асинхронный запрос к серверу google
            mProgress.show();
            GoogleCalendarAPI.getInstance().getCalendarGAPIbyId(new Subscriber<CalendarList>() {
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
        mProgress.show();
        initSubGetEventList();
        GoogleCalendarAPI.getInstance().getEventList(mSubGetEventList);

    }

    /**
     * Подписчик на получение списка событий
     */
    Subscriber<Events> mSubGetEventList;
    private void initSubGetEventList() {
        mSubGetEventList = new Subscriber<Events>() {
            @Override
            public void onCompleted() {
                mProgress.hide();
                setEventDays(DataManager.getInstance().eventlist);
                Date selectedDate = mCalendarView.getSelectedDate()==null ?
                        java.util.Calendar.getInstance().getTime() :
                        mCalendarView.getSelectedDate().getDate();
                selectDate(selectedDate);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(SportCalendarFragment.class.getName(), "error: " + e.toString());
            }

            @Override
            public void onNext(Events events) {
            }
        };
    }


    private void workWithCalendarCP() {
        mCalendar = CalendarManager.getInstance(getActivity())
                .getSportCalendar();
        Toast.makeText(getActivity(), "ContentProvider", Toast.LENGTH_SHORT).show();
    }

}