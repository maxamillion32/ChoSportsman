package com.chokavo.chosportsman.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.chokavo.chosportsman.App;
import com.chokavo.chosportsman.AppUtils;
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ormlite.models.STeam;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Дашицырен on 27.04.2016.
 */
public class TeamsListAdapter extends RecyclerView.Adapter<TeamsListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(STeam team);
    }

    private List<STeam> mTeams = new ArrayList<>();
    private final OnItemClickListener mItemListener;
    private final OnItemClickListener mDeleteListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public TeamsListAdapter(List<STeam> teams, OnItemClickListener listener, OnItemClickListener deleteListener) {
        mTeams = teams;
        mItemListener = listener;
        mDeleteListener = deleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teams_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mTeams.get(position), mItemListener, mDeleteListener);
        viewBinderHelper.bind(holder.mSwipeRevealLayout, Integer.toString(mTeams.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return mTeams == null ? 0 : mTeams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTxtSportType, mTxtTeamName;
        public CircleImageView mImgTeamPhoto;
        public ImageView mImgHaveProfile;
        public SwipeRevealLayout mSwipeRevealLayout;
        public FrameLayout mFrameDeleteSport;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtSportType = (TextView) itemView.findViewById(R.id.txt_sporttype);
            mImgTeamPhoto = (CircleImageView) itemView.findViewById(R.id.image_team);
            mImgHaveProfile = (ImageView) itemView.findViewById(R.id.img_have_sport_profile);
            mSwipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            mFrameDeleteSport = (FrameLayout) itemView.findViewById(R.id.frame_delete_user_sport);
            mTxtTeamName = (TextView) itemView.findViewById(R.id.txt_team_name);
        }

        public void bind(final STeam item, final OnItemClickListener listener, final OnItemClickListener deleteListener) {
            Picasso.with(App.getInstance())
                    .load(item.getIconId())
                    .resize((int) AppUtils.convertDpToPixel(56, App.getInstance()), 0)
                    .into(mImgTeamPhoto);
            mTxtTeamName.setText(item.getName());
            mTxtSportType.setText(item.getSportType().getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            mFrameDeleteSport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onItemClick(item);
                }
            });
        }
    }
}
