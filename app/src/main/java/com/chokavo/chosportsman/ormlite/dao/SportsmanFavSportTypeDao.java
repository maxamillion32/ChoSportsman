package com.chokavo.chosportsman.ormlite.dao;

import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.models.SportType;
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

    public void createList(Sportsman sportsman, List<SportType> sportTypes) throws SQLException {
        for (SportType sportType: sportTypes) {
            this.create(new SportsmanFavSportType(sportsman, sportType));
        }
    }

    public void createListIfNotExist(Sportsman sportsman, List<SportType> sportTypes) throws SQLException {
        for (SportType sportType: sportTypes) {
            this.createIfNotExists(new SportsmanFavSportType(sportsman, sportType));
        }
    }

    private PreparedQuery<SportType> sportTypesForSportsmanQuery = null;

    public List<SportType> getFavSportTypesForSportsman(Sportsman sportsman) throws SQLException {
        SportTypeDao stDao = DBHelperFactory.getHelper().getSportTypeDao();
        if (sportTypesForSportsmanQuery == null) {
            sportTypesForSportsmanQuery = makeSportTypesForSportsmanQuery(stDao);
        }
        sportTypesForSportsmanQuery.setArgumentHolderValue(0, sportsman);
        return DBHelperFactory.getHelper().getSportTypeDao().query(sportTypesForSportsmanQuery);
    }

    private PreparedQuery<SportType> makeSportTypesForSportsmanQuery(SportTypeDao stDao) throws SQLException {
        // build our inner query for UserPost objects
        QueryBuilder<SportsmanFavSportType, Integer> sportsmanFavSportTypeQb = queryBuilder();
        // just select the post-id field
        sportsmanFavSportTypeQb.selectColumns(SportsmanFavSportType.SPORTTYPE_ID_FIELD_NAME);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        sportsmanFavSportTypeQb.where().eq(SportsmanFavSportType.SPORTSMAN_ID_FIELD_NAME, userSelectArg);

        // build our outer query for Post objects
        QueryBuilder<SportType, Integer> postQb = stDao.queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(SportType.ID_FIELD_NAME, sportsmanFavSportTypeQb);
        return postQb.prepare();
    }

}
