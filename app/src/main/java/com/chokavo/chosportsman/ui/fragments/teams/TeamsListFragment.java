package com.chokavo.chosportsman.ui.fragments.teams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.ormlite.models.SSportType;
import com.chokavo.chosportsman.ormlite.models.STeam;
import com.chokavo.chosportsman.ui.activities.teams.TeamsListActivity;
import com.chokavo.chosportsman.ui.adapters.TeamsListAdapter;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дашицырен on 27.04.2016.
 */
public class TeamsListFragment extends BaseFragment {
    @Override
    public String getFragmentTitle() {
        switch (mMode) {
            case TeamsListActivity.EXTRA_MODE_FAN:
                return getString(R.string.Im_fan_of);
            case TeamsListActivity.EXTRA_MODE_MEMBER:
                return getString(R.string.Im_player_in);
            default:
                return "Список команд";
        }
    }

    RecyclerView mRecViewTeams;
    TeamsListAdapter mTeamsListAdapter;
//    boolean isFanMode = true;
    private int mMode;
    List<STeam> mTeams = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teams_list, container, false);
        mRecViewTeams = (RecyclerView) rootView.findViewById(R.id.recview_teams);
        loadUserTeams();
        mTeams = mMode == TeamsListActivity.EXTRA_MODE_FAN ? DataManager.getInstance().mSportsman.getFanTeams()
                : DataManager.getInstance().mSportsman.getPlayerTeams();
        mTeamsListAdapter = new TeamsListAdapter(mTeams, new TeamsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(STeam team) {
                //TODO:open team something
            }
        }, new TeamsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(STeam team) {
                //TODO:delete team from user account
            }
        });
        mRecViewTeams.setAdapter(mTeamsListAdapter);
        mRecViewTeams.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mMode = getArguments().getInt(TeamsListActivity.EXTRA_MODE);
        }
        ((TeamsListActivity) getActivity()).setToolbarTitle(getFragmentTitle());
    }

    public static TeamsListFragment newInstance(Bundle args) {
        TeamsListFragment fragment = new TeamsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Hardcode method
     */
    public static void loadUserTeams() {
        List<STeam> fanteams = new ArrayList<>();
        List<STeam> playerteams = new ArrayList<>();
        List<SSportType> sportTypes = DataManager.getInstance().getSportTypes();
        fanteams.add(new STeam("Lakers", sportTypes.get(2)));
        fanteams.add(new STeam("Локомотив", sportTypes.get(0)));
        fanteams.add(new STeam("Manchester United", sportTypes.get(0)));
        fanteams.add(new STeam("Зенит", sportTypes.get(0)));
        DataManager.getInstance().mSportsman.setFanTeams(fanteams);
        playerteams.add(new STeam("Челябинский тракторист", sportTypes.get(4)));
        playerteams.add(new STeam("Зеленоградский электрон", sportTypes.get(1)));
        playerteams.add(new STeam("Еду", sportTypes.get(5)));
        playerteams.add(new STeam("Сборная МИЭТ по теннису", sportTypes.get(3)));
        DataManager.getInstance().mSportsman.setPlayerTeams(playerteams);
    }

}
