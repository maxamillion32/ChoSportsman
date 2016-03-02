package com.chokavo.opendatamos.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chokavo.opendatamos.R;
import com.chokavo.opendatamos.network.DataRow;

import java.util.List;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class TariffsAdapter extends RecyclerView.Adapter<TariffsAdapter.ViewHolder> {

    List<DataRow> mDataRows;

    public TariffsAdapter(List<DataRow> dataRows) {
        mDataRows = dataRows;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTxtTitle;
        public ViewHolder(View v) {
            super(v);
            mTxtTitle = (TextView) v.findViewById(R.id.txt_title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tariff_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataRow dataRow = mDataRows.get(position);
        holder.mTxtTitle.setText(dataRow.getCells().getNameOfTariff());
    }

    @Override
    public int getItemCount() {
        return (mDataRows == null) ? 0 : mDataRows.size();
    }
}
