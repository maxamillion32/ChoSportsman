package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
@DatabaseTable(tableName = "sport_type")
public class SSportType {

    public final static String ID_FIELD_NAME = "id";

    @SerializedName("sport_type_id")
    @Expose
    @DatabaseField(id = true)
    private int id;

    @SerializedName("sport_type_name")
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

    SSportType() {

    }

    public SSportType(int id) {
        this.id = id;
    }

    public SSportType(String title) {
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
