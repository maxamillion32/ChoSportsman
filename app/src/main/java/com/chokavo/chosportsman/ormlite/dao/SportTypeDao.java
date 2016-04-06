package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SportType;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
public class SportTypeDao extends BaseDaoImpl<SportType, Integer> {

    public SportTypeDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SportType.class);
    }

    public List<SportType> getAll() throws SQLException {
        return this.queryForAll();
    }

    public void createList(List<SportType> sportTypes) throws SQLException {
        for (SportType sportType : sportTypes) {
            createIfNotExists(sportType);
        }
    }
}