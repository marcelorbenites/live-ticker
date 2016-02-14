package com.globo.brasileirao.scheduler;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SyncSchedulerManagerTest {

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
        when(gcmTaskFactoryMock.createPeriodicTask(tag, 49, serviceClass)).thenReturn(periodicTaskMock);
        scheduler.syncMatches(49);

        verify(gcmTaskFactoryMock).createPeriodicTask(tag, 49, serviceClass);
        verify(gcmNetworkManagerMock).schedule(periodicTaskMock);
    }

    @Test public void cancelSync() throws Exception {
        scheduler.cancelMatchesSync();
        verify(gcmNetworkManagerMock).cancelTask("com.globo.sync.tag.SYNC_MATCHES", serviceClass);
    }
}