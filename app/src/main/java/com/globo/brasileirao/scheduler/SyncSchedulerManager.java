package com.globo.brasileirao.scheduler;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;

public class SyncSchedulerManager implements MatchSyncScheduler {

    public static final String TAG_SYNC_MATCHES = "com.globo.sync.tag.SYNC_MATCHES";

    private GcmNetworkManager gcmNetworkManager;
    private final GcmTaskFactory gcmTaskFactory;
    private final Class<? extends GcmTaskService> serviceClass;

    public SyncSchedulerManager(GcmNetworkManager gcmNetworkManager, GcmTaskFactory gcmTaskFactory, Class<? extends GcmTaskService> serviceClass) {
        this.gcmNetworkManager = gcmNetworkManager;
        this.gcmTaskFactory = gcmTaskFactory;
        this.serviceClass = serviceClass;
    }

    @Override public void syncMatches(long interval) {
        gcmNetworkManager.schedule(gcmTaskFactory.createPeriodicTask(TAG_SYNC_MATCHES, interval, serviceClass));
    }

    @Override public void cancelMatchesSync() {
        gcmNetworkManager.cancelTask(TAG_SYNC_MATCHES, serviceClass);
    }
}