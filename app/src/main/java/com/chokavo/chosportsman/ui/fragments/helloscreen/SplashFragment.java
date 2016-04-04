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
import com.chokavo.chosportsman.models.SportKind;
import com.chokavo.chosportsman.network.RFManager;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.MainActivity;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.vk.sdk.VKSdk;

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
        (new WaitTask()).execute();

        return rootView;
    }

    private void loadSportTypes() {
        RFManager.getInstance().getSportTypes(new Callback<List<SportKind>>() {
            @Override
            public void onResponse(Call<List<SportKind>> call, Response<List<SportKind>> response) {
                DataManager.getInstance().setSportKinds(response.body());
                asyncDone = true;
//                mProgressSplash.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<SportKind>> call, Throwable t) {
                Log.e("RETRO", "onFailure: "+t.toString());
                mProgressSplash.setVisibility(View.GONE);
                ImageSnackbar.make(mProgressSplash, ImageSnackbar.TYPE_ERROR, String.format("Возникла ошибка при загрузке данных"), Snackbar.LENGTH_SHORT).show();
            }
        });
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
