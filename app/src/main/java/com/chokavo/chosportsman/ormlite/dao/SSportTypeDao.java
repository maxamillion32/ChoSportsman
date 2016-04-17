package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
public class SSportTypeDao extends BaseDaoImpl<SSportType, Integer> {

    public SSportTypeDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SSportType.class);
    }

    public List<SSportType> getAll() throws SQLException {
        return this.queryForAll();
    }

    public void createList(List<SSportType> sportTypes) throws SQLException {
        for (SSportType sportType : sportTypes) {
            createIfNotExists(sportType);
        }
    }
}