package com.globo.brasileirao.scheduler;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.BrasileiraoApplication;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import javax.inject.Inject;

import timber.log.Timber;

public class SyncService extends GcmTaskService {

    @Inject GcmTaskTagExecutor executor;

    @Override public void onCreate() {
        super.onCreate();
        inject();
    }

    private void inject() {
        DaggerSyncServiceComponent.builder()
                .applicationComponent(getApplicationComponent())
                .syncServiceModule(new SyncServiceModule())
                .build()
                .injectSyncService(this);
    }

    @Override public int onRunTask(TaskParams taskParams) {
        final int result = executor.execute(taskParams.getTag(), taskParams.getExtras());
        Timber.d("Synchronization finished for tag %s with result %d.", taskParams.getTag(), result);
        return result;
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((BrasileiraoApplication) getApplication()).getApplicationComponent();
    }
}