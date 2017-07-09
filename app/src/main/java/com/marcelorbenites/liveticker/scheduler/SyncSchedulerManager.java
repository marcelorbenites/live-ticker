package com.marcelorbenites.liveticker.scheduler;

import android.os.Bundle;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;

public class SyncSchedulerManager implements MatchSyncScheduler, MatchLiveTickerScheduler {

  public static final String TAG_SYNC_MATCHES = "com.globo.sync.tag.SYNC_MATCHES";
  public static final String TAG_SYNC_MATCH_LIVE_TICKER =
      "com.globo.sync.tag.SYNC_MATCH_LIVE_TICKER";
  public static final String EXTRA_MATCH_ID = "com.globo.sync.extra.EXTRA_MATCH_ID";
  private final GcmTaskFactory gcmTaskFactory;
  private final Class<? extends GcmTaskService> serviceClass;
  private GcmNetworkManager gcmNetworkManager;

  public SyncSchedulerManager(GcmNetworkManager gcmNetworkManager, GcmTaskFactory gcmTaskFactory,
      Class<? extends GcmTaskService> serviceClass) {
    this.gcmNetworkManager = gcmNetworkManager;
    this.gcmTaskFactory = gcmTaskFactory;
    this.serviceClass = serviceClass;
  }

  @Override public void syncMatches(long interval) {
    gcmNetworkManager.schedule(
        gcmTaskFactory.createPeriodicTask(TAG_SYNC_MATCHES, interval, serviceClass, null));
  }

  @Override public void cancelMatchesSync() {
    gcmNetworkManager.cancelTask(TAG_SYNC_MATCHES, serviceClass);
  }

  @Override public void syncMatchLiveTicker(int matchId, long interval) {
    final Bundle bundle = new Bundle();
    bundle.putInt(EXTRA_MATCH_ID, matchId);
    gcmNetworkManager.schedule(
        gcmTaskFactory.createPeriodicTask(TAG_SYNC_MATCH_LIVE_TICKER, interval, serviceClass,
            bundle));
  }

  @Override public void cancelMatchesLiveTickerSync() {
    gcmNetworkManager.cancelTask(TAG_SYNC_MATCH_LIVE_TICKER, serviceClass);
  }
}