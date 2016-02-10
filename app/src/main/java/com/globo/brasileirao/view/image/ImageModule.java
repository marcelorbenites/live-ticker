package com.globo.brasileirao.view.image;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ImageModule {

    @Provides ImageLoader provideImageLoader(@Named("application") Context context) {
        return new PicassoImageLoader(context);
    }
}
