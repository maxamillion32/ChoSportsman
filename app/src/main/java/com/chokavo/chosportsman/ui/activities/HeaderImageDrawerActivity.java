package com.chokavo.chosportsman.ui.activities;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.R;

/**
 * Created by repitch on 06.03.16.
 */
public abstract class HeaderImageDrawerActivity extends NavigationDrawerActivity {

    protected ScrollView mBaseScrollView;
    protected View mBlueTopOverlay;
    protected View mWrapToolbar;

    @Override
    protected void onCreate(int resId, int checkedMenuItem) {
        super.onCreate(resId, checkedMenuItem);

        mBaseScrollView = (ScrollView) findViewById(R.id.base_scroll_view);
        mWrapToolbar = findViewById(R.id.wrap_toolbar);
        mBlueTopOverlay = findViewById(R.id.blue_top_overlay);
        initScrolling();
    }

    private void initScrolling() {
        mBaseScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int avatarHeight = getAvatarHeight(); // высота аватарки вверху
                int toolbarH = AppUtils.getToolbarHeight(HeaderImageDrawerActivity.this); // высота тулбара
                int statusBarH = AppUtils.getStatusBarHeight();
                int delta = avatarHeight - toolbarH - statusBarH; // после чего происходит магия

                int scrollY = mBaseScrollView.getScrollY(); //for verticalScrollView

                if (scrollY < delta) {
                    // меняем цвет от прозрачного к синему у appbarlayout
                    float scrollPercent = (float) scrollY / (float) delta;
                    mBlueTopOverlay.setAlpha(scrollPercent);
                    mWrapToolbar.setBackgroundResource(android.R.color.transparent);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                } else {
                    mBlueTopOverlay.setAlpha(1);
                    // меняем background у toolbara на синий
                    mWrapToolbar.setBackgroundResource(R.color.colorPrimary);
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                }
            }
        });
    }

    protected int getAvatarHeight() {
        return (int) getResources().getDimension(R.dimen.lockerroom_avatar_height);
    }

}
