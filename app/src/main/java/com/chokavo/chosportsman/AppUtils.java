package com.chokavo.chosportsman;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;

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

    public static String getApplicationName() {
        Context context = App.getInstance();
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    public static int getToolbarHeight(Context context) {
        // Calculate ActionBar height
        int actionBarHeight = -1;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int getStatusBarHeight() {
        Context context = App.getInstance();
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
