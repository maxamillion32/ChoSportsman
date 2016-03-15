package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class CloakRoomActivity extends NavigationDrawerActivity {

    TextView mTextName;
    ImageView mImgAvatar;
    SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onStart() {
        super.onStart();
        mTextName = (TextView) findViewById(R.id.text_name);
        mImgAvatar = (ImageView) findViewById(R.id.img_avatar);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                loadVKProfile();
            }
        });

        if (!VKSdk.isLoggedIn()) {
            startActivityForResult(new Intent(CloakRoomActivity.this, VKAuthActivity.class),
                    VKAuthActivity.REQUEST_LOGIN_VK);
            return;
        }
        loadVKProfile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_cloakroom, R.id.nav_cloakroom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VKAuthActivity.REQUEST_LOGIN_VK) {
            switch (resultCode) {
                case VKAuthActivity.RESULT_LOGIN_OK:
                    ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_SUCCESS, "Авторизация прошла успешно", Snackbar.LENGTH_SHORT).show();
                    loadVKProfile();
                    break;
                case VKAuthActivity.RESULT_LOGIN_ERROR:
                    ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_ERROR, "Ошибка при авторизации", Snackbar.LENGTH_LONG).show();
                    finish();
                    break;
                default:
                    ImageSnackbar.make(mSwipeRefresh, ImageSnackbar.TYPE_ERROR, "Вы отменили регистрацию", Snackbar.LENGTH_LONG).show();
                    finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadVKProfile() {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200,sex,bdate,city"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> vkUsers = ((VKList) response.parsedModel);
                VKApiUserFull vkUser = vkUsers.get(0);
                DataManager.getInstance().setVkUser(vkUser,getString(R.string.vk_user_id));
                String fullName = String.format("%s %s", vkUser.first_name, vkUser.last_name);
                mTextName.setText(fullName);
                mTxtNavName.setText(fullName);
                String photo200 = vkUser.photo_200;
                RequestCreator picasoRequest = Picasso.with(CloakRoomActivity.this)
                        .load(photo200)
                        .placeholder(R.drawable.ic_account_box_24dp);
                picasoRequest.into(mImgAvatar);
                picasoRequest.into(mImgNavAvatar);
                mSwipeRefresh.setRefreshing(false);
                ImageSnackbar.make(mTextName, ImageSnackbar.TYPE_SUCCESS, String.format("Привет, %s", fullName), Snackbar.LENGTH_SHORT).show();
                super.onComplete(response);
            }
        });
    }
}
