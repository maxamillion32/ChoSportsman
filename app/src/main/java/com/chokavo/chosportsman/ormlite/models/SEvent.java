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

    @SerializedName("event_id")
    @Expose
    @DatabaseField()
    private int serverId;

    @SerializedName("google_api_id")
    private String googleAPIid;

    @SerializedName("calendar_id")
    private int calendarId;

    @SerializedName("sport_type_id")
    private int sportTypeId;
    @SerializedName("sport_type_name")
    private String sportTypeName;

    @SerializedName("event_type")
    private String type; // тренировка или соревнование

    @SerializedName("date_created")
    private Date dateCreated;
    @SerializedName("last_update")
    private Date lastUpdate;

    private SUser owner;
    private SCalendar calendar;
    private SSportType sportType;

    public SEvent() {
    }

    public SEvent(int serverId, int calendarId, String googleAPIid, int sportTypeId, String type) {
        this.serverId = serverId;
        this.calendarId = calendarId;
        this.googleAPIid = googleAPIid;
        this.sportTypeId = sportTypeId;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getGoogleAPIid() {
        return googleAPIid;
    }

    public void setGoogleAPIid(String googleAPIid) {
        this.googleAPIid = googleAPIid;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public int getSportTypeId() {
        return sportTypeId;
    }

    public void setSportTypeId(int sportTypeId) {
        this.sportTypeId = sportTypeId;
    }

    public String getSportTypeName() {
        return sportTypeName;
    }

    public void setSportTypeName(String sportTypeName) {
        this.sportTypeName = sportTypeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SUser getOwner() {
        return owner;
    }

    public void setOwner(SUser owner) {
        this.owner = owner;
    }

    public SCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(SCalendar calendar) {
        this.calendar = calendar;
    }

    public SSportType getSportType() {
        return sportType;
    }

    public void setSportType(SSportType sportType) {
        this.sportType = sportType;
    }

}
