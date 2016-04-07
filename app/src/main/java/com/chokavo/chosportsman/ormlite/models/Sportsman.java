package com.chokavo.chosportsman.ormlite.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
@DatabaseTable(tableName = "sportsmans")
public class Sportsman {
    /**
     * + id : Primary Integer
     + vkid : Integer // id вконтакте
     + sportTypes : SportType[] // массив избранных видов спорта
     + googleAccount : String
     + teams : SportTeam[]
     + dateJoined : DateTime
     */

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField()
    private int serverId;

    @DatabaseField()
    private int vkid;

    @DatabaseField()
    private String googleAccount;

    @DatabaseField()
    private Date dateJoined;

    public Sportsman() {

    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public int getId() {
        return id;
    }

    public int getVkid() {
        return vkid;
    }

    public void setVkid(int vkid) {
        this.vkid = vkid;
    }

    public String getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        this.googleAccount = googleAccount;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
