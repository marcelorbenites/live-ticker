package com.globo.brasileirao.view;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.view.image.ImageModule;
import com.globo.brasileirao.view.modules.MatchListModule;
import com.globo.brasileirao.view.scopes.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MatchListModule.class)
public interface MatchListComponent {

    void injectMatchListActivity(MatchListActivity activity);

}