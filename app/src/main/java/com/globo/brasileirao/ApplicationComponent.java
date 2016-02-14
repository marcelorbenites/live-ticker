package com.globo.brasileirao;

import android.content.Context;

import com.globo.brasileirao.scheduler.GcmTaskFactory;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @Named("application") Context context();

    Retrofit retrofit();

    BriteDatabase briteDatabase();

    GcmNetworkManager gcmNetworkManager();

    GcmTaskFactory gcmTaskFactory();
}
