package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.adapters.UserSportsAdapter;
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
public class LockerRoomActivity extends NavigationDrawerActivity implements AppBarLayout.OnOffsetChangedListener  {
//public class LockerRoomActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener  {

    TextView mTextName;
    ImageView mImgAvatar;
    SwipeRefreshLayout mSwipeRefresh;

    private RecyclerView mRecyclerSports;
    private UserSportsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private FloatingActionButton mFabEdit;
    private LinearLayout mlinearLayoutAdd;

    private boolean isEditing = false;

    @Override
    protected void onStart() {
        super.onStart();
        mTextName = (TextView) findViewById(R.id.text_name);
        mImgAvatar = (ImageView) findViewById(R.id.img_avatar);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mCollapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                loadVKProfile();
            }
        });

        mRecyclerSports = (RecyclerView)findViewById(R.id.recview_sports_ilove);
        adapter = new UserSportsAdapter(DataManager.getInstance().loadUserSports(getString(R.string.sport_kinds)));
        layoutManager = new LinearLayoutManager(this);

        mRecyclerSports.setAdapter(adapter);
        mRecyclerSports.setLayoutManager(layoutManager);
        mRecyclerSports.setItemAnimator(new DefaultItemAnimator());

        mFabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        mlinearLayoutAdd = (LinearLayout) findViewById(R.id.linlayout_add);
        mFabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });


        if (!VKSdk.isLoggedIn()) {
            startActivityForResult(new Intent(LockerRoomActivity.this, VKAuthActivity.class),
                    VKAuthActivity.REQUEST_LOGIN_VK);
            return;
        }
        loadVKProfile();
    }

    private void editProfile() {
        Toast.makeText(this, "editProfile", Toast.LENGTH_SHORT).show();
        isEditing = true;
        mFabEdit.setVisibility(View.GONE);
        mlinearLayoutAdd.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    private void saveProfile() {
        Toast.makeText(this, "SaveProfile", Toast.LENGTH_SHORT).show();
        isEditing = false;
        mFabEdit.setVisibility(View.VISIBLE);
        mlinearLayoutAdd.setVisibility(View.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lockerroom, menu);
        MenuItem miSave = menu.findItem(R.id.action_save);
        miSave.setVisible(isEditing);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lockerroom_2);
        super.onCreate(R.layout.activity_lockerroom, R.id.nav_cloakroom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VKAuthActivity.REQUEST_LOGIN_VK) {
            switch (resultCode) {
                case VKAuthActivity.RESULT_LOGIN_OK:
                    ImageSnackbar.make(mAppBarLayout, ImageSnackbar.TYPE_SUCCESS, "Авторизация прошла успешно", Snackbar.LENGTH_SHORT).show();
//                    loadVKProfile();
                    break;
                case VKAuthActivity.RESULT_LOGIN_ERROR:
                    ImageSnackbar.make(mAppBarLayout, ImageSnackbar.TYPE_ERROR, "Ошибка при авторизации", Snackbar.LENGTH_LONG).show();
                    finish();
                    break;
                default:
                    ImageSnackbar.make(mAppBarLayout, ImageSnackbar.TYPE_ERROR, "Вы отменили регистрацию", Snackbar.LENGTH_LONG).show();
                    finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    private void loadVKProfile() {
            mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200,photo_400_orig,sex,bdate,city"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> vkUsers = ((VKList) response.parsedModel);
                VKApiUserFull vkUser = vkUsers.get(0);
                DataManager.getInstance().setVkUser(vkUser,getString(R.string.vk_user_id));
                String fullName = String.format("%s %s", vkUser.first_name, vkUser.last_name);
                mTextName.setText(fullName);
                mTxtNavName.setText(fullName);
                mCollapsingToolbar.setTitle(fullName);
                String photo400_orig = vkUser.photo_400_orig;
                String photo200 = vkUser.photo_200;
                RequestCreator picasoRequest = Picasso.with(LockerRoomActivity.this)
                        .load(photo200)
                        .placeholder(R.drawable.ic_account_box_24dp);
                RequestCreator picasoRequest1 = Picasso.with(LockerRoomActivity.this)
                        .load(photo400_orig)
                        .placeholder(R.drawable.ic_account_box_24dp);
                picasoRequest1.into(mImgAvatar);
                picasoRequest.into(mImgNavAvatar);
                mSwipeRefresh.setRefreshing(false);
                ImageSnackbar.make(mTextName, ImageSnackbar.TYPE_SUCCESS, String.format("Привет, %s", fullName), Snackbar.LENGTH_SHORT).show();
                super.onComplete(response);
            }
        });
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            mSwipeRefresh.setEnabled(true);
        } else {
            mSwipeRefresh.setEnabled(false);
        }
    }
}
