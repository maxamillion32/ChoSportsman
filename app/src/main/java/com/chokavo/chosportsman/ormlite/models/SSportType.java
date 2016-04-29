package com.chokavo.chosportsman.ormlite.models;

import android.support.annotation.NonNull;

import com.chokavo.chosportsman.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
@DatabaseTable(tableName = "sport_type")
public class SSportType {

    public final static String ID_FIELD_NAME = "id";

    /**
     * Hadrcode
     */
    public final static int SPORT_FOOTBAL = 1;
    public final static int SPORT_BASKETBALL = 2;
    public final static int SPORT_ICE_HOCKEY = 3;
    public final static int SPORT_VOLLEYBALL= 4;
    public final static int SPORT_TENNIS = 5;
    public final static int SPORT_CURLING = 6;
    public final static int SPORT_SWIMMING = 7;

    public int getIconId() {
        switch (id) {
            case SPORT_FOOTBAL:
                return R.drawable.sport_football;
            case SPORT_BASKETBALL:
                return R.drawable.sport_basketball;
            case SPORT_ICE_HOCKEY:
                return R.drawable.sport_ice_hockey;
            case SPORT_VOLLEYBALL:
                return R.drawable.sport_volleyball;
            case SPORT_TENNIS:
                return R.drawable.sport_tennis;
            case SPORT_CURLING:
                return R.drawable.sport_curling;
            case SPORT_SWIMMING:
                return R.drawable.sport_swimming;
            default:
                return R.drawable.sport_football;
        }
    }

    public int getBigImageId() {
        switch (id) {
            case SPORT_FOOTBAL:
                return R.drawable.sport_big_footbal;
            case SPORT_BASKETBALL:
                return R.drawable.sport_big_basketball;
            case SPORT_ICE_HOCKEY:
                return R.drawable.sport_big_ice_hockey;
            case SPORT_VOLLEYBALL:
                return R.drawable.sport_big_volleyball;
            case SPORT_TENNIS:
                return R.drawable.sport_big_tennis;
            case SPORT_CURLING:
                return R.drawable.sport_big_curling;
            case SPORT_SWIMMING:
                return R.drawable.sport_big_swimming;
            default:
                return R.drawable.test_ava;
        }
    }

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

    public static SSportType findByName(List<SSportType> sportTypes, @NonNull String title) {
        for (SSportType sportType: sportTypes) {
            if (title.equals(sportType.getTitle())) {
                return sportType;
            }
        }
        return null;
    }

    public static SSportType getById(List<SSportType> sportTypes, int sportTypeId) {
        for (SSportType sportType: sportTypes) {
            if (sportType.getId() == sportTypeId) {
                return sportType;
            }
        }
        return null;
    }

    /**
     * Remove all entries of listRemove from listSrc
     * @param listSrc list FROM which we will remove
     * @param listRemove list which we will remove
     */
    public static List<SSportType> diffArrays(List<SSportType> listSrc, List<SSportType> listRemove) {
        List<SSportType> result = new ArrayList<>();
        for (SSportType src: listSrc) {
            boolean toAdd = true;
            for (SSportType remove: listRemove) {
                if (remove.getId() == src.getId()) {
                    toAdd = false;
                    break;
                }
            }
            if (toAdd) {
                result.add(src);
            }
        }
        return result;
    }

    public static CharSequence[] getAsChars(List<SSportType> sportTypes) {
        CharSequence[] chars = new CharSequence[sportTypes.size()];
        int i = 0;
        for (SSportType sportType: sportTypes) {
            chars[i++] = sportType.getTitle();
        }
        return chars;
    }
}
