package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

/**
 * Created by repitch on 16.04.16.
 *
 * SUser (Пользователь)
 * Любой человек, который зашел в наше приложение.
 * Новый пользователь создается в самом запуске Android приложение,
 * когда человек заходит под своим аккаунтом в ВК.
 * При создании обязательный параметр - vk_id
 *
 */
@DatabaseTable(tableName = "user")
public class SUser {
    @DatabaseField(generatedId = true)
    private transient int id;

    @SerializedName("id")
    @Expose
    @DatabaseField()
    private int serverId;

    private int vkId;

    private Date dateCreated;

    private Date lastUpdate;

    private String googleAccount;

    private List<SSportType> sportTypes;

    private boolean isSportsman;
}
