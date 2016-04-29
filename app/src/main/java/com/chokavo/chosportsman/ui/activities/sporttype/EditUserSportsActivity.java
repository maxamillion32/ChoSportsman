package com.chokavo.chosportsman.ui.activities.sporttype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

    List<SSportType> mSportTypes = new ArrayList<>();

    private MaterialDialog mDialogSportType;
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

        mSportsRecyclerView = (RecyclerView)findViewById(R.id.recview_favorite_sports);
        mSportTypes = DataManager.getInstance().mSportsman.getFavSportTypes();
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
                DataManager.getInstance().mSportsman.setFavSportTypes(mSportTypes);
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
        if (DataManager.getInstance().mSportsman.getFavSportTypes().size() ==
                DataManager.getInstance().getSportTypes().size()) {
            mBtnAddSport.setVisibility(View.GONE);
        } else {
            mBtnAddSport.setVisibility(View.VISIBLE);
        }
        mSportsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogSportKind() {
        List<SSportType> sportTypesToChoose = SSportType.diffArrays(DataManager.getInstance().getSportTypes(),
                DataManager.getInstance().mSportsman.getFavSportTypes());
        final CharSequence[] mSportTypesChars = SSportType.getAsChars(sportTypesToChoose);
        mDialogSportType = new MaterialDialog.Builder(this)
                .adapter(new CheckableItemAdapter(this, mSportTypesChars, mChosenSportKindId),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                mChosenSportKindId = which;
                                mChosenSportType = DataManager.getInstance().getSportTypeByName(mSportTypesChars[which]);
                                mSportTypes.add(mChosenSportType);
                                DataManager.getInstance().mSportsman.setFavSportTypes(mSportTypes);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        })
                .build();
        mDialogSportType.show();
        mChosenSportKindId = -1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        setSportTypesOnServer();
    }

    private void setSportTypesOnServer() {
        RFManager.setUserSportTypes(DataManager.getInstance().mSportsman.getServerId(),
                mSportTypes, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e(ChooseSportsActivity.class.getSimpleName(), "onResponse");
                        // TODO save in SQLite
//                        saveFavSportsSQLite(DataManager.getInstance().mSportsman,
//                                mSportTypes);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(ChooseSportsActivity.class.getSimpleName(), "onFailure: " + t);
                    }
                });
    }

    private void saveFavSportsSQLite(Sportsman sportsman, List<SSportType> favSportTypes) {
        try {
            SportsmanFavSportTypeDao dao = DBHelperFactory.getHelper().getSportsmanFavSportTypeDao();
            dao.createListIfNotExist(sportsman, favSportTypes);
            DataManager.getInstance().mSportsman.setFavSportTypes(favSportTypes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
