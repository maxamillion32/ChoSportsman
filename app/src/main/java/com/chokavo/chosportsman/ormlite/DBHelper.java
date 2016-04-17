package com.chokavo.chosportsman.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chokavo.chosportsman.ormlite.dao.SCalendarDao;
import com.chokavo.chosportsman.ormlite.dao.SSportTypeDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanFavSportTypeDao;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.SCalendar;
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
    private SportsmanDao mSportsmanDao = null;
    private SSportTypeDao mSportTypeDao = null;
    private SportsmanFavSportTypeDao mSportsmanFavSportTypeDao = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTableIfNotExists(connectionSource, Sportsman.class);
            TableUtils.createTableIfNotExists(connectionSource, SCalendar.class);
            TableUtils.createTableIfNotExists(connectionSource, SSportType.class);
            TableUtils.createTableIfNotExists(connectionSource, SportsmanFavSportType.class);
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
            TableUtils.dropTable(connectionSource, SCalendar.class, true);
            TableUtils.dropTable(connectionSource, SSportType.class, true);
            TableUtils.dropTable(connectionSource, SportsmanFavSportType.class, true);
            onCreate(database, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVersion);
            throw new RuntimeException(e);
        }
    }

    public SCalendarDao getSCalendarDao() throws SQLException {
        if (mSCalendarDao == null) {
            mSCalendarDao = new SCalendarDao(getConnectionSource());
        }
        return mSCalendarDao;
    }

    public SportsmanDao getSportsmanDao() throws SQLException {
        if (mSportsmanDao == null) {
            mSportsmanDao = new SportsmanDao(getConnectionSource());
        }
        return mSportsmanDao;
    }

    public SSportTypeDao getSportTypeDao() throws SQLException {
        if (mSportTypeDao == null) {
            mSportTypeDao = new SSportTypeDao(getConnectionSource());
        }
        return mSportTypeDao;
    }

    public SportsmanFavSportTypeDao getSportsmanFavSportTypeDao() throws SQLException {
        if (mSportsmanFavSportTypeDao == null) {
            mSportsmanFavSportTypeDao = new SportsmanFavSportTypeDao(getConnectionSource());
        }
        return mSportsmanFavSportTypeDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        mSCalendarDao = null;
        mSportsmanDao = null;
        mSportTypeDao = null;
        mSportsmanFavSportTypeDao = null;
    }
}
