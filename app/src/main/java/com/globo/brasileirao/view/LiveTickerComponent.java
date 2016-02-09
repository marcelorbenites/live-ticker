package com.globo.brasileirao.view;

import com.globo.brasileirao.view.scopes.PerLiveTickerActivity;

import dagger.Component;

@PerLiveTickerActivity
@Component(dependencies = ActivityComponent.class)
public interface LiveTickerComponent {

    void injectLiveTickerActivity(LiveTickerActivity liveTickerActivity);
}