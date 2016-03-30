package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.models.SportKind;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public interface SportsmanRestInterface {

    @GET("sporttype/get")
    Call<List<SportKind>> getSportTypes();
}
