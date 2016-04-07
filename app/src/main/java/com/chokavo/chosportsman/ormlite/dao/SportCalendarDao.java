package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SportCalendar;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
public class SportCalendarDao extends BaseDaoImpl<SportCalendar, Integer> {

    public SportCalendarDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SportCalendar.class);
    }

    public List<SportCalendar> getAll() throws SQLException{
        return this.queryForAll();
    }
}