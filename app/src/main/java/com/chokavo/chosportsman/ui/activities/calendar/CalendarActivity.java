package com.chokavo.chosportsman.ui.activities.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.activities.NavigationDrawerActivity;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by repitch on 10.03.16.
 */
public class CalendarActivity extends NavigationDrawerActivity {

    private FlexibleCalendarView mCalendarView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_calendar, R.id.nav_calendar);

        mCalendarView = (FlexibleCalendarView) findViewById(R.id.month_view);
        mCalendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, int direction) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, 1);
                Toast.makeText(CalendarActivity.this, cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " " + year, Toast.LENGTH_SHORT).show();
            }
        });

        mCalendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                if (year == 2015 && month == 7 && day == 25) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if (year == 2015 && month == 7 && day == 8) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                    colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                if (year == 2015 && month == 7 && day == 5) {
                    List<CustomEvent> colorLst1 = new ArrayList<>();
                    colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                    return colorLst1;
                }
                return null;
            }
        });

        mButton = (Button)findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCalendarView.isShown()){
                    mCalendarView.collapse();
                    mButton.setText(R.string.to_show);
                }else{
                    mCalendarView.expand();
                    mButton.setText(R.string.to_hide);
                }
            }
        });
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

}
