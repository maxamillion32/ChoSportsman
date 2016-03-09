package com.chokavo.chosportsman.ui.views;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chokavo.chosportsman.R;

/**
 * Created by ilyapyavkin on 24.02.16.
 */
public class ImageSnackbar {
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_ERROR = 1;
    public static final int TYPE_SUCCESS = 2;
    // UI
    private Snackbar mSnackbar;
    private ImageView mImage;
    private TextView mTxtMessage;
    private View mParent;

    // params
    private boolean mIsDismissed;
    private String mMessage;
    private int mDuration;
    private int mType;

    public ImageSnackbar(@NonNull View view) {
        mParent = view;
        mSnackbar = initSnackbar();
    }

    public static ImageSnackbar make(@NonNull View parent, int type, String message, int duration) {
        ImageSnackbar imageSnackbar = new ImageSnackbar(parent);

        imageSnackbar.setType(type);
        imageSnackbar.setMessage(message);
        imageSnackbar.setDuration(duration);

        return imageSnackbar;
    }

    public static ImageSnackbar make(@NonNull View parent, String message, int duration) {
        return ImageSnackbar.make(parent, TYPE_DEFAULT, message, duration);
    }

    public void setImageResource(int resId) {
        mImage.setImageResource(resId);
    }

    public void show() {
        mSnackbar.show();
    }

    private Snackbar initSnackbar() {
        View customView = LayoutInflater.from(mParent.getContext()).inflate(R.layout.image_snackbar, null, false);
        mTxtMessage = (TextView)customView.findViewById(R.id.message);
        mImage = (ImageView)customView.findViewById(R.id.image);

        Snackbar snackbar = Snackbar.make(mParent, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        Button button = (Button) snackbarLayout.findViewById(android.support.design.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        snackbarLayout.addView(customView, 0);

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                mIsDismissed = true;
                super.onDismissed(snackbar, event);
            }

            @Override
            public void onShown(Snackbar snackbar) {
                mIsDismissed = false;
                super.onShown(snackbar);
            }
        });

        return snackbar;
    }
    public Snackbar getSnackbar() {
        return mSnackbar;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
        mTxtMessage.setText(message);
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
        mSnackbar.setDuration(duration);
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
        if (mImage != null) {
            switch (type) {
                case TYPE_ERROR:
                    mImage.setImageResource(R.drawable.ic_warning_24dp);
                    break;
                case TYPE_SUCCESS:
                    mImage.setImageResource(R.drawable.ic_done_24dp);
                    break;
            }
        }
    }
}
