package com.chokavo.chosportsman.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chokavo.chosportsman.ormlite.dao.SportCalendarDao;
import com.chokavo.chosportsman.ormlite.dao.SportTypeDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanDao;
import com.chokavo.chosportsman.ormlite.models.SportCalendar;
import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
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

    private SportCalendarDao mSportCalendarDao = null;
    private SportsmanDao mSportsmanDao = null;
    private SportTypeDao mSportTypeDao = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTableIfNotExists(connectionSource, Sportsman.class);
            TableUtils.createTableIfNotExists(connectionSource, SportCalendar.class);
            TableUtils.createTableIfNotExists(connectionSource, SportType.class);
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
            TableUtils.dropTable(connectionSource, Sportsman.class, true);
            TableUtils.dropTable(connectionSource, SportCalendar.class, true);
            TableUtils.dropTable(connectionSource, SportType.class, true);
            onCreate(database, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVersion);
            throw new RuntimeException(e);
        }
    }

    public SportCalendarDao getSportCalendarDao() throws SQLException {
        if (mSportCalendarDao == null) {
            mSportCalendarDao = new SportCalendarDao(getConnectionSource());
        }
        return mSportCalendarDao;
    }

    public SportsmanDao getSportsmanDao() throws SQLException {
        if (mSportsmanDao == null) {
            mSportsmanDao = new SportsmanDao(getConnectionSource());
        }
        return mSportsmanDao;
    }

    public SportTypeDao getSportTypeDao() throws SQLException {
        if (mSportTypeDao == null) {
            mSportTypeDao = new SportTypeDao(getConnectionSource());
        }
        return mSportTypeDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        mSportCalendarDao = null;
        mSportsmanDao = null;
        mSportTypeDao = null;
    }
}
