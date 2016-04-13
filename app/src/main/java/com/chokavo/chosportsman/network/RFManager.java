package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.Constants;
import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.SPORTSMAN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mRestInterface = mRetrofit.create(SportsmanRestInterface.class);
    }

    public static RFManager getInstance() {
        if (sRFManager == null) {
            sRFManager = new RFManager();
        }
        return sRFManager;
    }

    public void getSportTypes(Callback<List<SportType>> callback) {
        Call<List<SportType>> call = mRestInterface.getSportTypes();
        call.enqueue(callback);
    }

    public void vkAuth(int vkId,
                       Callback<Sportsman> callback) {
        Call<Sportsman> call = mRestInterface.vkAuth(vkId);
        call.enqueue(callback);
    }

    public void updateUser(Sportsman sportsman,
                           Callback<Sportsman> callback) {
        Call<Sportsman> call = mRestInterface.updateUser(sportsman.getServerId(), sportsman);
        call.enqueue(callback);
    }

    public void setUserSportTypes(int userId,
                                  List<SportType> sportTypes,
                       Callback<Void> callback) {
        Call<Void> call = mRestInterface.setUserSportTypes(userId, sportTypes);
        call.enqueue(callback);
    }

    public void getUserSportTypes(int userId,
                                  Callback<List<SportType>> callback) {
        Call<List<SportType>> call = mRestInterface.getUserSportTypes(userId);
        call.enqueue(callback);
    }

}
