package com.globo.brasileirao.view;

import android.app.Activity;
import android.os.Bundle;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.BrasileiraoApplication;
import com.globo.brasileirao.R;
import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.data.MatchRepository;

import javax.inject.Inject;

public class MatchListActivity extends Activity {

    @Inject MatchRepository repository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        DaggerMatchListComponent.builder()
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule())
                .build()
                .injectMatchListActivity(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((BrasileiraoApplication) getApplication()).getApplicationComponent();
    }
}
