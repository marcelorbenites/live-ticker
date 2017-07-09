package com.marcelorbenites.liveticker.view.image;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module public class ImageModule {

  @Provides ImageLoader provideImageLoader(@Named("application") Context context) {
    return new PicassoImageLoader(context);
  }
}
