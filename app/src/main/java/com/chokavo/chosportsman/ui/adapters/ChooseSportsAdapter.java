package com.chokavo.chosportsman.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.SportKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Дашицырен on 13.03.2016.
 */
public class ChooseSportsAdapter extends RecyclerView.Adapter<ChooseSportsAdapter.ViewHolder> {

    private List<SportKind> sports = new ArrayList<>();

    public ChooseSportsAdapter(List<SportKind> sportKinds) {
        sports = sportKinds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_sportkind, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SportKind sport = sports.get(position);
        holder.sportKindName.setText(sport.getName());
        holder.isChoosed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sport.setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView sportKindName;
        public CheckBox isChoosed;

        public ViewHolder(View itemView) {
            super(itemView);
            sportKindName = (TextView) itemView.findViewById(R.id.sportkind_tv);
            isChoosed = (CheckBox) itemView.findViewById(R.id.ischoosed_chbox);
        }
    }
}
