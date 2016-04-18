package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.models.SUser;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by repitch on 18.04.16.
 */
public class SUserDao extends BaseDaoImpl<SUser, Integer> {

    public SUserDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SUser.class);
    }

    public List<SUser> getAll() throws SQLException {
        return this.queryForAll();
    }
}
