package com.globo.brasileirao;

import android.app.Application;

import com.globo.brasileirao.data.disk.BrasileiraoDatabaseHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.mongolab.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient())
                .build();
    }

    @Provides @Singleton BriteDatabase provideDatabase() {
        return SqlBrite.create().wrapDatabaseHelper(new BrasileiraoDatabaseHelper(application));
    }
}