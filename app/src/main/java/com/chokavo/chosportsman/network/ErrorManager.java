package com.chokavo.chosportsman.network;

import com.chokavo.chosportsman.ormlite.models.Sportsman;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by repitch on 14.04.16.
 */
public class ErrorManager {
    public static String getErrorString(Response<Sportsman> response) {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        try {
            return response.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "IOexception " + e;
        }
    }
}
