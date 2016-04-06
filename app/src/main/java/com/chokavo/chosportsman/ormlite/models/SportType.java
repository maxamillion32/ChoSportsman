package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
@DatabaseTable(tableName = "sporttypes")
public class SportType {

    @SerializedName("sport_type_id")
    @Expose
    @DatabaseField(id = true)
    private int id;

    @SerializedName("name")
    @Expose
    @DatabaseField(useGetSet = true)
    private String title;

    @DatabaseField(useGetSet = true)
    private String iconUrl;

    @DatabaseField(useGetSet = true)
    private String backgroundUrl;

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    SportType() {

    }

    public SportType(int id) {
        this.id = id;
    }

    public SportType(String title) {
        this.title = title;
    }

    private boolean mChecked;

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
