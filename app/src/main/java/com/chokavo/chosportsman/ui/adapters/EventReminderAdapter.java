package com.chokavo.chosportsman.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.calendar.EventReminderX;
import com.google.api.services.calendar.model.EventReminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilyapyavkin on 04.04.16.
 */
public class EventReminderAdapter extends RecyclerView.Adapter<EventReminderAdapter.ViewHolder> {

    private View.OnClickListener mOnItemClickListener;
    private Context mContext;
    private List<EventReminder> mEventReminders = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtText;

        public ViewHolder(View v) {
            super(v);
            mTxtText = (TextView) v.findViewById(R.id.text);
        }
    }

    @Override
    public int getItemCount() {
        return (mEventReminders == null) ? 0 : mEventReminders.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_reminder, parent, false);
        v.setOnClickListener(mOnItemClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventReminder item = mEventReminders.get(position);
        holder.mTxtText.setText(EventReminderX.toReadableString(item));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public EventReminderAdapter(Context context, List<EventReminder> eventReminders, View.OnClickListener onItemClickListener) {
        mContext = context;
        mEventReminders = eventReminders;
        mOnItemClickListener = onItemClickListener;
    }
}
