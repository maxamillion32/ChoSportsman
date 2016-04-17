package com.chokavo.chosportsman.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SharedPrefsManager;
import com.chokavo.chosportsman.network.RFManager;
import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.dao.SportsmanFavSportTypeDao;
import com.chokavo.chosportsman.ormlite.models.SSportType;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelloScreenActivity extends BaseActivity {

    private View mContentFrame;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_screen);
        mContentFrame = findViewById(R.id.content_frame);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.wait_second));

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
        mProgress.show();
        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200,sex,bdate,city"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> vkUsers = ((VKList) response.parsedModel);
                VKApiUserFull vkUser = vkUsers.get(0);
                DataManager.getInstance().setVkUser(vkUser, getString(R.string.vk_user_id));

                vkAuth(vkUser.id);
                super.onComplete(response);
            }
        });
    }

    private void vkAuth(int id) {
        RFManager.getInstance().vkAuth(id,
                new Callback<Sportsman>() {
                    @Override
                    public void onResponse(Call<Sportsman> call, Response<Sportsman> response) {
                        if (response != null && response.isSuccess()) {
                            // регистрация на сайте успешна
                            Sportsman sportsman = response.body();
                            // сохранине в базе данных SQLite
                            saveUserSQLite(sportsman);
                            // проверим - есть ли у данного юзера на сервере "любимые виды спорта"
                            checkUserSportTypes(sportsman);
                        } else {
                            // error
                            int code = response.raw().code();
                            try {
                                String errorStr = response.errorBody().string();
                                Log.e(HelloScreenActivity.class.getSimpleName(),
                                        "Response error: "+errorStr);
                                ImageSnackbar.make(mContentFrame, ImageSnackbar.TYPE_ERROR,
                                        String.format("Ошибка %d на сайте", code), Snackbar.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Sportsman> call, Throwable t) {
                        Log.e(HelloScreenActivity.class.getSimpleName(),
                                "Error logging out site: "+t.getLocalizedMessage());
                        ImageSnackbar.make(mContentFrame, ImageSnackbar.TYPE_ERROR,
                                "Ошибка при регистрации на сайте", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void checkUserSportTypes(final Sportsman sportsman) {
        RFManager.getInstance().getUserSportTypes(sportsman.getServerId(),
                new Callback<List<SSportType>>() {
                    @Override
                    public void onResponse(Call<List<SSportType>> call, Response<List<SSportType>> response) {
                        mProgress.hide();
                        List<SSportType> favSportTypes = response.body();
                        if (favSportTypes.size() == 0) {
                            // любимых видов спорта нет - открываем соответствующее активити
                            startActivity(new Intent(HelloScreenActivity.this, ChooseSportsActivity.class));
                            finish();
                        } else {
                            // любимые виды спорта есть - сохраняем их ORMLite и выходим
                            saveFavSportsSQLite(sportsman, favSportTypes);
                            startActivity(new Intent(HelloScreenActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SSportType>> call, Throwable t) {
                        mProgress.hide();
                        Log.e(HelloScreenActivity.class.getSimpleName(), "Ошибка при получении избранных видов спорта: "+t);
                        ImageSnackbar.make(mContentFrame, ImageSnackbar.TYPE_ERROR,
                                "Ошибка при получении избранных видов спорта", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void saveFavSportsSQLite(Sportsman sportsman, List<SSportType> favSportTypes) {
        try {
            SportsmanFavSportTypeDao dao = DBHelperFactory.getHelper().getSportsmanFavSportTypeDao();
            dao.createListIfNotExist(sportsman, favSportTypes);
            DataManager.getInstance().mSportsman.setFavSportTypes(favSportTypes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сохранение юзера в базу данных SQLite
     */
    private void saveUserSQLite(Sportsman sportsman) {
        try {
            DBHelperFactory.getHelper().getSportsmanDao().createIfNotExists(sportsman);
            DataManager.getInstance().userIdOTMLite = sportsman.getId();
            SharedPrefsManager.saveUserIdORMLite();
            DataManager.getInstance().mSportsman = sportsman;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
