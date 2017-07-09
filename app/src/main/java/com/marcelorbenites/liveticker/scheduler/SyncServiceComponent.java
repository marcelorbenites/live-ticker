package com.marcelorbenites.liveticker.scheduler;

import com.marcelorbenites.liveticker.ApplicationComponent;
import dagger.Component;

@PerService @Component(dependencies = ApplicationComponent.class, modules = SyncServiceModule.class)
public interface SyncServiceComponent {

  void injectSyncService(SyncService syncService);
}