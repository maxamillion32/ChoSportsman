package com.chokavo.chosportsman.ormlite.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by repitch on 16.04.16.
 * <p/>
 * SMemberProfile (Профиль участника)
 *
 * Это связь пользователя (спортсмена) с командой. Это усовершенствованный SSportProfile,
 * настроенный под определенную команду (позиция на поле, игровой номер, с какой даты участник).
 * Тип связи может быть не только как с спортсменом, но и как с тренером, менеджером или,
 * например даже массажистом
 */
@DatabaseTable(tableName = "member_profile")
public class SMemberProfile {

    public static final int FUNC_TRENER = 1;
    public static final int FUNC_CAPTAIN = 2;
    public static final int FUNC_MANAGER = 3;
    public static final int FUNC_SPORTSMAN = 4;
    public static final int FUNC_FAN = 5;

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MODERATOR = 2;
    public static final int ROLE_SPORTSMAN = 3;
    public static final int ROLE_MEMBER = 4;

    @DatabaseField(generatedId = true)
    private transient int id;

    @SerializedName("id")
    @Expose
    @DatabaseField()
    private int serverId;

    private Date dateCreated;

    private Date lastUpdate;

    private STeam team;

    private SUser user;

    private Date startCareer;

    private Date finishCareer;

    private int role; // см. ROLE_

    private int function; // см. FUNC_

}
