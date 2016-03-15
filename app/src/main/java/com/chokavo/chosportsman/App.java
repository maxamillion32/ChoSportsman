package com.chokavo.chosportsman;

import android.app.Application;
import android.util.Log;

import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;


public class App extends Application {
    public static final String TAG = "Chokavo logs";

    private static App application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        // Инициализация VK
        VKSdk.initialize(this);
    }

    public static App getInstance() {
        return application;
    }

}
