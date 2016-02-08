package com.globo.brasileirao.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.globo.brasileirao.view.image.ImageLoader;
import com.globo.brasileirao.view.image.PicassoImageLoader;

import java.text.DateFormat;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides @Named("activity") Context provideContext() {
        return activity;
    }

    @Provides LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(activity);
    }

    @Provides LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Provides ImageLoader provideImageLoader(@Named("application") Context context) {
        return new PicassoImageLoader(context);
    }

    @Provides DateFormat provideDateFormat() {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    }
}