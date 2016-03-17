package com.chokavo.chosportsman.ui.adapters;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.SportKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Дашицырен on 17.03.2016.
 */
public class UserSportsAdapter extends RecyclerView.Adapter<UserSportsAdapter.ViewHolder> {

    private List<SportKind> sports = new ArrayList<>();

    public UserSportsAdapter(Set<SportKind> sportKinds) {
        sports.addAll(sportKinds);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_sports, parent, false);
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
        public ImageView sportImage;

        public ViewHolder(View itemView) {
            super(itemView);
            sportKindName = (TextView) itemView.findViewById(R.id.sportkind_tv);
            isChoosed = (CheckBox) itemView.findViewById(R.id.checkbox_delete_sport);
            sportImage = (ImageView) itemView.findViewById(R.id.image_sport);
        }
    }
}
