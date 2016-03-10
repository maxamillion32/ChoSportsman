package com.chokavo.chosportsman.models;

import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.List;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<SportObjectDataRow> sportObjects;
    public VKApiUserFull vkUser;

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }
}
