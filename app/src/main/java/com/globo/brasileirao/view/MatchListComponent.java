package com.globo.brasileirao.view;

import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.view.scopes.PerMatchListActivity;

import dagger.Component;

@PerMatchListActivity
@Component(dependencies = ActivityComponent.class, modules = {DataModule.class, MatchListModule.class})
public interface MatchListComponent {

    void injectMatchListActivity(MatchListActivity activity);

}