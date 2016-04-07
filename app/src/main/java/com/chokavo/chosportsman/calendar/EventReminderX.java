package com.chokavo.chosportsman.calendar;

import com.google.api.services.calendar.model.EventReminder;

/**
 * Created by ilyapyavkin on 04.04.16.
 */
public class EventReminderX {

    public static final String REMINDER_METHOD_EMAIL = "email";
    public static final String REMINDER_METHOD_POPUP = "popup";

    public static final EventReminder[] DEFAULT_REMINDERS_OPTIONS = {
            null,
            new EventReminder().setMethod(REMINDER_METHOD_POPUP).setMinutes(0),
            new EventReminder().setMethod(REMINDER_METHOD_POPUP).setMinutes(30),
            new EventReminder().setMethod(REMINDER_METHOD_EMAIL).setMinutes(30),
    };

    public static final EventReminder[] DEFAULT_REMINDERS = {
            new EventReminder().setMethod(REMINDER_METHOD_POPUP).setMinutes(30),
            new EventReminder().setMethod(REMINDER_METHOD_EMAIL).setMinutes(30),
    };

    public static int getDefaultId(EventReminder eventReminder) {
        if (eventReminder == null) {
            return 0;
        }
        for (int i = 1; i< DEFAULT_REMINDERS_OPTIONS.length; i++) {
            if (eventReminder.equals(DEFAULT_REMINDERS_OPTIONS[i])) {
                return i;
            }
        }
        return -1;
    }

    public static CharSequence[] toCharSequence(EventReminder[] reminders) {
        CharSequence[] charSequences = new CharSequence[reminders.length];
        for (int i = 0; i<reminders.length; i++) {
            charSequences[i] = toReadableString(reminders[i]);
        }
        return charSequences;
    }

    public static String toReadableString(EventReminder eventReminder) {
        if (eventReminder == null) {
            return "Не оповещать";
        }
        StringBuilder sb = new StringBuilder();
        if (eventReminder.getMinutes() == null || eventReminder.getMinutes() <= 0) {
            sb.append("Перед мероприятием");
        } else {
            sb.append("За ")
                    .append(eventReminder.getMinutes())
                    .append(" мин.");
        }
        switch (eventReminder.getMethod()) {
            case REMINDER_METHOD_EMAIL:
                sb.append(", Эл. почта");
        }
        return sb.toString();
    }

}
