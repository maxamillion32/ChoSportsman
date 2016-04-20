package com.chokavo.chosportsman.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.google.api.services.calendar.model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дашицырен on 13.03.2016.
 */
public class DayEventAdapter extends RecyclerView.Adapter<DayEventAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    private List<Event> mDayEvents = new ArrayList<>();
    private final OnItemClickListener mListener;

    public DayEventAdapter(List<Event> dayEvents, OnItemClickListener listener) {
        mDayEvents = dayEvents;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = mDayEvents.get(position);

        boolean allDay = event.getStart().getDate() != null;
        long startMs, endMs;

        if (allDay) {
            startMs = event.getStart().getDate().getValue();
            holder.mTxtTimeStart.setText("Весь день");
            holder.mTxtTimeEnd.setText("");
        } else {
            startMs = event.getStart().getDateTime().getValue();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            holder.mTxtTimeStart.setText(dateFormat.format(startMs));

            endMs = event.getEnd().getDateTime().getValue();
            holder.mTxtTimeEnd.setText(dateFormat.format(endMs));
        }

        if (event.getSummary() == null || event.getSummary().isEmpty()) {
            holder.mTxtSummary.setText("Без названия");
        } else {
            holder.mTxtSummary.setText(event.getSummary());
        }
        if (event.getLocation() == null || event.getLocation().isEmpty()) {
            holder.mTxtLocation.setText("Без позиции");
        } else {
            holder.mTxtLocation.setText(event.getLocation());
        }

        holder.bind(mDayEvents.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDayEvents == null ? 0 : mDayEvents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTxtTimeStart, mTxtTimeEnd, mTxtSummary, mTxtLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtTimeStart = (TextView) itemView.findViewById(R.id.txt_time_start);
            mTxtTimeEnd = (TextView) itemView.findViewById(R.id.txt_time_end);
            mTxtSummary = (TextView) itemView.findViewById(R.id.txt_summary);
            mTxtLocation = (TextView) itemView.findViewById(R.id.txt_location);
        }

        public void bind(final Event item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
