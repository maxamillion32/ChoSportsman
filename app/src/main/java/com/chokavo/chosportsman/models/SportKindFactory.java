package com.chokavo.chosportsman.models;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Дашицырен on 13.03.2016.
 */
public class SportKindFactory {
    private ArrayList<SportKind> mSportKinds;
    private static SportKindFactory sFactory;
    private Context mAppContext;

    public SportKindFactory(Context appContext) {
        mAppContext = appContext;
        mSportKinds = new ArrayList<>();
    }

    public static SportKindFactory get(Context context) {
        if (sFactory == null) {
            sFactory = new SportKindFactory(context.getApplicationContext());
        }
        return sFactory;
    }

    public ArrayList<SportKind> getSportKinds() {
        return mSportKinds;
    }

    public void setSportKinds(ArrayList<SportKind> sportKinds) {
        mSportKinds = sportKinds;
    }

    public void addSportKind(SportKind sport) {
        mSportKinds.add(sport);
    }
}
