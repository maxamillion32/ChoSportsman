package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.STeam;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by repitch on 18.04.16.
 */
public class STeamDao extends BaseDaoImpl<STeam, Integer> {
    public STeamDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, STeam.class);
    }

    public List<STeam> getAll() throws SQLException {
        return this.queryForAll();
    }
}
