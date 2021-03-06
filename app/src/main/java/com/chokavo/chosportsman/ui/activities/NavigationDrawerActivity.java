package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.graphics.Path;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.activities.calendar.CalendarActivity;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.activities.teams.TeamsActivity;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.model.VKApiUserFull;

/**
 * Created by repitch on 06.03.16.
 */
public abstract class NavigationDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static int NAV_NO_CHOSEN = -1;

    protected NavigationView mNavigationView;
    protected ImageView mImgNavAvatar;
    protected TextView mTxtNavName, mTxtNavEmail;

    protected void onCreate(int resId, int checkedMenuItem) {
        setContentView(resId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (checkedMenuItem != NAV_NO_CHOSEN) {
            mNavigationView.getMenu().findItem(checkedMenuItem).setChecked(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        View header = mNavigationView.getHeaderView(0);
        mImgNavAvatar = (ImageView) header.findViewById(R.id.img_nav_avatar);
        mTxtNavName = (TextView) header.findViewById(R.id.txt_nav_name);
        mTxtNavEmail = (TextView) header.findViewById(R.id.txt_nav_email);
        mNavigationView.setNavigationItemSelectedListener(this);

        if (VKSdk.isLoggedIn()) {

        }
        VKApiUserFull vkUser = DataManager.getInstance().vkUser;
        if (vkUser != null) {
            mTxtNavName.setText(String.format("%s %s", vkUser.first_name, vkUser.last_name));
            Picasso.with(this)
                    .load(vkUser.photo_200)
                    .into(mImgNavAvatar);
        }

        if (DataManager.getInstance().mSportsman.getGoogleAccount() != null) {
            mTxtNavEmail.setText(DataManager.getInstance().mSportsman.getGoogleAccount());
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Intent intent;
        switch (id) {
            case R.id.nav_open_data:
                if (!(this instanceof OpenDataActivity)) {
                    intent = new Intent(this, OpenDataActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.nav_cloakroom:
//            if (!(this instanceof LockerRoomActivity)) {
                intent = new Intent(this, LockerRoomActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//            }
                break;
            case R.id.nav_news:
                Toast.makeText(this, "Пока не доступно", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_calendar:
                if (!(this instanceof CalendarActivity)) {
                    intent = new Intent(this, CalendarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.nav_teams:
                if (!(this instanceof TeamsActivity)) {
                    intent = new Intent(this, TeamsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.nav_intro:
                intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
