package com.globo.brasileirao;

import android.content.Context;

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
}
