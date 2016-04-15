package com.chokavo.chosportsman.ui.fragments.calendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.GoogleCalendarAPI;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.network.ErrorManager;
import com.chokavo.chosportsman.network.RFManager;
import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.calendar.CalendarActivity;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.calendar.model.CalendarList;

import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by ilyapyavkin on 21.03.16.
 */
public class NoSportCalendarFragment extends BaseFragment {

    @Override
    public String getFragmentTitle() {
        return "Календарь";
    }

    Button mBtnCreateCal;
    ProgressDialog mProgress, mProgressUpdateAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_sport_calendar, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        mBtnCreateCal = (Button) rootView.findViewById(R.id.btn_create_cal);
        mBtnCreateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginCreatingCalendar();
            }
        });

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.progress_gapi));
        mProgressUpdateAccount = new ProgressDialog(getActivity());
        mProgressUpdateAccount.setMessage(getString(R.string.progress_update_user));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BaseActivity.MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseAccount();
                } else {
                    ImageSnackbar.make(getView(), ImageSnackbar.TYPE_ERROR, "К сожалению, вы запретили доступ к аккаунтам", Snackbar.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == CalendarActivity.RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        DataManager.getInstance().getGoogleCredential().setSelectedAccountName(accountName);
                        // аккаунт выбран - сохраним googleAccount на сервер и локально
                        updateGoogleAccount(accountName);
                    } else {
                        ImageSnackbar.make(getView(), ImageSnackbar.TYPE_ERROR, "Аккаунт == null", Snackbar.LENGTH_LONG).show();
                    }
                } else if (resultCode == CalendarActivity.RESULT_CANCELED) {
                    ImageSnackbar.make(getView(), ImageSnackbar.TYPE_ERROR, "Аккаунт не выбран", Snackbar.LENGTH_LONG).show();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != CalendarActivity.RESULT_OK) {
                    chooseAccount();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateGoogleAccount(String accountName) {
        mProgressUpdateAccount.show();
        // 0 обновим юзера
        Sportsman sportsman = DataManager.getInstance().mSportsman;
        sportsman.setGoogleAccount(accountName);
        // 1 сохраним аккаунт в sqlite
        try {
            DBHelperFactory.getHelper().getSportsmanDao().update(sportsman);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 2 сохраним аккаунт на сервере
        RFManager.getInstance().updateUser(sportsman,
                new Callback<Sportsman>() {
                    @Override
                    public void onResponse(Call<Sportsman> call, Response<Sportsman> response) {
                        if (response.isSuccess()) {
                            beginCreatingCalendar();
                        } else {
                            String strError = ErrorManager.getErrorString(response);
                            Log.e(NoSportCalendarFragment.getFragmentTag(), "strError: "+strError);
                        }
                        mProgressUpdateAccount.hide();
                    }

                    @Override
                    public void onFailure(Call<Sportsman> call, Throwable t) {
                        mProgressUpdateAccount.hide();
                        Log.e(NoSportCalendarFragment.getFragmentTag(), "onFailure when updateUser: "+t);
                        ImageSnackbar.make(getView(), ImageSnackbar.TYPE_ERROR, "Возникла ошибка при обновлении пользователя", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Данная функция вызывается по нажитю на кнопкуу "новый календарь"
     */
    private void beginCreatingCalendar() {
        Sportsman sportsman = DataManager.getInstance().mSportsman;
        // 1 аккаунт google
        if (sportsman == null) {
            // no sportsman
            Log.e(NoSportCalendarFragment.getFragmentTag(), "no sportsman");
            return;
        }
        if (sportsman.getGoogleAccount() == null ||
                sportsman.getGoogleAccount().isEmpty()) {
            // позволяем юзеру выбрать аккаунт гугл из списка
            chooseAccount();
            return;
        }
        // 2 проверка на наличие CP calendar "Чо спортсмен?"
        if (DataManager.getInstance().calendarCPid > 0) {
            // контент провайдер есть - используем его для работы
            // запускаем фрагмент с календарем
            showCalendar();
            return;
        }
        // контент провайдера нету. Может быть 2 варианта:
        // 1. Календаря вообще нет
        // 2. Календарь есть на сервере GAPI, но не появился в CP
        if (DataManager.getInstance().calendarGAPIid == null) {
            // GAPI id календаря у нас нету ("Чо, спортсмен?").
            // проверяем доступ к интернету
            if (AppUtils.isDeviceOnline()) {
                mProgress.show();
                GoogleCalendarAPI.getCalendarGAPI(mSubscriberGetCalendarGAPI);
            } else {
                ImageSnackbar.make(mBtnCreateCal, ImageSnackbar.TYPE_ERROR, getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT).show();
            }
            return;
        }
        // календарь есть на сервере, у нас есть его GAPI, но пока его нет в CoPr
        // поэтому работаем с GAPI
        GoogleCalendarAPI.getInstance().getCalendarGAPIbyId(mSubscriberGetCalendarGAPI, DataManager.getInstance().calendarGAPIid);

    }

    private void showCalendar() {
        launchFragmentNoBackStack(new SportCalendarFragment(), SportCalendarFragment.getFragmentTag());
    }

    Subscriber<CalendarList> mSubscriberGetCalendarGAPI = new Subscriber<CalendarList>() {
        @Override
        public void onCompleted() {
            // здесь мы уже получили calendarGAPI с сервера, отобразим данные в UI
            mProgress.hide();
            showCalendar();
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UserRecoverableAuthIOException) {
                startActivityForResult(
                        ((UserRecoverableAuthIOException) e).getIntent(),
                        REQUEST_AUTHORIZATION);
            } else {
                Log.e(CalendarActivity.class.getName(), "error: " + e.toString());
                ImageSnackbar.make(getView(), "Ошибка! " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNext(CalendarList calendarList) {
            System.out.println("onNext: " + calendarList.size());
        }
    };

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    BaseActivity.MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return;
        }
        startActivityForResult(
                DataManager.getInstance().getGoogleCredential().newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

    }


}
