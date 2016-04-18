package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SEvent;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by repitch on 18.04.16.
 */
public class SEventDao extends BaseDaoImpl<SEvent, Integer> {

    public SEventDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SEvent.class);
    }

    public List<SEvent> getAll() throws SQLException {
        return this.queryForAll();
    }
}
