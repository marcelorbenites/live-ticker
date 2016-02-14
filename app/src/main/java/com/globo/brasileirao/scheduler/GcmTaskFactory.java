package com.globo.brasileirao.scheduler;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import javax.inject.Inject;

public class GcmTaskFactory {

    @Inject public GcmTaskFactory() {
    }

    public PeriodicTask createPeriodicTask(String tag, long interval, Class<? extends GcmTaskService> service) {
        return new PeriodicTask.Builder()
                .setService(service)
                .setPersisted(false)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setPeriod(interval)
                .setUpdateCurrent(true)
                .setTag(tag)
                .build();
    }
}