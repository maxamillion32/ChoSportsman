package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public interface SportsmanRestInterface {

    @GET("sporttype/get")
    Call<List<SportType>> getSportTypes();

    @FormUrlEncoded
    @POST("auth/vk")
    Call<Sportsman> vkAuth(@Field("vkid") int vkId);
}
