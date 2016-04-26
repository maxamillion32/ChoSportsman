package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.Constants;
import com.chokavo.chosportsman.ormlite.models.SCalendar;
import com.chokavo.chosportsman.ormlite.models.SEvent;
import com.chokavo.chosportsman.ormlite.models.SSportType;
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

    public static void getSportTypes(Callback<List<SSportType>> callback) {
        Call<List<SSportType>> call = getInstance().mRestInterface.getSportTypes();
        call.enqueue(callback);
    }

    public static void vkAuth(int vkId,
                       Callback<Sportsman> callback) {
        Call<Sportsman> call = getInstance().mRestInterface.vkAuth(vkId);
        call.enqueue(callback);
    }

    public static void getUser(int userId,
                           Callback<Sportsman> callback) {
        Call<Sportsman> call = getInstance().mRestInterface.getUser(userId);
        call.enqueue(callback);
    }

    public static void updateUser(Sportsman sportsman,
                           Callback<Sportsman> callback) {
        Call<Sportsman> call = getInstance().mRestInterface.updateUser(sportsman.getServerId(), sportsman);
        call.enqueue(callback);
    }

    public static void setUserSportTypes(int userId,
                                  List<SSportType> sportTypes,
                       Callback<Void> callback) {
        Call<Void> call = getInstance().mRestInterface.setUserSportTypes(userId, sportTypes);
        call.enqueue(callback);
    }

    public static void getUserSportTypes(int userId,
                                  Callback<List<SSportType>> callback) {
        Call<List<SSportType>> call = getInstance().mRestInterface.getUserSportTypes(userId);
        call.enqueue(callback);
    }

    /**
     * SCalendar / Календарь
     */
    public static void createCalendar(
            int userId,
            String googleApiId,
            Callback<SCalendar> callback) {
        Call<SCalendar> call = getInstance().mRestInterface.createCalendar(userId, googleApiId);
        call.enqueue(callback);
    }

    /**
     * SEvent / Событие
     */
    public static void createEvent(
            int userId,
            String googleApiId,
            SEvent event,
            Callback<SEvent> callback) {
        Call<SEvent> call = getInstance().mRestInterface.createEvent(userId, googleApiId, event);
        call.enqueue(callback);
    }

}
