package com.chokavo.chosportsman;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by repitch on 06.03.16.
 */
public class AppUtils {
    public static void callTo(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+7" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(intent);
    }

    public static void openMaps2(String lat, String lon, String label) {
        String uri = String.format("geo:%s,%s (%s)", lat, lon, label);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(intent);
    }

    public static void openMaps(String lat, String lon, String label) {
        String mapUri = String.format("http://maps.google.com/maps?q=loc:%s,%s (%s)", lat, lon, label);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri));

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(intent);
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    public static boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
