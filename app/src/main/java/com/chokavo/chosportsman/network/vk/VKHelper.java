package com.chokavo.chosportsman.network.vk;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;

/**
 * Created by repitch on 27.04.16.
 */
public class VKHelper {

    public static final String VK_FIELDS = "photo_200,photo_400_orig,sex,bdate,city";

    public static void loadVKUser(VKRequest.VKRequestListener vkRequestListener) {
        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, VK_FIELDS));
        vkRequest.executeWithListener(vkRequestListener);
    }
}
