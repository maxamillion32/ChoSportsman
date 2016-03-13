package com.chokavo.chosportsman.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chokavo.chosportsman.R;
import com.chokavo.chosportsman.models.SportKind;
import com.chokavo.chosportsman.models.SportKindFactory;
import com.chokavo.chosportsman.ui.adapters.ChooseSportsAdapter;

public class ChooseSportsActivity extends AppCompatActivity {
    SportKindFactory mSportKindFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sports);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(R.string.choose_sport_please);

        loadSports();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.choose_sports_recview);
        ChooseSportsAdapter adapter = new ChooseSportsAdapter(mSportKindFactory.getSportKinds());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

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
}
