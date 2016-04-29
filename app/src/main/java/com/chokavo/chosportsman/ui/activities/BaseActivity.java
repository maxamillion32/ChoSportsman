package com.chokavo.chosportsman.ui.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.chokavo.chosportsman.R;

import java.util.List;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 2;

    protected Toolbar mToolbar;

    public void launchActivityWithSharedElement(Class activity, View sharedElement, Bundle args) {
        Intent intent = new Intent(this, activity);
        // Pass data object in the bundle and populate details activity.
        if (args != null) {
            intent.putExtras(args);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = null;
            options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, sharedElement, sharedElement.getTransitionName());
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void launchDialog(DialogFragment dialogFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialogFragment.show(ft, tag);
    }

    public void launchFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, tag)
//        ft.add(R.id.content_frame, fragment, fragment.getTag())
                .addToBackStack(tag)
                .commit();
    }

    public void launchFragmentNoBackStack(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, tag)
                .commit();
    }

    public static final int ANIM_LEFT2RIGHT = 1;

    public void launchAnimationFragment(Fragment fragment, String tag, int animType) {
        clearBackStack();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // тут буду мутить свои анимашки, пока что тест
        switch (animType) {
            case ANIM_LEFT2RIGHT:
                ft.setCustomAnimations(
                        R.anim.left_to_right, R.anim.right_to_left);
                break;
            default:
                return;
        }
        ft.replace(R.id.content_frame, fragment, tag)
                .commit();

    }

    public void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("STATES", "onCreate by " + this.getClass());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        shouldDisplayHomeUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // methods for back stack toggle
    private final FragmentManager.OnBackStackChangedListener
            mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            shouldDisplayHomeUp();
        }
    };

    protected void showBackStackToggle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        shouldDisplayHomeUp();
        getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
    }

    /**
     * Является ли текущее активити последним в стеке
     * @return true, если активити последнее в стеке
     */
    public boolean isLastActivityInStack() {
        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if(taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            return true;
        }
        return false;
    }

    public void shouldDisplayHomeUp() {
        boolean canBack;
        if (isLastActivityInStack()) {
            // последний активити в стеке
            canBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        } else {
            canBack = true;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
        }
        if (mToolbar != null && canBack) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    shouldDisplayHomeUp();
                }
            });
        }
    }
}
