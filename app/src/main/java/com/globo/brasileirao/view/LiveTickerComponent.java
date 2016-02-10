package com.globo.brasileirao.view;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.view.image.ImageModule;
import com.globo.brasileirao.view.modules.ActivityModule;
import com.globo.brasileirao.view.scopes.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ImageModule.class})
public interface LiveTickerComponent {

    void injectLiveTickerActivity(LiveTickerActivity liveTickerActivity);
}