package com.chokavo.chosportsman.ormlite.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
@DatabaseTable(tableName = "sportcalendars")
public class SportCalendar {

    public static final String SPORTSMAN_ID_FIELD_NAME = "sportsman_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(useGetSet = true)
    private int serverId;

    @DatabaseField(useGetSet = true)
    private String googleAPIid;

    @DatabaseField(foreign = true, columnName = SPORTSMAN_ID_FIELD_NAME, useGetSet = true)
    private Sportsman owner;

    @DatabaseField(dataType = DataType.DATE, useGetSet = true)
    private Date dateCreated;

    SportCalendar() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public SportCalendar(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public Sportsman getOwner() {
        return owner;
    }

    public void setOwner(Sportsman owner) {
        this.owner = owner;
    }

    public String getGoogleAPIid() {
        return googleAPIid;
    }

    public void setGoogleAPIid(String googleAPIid) {
        this.googleAPIid = googleAPIid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
