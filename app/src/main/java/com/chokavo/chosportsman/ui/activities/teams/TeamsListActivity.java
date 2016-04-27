package com.chokavo.chosportsman.ui.activities.teams;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.fragments.teams.TeamsListFragment;

/**
 * Created by Дашицырен on 27.04.2016.
 */
public class TeamsListActivity extends BaseActivity {

    public final static String EXTRA_MODE = "EXTRA_MODE";

    View mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_list);
        mContentFrame = findViewById(R.id.content_frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        launchFragmentNoBackStack(TeamsListFragment.newInstance(getIntent().getExtras()), TeamsListFragment.getFragmentTag());
    }

    public void setToolbarTitle(String title){
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
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
}
