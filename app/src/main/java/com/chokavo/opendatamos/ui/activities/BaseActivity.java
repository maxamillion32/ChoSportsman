package com.chokavo.opendatamos.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.chokavo.opendatamos.R;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public abstract class BaseActivity extends AppCompatActivity {
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
}
