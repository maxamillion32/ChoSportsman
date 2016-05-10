package com.chokavo.chosportsman.ormlite.models;

import com.chokavo.chosportsman.MathUtils;
import com.chokavo.chosportsman.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SSportType getSportType() {
        return sportType;
    }

    public STeam() {
    }

    /**
     * Hadrcode
     */
    public STeam(String name, SSportType sportType) {
        this.name = name;
        this.sportType = sportType;
    }

    public int getIconId() {
        switch (name) {
            case "Lakers":
                return R.drawable.team_lakers;
            case "Локомотив":
                return R.drawable.team_loko;
            case "Manchester United":
                return R.drawable.team_mnu;
            case "Зенит":
                return R.drawable.team_zenit;
            case "Челябинский тракторист":
                return R.drawable.team_track;
            case "Зеленоградский электрон":
                return R.drawable.team_electron;
            case "Еду":
                return R.drawable.team_food;
            case "Сборная МИЭТ по теннису":
                return R.drawable.team_tennis;
            default:
                return R.drawable.team_tennis;
        }
    }

    public static List<STeam> randomTeams(List<STeam> teams, int count) {
        List<STeam> result = new ArrayList<>();
        List<Integer> randInts = MathUtils.getRandomNumbers(0, teams.size(), count);
        for (Integer loc: randInts) {
            result.add(teams.get(loc));
        }
        return result;
    }
}
