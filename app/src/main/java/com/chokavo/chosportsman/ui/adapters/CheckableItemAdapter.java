package com.chokavo.chosportsman.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ArrayRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chokavo.chosportsman.R;

/**
 * Created by ilyapyavkin on 28.03.16.
 */
public class CheckableItemAdapter extends BaseAdapter implements View.OnClickListener {

    private Toast mToast;
    private final Context mContext;
    private final CharSequence[] mItems;
    private int mChosenItem;

    public CheckableItemAdapter(Context context, @ArrayRes int arrayResId) {
        this(context, context.getResources().getTextArray(arrayResId));
    }

    public CheckableItemAdapter(Context context, CharSequence[] items) {
        this(context, items, -1);
    }

    public CheckableItemAdapter(Context context, CharSequence[] items, int chosenItem) {
        this.mContext = context;
        this.mItems = items;
        this.mChosenItem = chosenItem;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public CharSequence getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    ColorStateList oldTextColor;

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean chosen = (mChosenItem == position);
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_dialog_checkable, null);
        TextView txtTitle = ((TextView) convertView.findViewById(R.id.title));
        ImageView imgCheck = ((ImageView) convertView.findViewById(R.id.img_check));

        if (oldTextColor == null) {
            oldTextColor = txtTitle.getTextColors();
        }

        if (chosen) {
            int primaryColor = mContext.getResources().getColor(R.color.colorPrimary);
            txtTitle.setTextColor(primaryColor);
            imgCheck.setColorFilter(primaryColor);
        } else {
            txtTitle.setTextColor(oldTextColor);
        }

        txtTitle.setText(mItems[position]);
        imgCheck.setVisibility(chosen ? View.VISIBLE : View.GONE);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Integer index = (Integer) v.getTag();
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(mContext, "Clicked button " + index, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
