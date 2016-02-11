package com.globo.brasileirao.view.modules;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.globo.brasileirao.BuildConfig;
import com.globo.brasileirao.data.MatchRepository;
import com.globo.brasileirao.data.MatchRepositoryManager;
import com.globo.brasileirao.data.disk.MatchDiskRepository;
import com.globo.brasileirao.data.disk.MatchSQLiteRepository;
import com.globo.brasileirao.data.network.MatchNetworkRepository;
import com.globo.brasileirao.data.network.MatchRestRepository;
import com.globo.brasileirao.data.network.MatchRestService;
import com.globo.brasileirao.exceptions.NetworkErrorToStringResourceConverter;
import com.globo.brasileirao.exceptions.ThrowableToStringResourceConverter;
import com.globo.brasileirao.view.image.ImageLoader;
import com.globo.brasileirao.view.image.PicassoImageLoader;
import com.squareup.sqlbrite.BriteDatabase;

import java.text.DateFormat;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(activity);
    }

    @Provides LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Provides DateFormat provideDateFormat() {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    }

    @Provides ThrowableToStringResourceConverter provideThrowableToStringResourceConverter() {
        return new NetworkErrorToStringResourceConverter();
    }
}