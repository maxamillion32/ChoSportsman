package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SharedPrefsManager;
import com.chokavo.chosportsman.network.RFManager;
import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.dao.SSportTypeDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanFavSportTypeDao;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ui.adapters.ChooseSportsAdapter;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSportsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ChooseSportsAdapter adapter;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sports);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar()!=null)
            getSupportActionBar().setTitle(R.string.choose_sport_please);

        SharedPreferences preferences = DataManager.getInstance().mPreferences;
        Set<String> sportKinds = preferences.getStringSet(getString(R.string.sport_kinds),null);
        if (sportKinds != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = (RecyclerView)findViewById(R.id.choose_sports_recview);
        List<SSportType> sportTypes = DataManager.getInstance().getSportTypes();
        adapter = new ChooseSportsAdapter(sportTypes);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                loadSportTypes();
            }
        });

        if (sportTypes == null || sportTypes.size() == 0) {
            mSwipeRefresh.setRefreshing(true);
            loadSportTypes();
        }
    }

    private void setSportTypes() {
        adapter = new ChooseSportsAdapter(DataManager.getInstance().getSportTypes());
        recyclerView.swapAdapter(adapter, true);
        mSwipeRefresh.setRefreshing(false);
        ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_SUCCESS, String.format("Данные успешно загружены"), Snackbar.LENGTH_SHORT).show();
        if (DataManager.getInstance().userSportsChosen) {
            startActivity(new Intent(ChooseSportsActivity.this, MainActivity.class));
            finish();
        }
    }

    private void loadSportTypes() {
        RFManager.getSportTypes(new Callback<List<SSportType>>() {
            @Override
            public void onResponse(Call<List<SSportType>> call, Response<List<SSportType>> response) {
                DataManager.getInstance().setSportTypes(response.body());
                try {
                    SSportTypeDao stDao = DBHelperFactory.getHelper().getSportTypeDao();
                    stDao.createList(response.body());
                    setSportTypes();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<SSportType>> call, Throwable t) {
                Log.e("RETRO", "onFailure: "+t.toString());
                mSwipeRefresh.setRefreshing(false);
                ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_ERROR, String.format("Возникла ошибка при загрузке данных"), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_sports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_continue) {
            final List<SSportType> mCheckedSports = new ArrayList<>();
            Iterator<SSportType> iterator = DataManager.getInstance().getSportTypes().iterator();
            boolean isEmpty = true;
            while(iterator.hasNext()) {
                SSportType sport = iterator.next();
                if (sport.isChecked()) {
                    mCheckedSports.add(sport);
                    if (isEmpty)
                        isEmpty = false;
                }
            }
            if (isEmpty) {
                Snackbar.make(recyclerView, "Выберите хотя бы один вид спорта", Snackbar.LENGTH_LONG).show();
                return false;
            }
            DataManager.getInstance().userSportsChosen = true;
            SharedPrefsManager.saveUserSportsChosen();

            DataManager.getInstance().setUserSports(mCheckedSports, getString(R.string.sport_kinds));
            RFManager.setUserSportTypes(DataManager.getInstance().mSportsman.getServerId(),
                    mCheckedSports, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e(ChooseSportsActivity.class.getSimpleName(), "onResponse");
                            // TODO save in SQLite
                            saveFavSportsSQLite(DataManager.getInstance().mSportsman,
                                    mCheckedSports);
                            startActivity(new Intent(ChooseSportsActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e(ChooseSportsActivity.class.getSimpleName(), "onFailure: "+t);
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void saveFavSportsSQLite(Sportsman sportsman, List<SSportType> favSportTypes) {
        try {
            SportsmanFavSportTypeDao dao = DBHelperFactory.getHelper().getSportsmanFavSportTypeDao();
            dao.createListIfNotExist(sportsman, favSportTypes);
            DataManager.getInstance().mSportsman.setFavSportTypes(favSportTypes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
