package com.chokavo.chosportsman.calendar;

import java.util.Calendar;

/**
 * Created by repitch on 19.04.16.
 *
 * Помощник для календаря
 */
public class CalendarX {
    // Helper for calendar

    public static boolean sameDay(Calendar cal1, Calendar cal2) {
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

}
