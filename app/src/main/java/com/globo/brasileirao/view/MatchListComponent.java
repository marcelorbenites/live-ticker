package com.globo.brasileirao.view;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.data.DataModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = DataModule.class)
public interface MatchListComponent {

    void injectMatchListActivity(MatchListActivity activity);

}