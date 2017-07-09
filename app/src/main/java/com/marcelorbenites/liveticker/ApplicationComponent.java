package com.marcelorbenites.liveticker;

import android.content.Context;
import com.marcelorbenites.liveticker.scheduler.GcmTaskFactory;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.squareup.sqlbrite.BriteDatabase;
import dagger.Component;
import javax.inject.Named;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Singleton @Component(modules = ApplicationModule.class) public interface ApplicationComponent {

  @Named("application") Context context();

  Retrofit retrofit();

  BriteDatabase briteDatabase();

  GcmNetworkManager gcmNetworkManager();

  GcmTaskFactory gcmTaskFactory();
}
