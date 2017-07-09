package com.marcelorbenites.liveticker.scheduler;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class) public class SyncSchedulerManagerTest {

  private GcmNetworkManager gcmNetworkManagerMock;
  private SyncSchedulerManager scheduler;
  private GcmTaskFactory gcmTaskFactoryMock;
  private Class<? extends GcmTaskService> serviceClass;

  @Before public void setUp() throws Exception {
    gcmTaskFactoryMock = mock(GcmTaskFactory.class);
    gcmNetworkManagerMock = mock(GcmNetworkManager.class);
    serviceClass = SyncService.class;
    scheduler = new SyncSchedulerManager(gcmNetworkManagerMock, gcmTaskFactoryMock, serviceClass);
  }

  @Test public void syncMatches() throws Exception {
    String tag = "com.globo.sync.tag.SYNC_MATCHES";
    PeriodicTask periodicTaskMock = mock(PeriodicTask.class);
    when(gcmTaskFactoryMock.createPeriodicTask(tag, 49, serviceClass, null)).thenReturn(
        periodicTaskMock);
    scheduler.syncMatches(49);

    verify(gcmTaskFactoryMock).createPeriodicTask(tag, 49, serviceClass, null);
    verify(gcmNetworkManagerMock).schedule(periodicTaskMock);
  }

  @Test public void cancelSync() throws Exception {
    scheduler.cancelMatchesSync();
    verify(gcmNetworkManagerMock).cancelTask("com.globo.sync.tag.SYNC_MATCHES", serviceClass);
  }

  @Test public void syncMatchLiveTicker() throws Exception {
    String tag = "com.globo.sync.tag.SYNC_MATCH_LIVE_TICKER";
    PeriodicTask periodicTaskMock = mock(PeriodicTask.class);
    when(gcmTaskFactoryMock.createPeriodicTask(eq(tag), eq(80L), eq(serviceClass),
        any(Bundle.class))).thenReturn(periodicTaskMock);
    scheduler.syncMatchLiveTicker(1, 80);

    verify(gcmTaskFactoryMock).createPeriodicTask(eq(tag), eq(80L), eq(serviceClass),
        any(Bundle.class));
    verify(gcmNetworkManagerMock).schedule(periodicTaskMock);
  }

  @Test public void cancelMatchesLiveTickerSync() throws Exception {
    scheduler.cancelMatchesLiveTickerSync();
    verify(gcmNetworkManagerMock).cancelTask("com.globo.sync.tag.SYNC_MATCH_LIVE_TICKER",
        serviceClass);
  }
}