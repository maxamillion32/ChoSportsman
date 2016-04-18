package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by repitch on 16.04.16.
 * Модель события -
 */
@DatabaseTable(tableName = "event")
public class SEvent {
    @DatabaseField(generatedId = true)
    private transient int id;

    @SerializedName("id") // TODO
    @Expose
    @DatabaseField()
    private int serverId;

    private String googleAPIid;
    private SUser owner;
    private SCalendar calendar;

    private SSportType sportType;
    private String type; // тренировка или соревнование

    private Date dateCreated;
    private Date lastUpdate;
}
