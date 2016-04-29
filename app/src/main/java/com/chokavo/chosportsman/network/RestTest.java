package com.chokavo.chosportsman.network;

import android.util.Log;

import com.chokavo.chosportsman.ormlite.models.SCalendar;
import com.chokavo.chosportsman.ormlite.models.SEvent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by repitch on 29.04.16.
 * Testing REST methods
 */
public class RestTest {
    public static final int TEST_SPORTTYPE_ID = 4;
    public static final String TEST_LOG = "Test API";
    public static final int TEST_USER_ID = 4;
    public static final String TEST_CAL_GAPI_ID = "1";
    public static final int TEST_CAL_ID = 5;
    public static final SCalendar TEST_CAL = new SCalendar(TEST_CAL_ID, "testGoogleApiId");
    public static final int TEST_EVENT_ID = 1;
    public static final SEvent TEST_EVENT = new SEvent(TEST_EVENT_ID, TEST_CAL_ID,
            "testGoogleApiId",TEST_SPORTTYPE_ID, "Competition");

    /**
     * Testing all api methods
     */
    public static void testAPI() {
        // calendar
        Log.d(TEST_LOG, String.format("1) createCalendar(%d, %s)", TEST_USER_ID, TEST_CAL_GAPI_ID));
        RFManager.createCalendar(TEST_USER_ID, TEST_CAL_GAPI_ID, new Callback<SCalendar>() {
            @Override
            public void onResponse(Call<SCalendar> call, Response<SCalendar> response) {
                if (response.isSuccess()) {
                    Log.d(TEST_LOG, "1) createCalendar: success");
                } else {
                    Log.d(TEST_LOG, "1) createCalendar: not success");
                }
            }

            @Override
            public void onFailure(Call<SCalendar> call, Throwable t) {
                Log.d(TEST_LOG, "1) createCalendar: onFailure");
            }
        });
        Log.d(TEST_LOG, String.format("2) getCalendar(%d)", TEST_CAL_ID));
        RFManager.getCalendar(TEST_CAL_ID, new Callback<SCalendar>() {
            @Override
            public void onResponse(Call<SCalendar> call, Response<SCalendar> response) {
                if (response.isSuccess()) {
                    Log.d(TEST_LOG, "2) getCalendar: success");
                } else {
                    Log.d(TEST_LOG, "2) getCalendar: not success");
                }
            }

            @Override
            public void onFailure(Call<SCalendar> call, Throwable t) {
                Log.d(TEST_LOG, "2) getCalendar: onFailure");
            }
        });
        Log.d(TEST_LOG, String.format("3) updateCalendar(%s)", TEST_CAL.toString()));
        RFManager.updateCalendar(TEST_CAL, new Callback<SCalendar>() {
            @Override
            public void onResponse(Call<SCalendar> call, Response<SCalendar> response) {
                if (response.isSuccess()) {
                    Log.d(TEST_LOG, "3) updateCalendar: success");
                } else {
                    Log.d(TEST_LOG, "3) updateCalendar: not success");
                }
            }

            @Override
            public void onFailure(Call<SCalendar> call, Throwable t) {
                Log.d(TEST_LOG, "3) updateCalendar: onFailure");
            }
        });
        // events
        Log.d(TEST_LOG, String.format("4) getAllEvents(%d)", TEST_CAL_ID));
        RFManager.getAllEvents(TEST_CAL_ID, new Callback<List<SEvent>>() {
            @Override
            public void onResponse(Call<List<SEvent>> call, Response<List<SEvent>> response) {
                if (response.isSuccess()) {
                    Log.d(TEST_LOG, "4) getAllEvents: success");
                } else {
                    Log.d(TEST_LOG, "4) getAllEvents: not success");
                }
            }

            @Override
            public void onFailure(Call<List<SEvent>> call, Throwable t) {
                Log.d(TEST_LOG, "4) getAllEvents: onFailure");
            }
        });
        /*Log.d(TEST_LOG, String.format("createEvent(%d, %s)", TEST_CAL_ID, TEST_EVENT.toString()));
        createEvent(TEST_CAL_ID, TEST_EVENT, new Callback<SEvent>() {
            @Override
            public void onResponse(Call<SEvent> call, Response<SEvent> response) {
                if (response.isSuccess()) {
                    Log.d(TEST_LOG, "createEvent: success");
                } else {
                    Log.d(TEST_LOG, "createEvent: not success");
                }
            }

            @Override
            public void onFailure(Call<SEvent> call, Throwable t) {
                Log.d(TEST_LOG, "createEvent: onFailure");
            }
        });*/
        Log.d(TEST_LOG, String.format("5) getEvent(calId=%d, eventId=%d)", TEST_CAL_ID, TEST_EVENT_ID));
        RFManager.getEvent(TEST_CAL_ID, TEST_EVENT_ID, new Callback<SEvent>() {
            @Override
            public void onResponse(Call<SEvent> call, Response<SEvent> response) {
                if (response.isSuccess()) {
                    Log.d(TEST_LOG, "5) getEvent: success");
                } else {
                    Log.d(TEST_LOG, "5) getEvent: not success");
                }
            }

            @Override
            public void onFailure(Call<SEvent> call, Throwable t) {
                Log.d(TEST_LOG, "5) getEvent: onFailure");
            }
        });
        Log.d(TEST_LOG, String.format("6) updateEvent(calId=%d, eventId=%d)", TEST_CAL_ID, TEST_EVENT.getServerId()));
        RFManager.updateEvent(TEST_CAL_ID, TEST_EVENT, new Callback<SEvent>() {
            @Override
            public void onResponse(Call<SEvent> call, Response<SEvent> response) {
                if (response.isSuccess()) {
                    Log.d(TEST_LOG, "6) updateEvent: success");
                } else {
                    Log.d(TEST_LOG, "6) updateEvent: not success");
                }
            }

            @Override
            public void onFailure(Call<SEvent> call, Throwable t) {
                Log.d(TEST_LOG, "6) updateEvent: onFailure");
            }
        });
    }
}
