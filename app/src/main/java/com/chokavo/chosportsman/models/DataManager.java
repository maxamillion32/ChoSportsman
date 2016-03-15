package com.chokavo.chosportsman.models;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chokavo.chosportsman.App;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class DataManager {

    public List<SportObjectDataRow> sportObjects;
    public VKApiUserFull vkUser;
    private Set<SportKind> mSportKinds;
    private Set<SportKind> mUserSports;
    public SharedPreferences mPreferences;

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }

    public void setVkUser(VKApiUserFull vkUser, String key) {
        this.vkUser = vkUser;
        mPreferences.edit().putInt(key, vkUser.getId()).apply();
    }

    public void setUserSports(Set<SportKind> sports, String key){
        mUserSports = sports;
        Set<String> sportNames = new HashSet<>();
        for (SportKind sport :
                sports) {
            sportNames.add(sport.getName());
        }
        mPreferences.edit().putStringSet(key, sportNames).apply();
    }
}
