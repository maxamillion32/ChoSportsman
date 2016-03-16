package com.chokavo.chosportsman.calendar;

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

    public static void getCalendarList(Subscriber<CalendarList> subscriber) {

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
                    String sportCalendarId = getSportCalendarId(calendarList);
                    if (sportCalendarId == null) {
                        // создадим календарь
                        sportCalendarId = createSportCalendar(mService);
                    }
                    // здесь мы уже имеем календарь c id=sportCalendarServerId
                    // попробуем залезть в contentProvider и выудить оттуда новый календарь
                    DataManager.getInstance().sportCalendarServerId = sportCalendarId;
                    SharedPrefsManager.saveSportCalendarServerId();

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

    private static String createSportCalendar(com.google.api.services.calendar.Calendar service) throws IOException {
        // Create a new calendar
        Calendar calendar = new Calendar()
                .setSummary(SPORT_CALENDAR_SUMMARY)
                .setDescription(SPORT_CALENDAR_DESCRIPTION) // добавим описание
                .setTimeZone(TimeZone.getDefault().getID()); // текущий часовой пояс

        // Insert the new calendar
        Calendar createdCalendar = service.calendars().insert(calendar).execute();

        System.out.println(createdCalendar.getId());
        return createdCalendar.getId();
    }

    private static String getSportCalendarId(CalendarList calendarList) {
        for (CalendarListEntry calendar: calendarList.getItems()) {
            String summary = calendar.getSummary();
            if (summary.equals(SPORT_CALENDAR_SUMMARY)){
                return calendar.getId();
            }
        }
        return null;
    }
}

