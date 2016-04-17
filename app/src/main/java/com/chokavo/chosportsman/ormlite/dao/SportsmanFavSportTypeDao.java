package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ormlite.models.SportsmanFavSportType;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ilyapyavkin on 13.04.16.
 */
public class SportsmanFavSportTypeDao extends BaseDaoImpl<SportsmanFavSportType, Integer> {

    public SportsmanFavSportTypeDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SportsmanFavSportType.class);
    }

    public List<SportsmanFavSportType> getAll() throws SQLException{
        return this.queryForAll();
    }

    public void createList(Sportsman sportsman, List<SSportType> sportTypes) throws SQLException {
        for (SSportType sportType: sportTypes) {
            this.create(new SportsmanFavSportType(sportsman, sportType));
        }
    }

    public void createListIfNotExist(Sportsman sportsman, List<SSportType> sportTypes) throws SQLException {
        for (SSportType sportType: sportTypes) {
            this.createIfNotExists(new SportsmanFavSportType(sportsman, sportType));
        }
    }

    private PreparedQuery<SSportType> sportTypesForSportsmanQuery = null;

    public List<SSportType> getFavSportTypesForSportsman(Sportsman sportsman) throws SQLException {
        SSportTypeDao stDao = DBHelperFactory.getHelper().getSportTypeDao();
        if (sportTypesForSportsmanQuery == null) {
            sportTypesForSportsmanQuery = makeSportTypesForSportsmanQuery(stDao);
        }
        sportTypesForSportsmanQuery.setArgumentHolderValue(0, sportsman);
        return DBHelperFactory.getHelper().getSportTypeDao().query(sportTypesForSportsmanQuery);
    }

    private PreparedQuery<SSportType> makeSportTypesForSportsmanQuery(SSportTypeDao stDao) throws SQLException {
        // build our inner query for UserPost objects
        QueryBuilder<SportsmanFavSportType, Integer> sportsmanFavSportTypeQb = queryBuilder();
        // just select the post-id field
        sportsmanFavSportTypeQb.selectColumns(SportsmanFavSportType.SPORTTYPE_ID_FIELD_NAME);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        sportsmanFavSportTypeQb.where().eq(SportsmanFavSportType.SPORTSMAN_ID_FIELD_NAME, userSelectArg);

        // build our outer query for Post objects
        QueryBuilder<SSportType, Integer> postQb = stDao.queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(SSportType.ID_FIELD_NAME, sportsmanFavSportTypeQb);
        return postQb.prepare();
    }

}
