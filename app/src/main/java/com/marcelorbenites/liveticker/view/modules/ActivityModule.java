package com.marcelorbenites.liveticker.view.modules;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import com.marcelorbenites.liveticker.exceptions.NetworkErrorToStringResourceConverter;
import com.marcelorbenites.liveticker.exceptions.ThrowableToStringResourceConverter;
import dagger.Module;
import dagger.Provides;
import java.text.DateFormat;

@Module public class ActivityModule {

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