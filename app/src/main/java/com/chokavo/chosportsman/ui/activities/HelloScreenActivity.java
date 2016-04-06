package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ui.fragments.helloscreen.SplashFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.sql.SQLException;

public class HelloScreenActivity extends BaseActivity {

    private View mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_screen);
        mContentFrame = findViewById(R.id.content_frame);

        launchFragmentNoBackStack(new SplashFragment(),
                SplashFragment.getFragmentTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                Log.e(HelloScreenActivity.class.getSimpleName(), "VKSdk onResult");
                saveUser();
            }

            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Log.e(HelloScreenActivity.class.getSimpleName(), "VKSdk onError");
                ImageSnackbar.make(mContentFrame, ImageSnackbar.TYPE_ERROR, "Ошибка при авторизации", Snackbar.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void saveUser() {
        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200,sex,bdate,city"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> vkUsers = ((VKList) response.parsedModel);
                VKApiUserFull vkUser = vkUsers.get(0);
                DataManager.getInstance().setVkUser(vkUser, getString(R.string.vk_user_id));
                // TODO регистрация юзера на нашем сайте
                // есть vkUser - сохраняем в бд и на сервер
                saveUserSQLite();

                startActivity(new Intent(HelloScreenActivity.this, ChooseSportsActivity.class));
                finish();
                super.onComplete(response);
            }
        });
    }

    /**
     * Сохранение юзера в базу данных SQLite
     */
    private void saveUserSQLite() {
        try {
            VKApiUserFull vkUser = DataManager.getInstance().vkUser;
            Sportsman sportsman = new Sportsman();
            sportsman.setVkid(vkUser.id);
//                sportsman.setServerId(serverId);
            DBHelperFactory.getHelper().getSportsmanDao().createIfNotExists(sportsman);
            DataManager.getInstance().mSportsman = sportsman;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
