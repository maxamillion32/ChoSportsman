package com.chokavo.chosportsman.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Дашицырен on 13.03.2016.
 */
public class SportKind {

    @SerializedName("sport_type_id")
    @Expose
    private long mId;

    @SerializedName("name")
    @Expose
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

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }
}
