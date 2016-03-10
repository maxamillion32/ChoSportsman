package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public interface RestInterface {

    @GET("datasets/{id}/rows")
    Call<List<SportObjectDataRow>> getDataSet(
            @Path("id") Integer dataSetId,
            @Query("$top") Integer top,
            @Query("$skip") Integer skip,
            @Query("$inlinecount") String inlinecount,
            @Query("$orderby") String orderby);
}
