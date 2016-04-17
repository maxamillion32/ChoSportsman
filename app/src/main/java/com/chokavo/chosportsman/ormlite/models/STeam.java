package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by repitch on 16.04.16.
 * Модель команды
 */
@DatabaseTable(tableName = "team")
public class STeam {
    @DatabaseField(generatedId = true)
    private transient int id;

    @SerializedName("id")
    @Expose
    @DatabaseField()
    private int serverId;

    private Date dateCreated;

    private Date lastUpdate;

    private SSportType sportType;

    private String locationDesc; // описание положения

    private double locationLat; // latitude - широта
    private double locationLon; // longitude - долгота

    private String name; // название команды

    private int vkGroudId;
    private String site;
    private String email;
    private String photo; // URL
}
