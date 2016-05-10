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
import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dashitsyren on 27.04.2016.
 */
public class EditUserSportsAdapter extends RecyclerView.Adapter<EditUserSportsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SSportType sportType);
    }

    private List<SSportType> mUserSports = new ArrayList<>();
    private final OnItemClickListener mItemListener;
    private final OnItemClickListener mDeleteListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public EditUserSportsAdapter(List<SSportType> sportTypes, OnItemClickListener listener, OnItemClickListener deleteListener) {
        mUserSports = sportTypes;
        mItemListener = listener;
        mDeleteListener = deleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_user_sports, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mUserSports.get(position), mItemListener, mDeleteListener);
        viewBinderHelper.bind(holder.mSwipeRevealLayout, Integer.toString(mUserSports.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return mUserSports == null ? 0 : mUserSports.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTxtSportType;
        public ImageView mImgSport, mImgHaveProfile;
        public SwipeRevealLayout mSwipeRevealLayout;
        public FrameLayout mFrameDeleteSport;
        public View mSwipeMainLayout; // main layout where all main stuff situated

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtSportType = (TextView) itemView.findViewById(R.id.txt_sporttype);
            mSwipeMainLayout = itemView.findViewById(R.id.swipe_main_layout);
            mImgSport = (ImageView) itemView.findViewById(R.id.image_sport);
            mImgHaveProfile = (ImageView) itemView.findViewById(R.id.img_have_sport_profile);
            mSwipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            mFrameDeleteSport = (FrameLayout) itemView.findViewById(R.id.frame_delete_user_sport);
        }

        public void bind(final SSportType item, final OnItemClickListener listener, final OnItemClickListener deleteListener) {
            mImgSport.setImageResource(item.getIconId());
            mTxtSportType.setText(item.getTitle());
            mSwipeMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            mFrameDeleteSport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onItemClick(item);
                    mSwipeRevealLayout.close(true);
                }
            });

        }
    }
}
