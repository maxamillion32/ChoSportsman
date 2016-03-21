package com.chokavo.chosportsman.calendar;

import android.util.Log;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SharedPrefsManager;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

import java.io.IOException;
import java.util.TimeZone;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by repitch on 15.03.16.
 */
public class GoogleCalendarAPI {

    public static final String SPORT_CALENDAR_SUMMARY = "Чо, спортсмен?";
    private static final String SPORT_CALENDAR_DESCRIPTION = "Спортивные мероприятия (тренировки, соревнования, игры), созданные с помощью приложения 'Чо, спортсмен?' ";
    public static final String[] SCOPES = {CalendarScopes.CALENDAR};
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory sJsonFactory =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport sHttpTransport = AndroidHttp.newCompatibleTransport();


    /**
     * async - выполняется в дополнительном потоке
     * 1) заходим на GAPI
     * 2) извлекаем все CalendarList нашего юзера
     * 3) если нет ListEntry, то создаем календарь и запонимаем его GAPIid
     * 4) иначе берем календарь по найденному id
     * 5) сохраняем GAPIid в ShPr
     * 6) посылаем сигнал подписчику, что календарь у нас
     * @param subscriber
     */
    public static void getCalendarGAPI(Subscriber<CalendarList> subscriber) {

        Observable<CalendarList> myObservable = Observable.create(new Observable.OnSubscribe<CalendarList>() {
            @Override
            public void call(Subscriber<? super CalendarList> sub) {
                try {
//                        new MakeRequestTask(DataManager.getInstance().googleCredential, sub).execute();
                    com.google.api.services.calendar.Calendar mService = new com.google.api.services.calendar.Calendar.Builder(
                            sHttpTransport, sJsonFactory, DataManager.getInstance().googleCredential)
                            .setApplicationName(AppUtils.getApplicationName())
                            .build();
                    CalendarList calendarList = mService.calendarList().list().execute();
                    System.out.println("calendarList size: " + calendarList.size());
                    // получили список календарей, теперь нужно сделать проверку на наличие нашего календаря с определенным Summary

                    CalendarListEntry sportCalendarListEntry = getSportCalendarListEntry(calendarList);
                    String calendarGAPIid;
                    Calendar calendarGAPI;
                    if (sportCalendarListEntry == null) {
                        // создадим календарь
                        calendarGAPI = createSportCalendar(mService);
                        calendarGAPIid = calendarGAPI.getId();
                    } else {
                        calendarGAPIid = sportCalendarListEntry.getId();
                        calendarGAPI = getCalendarById(calendarGAPIid);
                    }
                    // здесь мы уже имеем календарь c id=calendarGAPIid
                    DataManager.getInstance().calendarGAPIid = calendarGAPIid;
                    SharedPrefsManager.saveCalendarGAPIid();
                    if (calendarGAPIid == null) {
                        sub.onNext(null);
                        Log.e(GoogleCalendarAPI.class.getName(), "no calendarGAPIid");
                        sub.onCompleted();
                        return;
                    }
                    // имеем id и сам календарь, можно спокойно выходить
                    sub.onNext(calendarList);
                    sub.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    sub.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        myObservable.subscribe(subscriber);
    }

    private static Calendar getCalendarById (final String calID) throws IOException {
        com.google.api.services.calendar.Calendar mService = new com.google.api.services.calendar.Calendar.Builder(
                sHttpTransport, sJsonFactory, DataManager.getInstance().googleCredential)
                .setApplicationName(AppUtils.getApplicationName())
                .build();
        Calendar calendar = mService.calendars().get(calID).execute();
        if (calendar == null) {
            System.out.println("calendar null!");
        } else {
            System.out.println("calendar desc: " + calendar.getDescription());
        }
        DataManager.getInstance().calendarGAPI = calendar;
        return calendar;
    }

    private static Calendar createSportCalendar(com.google.api.services.calendar.Calendar service) throws IOException {
        // Create a new calendar
        Calendar calendar = new Calendar()
                .setSummary(SPORT_CALENDAR_SUMMARY)
                .setDescription(SPORT_CALENDAR_DESCRIPTION) // добавим описание
                .setTimeZone(TimeZone.getDefault().getID()); // текущий часовой пояс

        // Insert the new calendar
        Calendar createdCalendar = service.calendars().insert(calendar).execute();

        System.out.println(createdCalendar.getId());
        return createdCalendar;
    }


    /**
     * Ищет среди CalendarList календарь с названием SPORT_CALENDAR_SUMMARY ("Чо, спортсмен?")
     * @param calendarList лист календарей юзера
     * @return спортивный календарь, если он есть, иначе null
     */
    private static CalendarListEntry getSportCalendarListEntry(CalendarList calendarList) {
        for (CalendarListEntry calendar: calendarList.getItems()) {
            String summary = calendar.getSummary();
            if (summary.equals(SPORT_CALENDAR_SUMMARY)){
                return calendar;
            }
        }
        return null;
    }

    public static void getCalendarGAPIbyId(Subscriber<CalendarList> subscriber, final String calendarGAPIid) {
        Observable<CalendarList> myObservable = Observable.create(new Observable.OnSubscribe<CalendarList>() {
            @Override
            public void call(Subscriber<? super CalendarList> sub) {
                try {
//                        new MakeRequestTask(DataManager.getInstance().googleCredential, sub).execute();
                    com.google.api.services.calendar.Calendar mService = new com.google.api.services.calendar.Calendar.Builder(
                            sHttpTransport, sJsonFactory, DataManager.getInstance().googleCredential)
                            .setApplicationName(AppUtils.getApplicationName())
                            .build();

                    Calendar calendarGAPI = getCalendarById(calendarGAPIid);
                    // здесь мы уже имеем календарь c id=calendarGAPIid
                    if (calendarGAPI == null) {
                        DataManager.getInstance().calendarGAPIid = null;
                        SharedPrefsManager.saveCalendarGAPIid();
                        sub.onNext(null);
                        Log.e(GoogleCalendarAPI.class.getName(), "no calendarGAPIid");
                        sub.onCompleted();
                        return;
                    }
                    // имеем id и сам календарь, можно спокойно выходить
                    sub.onNext(null);
                    sub.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    sub.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        myObservable.subscribe(subscriber);
    }
}

