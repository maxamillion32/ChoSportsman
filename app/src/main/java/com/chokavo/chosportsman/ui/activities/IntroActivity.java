package com.chokavo.chosportsman.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.fragments.IntroPagerFragment;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by repitch on 26.05.16.
 */
public class IntroActivity extends BaseActivity {

    public static final String LAUNCHED_MANUALLY = "LAUNCHED_MANUALLY";

    private ViewPager mViewPagerIntro;
    private CirclePageIndicator indicator;
    private FloatingActionButton mFabSkip;
    private boolean launchedManually; // если true - то мы открыли это вручную
    // если false, то открылось автоматом при запуске приложения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // по умолчанию запущен в автоматическом режиме
        launchedManually = getIntent().getBooleanExtra(LAUNCHED_MANUALLY, false);

        mViewPagerIntro = (ViewPager) findViewById(R.id.view_pager_intro);
        mViewPagerIntro.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPagerIntro);

        mFabSkip = (FloatingActionButton) findViewById(R.id.fab_skip);
        mFabSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Skip intro
//                PreferencesManager.setIntroShown();
                finish();
            }
        });
    }

    public class IntroPagerAdapter extends FragmentStatePagerAdapter {

        private static final int PAGES_NUM = 4;

        SparseArray<Fragment> fragments = new SparseArray<>();

        public IntroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return IntroPagerFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGES_NUM;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fragments.put(position, fragment);
            return fragment;
        }

        public Fragment getFragment(int position) {
            return fragments.get(position);
        }

    }

    @Override
    public void onBackPressed() {
//        if (launchedManually) {
            super.onBackPressed();
//        }
    }
}
