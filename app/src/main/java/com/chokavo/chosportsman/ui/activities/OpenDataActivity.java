package com.chokavo.chosportsman.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chokavo.chosportsman.Constants;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.network.RestInterface;
import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.chokavo.chosportsman.ui.adapters.SportObjectAdapter;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class OpenDataActivity extends NavigationDrawerActivity {

    RecyclerView mRvTariffs;
    SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_open_data, R.id.nav_open_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
        loadData();
    }

    private void initViews() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                loadData();
            }
        });

        mRvTariffs = (RecyclerView) findViewById(R.id.rv_opendata);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvTariffs.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        SportObjectAdapter adapter = new SportObjectAdapter(DataManager.getInstance().sportObjects);
        mRvTariffs.setAdapter(adapter);
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<List<SportObjectDataRow>> call = restInterface.getDataSet(
                Constants.SPORT_OBJECTS_DATASET_ID,
                5,
                (int) (Math.random()*10),
                null,
                "Number"
        );
        call.enqueue(new Callback<List<SportObjectDataRow>>() {
            @Override
            public void onResponse(Call<List<SportObjectDataRow>> call, Response<List<SportObjectDataRow>> response) {
                Log.e("RETRO", "onResponse: " + response.toString());
                DataManager.getInstance().sportObjects = response.body();
                SportObjectAdapter adapter = new SportObjectAdapter(DataManager.getInstance().sportObjects);
                mRvTariffs.swapAdapter(adapter, true);
                mSwipeRefresh.setRefreshing(false);
                ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_SUCCESS, String.format("Данные успешно загружены"), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<SportObjectDataRow>> call, Throwable t) {
                Log.e("RETRO", "onFailure: "+t.toString());
                mSwipeRefresh.setRefreshing(false);
                ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_ERROR, String.format("Возникла ошибка при загрузке данных"), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
