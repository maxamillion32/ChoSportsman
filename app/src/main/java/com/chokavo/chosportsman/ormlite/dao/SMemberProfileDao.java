package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SMemberProfile;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by repitch on 18.04.16.
 */
public class SMemberProfileDao extends BaseDaoImpl<SMemberProfile, Integer> {

    public SMemberProfileDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SMemberProfile.class);
    }

    public List<SMemberProfile> getAll() throws SQLException {
        return this.queryForAll();
    }
}