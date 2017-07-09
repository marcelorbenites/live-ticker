package com.marcelorbenites.liveticker.view;

import com.marcelorbenites.liveticker.ApplicationComponent;
import com.marcelorbenites.liveticker.view.modules.MatchListModule;
import com.marcelorbenites.liveticker.view.scopes.PerActivity;
import dagger.Component;

@PerActivity @Component(dependencies = ApplicationComponent.class, modules = MatchListModule.class)
public interface MatchListComponent {

  void injectMatchListActivity(MatchListActivity activity);
}