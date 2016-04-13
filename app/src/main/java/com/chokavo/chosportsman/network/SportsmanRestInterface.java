package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public interface SportsmanRestInterface {

    @GET("sporttype/get")
    Call<List<SportType>> getSportTypes();

    @FormUrlEncoded
    @POST("auth/vk")
    Call<Sportsman> vkAuth(@Field("vkid") int vkId);

    @GET("user/{id}/sporttypes")
    Call<List<SportType>> getUserSportTypes(@Path("id") int userId);

    @POST("user/{id}/sporttypes")
    Call<Void> setUserSportTypes(@Path("id") int userId, @Body List<SportType> sportTypes);

    @PUT("user/{id}")
    Call<Sportsman> updateUser(@Path("id") int userId, @Body Sportsman sportsman);
}
