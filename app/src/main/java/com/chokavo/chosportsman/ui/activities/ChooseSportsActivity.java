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

import com.chokavo.chosportsman.Constants;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SharedPrefsManager;
import com.chokavo.chosportsman.models.SportKind;
import com.chokavo.chosportsman.network.SportsmanRestInterface;
import com.chokavo.chosportsman.ui.adapters.ChooseSportsAdapter;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        /*SharedPreferences preferences = DataManager.getInstance().mPreferences;
        Set<String> sportKinds = preferences.getStringSet(getString(R.string.sport_kinds),null);
        if (sportKinds != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }*/

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = (RecyclerView)findViewById(R.id.choose_sports_recview);
        adapter = new ChooseSportsAdapter(DataManager.getInstance().getSportKinds());
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

        mSwipeRefresh.setRefreshing(true);
        loadSportTypes();
    }

    private void loadSportTypes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SPORTSMAN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SportsmanRestInterface restInterface = retrofit.create(SportsmanRestInterface.class);
        Call<List<SportKind>> call = restInterface.getSportTypes();
        call.enqueue(new Callback<List<SportKind>>() {
            @Override
            public void onResponse(Call<List<SportKind>> call, Response<List<SportKind>> response) {
                DataManager.getInstance().setSportKinds(response.body());

                adapter = new ChooseSportsAdapter(DataManager.getInstance().getSportKinds());
                recyclerView.swapAdapter(adapter, true);
                mSwipeRefresh.setRefreshing(false);
                ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_SUCCESS, String.format("Данные успешно загружены"), Snackbar.LENGTH_SHORT).show();
                if (DataManager.getInstance().userSportsChosen) {
                    startActivity(new Intent(ChooseSportsActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<SportKind>> call, Throwable t) {
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
            Set<SportKind> mCheckedSports = new HashSet<>();
            Iterator<SportKind> iterator = DataManager.getInstance().getSportKinds().iterator();
            boolean isEmpty = true;
            while( iterator.hasNext()) {
                SportKind sport = iterator.next();
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
            startActivity(new Intent(ChooseSportsActivity.this, MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
