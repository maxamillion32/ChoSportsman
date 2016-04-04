package com.chokavo.chosportsman;

import android.app.Application;

import com.chokavo.chosportsman.models.SharedPrefsManager;
import com.vk.sdk.VKSdk;

import net.danlew.android.joda.JodaTimeAndroid;

public class App extends Application {
    public static final String TAG = "Chokavo logs";

    private static App application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        SharedPrefsManager.restoreGoogleAccount();
        SharedPrefsManager.restoreCalendarGAPIid();
        SharedPrefsManager.restoreCalendarCPid();
        SharedPrefsManager.restoreUserSportsChosen();

        // Инициализация VK
        VKSdk.initialize(this);
        // йода тайм
        JodaTimeAndroid.init(this);
    }

    public static App getInstance() {
        return application;
    }

}
