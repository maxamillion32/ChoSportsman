package com.chokavo.opendatamos.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chokavo.opendatamos.Constants;
import com.chokavo.opendatamos.R;
import com.chokavo.opendatamos.models.DataManager;
import com.chokavo.opendatamos.network.DataRow;
import com.chokavo.opendatamos.network.RestInterface;
import com.chokavo.opendatamos.ui.adapters.TariffsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class TariffsActivity extends BaseActivity {

    RecyclerView mRvTariffs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariffs);
        initViews();
        loadTariffs();
    }

    private void initViews() {
        mRvTariffs = (RecyclerView) findViewById(R.id.rv_tariffs);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvTariffs.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        TariffsAdapter adapter = new TariffsAdapter(DataManager.getInstance().tariffs);
        mRvTariffs.setAdapter(adapter);
    }

    private void loadTariffs() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<List<DataRow>> call = restInterface.getDataSet(
                Constants.TARIFFS_DATASET_ID,
                3,
                null,
                null,
                "Number"
        );
        call.enqueue(new Callback<List<DataRow>>() {
            @Override
            public void onResponse(Call<List<DataRow>> call, Response<List<DataRow>> response) {
                Log.e("RETRO", "onResponse: " + response.toString());
                DataManager.getInstance().tariffs = response.body();
                TariffsAdapter adapter = new TariffsAdapter(DataManager.getInstance().tariffs);
                mRvTariffs.swapAdapter(adapter, true);
            }

            @Override
            public void onFailure(Call<List<DataRow>> call, Throwable t) {
                Log.e("RETRO", "onFailure: "+t.toString());
            }
        });
    }
}
