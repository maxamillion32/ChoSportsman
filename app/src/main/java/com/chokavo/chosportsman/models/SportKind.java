package com.chokavo.chosportsman.models;

/**
 * Created by Дашицырен on 13.03.2016.
 */
public class SportKind {

    private String mName;

    public SportKind(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    private boolean mChecked;

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
