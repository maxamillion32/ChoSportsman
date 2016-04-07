package com.chokavo.chosportsman.models;

import android.content.Context;

import com.chokavo.chosportsman.R;

/**
 * Created by ilyapyavkin on 28.03.16.
 */
public class SportEventType {
    // HardCode
    public static final int TYPE_TRAINING = 1;
    public static final int TYPE_COMPETITION = 2;

    public static final int[] TYPES = {
            TYPE_TRAINING,
            TYPE_COMPETITION
    };

    public static CharSequence[] getEventTypesAsChars(Context context) {
        CharSequence[] chars = new CharSequence[TYPES.length];
        for (int i=0; i<chars.length; i++) {
            chars[i] = context.getString(getStringId(TYPES[i]));
        }
        return chars;
    }

    private int mType;

    public SportEventType(int type) {
        mType = type;
    }

    public static int getStringId(int type) {
        switch (type) {
            case TYPE_TRAINING:
                return R.string.type_training;
            case TYPE_COMPETITION:
                return R.string.type_competition;
        }
        return -1;
    }

    public int getStringId() {
        return getStringId(mType);
    }
}
