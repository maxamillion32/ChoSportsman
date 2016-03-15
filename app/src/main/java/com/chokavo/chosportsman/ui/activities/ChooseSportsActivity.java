package com.chokavo.chosportsman.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.DataManager;
import com.chokavo.chosportsman.models.SportKind;
import com.chokavo.chosportsman.models.SportKindFactory;
import com.chokavo.chosportsman.ui.adapters.ChooseSportsAdapter;
import com.chokavo.chosportsman.ui.adapters.SportObjectAdapter;

import java.util.HashSet;
import java.util.Set;

public class ChooseSportsActivity extends AppCompatActivity {
    SportKindFactory mSportKindFactory;
    RecyclerView recyclerView;
    ChooseSportsAdapter adapter;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sports);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar()!=null)
            getSupportActionBar().setTitle(R.string.choose_sport_please);

        SharedPreferences preferences = DataManager.getInstance().mPreferences;
        Set<String> sportKinds = preferences.getStringSet(getString(R.string.sport_kinds),null);
        if (sportKinds != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        loadSports();
        recyclerView = (RecyclerView)findViewById(R.id.choose_sports_recview);
        adapter = new ChooseSportsAdapter(mSportKindFactory.getSportKinds());
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_sports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_continue) {
            Set<SportKind> mCheckedSports = new HashSet<>();
            boolean isEmpty = true;
            for (int i = 0; i < mSportKindFactory.getSportKinds().size(); i++) {
                SportKind sport = mSportKindFactory.getSportKinds().get(i);
                if (sport.isChecked()) {
                    mCheckedSports.add(sport);
                    if (isEmpty)
                        isEmpty = false;
                }
            }
            if (isEmpty) {
                Snackbar.make(recyclerView, "Выберите хотя бы один вид спорта", Snackbar.LENGTH_LONG).show();
                return false;
            }
            DataManager.getInstance().setUserSports(mCheckedSports, getString(R.string.sport_kinds));
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadSports(){
        mSportKindFactory = SportKindFactory.get(getApplicationContext());
        SportKind football = new SportKind("Футбол");
        SportKind voleyball = new SportKind("Волейбол");
        SportKind hockey = new SportKind("Хокей");
        SportKind basketball = new SportKind("Баскетбол");
        mSportKindFactory.addSportKind(football);
        mSportKindFactory.addSportKind(voleyball);
        mSportKindFactory.addSportKind(hockey);
        mSportKindFactory.addSportKind(basketball);
    }

    @Override
    protected void onDestroy() {
        mSportKindFactory.clear();
        super.onDestroy();
    }
}
