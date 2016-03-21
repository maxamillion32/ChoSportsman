package com.chokavo.chosportsman.ui.fragments.calendar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.calendar.CalendarActivity2;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.calendar.model.CalendarList;

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

    }

    /**
     * Данная функция вызывается по нажитю на кнопкуу "новый календарь"
     */
    private void beginCreatingCalendar() {
        // 1 аккаунт google
        if (DataManager.getInstance().getGoogleAccount() == null) {
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
                GoogleCalendarAPI.getCalendarGAPI(mSubscriberGetCalendarGAPI);
            } else {
                ImageSnackbar.make(mBtnCreateCal, ImageSnackbar.TYPE_ERROR, getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT).show();
            }
            return;
        }
        // календарь есть на сервере, у нас есть его GAPI, но пока его нет в CoPr
        // поэтому работаем с GAPI
        GoogleCalendarAPI.getCalendarGAPIbyId(mSubscriberGetCalendarGAPI, DataManager.getInstance().calendarGAPIid);

    }

    private void showCalendar() {
        launchFragmentNoBackStack(new SportCalendarFragment(), SportCalendarFragment.getFragmentTag());
    }

    Subscriber<CalendarList> mSubscriberGetCalendarGAPI = new Subscriber<CalendarList>() {
        @Override
        public void onCompleted() {
            System.out.println("onCompleted: ");
            // здесь мы уже получили calendarGAPI с сервера, отобразим данные в UI
            showCalendar();
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UserRecoverableAuthIOException) {
                startActivityForResult(
                        ((UserRecoverableAuthIOException) e).getIntent(),
                        CalendarActivity2.REQUEST_AUTHORIZATION);
            } else {
                Log.e(CalendarActivity2.class.getName(), "error: " + e.toString());
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
            ActivityCompat.requestPermissions((getActivity()),
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    BaseActivity.MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return;
        }
        startActivityForResult(
                DataManager.getInstance().googleCredential.newChooseAccountIntent(), CalendarActivity2.REQUEST_ACCOUNT_PICKER);

    }

}
