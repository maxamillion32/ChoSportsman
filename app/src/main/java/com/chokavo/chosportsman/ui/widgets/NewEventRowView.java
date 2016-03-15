package com.chokavo.chosportsman.ui.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chokavo.chosportsman.R;

/**
 * Created by repitch on 15.03.16.
 */
public class NewEventRowView extends LinearLayout {

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
        mTxtValue.setText(mValue);
    }

    private Drawable mTypeIconDrawable;
    private String mValue;
    private String mDesc;
    private ImageView mImgIcon;
    private TextView mTxtValue, mTxtDesc;

    public NewEventRowView(Context context) {
        super(context);
    }

    public NewEventRowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initViews();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NewEventRowView, 0, 0);
        try {
            mTypeIconDrawable = ta.getDrawable(R.styleable.NewEventRowView_type_icon);
            mValue = ta.getString(R.styleable.NewEventRowView_value);
            mDesc = ta.getString(R.styleable.NewEventRowView_desc);
        } finally {
            ta.recycle();
        }

        loadData();

    }

    private void loadData() {
        mImgIcon.setImageDrawable(mTypeIconDrawable);
        mTxtValue.setText(mValue);
        mTxtDesc.setText(mDesc);
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_new_event_row, this, true);
        mImgIcon = (ImageView) view.findViewById(R.id.img_icon);
        mTxtValue = (TextView) view.findViewById(R.id.txt_value);
        mTxtDesc = (TextView) view.findViewById(R.id.txt_desc);
    }
}