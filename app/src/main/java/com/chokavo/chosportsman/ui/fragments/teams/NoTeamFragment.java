package com.chokavo.chosportsman.ui.fragments.teams;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.activities.teams.CreateTeamActivity;
import com.chokavo.chosportsman.ui.activities.teams.TeamsListActivity;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;

/**
 * Created by Дашицырен on 21.04.2016.
 */
public class NoTeamFragment extends BaseFragment {
    @Override
    public String getFragmentTitle() {
        return "Мои команды";
    }

    Button mBtnCreateTeam;

    Button mBtnTestPlayerTeams;
    Button mBtnTestFanTeams;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_team, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        mBtnCreateTeam = (Button) rootView.findViewById(R.id.btn_create_team);
        mBtnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateTeamActivity.class);
                startActivity(intent);
            }
        });


        mBtnTestPlayerTeams = (Button) rootView.findViewById(R.id.btn_test_player_teams);
        mBtnTestPlayerTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeamsListActivity.class);
                intent.putExtra(TeamsListActivity.EXTRA_MODE, false);
                startActivity(intent);
            }
        });

        mBtnTestFanTeams = (Button) rootView.findViewById(R.id.btn_test_fan_teams);
        mBtnTestFanTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeamsListActivity.class);
                intent.putExtra(TeamsListActivity.EXTRA_MODE, true);
                startActivity(intent);
            }
        });
    }
}
