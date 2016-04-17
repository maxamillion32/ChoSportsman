package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by repitch on 16.04.16.
 *
 * SSportProfile (Спортивный профиль)
 *
 * Привязка SUser (пользователя) к SSportType (виду спорта) с кучей дополнительных полей.
 * Создав привязку к одному из видов спорта юзер становится спортсменом (isSportsman = true)
 */
@DatabaseTable(tableName = "sport_profile")
public class SSportProfile {
    @DatabaseField(generatedId = true)
    private transient int id;

    @SerializedName("id")
    @Expose
    @DatabaseField()
    private int serverId;

    private Date dateCreated;

    private Date lastUpdate;

    private SSportType sportType;

    private SUser user;

    private Date startCareer;

    private Date finishCareer;

    private String nickname;
}
