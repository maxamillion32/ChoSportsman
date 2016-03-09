package com.chokavo.chosportsman;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;

import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.Locale;

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
}
