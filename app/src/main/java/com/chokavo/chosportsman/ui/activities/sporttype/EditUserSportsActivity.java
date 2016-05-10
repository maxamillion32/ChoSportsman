package com.chokavo.chosportsman.ui.activities.sporttype;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.network.RFManager;
import com.chokavo.chosportsman.ormlite.DBHelperFactory;
import com.chokavo.chosportsman.ormlite.dao.SportsmanFavSportTypeDao;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.Sportsman;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.activities.ChooseSportsActivity;
import com.chokavo.chosportsman.ui.adapters.CheckableItemAdapter;
import com.chokavo.chosportsman.ui.adapters.EditUserSportsAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dashitsyren on 27.04.2016.
 */
public class EditUserSportsActivity extends BaseActivity {
    RecyclerView mSportsRecyclerView;
    EditUserSportsAdapter mSportsAdapter;
    Button mBtnAddSport;
    private ProgressDialog mProgress;

    List<SSportType> mSportTypes = new ArrayList<>();

    private MaterialDialog mDialogSportType;
    private FrameLayout mWrapPlaceholder;
    int mChosenSportKindId = -1;
    SSportType mChosenSportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sports);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(R.string.favorite_user_sports);
        }

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.wait_second));
        mProgress.setCancelable(false);

        mSportTypes.addAll(DataManager.getInstance().mSportsman.getFavSportTypes()); // создаем клон
        mWrapPlaceholder = (FrameLayout) findViewById(R.id.wrap_placeholder);
        mWrapPlaceholder.setVisibility(mSportTypes.isEmpty() ? View.VISIBLE : View.GONE);
        mSportsRecyclerView = (RecyclerView)findViewById(R.id.recview_favorite_sports);
        mSportsAdapter = new EditUserSportsAdapter(mSportTypes, new EditUserSportsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SSportType sportType) {
                Intent intent = new Intent(EditUserSportsActivity.this,
                        DetailSportTypeActivity.class);
                intent.putExtra(DetailSportTypeActivity.EXTRA_ID, sportType.getId());
                startActivity(intent);
            }
        }, new EditUserSportsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SSportType sportType) {
                mSportTypes.remove(sportType);
                notifyDataSetChanged();
            }
        });
        mSportsRecyclerView.setAdapter(mSportsAdapter);
        mSportsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBtnAddSport = (Button) findViewById(R.id.btn_add_sporttype);
        mBtnAddSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSportKind();
            }
        });
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        mWrapPlaceholder.setVisibility(mSportTypes.isEmpty() ? View.VISIBLE : View.GONE);
        mSportsRecyclerView.setVisibility(!mSportTypes.isEmpty() ? View.VISIBLE : View.GONE);
        if (mSportTypes.size() == DataManager.getInstance().getSportTypes().size()) {
            mBtnAddSport.setVisibility(View.GONE);
        } else {
            mBtnAddSport.setVisibility(View.VISIBLE);
        }
        mSportsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_sports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                saveSportsAndQuit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSportsAndQuit() {
        DataManager.getInstance().mSportsman.setFavSportTypes(mSportTypes);
        saveFavSportsSQLite(DataManager.getInstance().mSportsman, mSportTypes);
        setSportTypesOnServer(true);

    }

    private void showDialogSportKind() {
        List<SSportType> sportTypesToChoose = SSportType.diffArrays(DataManager.getInstance().getSportTypes(),
                mSportTypes);
        final CharSequence[] mSportTypesChars = SSportType.getAsChars(sportTypesToChoose);
        mDialogSportType = new MaterialDialog.Builder(this)
                .adapter(new CheckableItemAdapter(this, mSportTypesChars, -1),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                mChosenSportKindId = which;
                                mChosenSportType = DataManager.getInstance().getSportTypeByName(mSportTypesChars[which]);
                                mSportTypes.add(mChosenSportType);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        })
                .build();
        mDialogSportType.show();
        mChosenSportKindId = -1;
    }

    private void setSportTypesOnServer(final boolean quit) {
        mProgress.show();
        RFManager.setUserSportTypes(DataManager.getInstance().mSportsman.getServerId(),
                mSportTypes, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        mProgress.hide();
                        Log.e(ChooseSportsActivity.class.getSimpleName(), "onResponse");
                        if (quit) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        mProgress.hide();
                        Log.e(ChooseSportsActivity.class.getSimpleName(), "onFailure: " + t);
                    }
                });
    }

    private void saveFavSportsSQLite(Sportsman sportsman, List<SSportType> favSportTypes) {
        try {
            SportsmanFavSportTypeDao dao = DBHelperFactory.getHelper().getSportsmanFavSportTypeDao();
            dao.createListIfNotExist(sportsman, favSportTypes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
