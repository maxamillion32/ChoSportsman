package com.chokavo.chosportsman.ormlite;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by ilyapyavkin on 06.04.16.
 */
public class DBHelperFactory {
    private static DBHelper sDBHelper;

    public static DBHelper getHelper(){
        if (sDBHelper == null) {
            throw new RuntimeException("DBHelper is not initiated");
        }
        return sDBHelper;
    }
    public static void initHelper(Context context){
        sDBHelper = OpenHelperManager.getHelper(context, DBHelper.class);
    }
    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        sDBHelper = null;
    }
}
