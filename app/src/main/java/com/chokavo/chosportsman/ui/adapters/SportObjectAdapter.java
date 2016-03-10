package com.chokavo.chosportsman.ui.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.network.cells.SportObjectCells;
import com.chokavo.chosportsman.network.datarows.SportObjectDataRow;

import java.util.List;

/**
 * Created by ilyapyavkin on 02.03.16.
 */
public class SportObjectAdapter extends RecyclerView.Adapter<SportObjectAdapter.ViewHolder> {

    List<SportObjectDataRow> mDataRows;

    public SportObjectAdapter(List<SportObjectDataRow> dataRows) {
        mDataRows = dataRows;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTxtTitle, mTxtAddress, mTxtWorkingHours;
        public Button mBtnCall, mBtnMap;

        public ViewHolder(View v) {
            super(v);
            mTxtTitle = (TextView) v.findViewById(R.id.txt_title);
            mTxtAddress = (TextView) v.findViewById(R.id.txt_address);
            mTxtWorkingHours = (TextView) v.findViewById(R.id.txt_working_hours);

            mBtnCall = (Button) v.findViewById(R.id.btn_call);
            mBtnMap = (Button) v.findViewById(R.id.btn_map);
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
        final SportObjectDataRow dataRow = mDataRows.get(position);
        final SportObjectCells cell = dataRow.getCells();
        holder.mTxtTitle.setText(cell.getCommonName());
        holder.mTxtAddress.setText(cell.getFullAddress());
        holder.mTxtWorkingHours.setText(cell.getWorkingHours());
        holder.mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.callTo(cell.getHelpPhone());
            }
        });
        holder.mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, String.format("Координаты (%s %s)", cell.getLatitude(), cell.getLongitude()), Snackbar.LENGTH_SHORT).show();
                AppUtils.openMaps(cell.getLatitude(), cell.getLongitude(), cell.getCommonName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataRows == null) ? 0 : mDataRows.size();
    }
}
