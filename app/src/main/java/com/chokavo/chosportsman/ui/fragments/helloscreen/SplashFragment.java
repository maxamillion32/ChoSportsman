package com.chokavo.chosportsman.ui.fragments.helloscreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.network.RFManager;
import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.dao.SportTypeDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanDao;
import com.chokavo.chosportsman.ormlite.dao.SportsmanFavSportTypeDao;
import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.MainActivity;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.sql.SQLException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ilyapyavkin on 01.04.16.
 */
public class SplashFragment extends BaseFragment {

    private static final int WAIT_MS = 3000;
    ProgressBar mProgressSplash;

    private boolean
            waitDone = false,
            justWait = false;
    private boolean
            favSportTypesLoaded = false,
            vkProfileLoaded = false,
            sportTypesLoaded = false;

    @Override
    public String getFragmentTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        initViews(rootView);

        if (VKSdk.isLoggedIn()) {
            // залогинены, подгружаем все необходимые данные
            // TODO: если устройство онлайн - то с сервера, иначе из SQLite
            // 0 загружаем информацию из ВКонтакте
            loadVKProfile();
            // 1 загружаем все виды спорта
            loadSportTypes();
            // тестируем sqlite
            try {
                SportsmanDao sportsmanDao = DBHelperFactory.getHelper().getSportsmanDao();
                SportTypeDao sportTypeDao = DBHelperFactory.getHelper().getSportTypeDao();
                SportsmanFavSportTypeDao sportsmanFavSportTypeDao = DBHelperFactory.getHelper().getSportsmanFavSportTypeDao();
                // берем id текущего юзера в sqlite таблице
                int userIdSQLite = DataManager.getInstance().userIdOTMLite;
                if (userIdSQLite > 0) {
                    // извлекаем текущего юзера из SQLite и сохраняем в DataManager
                    Sportsman currentSportsman = sportsmanDao.queryForId(userIdSQLite);
                    DataManager.getInstance().mSportsman = currentSportsman;
                    // из SQLite берем любимые виды спорта юзера
                    loadFavSportTypes(currentSportsman);
                } else {
                    // в SharedPrefs не сохранено id текущего юзера
                    // TODO либо взять с сервера, либо query по каким либо данным
                }

                Log.e("","");
            } catch (SQLException e) {
                ImageSnackbar.make(rootView, ImageSnackbar.TYPE_ERROR, "SQLite error: "+e, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            // просто ждем
            justWait = true;
        }

        (new WaitTask()).execute();

        return rootView;
    }

    private void loadVKProfile() {
        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200,photo_400_orig,sex,bdate,city"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> vkUsers = ((VKList) response.parsedModel);
                VKApiUserFull vkUser = vkUsers.get(0);
                DataManager.getInstance().setVkUser(vkUser,getString(R.string.vk_user_id));
                vkProfileLoaded = true;
                tryMovingOut();
                super.onComplete(response);
            }
        });
    }

    private void loadFavSportTypes(final Sportsman sportsman) {
        try {
            final SportsmanFavSportTypeDao dao = DBHelperFactory.getHelper().getSportsmanFavSportTypeDao();
            final List<SportType> favSportTypes = dao.getFavSportTypesForSportsman(sportsman);
            if (favSportTypes == null || favSportTypes.size() == 0) {
                // в базе данных видов спорта нет, добавим их через retrofit
                RFManager.getInstance().getUserSportTypes(sportsman.getServerId(),
                        new Callback<List<SportType>>() {
                    @Override
                    public void onResponse(Call<List<SportType>> call, Response<List<SportType>> response) {
                        List<SportType> favSportTypes = response.body();
                        DataManager.getInstance().mSportsman.setFavSportTypes(favSportTypes);

                        try {
                            dao.createListIfNotExist(sportsman, favSportTypes);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        favSportTypesLoaded = true;
                        tryMovingOut();
//                mProgressSplash.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<SportType>> call, Throwable t) {
                        Log.e("RETRO", "onFailure: "+t.toString());
                        mProgressSplash.setVisibility(View.GONE);
                        ImageSnackbar.make(mProgressSplash, ImageSnackbar.TYPE_ERROR, String.format("Возникла ошибка при загрузке избранных видов спорта"), Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                // в базе данных есть виды спорта. Можно выгрузить в DataManager
                DataManager.getInstance().mSportsman.setFavSportTypes(favSportTypes);
                favSportTypesLoaded = true;
                tryMovingOut();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSportTypes() {
        try {
            final SportTypeDao stDao = DBHelperFactory.getHelper().getSportTypeDao();
            final List<SportType> sportTypes = stDao.getAll();
            if (sportTypes == null || sportTypes.size() == 0) {
                // в базе данных видов спорта нет, добавим их через retrofit
                RFManager.getInstance().getSportTypes(new Callback<List<SportType>>() {
                    @Override
                    public void onResponse(Call<List<SportType>> call, Response<List<SportType>> response) {
                        DataManager.getInstance().setSportTypes(response.body());
                        try {
                            stDao.createList(response.body());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sportTypesLoaded = true;
                        tryMovingOut();
//                mProgressSplash.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<SportType>> call, Throwable t) {
                        Log.e("RETRO", "onFailure: "+t.toString());
                        mProgressSplash.setVisibility(View.GONE);
                        ImageSnackbar.make(mProgressSplash, ImageSnackbar.TYPE_ERROR, String.format("Возникла ошибка при загрузке видов спорта"), Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                // в базе данных есть виды спорта. Можно выгрузить в DataManager
                DataManager.getInstance().setSportTypes(sportTypes);
                sportTypesLoaded = true;
                tryMovingOut();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void initViews(View rootView) {
        mProgressSplash = (ProgressBar) rootView.findViewById(R.id.progress_splash);
    }

    private void tryMovingOut() {
        if ((sportTypesLoaded && favSportTypesLoaded && vkProfileLoaded || justWait) && waitDone) {
            // проверим, есть ли сейчас активный аккаунт
            if (!VKSdk.isLoggedIn()) {
                launchAnimationFragment(new VKAuthFragment(),
                        VKAuthFragment.getFragmentTag(),
                        BaseActivity.ANIM_LEFT2RIGHT);
            } else {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }
    }

    private class WaitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(WAIT_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            waitDone = true;
            tryMovingOut();
        }
    }

}
