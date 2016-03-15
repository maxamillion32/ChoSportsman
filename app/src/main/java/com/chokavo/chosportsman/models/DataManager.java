package com.chokavo.chosportsman.models;

import android.content.SharedPreferences;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.List;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    private List<SportObjectDataRow> sportObjects;
    private VKApiUserFull vkUser;
    private List<SportKind> mSportKinds;
    private List<SportKind> mUserSports;

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {

    }

    public void setVkUser(VKApiUserFull vkUser) {
        this.vkUser = vkUser;
    }
}
