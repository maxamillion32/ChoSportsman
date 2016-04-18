package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SSportProfile;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by repitch on 18.04.16.
 */
public class SSportProfileDao extends BaseDaoImpl<SSportProfile, Integer> {

    public SSportProfileDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SSportProfile.class);
    }

    public List<SSportProfile> getAll() throws SQLException {
        return this.queryForAll();
    }
}
