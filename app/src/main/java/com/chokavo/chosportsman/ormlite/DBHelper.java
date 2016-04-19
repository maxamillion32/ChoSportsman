package com.chokavo.chosportsman.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chokavo.chosportsman.ormlite.dao.SCalendarDao;
import com.chokavo.chosportsman.ormlite.dao.SEventDao;
import com.chokavo.chosportsman.ormlite.dao.SMemberProfileDao;
import com.chokavo.chosportsman.ormlite.dao.SSportProfileDao;
import com.chokavo.chosportsman.ormlite.dao.SSportTypeDao;
import com.chokavo.chosportsman.ormlite.dao.STeamDao;
import com.chokavo.chosportsman.ormlite.dao.SUserDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanFavSportTypeDao;
import com.chokavo.chosportsman.ormlite.models.SEvent;
import com.chokavo.chosportsman.ormlite.models.SMemberProfile;
import com.chokavo.chosportsman.ormlite.models.SSportProfile;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.SCalendar;
import com.chokavo.chosportsman.ormlite.models.STeam;
import com.chokavo.chosportsman.ormlite.models.SUser;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ormlite.models.SportsmanFavSportType;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "chosportsman.db";
    private static final int DATABASE_VERSION = 1;
    private static Context mContext;

    private SCalendarDao mSCalendarDao = null;
    private SEventDao mSEventDao = null;
    private SMemberProfileDao mSMemberProfileDao = null;
    private SSportProfileDao mSSportProfileDao = null;
    private SportsmanDao mSportsmanDao = null;
    private SSportTypeDao mSportTypeDao = null;
    private STeamDao mSTeamDao = null;
    private SUserDao mSUserDao = null;
    private SportsmanFavSportTypeDao mSportsmanFavSportTypeDao = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTableIfNotExists(connectionSource, SCalendar.class);
            TableUtils.createTableIfNotExists(connectionSource, SEvent.class);
            TableUtils.createTableIfNotExists(connectionSource, SMemberProfile.class);
            TableUtils.createTableIfNotExists(connectionSource, Sportsman.class);
            TableUtils.createTableIfNotExists(connectionSource, SportsmanFavSportType.class);
            TableUtils.createTableIfNotExists(connectionSource, SSportProfile.class);
            TableUtils.createTableIfNotExists(connectionSource, SSportType.class);
            TableUtils.createTableIfNotExists(connectionSource, STeam.class);
            TableUtils.createTableIfNotExists(connectionSource, SUser.class);
        }
        catch (SQLException e){
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, SCalendar.class, true);
            TableUtils.dropTable(connectionSource, SEvent.class, true);
            TableUtils.dropTable(connectionSource, SMemberProfile.class, true);
            TableUtils.dropTable(connectionSource, Sportsman.class, true);
            TableUtils.dropTable(connectionSource, SportsmanFavSportType.class, true);
            TableUtils.dropTable(connectionSource, SSportProfile.class, true);
            TableUtils.dropTable(connectionSource, SSportType.class, true);
            TableUtils.dropTable(connectionSource, STeam.class, true);
            TableUtils.dropTable(connectionSource, SUser.class, true);
            onCreate(database, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVersion);
            throw new RuntimeException(e);
        }
    }

    // 1 Календарь
    public SCalendarDao getSCalendarDao() throws SQLException {
        if (mSCalendarDao == null) {
            mSCalendarDao = new SCalendarDao(getConnectionSource());
        }
        return mSCalendarDao;
    }

    // 2 Событие
    public SEventDao getSEventDao() throws SQLException {
        if (mSEventDao == null) {
            mSEventDao = new SEventDao(getConnectionSource());
        }
        return mSEventDao;
    }

    // 3 профиль члена команды
    public SMemberProfileDao getSMemberProfileDao() throws SQLException {
        if (mSMemberProfileDao == null) {
            mSMemberProfileDao = new SMemberProfileDao(getConnectionSource());
        }
        return mSMemberProfileDao;
    }

    // 4 спортсмен
    public SportsmanDao getSportsmanDao() throws SQLException {
        if (mSportsmanDao == null) {
            mSportsmanDao = new SportsmanDao(getConnectionSource());
        }
        return mSportsmanDao;
    }

    // 5 виды спорта -- спортсмен
    public SportsmanFavSportTypeDao getSportsmanFavSportTypeDao() throws SQLException {
        if (mSportsmanFavSportTypeDao == null) {
            mSportsmanFavSportTypeDao = new SportsmanFavSportTypeDao(getConnectionSource());
        }
        return mSportsmanFavSportTypeDao;
    }

    // 6 спортивный профиль
    public SSportProfileDao getSSportProfileDao() throws SQLException {
        if (mSSportProfileDao == null) {
            mSSportProfileDao = new SSportProfileDao(getConnectionSource());
        }
        return mSSportProfileDao;
    }

    // 7 вид спорта
    public SSportTypeDao getSportTypeDao() throws SQLException {
        if (mSportTypeDao == null) {
            mSportTypeDao = new SSportTypeDao(getConnectionSource());
        }
        return mSportTypeDao;
    }

    // 8 команда
    public STeamDao getSTeamDao() throws SQLException {
        if (mSTeamDao == null) {
            mSTeamDao = new STeamDao(getConnectionSource());
        }
        return mSTeamDao;
    }

    // 9 пользователь
    public SUserDao getSUserDao() throws SQLException {
        if (mSUserDao == null) {
            mSUserDao = new SUserDao(getConnectionSource());
        }
        return mSUserDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        mSCalendarDao = null;
        mSEventDao = null;
        mSMemberProfileDao = null;
        mSSportProfileDao = null;
        mSportsmanDao = null;
        mSportTypeDao = null;
        mSTeamDao = null;
        mSUserDao = null;
        mSportsmanFavSportTypeDao = null;
    }
}
