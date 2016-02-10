package com.globo.brasileirao.view;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.view.modules.LiveTickerModule;
import com.globo.brasileirao.view.scopes.PerActivity;
import com.globo.brasileirao.view.utils.UnitConverter;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = LiveTickerModule.class)
public interface LiveTickerComponent {

    void injectLiveTickerActivity(LiveTickerActivity liveTickerActivity);

}