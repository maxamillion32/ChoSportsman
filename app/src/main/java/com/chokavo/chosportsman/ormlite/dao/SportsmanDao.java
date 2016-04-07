package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
public class SportsmanDao extends BaseDaoImpl<Sportsman, Integer> {

    public SportsmanDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Sportsman.class);
    }

    public List<Sportsman> getAll() throws SQLException{
        return this.queryForAll();
    }
}