package com.marcelorbenites.liveticker.scheduler;

import com.marcelorbenites.liveticker.ApplicationComponent;
import com.marcelorbenites.liveticker.LiveTickerApplication;
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

  @Override public int onRunTask(TaskParams taskParams) {
    final int result = executor.execute(taskParams.getTag(), taskParams.getExtras());
    Timber.d("Synchronization finished for tag %s with result %d.", taskParams.getTag(), result);
    return result;
  }

  private void inject() {
    DaggerSyncServiceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .syncServiceModule(new SyncServiceModule())
        .build()
        .injectSyncService(this);
  }

  protected ApplicationComponent getApplicationComponent() {
    return ((LiveTickerApplication) getApplication()).getApplicationComponent();
  }
}