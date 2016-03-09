package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

/**
 * Created by ilyapyavkin on 17.02.16.
 */
public class VKAuthActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_LOGIN_VK = 1;
    public static final int RESULT_LOGIN_OK = 1;
    public static final int RESULT_LOGIN_ERROR = 2;

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_auth);
        initViews();

    }
    private Button mBtnVkAuth;

    private void initViews() {
        mBtnVkAuth = (Button) findViewById(R.id.btn_vk_auth);
        mBtnVkAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(VKAuthActivity.this, VKScope.AUDIO, VKScope.FRIENDS);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                Log.e(VKAuthActivity.class.getSimpleName(), "VKSdk onResult");
                setResult(RESULT_LOGIN_OK);
                finish();
                }

            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Log.e(VKAuthActivity.class.getSimpleName(), "VKSdk onError");
                ImageSnackbar.make(mBtnVkAuth, ImageSnackbar.TYPE_ERROR, "Ошибка при авторизации", Snackbar.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
