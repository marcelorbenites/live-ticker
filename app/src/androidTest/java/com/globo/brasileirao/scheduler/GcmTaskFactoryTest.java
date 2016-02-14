package com.globo.brasileirao.scheduler;

import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class GcmTaskFactoryTest {

    private GcmTaskFactory taskFactory;

    @Before public void setUp() throws Exception {
        taskFactory = new GcmTaskFactory();

    }

    @Test public void createPeriodicTask() throws Exception {
        final PeriodicTask periodicTask = taskFactory.createPeriodicTask("MyTag", 34, SyncService.class);
        assertEquals(Task.NETWORK_STATE_CONNECTED, periodicTask.getRequiredNetwork());
        assertEquals(false, periodicTask.isPersisted());
        assertEquals(true, periodicTask.isUpdateCurrent());
        assertEquals(false, periodicTask.getRequiresCharging());
        assertEquals(34, periodicTask.getPeriod());
        assertEquals("MyTag", periodicTask.getTag());
        assertEquals(SyncService.class.getName(), periodicTask.getServiceName());
    }
}