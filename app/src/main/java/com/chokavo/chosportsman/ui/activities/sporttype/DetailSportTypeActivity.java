package com.chokavo.chosportsman.ui.activities.sporttype;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ui.activities.HeaderImageDrawerActivity;
import com.chokavo.chosportsman.ui.activities.NavigationDrawerActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by repitch on 28.04.16.
 */
public class DetailSportTypeActivity extends HeaderImageDrawerActivity {
    public static final String EXTRA_ID = "EXTRA_ID";
    private SSportType mSportType;

    private TextView mTxtName;
    private ImageView mImgAvatar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_detail_sport_type, NavigationDrawerActivity.NAV_NO_CHOSEN);

        int sportTypeId = getIntent().getExtras().getInt(EXTRA_ID);
        mSportType = SSportType.getById(DataManager.getInstance().getSportTypes(), sportTypeId);

        initViews();
        fillViews();
    }

    private void fillViews() {
        getSupportActionBar().setTitle(mSportType.getTitle());
        mTxtName.setText(mSportType.getTitle());
        Picasso.with(this)
                .load(mSportType.getBigImageId())
                .resize(0, (int) AppUtils.convertDpToPixel(250, this))
                .into(mImgAvatar);
    }

    private void initViews() {
        mTxtName = (TextView) findViewById(R.id.text_name);
        mImgAvatar = (ImageView) findViewById(R.id.img_avatar);
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
