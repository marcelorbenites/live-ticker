package com.globo.brasileirao.scheduler;

import com.globo.brasileirao.ApplicationComponent;

import dagger.Component;

@PerService
@Component(dependencies = ApplicationComponent.class, modules = SyncServiceModule.class)
public interface SyncServiceComponent {

    void injectSyncService(SyncService syncService);

}