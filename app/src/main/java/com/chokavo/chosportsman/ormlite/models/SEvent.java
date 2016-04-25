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

    public static Builder newBuilder() {
        return new SEvent().new Builder();
    }

    public class Builder {
        private Builder() {

        }

        public Builder setServerId(int serverId) {
            SEvent.this.serverId = serverId;
            return this;
        }

        public Builder setCalendar(SCalendar calendar) {
            SEvent.this.calendar = calendar;
            return this;
        }

        public Builder setDateCreated(Date dateCreated) {
            SEvent.this.dateCreated = dateCreated;
            return this;
        }

        public Builder setLastUpdate(Date lastUpdate) {
            SEvent.this.lastUpdate = lastUpdate;
            return this;
        }

        public Builder setSportType(SSportType sportType) {
            SEvent.this.sportType = sportType;
            return this;
        }

        public Builder setType(String type) {
            SEvent.this.type = type;
            return this;
        }

        public SEvent build() {
            return SEvent.this;
        }
    }

}
