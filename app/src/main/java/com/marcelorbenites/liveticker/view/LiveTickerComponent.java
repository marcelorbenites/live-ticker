package com.marcelorbenites.liveticker.view;

import com.marcelorbenites.liveticker.ApplicationComponent;
import com.marcelorbenites.liveticker.view.modules.LiveTickerModule;
import com.marcelorbenites.liveticker.view.scopes.PerActivity;
import dagger.Component;

@PerActivity @Component(dependencies = ApplicationComponent.class, modules = LiveTickerModule.class)
public interface LiveTickerComponent {

  void injectLiveTickerActivity(LiveTickerActivity liveTickerActivity);
}