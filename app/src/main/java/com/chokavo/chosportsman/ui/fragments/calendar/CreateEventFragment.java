package com.chokavo.chosportsman.ui.fragments.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.EventReminderX;
import com.chokavo.chosportsman.calendar.GoogleCalendarAPI;
import com.chokavo.chosportsman.calendar.RecurrenceItem;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SportEventType;
import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ui.activities.calendar.CreateEventActivity;
import com.chokavo.chosportsman.ui.adapters.CheckableItemAdapter;
import com.chokavo.chosportsman.ui.adapters.EventReminderAdapter;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.chokavo.chosportsman.ui.widgets.NewEventRowView;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventReminder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by repitch on 15.03.16.
 */
public class CreateEventFragment extends BaseFragment {
    private static final String DATE_PICKER_TAG = "DATE_PICKER_TAG";
    @Override
    public String getFragmentTitle() {
        return "Новое событие";
    }

    private NewEventRowView mRowSportType, mRowEventType, mRowRepeat;
    private LinearLayout mLlWholeDayWrap, mLlRemindersWrap;
    private TextView mTxtDataStart, mTxtTimeStart, mTxtDataEnd, mTxtTimeEnd, mTxtAddReminder;
    private EditText mEditLocation, mEditSummary;
    private Switch mSwitchWholeDay;
    private RecyclerView mRvReminders;

    private MaterialDialog mDialogSportType, mDialogSportEventType,
            mDialogRecurrence, mDialogReminder, mDialogTimeError;

    private Date mDefaultDate;
    private Calendar mCalendarStart, mCalendarEnd;
    private Calendar mCalendarStartDate, mCalendarEndDate; // only date, no time
    private long diff, diffDate; // разница между началом и концом
    private boolean timeStartEndError;

    private ProgressDialog mProgress;

    private SportType mChosenSportType;
    private int mChosenSportKindId;
    private int mChosenSportEventType, mChosenRecurrence;
    private CharSequence mEventTypes[], mSportTypes[], mRecurrenceTypes[], mDefaultReminders[];
    private List<EventReminder> mEventReminders = new ArrayList<>();

    public static CreateEventFragment newInstance(Bundle args) {
        CreateEventFragment fragment = new CreateEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // args
        if (getArguments() != null) {
            mDefaultDate = (Date) getArguments().getSerializable(CreateEventActivity.EXTRA_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);
        initViews(rootView);
        initActions();
        return rootView;
    }

    private void initViews(View rootView) {
        mRowSportType = (NewEventRowView) rootView.findViewById(R.id.row_sport_type);
        mRowEventType = (NewEventRowView) rootView.findViewById(R.id.row_event_type);
        mRowRepeat = (NewEventRowView) rootView.findViewById(R.id.row_repeat);
        mLlWholeDayWrap = (LinearLayout) rootView.findViewById(R.id.ll_whole_day_wrap);
        mLlRemindersWrap = (LinearLayout) rootView.findViewById(R.id.ll_reminders_wrap);

        mTxtDataStart = (TextView) rootView.findViewById(R.id.txt_data_start);
        mTxtTimeStart = (TextView) rootView.findViewById(R.id.txt_time_start);
        mTxtDataEnd = (TextView) rootView.findViewById(R.id.txt_data_end);
        mTxtTimeEnd = (TextView) rootView.findViewById(R.id.txt_time_end);
        mTxtAddReminder = (TextView) rootView.findViewById(R.id.txt_add_reminder);
        mEditLocation = (EditText) rootView.findViewById(R.id.edit_location);
        mEditSummary = (EditText) rootView.findViewById(R.id.edit_summary);
        mSwitchWholeDay = (Switch) rootView.findViewById(R.id.switch_whole_day);
        mRvReminders = (RecyclerView) rootView.findViewById(R.id.rv_reminders);

        //dialogs
        createDialogs();
    }

    private void showDialogSportKind() {
        mDialogSportType = new MaterialDialog.Builder(getActivity())
                .adapter(new CheckableItemAdapter(getActivity(), mSportTypes, mChosenSportKindId),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                mChosenSportKindId = which;
                                mChosenSportType = DataManager.getInstance().getSportTypeByName(mSportTypes[which]);
                                mRowSportType.setValue(mSportTypes[which].toString());
                                dialog.cancel();
                            }
                        })
                .build();
        mDialogSportType.show();
    }

    private void showDialogSportEventType() {
        mDialogSportEventType = new MaterialDialog.Builder(getActivity())
                .adapter(new CheckableItemAdapter(getActivity(), mEventTypes, mChosenSportEventType),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                mChosenSportEventType = which;
                                mRowEventType.setValue(mEventTypes[which].toString());
                                dialog.cancel();
                            }
                        })
                .build();
        mDialogSportEventType.show();
    }

    private void showDialogRecurrence() {
        mDialogRecurrence = new MaterialDialog.Builder(getActivity())
                .adapter(new CheckableItemAdapter(getActivity(), mRecurrenceTypes, mChosenRecurrence),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                mChosenRecurrence = which;
                                mRowRepeat.setValue(mRecurrenceTypes[which].toString());
                                dialog.cancel();
                            }
                        })
                .build();
        mDialogRecurrence.show();
    }

    private class ReminderOnItemClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int chosenId = mRvReminders.getChildAdapterPosition(v);
            showDialogReminder(chosenId);
        }
    }

    private void notifyRvRemindersDataChanged() {
        mRvReminders.swapAdapter(new EventReminderAdapter(getActivity(), mEventReminders, new ReminderOnItemClick()), true);
    }

    private void showDialogReminder(final int chosenId) {
        EventReminder reminder = mEventReminders.get(chosenId);
        mDialogReminder = new MaterialDialog.Builder(getActivity())
                .adapter(new CheckableItemAdapter(getActivity(), mDefaultReminders, EventReminderX.getDefaultId(reminder)),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                EventReminder chosenReminder = EventReminderX.DEFAULT_REMINDERS_OPTIONS[which];
                                if (chosenReminder == null) {
                                    mEventReminders.remove(chosenId);
                                } else {
                                    mEventReminders.set(chosenId, chosenReminder);
                                }
                                notifyRvRemindersDataChanged();
                                dialog.cancel();
                            }
                        })
                .build();
        mDialogReminder.show();

    }

    private void createDialogs() {
        mSportTypes = DataManager.getInstance().getSportTypesAsChars();
        // choose sport
        mChosenSportKindId = 0;
        mRowSportType.setValue(mSportTypes[mChosenSportKindId].toString());

        /** choose event types **/
        mEventTypes = SportEventType.getEventTypesAsChars(getActivity());
        mChosenSportEventType = 0;
        mRowEventType.setValue(mEventTypes[mChosenSportEventType].toString());

        /** повторение (recurrence) */
        mRecurrenceTypes = RecurrenceItem.getAsChars(getActivity());
        mChosenRecurrence = 0;
        mRowRepeat.setValue(mRecurrenceTypes[mChosenRecurrence].toString());

        /** напоминалки (reminders) **/
        // по умолчанию заполним диалог
        mDefaultReminders = EventReminderX.toCharSequence(EventReminderX.DEFAULT_REMINDERS_OPTIONS);
        // по умолчанию за 30 минут почта и popup
        mEventReminders = new ArrayList<>(Arrays.asList(EventReminderX.DEFAULT_REMINDERS));
        // use a linear layout manager
        mRvReminders.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvReminders.setAdapter(new EventReminderAdapter(getActivity(), mEventReminders, new ReminderOnItemClick()));

        // progress dialog
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.progress_create_event));
    }

    private void initActions() {
        mRowSportType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSportKind();
            }
        });
        mRowEventType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSportEventType();
            }
        });
        mRowRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecurrence();
            }
        });
        mLlWholeDayWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchWholeDay.setChecked(!mSwitchWholeDay.isChecked());
            }
        });
        mSwitchWholeDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTxtTimeStart.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                mTxtTimeEnd.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                fillDataAndTime();
            }
        });
        mTxtAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // добавляем еще одно напоминание
                EventReminder eventReminder = EventReminderX.DEFAULT_REMINDERS_OPTIONS[1];
                mEventReminders.add(eventReminder);
                notifyRvRemindersDataChanged();
                showDialogReminder(mEventReminders.size()-1);
            }
        });

        // init start date and time
        mCalendarStart = Calendar.getInstance();
        mCalendarStartDate = Calendar.getInstance();
        mCalendarEndDate = Calendar.getInstance();
        mCalendarEnd = Calendar.getInstance();
        // при запуске фрагмента мы могли передать дефолтную дату
        if (mDefaultDate != null) {
            mCalendarStart.setTime(mDefaultDate);
            mCalendarStartDate.setTime(mDefaultDate);
            mCalendarEndDate.setTime(mDefaultDate);
            mCalendarEnd.setTime(mDefaultDate);
        }
        mCalendarEnd.add(Calendar.HOUR_OF_DAY, 1);
        mCalendarStartDate.set(Calendar.HOUR_OF_DAY, 0);
        mCalendarStartDate.set(Calendar.MINUTE, 0);
        mCalendarEndDate.set(Calendar.HOUR_OF_DAY, 0);
        mCalendarEndDate.set(Calendar.MINUTE, 0);
        fillDataAndTime();

        mTxtDataStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDiff();
                openDatePicker((TextView) v);
            }
        });
        mTxtDataEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker((TextView) v);
            }
        });
        mTxtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDiff();
                openTimePicker((TextView) v);
            }
        });
        mTxtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker((TextView) v);
            }
        });
    }

    private void countDiff() {
        if (!timeStartEndError) {
            diff = mCalendarEnd.getTimeInMillis() - mCalendarStart.getTimeInMillis();
            diffDate = mCalendarEndDate.getTimeInMillis() - mCalendarStartDate.getTimeInMillis();
        }
    }

    private void fillDataAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format_1),
                getResources().getConfiguration().locale);
        SimpleDateFormat timeFormat = new SimpleDateFormat(getString(R.string.time_format_1),
                getResources().getConfiguration().locale);
        mTxtDataStart.setText(dateFormat.format(mCalendarStart.getTime()));
        mTxtDataEnd.setText(dateFormat.format(mCalendarEnd.getTime()));
        long startTime, endTime;
        if (mSwitchWholeDay.isChecked()) {
            // whole day
            startTime = mCalendarStartDate.getTimeInMillis();
            endTime = mCalendarEndDate.getTimeInMillis();
        } else {
            mTxtTimeStart.setText(timeFormat.format(mCalendarStart.getTime()));
            mTxtTimeEnd.setText(timeFormat.format(mCalendarEnd.getTime()));
            startTime = mCalendarStart.getTimeInMillis();
            endTime = mCalendarEnd.getTimeInMillis();
        }
        timeStartEndError = startTime > endTime;
        if (timeStartEndError) {
            mTxtDataStart.setTextColor(getResources().getColor(R.color.error));
            mTxtTimeStart.setTextColor(getResources().getColor(R.color.error));
        } else {
            mTxtDataStart.setTextColor(getResources().getColor(R.color.black_100));
            mTxtTimeStart.setTextColor(getResources().getColor(R.color.black_100));
        }
    }

    private void openDatePicker(final TextView textView) {
        final Calendar calendar = textView.equals(mTxtDataStart) ? mCalendarStart : mCalendarEnd;
        final Calendar calendarDate = textView.equals(mTxtDataStart) ? mCalendarStartDate : mCalendarEndDate;
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, monthOfYear);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (textView.equals(mTxtDataStart)) {
                    mCalendarStart = calendar;
                    mCalendarStartDate = calendarDate;
                    if (!timeStartEndError) {
                        // плюсуем прошлый timeDif только если без ошибки
                        mCalendarEnd .setTimeInMillis(mCalendarStart.getTimeInMillis() + diff);
                        mCalendarEndDate.setTimeInMillis(mCalendarStartDate.getTimeInMillis() + diffDate);
                    }
                } else if (textView.equals(mTxtDataEnd)) {
                    mCalendarEnd = calendar;
                    mCalendarEndDate = calendarDate;
                }
                fillDataAndTime();
            }
        }, year, month, day);
        dpd.show(getActivity().getFragmentManager(), DATE_PICKER_TAG);
    }

    private void openTimePicker(final TextView textView) {
        final Calendar calendar = textView.equals(mTxtTimeStart) ? mCalendarStart : mCalendarEnd;
        int hour, minute;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                if (textView.equals(mTxtTimeStart)) {
                    mCalendarStart = calendar;
                    if (!timeStartEndError) {
                        // плюсуем прошлый timeDif только если без ошибки
                        mCalendarEnd .setTimeInMillis(mCalendarStart.getTimeInMillis() + diff);
                        mCalendarEndDate.setTimeInMillis(mCalendarStartDate.getTimeInMillis() + diffDate);
                    }
                } else if (textView.equals(mTxtTimeEnd)) {
                    mCalendarEnd = calendar;
                }
                fillDataAndTime();
            }
        }, hour, minute, true);
        tpd.show(getActivity().getFragmentManager(), DATE_PICKER_TAG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNewEvent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNewEvent() {
        if (timeStartEndError) {
            showDialogTimeError();
            return;
        }

        boolean allday = mSwitchWholeDay.isChecked();
        mProgress.show();
        RecurrenceItem recItem = RecurrenceItem.getItemById(mChosenRecurrence);
        GoogleCalendarAPI.getInstance().createEvent(
                new Subscriber<Event>() {
                    @Override
                    public void onCompleted() {
                        Log.e("createEvent", "onCompleted");
                        mProgress.hide();
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("createEvent", String.format("onError: %s", e));
                        mProgress.hide();
                        ImageSnackbar.make(mEditLocation, ImageSnackbar.TYPE_ERROR,
                                "При создании события произошла ошибка", Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(Event event) {
                        Log.e("createEvent", String.format("Event created: %s\n", event.getHtmlLink()));
                    }
                },
                mEditSummary.getText().toString(),
                mEditLocation.getText().toString(),
                allday,
                allday ? mCalendarStartDate : mCalendarStart,
                allday ? mCalendarEndDate : mCalendarEnd,
                recItem,
                mEventReminders
        );
    }

    private void showDialogTimeError() {
        mDialogTimeError = new MaterialDialog.Builder(getActivity())
                .content("Время начала события не может быть позже времени окончания")
                .positiveText(R.string.ok)
                .show();
    }
}
