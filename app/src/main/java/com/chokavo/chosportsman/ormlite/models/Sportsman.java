package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
@DatabaseTable(tableName = "sportsman")
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

    @SerializedName("user_id")
    @Expose
    @DatabaseField()
    private int serverId;

    @SerializedName("vk_id")
    @Expose
    @DatabaseField()
    private int vkid;

    @SerializedName("googleAccount")
    @Expose
    @DatabaseField()
    private String googleAccount;

    @SerializedName("date_joined")
    @Expose
    @DatabaseField()
    private Date dateJoined;

    @SerializedName("last_update")
    @Expose
    @DatabaseField()
    private Date lastUpdate;

    List<SportType> favSportTypes;

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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<SportType> getFavSportTypes() {
        return favSportTypes;
    }

    public void setFavSportTypes(List<SportType> favSportTypes) {
        this.favSportTypes = favSportTypes;
    }
}
