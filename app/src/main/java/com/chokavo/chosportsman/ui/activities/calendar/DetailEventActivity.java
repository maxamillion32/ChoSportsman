package com.chokavo.chosportsman.ui.activities.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ui.activities.BaseActivity;
import com.chokavo.chosportsman.ui.fragments.calendar.DetailEventFragment;

/**
 * Created by Дашицырен on 17.04.2016.
 */
public class DetailEventActivity extends BaseActivity {
    private TextView mToolbarTitle;
    private FloatingActionButton mFabEditEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.text_title_toolbar);
        if (mActionBarToolbar != null) {
            setSupportActionBar(mActionBarToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mFabEditEvent = (FloatingActionButton) findViewById(R.id.fab_edit_event);
        mFabEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailEventActivity.this, EditEventActivity.class));
            }
        });

        launchFragmentNoBackStack(DetailEventFragment.newInstance(getIntent().getExtras()),
                DetailEventFragment.getFragmentTag());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataManager.getInstance().currentEvent = null;
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

    public void setToolbarTitle(String title){
        mToolbarTitle.setText(title);
    }
}
