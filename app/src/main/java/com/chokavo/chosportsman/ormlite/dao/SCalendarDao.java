package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SCalendar;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
public class SCalendarDao extends BaseDaoImpl<SCalendar, Integer> {

    public SCalendarDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SCalendar.class);
    }

    public List<SCalendar> getAll() throws SQLException{
        return this.queryForAll();
    }
}