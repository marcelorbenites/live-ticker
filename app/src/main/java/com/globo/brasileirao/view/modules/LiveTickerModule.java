package com.globo.brasileirao.view.modules;

import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.scheduler.GcmTaskFactory;
import com.globo.brasileirao.scheduler.MatchLiveTickerScheduler;
import com.globo.brasileirao.scheduler.SyncSchedulerManager;
import com.globo.brasileirao.scheduler.SyncService;
import com.globo.brasileirao.view.image.ImageModule;
import com.google.android.gms.gcm.GcmNetworkManager;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityModule.class, ImageModule.class, DataModule.class})
public class LiveTickerModule {

    @Provides MatchLiveTickerScheduler provideMatchLiveTickerScheduler(GcmNetworkManager gcmNetworkManager, GcmTaskFactory gcmTaskFactory) {
        return new SyncSchedulerManager(gcmNetworkManager, gcmTaskFactory, SyncService.class);
    }

}