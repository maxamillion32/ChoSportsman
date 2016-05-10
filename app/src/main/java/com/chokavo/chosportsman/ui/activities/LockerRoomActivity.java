package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.network.vk.VKHelper;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.STeam;
import com.chokavo.chosportsman.ui.activities.sporttype.EditUserSportsActivity;
import com.chokavo.chosportsman.ui.activities.teams.TeamsListActivity;
import com.chokavo.chosportsman.ui.adapters.UserSportsAdapter;
import com.chokavo.chosportsman.ui.fragments.teams.TeamsListFragment;
import com.chokavo.chosportsman.ui.views.ImageSnackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import su.levenetc.android.badgeview.BadgeView;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class LockerRoomActivity extends HeaderImageDrawerActivity /*implements AppBarLayout.OnOffsetChangedListener*/ {
//public class LockerRoomActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener  {

    private ImageView mImgAvatar;
    private TextView mTextName;
    private ActionBar mActionBar;

    SwipeRefreshLayout mSwipeRefresh;

    private UserSportsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private FloatingActionButton mFabEdit;
    private LinearLayout mlinearLayoutAdd;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_lockerroom_new, R.id.nav_cloakroom);

        initViews();
        initActions();
        loadVKProfile();

    }

    private void initViews() {
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false); // изначально title нет
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mImgAvatar = (ImageView) findViewById(R.id.img_avatar);
        mTextName = (TextView) findViewById(R.id.text_name);
        // inside
        // 1 fav sporttypes
        mBadgeFavSports = (BadgeView) findViewById(R.id.badge_fav_sports);
        mWrapFavSportsIcons = (LinearLayout) findViewById(R.id.wrap_fav_sports_icons);
        mWrapFavSports = (LinearLayout) findViewById(R.id.wrap_fav_sports);
        // 2 teamMembers
        mBadgeTeamMember = (BadgeView) findViewById(R.id.badge_team_member);
        mWrapTeamMemberIcons = (LinearLayout) findViewById(R.id.wrap_team_member_icons);
        mWrapTeamMember = (LinearLayout) findViewById(R.id.wrap_team_member);
        // 3 teamFans
        mBadgeTeamFan = (BadgeView) findViewById(R.id.badge_team_fan);
        mWrapTeamFan = (LinearLayout) findViewById(R.id.wrap_team_fan);
        mWrapTeamFanIcons = (LinearLayout) findViewById(R.id.wrap_team_fan_icons);
    }

    private void initActions() {
       // TODO hardcoded
        TeamsListFragment.loadUserTeams();
        fillFavSportTypes();
        fillTeamMember();
        fillTeamFan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // если вдруг что обновилось
        fillFavSportTypes();
        fillTeamMember();
        fillTeamFan();
    }

    private BadgeView mBadgeFavSports, mBadgeTeamMember, mBadgeTeamFan;
    private LinearLayout mWrapFavSportsIcons, mWrapFavSports,
            mWrapTeamMemberIcons, mWrapTeamMember,
        mWrapTeamFanIcons, mWrapTeamFan;
    public static final int MIN_ICONS_COUNT = 3;

    private void fillFavSportTypes() {
        mWrapFavSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LockerRoomActivity.this, EditUserSportsActivity.class));
            }
        });

        List<SSportType> favSportTypes = DataManager.getInstance().mSportsman.getFavSportTypes();

        mBadgeFavSports.setValue(favSportTypes.size());
        mWrapFavSportsIcons.removeAllViews();
        // берем рандомные три иконки
        List<SSportType> randomSportTypes = SSportType.randomSportTypes(favSportTypes, MIN_ICONS_COUNT);
        for (SSportType favSport : randomSportTypes) {
            // рандомные три иконки отображаем
            FrameLayout fl = (FrameLayout) getLayoutInflater().inflate(R.layout.widget_miniavatar_rtl, null);
            CircleImageView widgetMiniavatar = (CircleImageView) fl.findViewById(R.id.mini_avatar);
            widgetMiniavatar.setImageResource(favSport.getIconId());
            mWrapFavSportsIcons.addView(fl);
            ViewGroup.MarginLayoutParams params =
                    (ViewGroup.MarginLayoutParams) fl.getLayoutParams();
            params.leftMargin = (int) AppUtils.convertDpToPixel(-10, this);
            fl.requestLayout();

        }
    }

    private void fillTeamDescRow(List<STeam> teams,
                             BadgeView badge,
                             LinearLayout wrap,
                             LinearLayout wrapIcons, final int onClickMode) {

        wrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LockerRoomActivity.this, TeamsListActivity.class);
                intent.putExtra(TeamsListActivity.EXTRA_MODE, onClickMode);
                startActivity(intent);
            }
        });
        badge.setValue(teams.size());
        wrapIcons.removeAllViews();

        // берем рандомные три иконки
        List<STeam> randomTeams = STeam.randomTeams(teams, MIN_ICONS_COUNT);
        for (STeam team : randomTeams) {
            FrameLayout fl = (FrameLayout) getLayoutInflater().inflate(R.layout.widget_miniavatar_rtl, null);
            CircleImageView widgetMiniavatar = (CircleImageView) fl.findViewById(R.id.mini_avatar);

            // scale image
            loadPicasso(team.getIconId(), widgetMiniavatar);

            wrapIcons.addView(fl);
            ViewGroup.MarginLayoutParams params =
                    (ViewGroup.MarginLayoutParams) fl.getLayoutParams();
            params.leftMargin = (int) AppUtils.convertDpToPixel(-10, this);
            fl.requestLayout();
        }
    }

    private void loadPicasso(int iconId, CircleImageView widgetMiniavatar) {
        Picasso.with(LockerRoomActivity.this)
                .load(iconId)
                .resize((int) AppUtils.convertDpToPixel(36, this), 0)
                .into(widgetMiniavatar);
    }

    private void fillTeamMember() {
        List<STeam> playerTeams = DataManager.getInstance().mSportsman.getPlayerTeams();

        fillTeamDescRow(playerTeams,
                mBadgeTeamMember,
                mWrapTeamMember,
                mWrapTeamMemberIcons,
                TeamsListActivity.EXTRA_MODE_MEMBER);

    }

    private void fillTeamFan() {
        List<STeam> fanTeams = DataManager.getInstance().mSportsman.getFanTeams();

        fillTeamDescRow(fanTeams,
                mBadgeTeamFan,
                mWrapTeamFan,
                mWrapTeamFanIcons,
                TeamsListActivity.EXTRA_MODE_FAN);

    }

   /* @Override
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
        adapter = new UserSportsAdapter(DataManager.getInstance().mSportsman.getFavSportTypes());
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
    }*/


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

        if (id == R.id.action_save) {
            saveProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    /*@Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }*/

    private void loadVKProfile() {
        if (DataManager.getInstance().vkUser != null) {
            // уже есть вк пользователь
            VKApiUserFull vkUser = DataManager.getInstance().vkUser;
            String fullName = String.format("%s %s", vkUser.first_name, vkUser.last_name);
            mTextName.setText(fullName);
            mActionBar.setTitle(fullName);
            String photo400_orig = vkUser.photo_400_orig;
            RequestCreator picasoRequest1 = Picasso.with(LockerRoomActivity.this)
                    .load(photo400_orig)
                    .placeholder(R.drawable.ic_account_box_24dp);
            picasoRequest1.into(mImgAvatar);
            return;
        }
        VKHelper.loadVKUser(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> vkUsers = ((VKList) response.parsedModel);
                VKApiUserFull vkUser = vkUsers.get(0);
                DataManager.getInstance().vkUser = vkUser;
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
    /*@Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            mSwipeRefresh.setEnabled(true);
        } else {
            mSwipeRefresh.setEnabled(false);
        }
    }*/
}
