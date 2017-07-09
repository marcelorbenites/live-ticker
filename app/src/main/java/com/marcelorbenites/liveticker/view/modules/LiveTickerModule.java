package com.marcelorbenites.liveticker.view.modules;

import com.marcelorbenites.liveticker.data.DataModule;
import com.marcelorbenites.liveticker.scheduler.GcmTaskFactory;
import com.marcelorbenites.liveticker.scheduler.MatchLiveTickerScheduler;
import com.marcelorbenites.liveticker.scheduler.SyncSchedulerManager;
import com.marcelorbenites.liveticker.scheduler.SyncService;
import com.marcelorbenites.liveticker.view.image.ImageModule;
import com.google.android.gms.gcm.GcmNetworkManager;
import dagger.Module;
import dagger.Provides;

@Module(includes = { ActivityModule.class, ImageModule.class, DataModule.class })
public class LiveTickerModule {

  @Provides MatchLiveTickerScheduler provideMatchLiveTickerScheduler(
      GcmNetworkManager gcmNetworkManager, GcmTaskFactory gcmTaskFactory) {
    return new SyncSchedulerManager(gcmNetworkManager, gcmTaskFactory, SyncService.class);
  }
}