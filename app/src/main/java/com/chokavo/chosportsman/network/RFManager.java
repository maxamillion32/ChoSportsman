package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.Constants;
import com.chokavo.chosportsman.models.SportKind;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilyapyavkin on 04.04.16.
 * Retrofit manager for sportsman
 */
public class RFManager {

    private static RFManager sRFManager;
    private Retrofit mRetrofit;
    private SportsmanRestInterface mRestInterface;

    public RFManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.SPORTSMAN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRestInterface = mRetrofit.create(SportsmanRestInterface.class);
    }

    public static RFManager getInstance() {
        if (sRFManager == null) {
            sRFManager = new RFManager();
        }
        return sRFManager;
    }

    public void getSportTypes(Callback<List<SportKind>> callback) {
        Call<List<SportKind>> call = mRestInterface.getSportTypes();
        call.enqueue(callback);
    }

}
