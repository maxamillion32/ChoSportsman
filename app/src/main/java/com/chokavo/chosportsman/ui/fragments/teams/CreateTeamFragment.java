package com.chokavo.chosportsman.ui.fragments.teams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.ui.fragments.BaseFragment;

/**
 * Created by Дашицырен on 21.04.2016.
 */
public class CreateTeamFragment extends BaseFragment {
    @Override
    public String getFragmentTitle() {
        return "Создание команды";
    }

    public static CreateTeamFragment newInstance(Bundle args) {
        CreateTeamFragment fragment = new CreateTeamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_team, container, false);
      //  initViews(rootView);
     //   initActions();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_team, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNewTeam();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNewTeam() {
        //TODO:
    }
}
