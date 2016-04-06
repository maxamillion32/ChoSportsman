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
import com.chokavo.chosportsman.ormlite.models.SportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.MainActivity;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.vk.sdk.VKSdk;

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

    private boolean asyncDone = false, waitDone = false;

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
            // залогинены, подгружаем данные с сервера
            loadSportTypes();
        } else {
            // просто ждем
            asyncDone = true;
        }
        // тестируем sqlite
        try {
            // TODO убрать или применить
            SportsmanDao sportsmanDao = DBHelperFactory.getHelper().getSportsmanDao();
            SportTypeDao sportTypeDao = DBHelperFactory.getHelper().getSportTypeDao();
            List<Sportsman> sportsmen = DBHelperFactory.getHelper().getSportsmanDao().getAll();
            List<SportType> sportTypes = sportTypeDao.getAll();

            Log.e("","");
        } catch (SQLException e) {
            ImageSnackbar.make(rootView, ImageSnackbar.TYPE_ERROR, "SQLite error: "+e, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
        (new WaitTask()).execute();

        return rootView;
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
                        asyncDone = true;
                        try {
                            stDao.createList(response.body());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
//                mProgressSplash.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<SportType>> call, Throwable t) {
                        Log.e("RETRO", "onFailure: "+t.toString());
                        mProgressSplash.setVisibility(View.GONE);
                        ImageSnackbar.make(mProgressSplash, ImageSnackbar.TYPE_ERROR, String.format("Возникла ошибка при загрузке данных"), Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                // в базе данных есть виды спорта. Можно выгрузить в DataManager
                DataManager.getInstance().setSportTypes(sportTypes);
                asyncDone = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void initViews(View rootView) {
        mProgressSplash = (ProgressBar) rootView.findViewById(R.id.progress_splash);
    }

    private void tryMovingOut() {
        if (asyncDone && waitDone) {
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
