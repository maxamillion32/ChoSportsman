package com.chokavo.opendatamos.models;

import com.chokavo.opendatamos.network.DataRow;

import java.util.List;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<DataRow> tariffs;

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }
}
