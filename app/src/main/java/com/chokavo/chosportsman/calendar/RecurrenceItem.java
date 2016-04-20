package com.chokavo.chosportsman.calendar;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chokavo.chosportsman.R;

/**
 * Created by ilyapyavkin on 04.04.16.
 */
public class RecurrenceItem {
    public static final String RRULE = "RRULE:";
    public static final String FREQ = "FREQ=";
    public static final String FREQ_NO = "";
    public static final String FREQ_EVERY_DAY = "DAILY";
    public static final String FREQ_EVERY_WEEK = "WEEKLY";
    public static final String FREQ_EVERY_MONTH = "MONTHLY";
    public static final String FREQ_EVERY_YEAR = "YEARLY";

    public static final String[] FREQS = {
            FREQ_NO,
            FREQ_EVERY_DAY,
            FREQ_EVERY_WEEK,
            FREQ_EVERY_MONTH,
            FREQ_EVERY_YEAR
    };

    // HardCode
    public static final int TYPE_NO = 1;
    public static final int TYPE_EVERY_DAY = 2;
    public static final int TYPE_EVERY_WEEK = 3;
    public static final int TYPE_EVERY_MONTH = 4;
    public static final int TYPE_EVERY_YEAR = 5;

    public static final int[] TYPES = {
            TYPE_NO,
            TYPE_EVERY_DAY,
            TYPE_EVERY_WEEK,
            TYPE_EVERY_MONTH,
            TYPE_EVERY_YEAR
    };

    public static RecurrenceItem getItemByChar(@NonNull CharSequence[] chars, CharSequence charSequence) {
        for (int i = 0; i < chars.length; i++) {
            if (chars.equals(charSequence)) {
                return new RecurrenceItem(TYPES[i]);
            }
        }
        return null;
    }

    public static CharSequence[] getAsChars(Context context) {
        CharSequence[] chars = new CharSequence[TYPES.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = context.getString(getStringId(TYPES[i]));
        }
        return chars;
    }

    private int mType;

    public RecurrenceItem(int type) {
        mType = type;
    }

    public static int getStringId(int type) {
        switch (type) {
            case TYPE_NO:
                return R.string.recurrence_type_no;
            case TYPE_EVERY_DAY:
                return R.string.recurrence_type_every_day;
            case TYPE_EVERY_WEEK:
                return R.string.recurrence_type_every_week;
            case TYPE_EVERY_MONTH:
                return R.string.recurrence_type_every_month;
            case TYPE_EVERY_YEAR:
                return R.string.recurrence_type_every_year;
        }
        return -1;
    }

    public String getAsRule() {
        String rule = RRULE;
        if (mType == TYPE_NO) {

        } else {
            rule += FREQ + FREQS[getTypeId()];
        }
        return rule;
    }

    public int getTypeId() {
        return getTypeId(mType);
    }

    public static int getTypeId(int type) {
        for (int i = 0; i < TYPES.length; i++) {
            if (type == TYPES[i]) {
                return i;
            }
        }
        return -1;
    }

    public int getStringId() {
        return getStringId(mType);
    }

    public static RecurrenceItem getItemById(int id) {
        if (id >= 0 && id < TYPES.length) {
            return new RecurrenceItem(TYPES[id]);
        }
        return null;
    }

    public int getType() {
        return mType;
    }

    public static int getTypeFromRule(String rule){
        switch (rule) {
            case RRULE+FREQ+FREQ_EVERY_DAY:
                return TYPE_EVERY_DAY;
            case RRULE+FREQ+FREQ_EVERY_WEEK:
                return TYPE_EVERY_WEEK;
            case RRULE+FREQ+FREQ_EVERY_MONTH:
                return TYPE_EVERY_MONTH;
            case RRULE+FREQ+FREQ_EVERY_YEAR:
                return TYPE_EVERY_YEAR;
        }
        return -1;
    }
}
