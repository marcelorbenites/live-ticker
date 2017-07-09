package com.marcelorbenites.liveticker.view.modules;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import com.marcelorbenites.liveticker.data.DataModule;
import com.marcelorbenites.liveticker.scheduler.GcmTaskFactory;
import com.marcelorbenites.liveticker.scheduler.MatchSyncScheduler;
import com.marcelorbenites.liveticker.scheduler.SyncSchedulerManager;
import com.marcelorbenites.liveticker.scheduler.SyncService;
import com.marcelorbenites.liveticker.view.adapter.MatchListAdapter;
import com.marcelorbenites.liveticker.view.image.ImageLoader;
import com.marcelorbenites.liveticker.view.image.ImageModule;
import com.marcelorbenites.liveticker.view.utils.UnitConverter;
import com.google.android.gms.gcm.GcmNetworkManager;
import dagger.Module;
import dagger.Provides;
import java.text.DateFormat;

@Module(includes = { ActivityModule.class, DataModule.class, ImageModule.class })
public class MatchListModule {

  private final int teamIconWidth;
  private final int teamIconHeight;
  private final int teamIconPlaceholder;

  public MatchListModule(int teamIconWidth, int teamIconHeight,
      @DrawableRes int teamIconPlaceholder) {
    this.teamIconWidth = teamIconWidth;
    this.teamIconHeight = teamIconHeight;
    this.teamIconPlaceholder = teamIconPlaceholder;
  }

  @Provides MatchListAdapter provideMatchListAdapter(LayoutInflater layoutInflater,
      ImageLoader imageLoader, UnitConverter unitConverter, DateFormat matchDateFormat) {
    return new MatchListAdapter(layoutInflater, imageLoader,
        unitConverter.dpToPixels(teamIconWidth), unitConverter.dpToPixels(teamIconHeight),
        teamIconPlaceholder, matchDateFormat);
  }

  @Provides MatchSyncScheduler provideMatchSyncScheduler(GcmNetworkManager gcmNetworkManager,
      GcmTaskFactory gcmTaskFactory) {
    return new SyncSchedulerManager(gcmNetworkManager, gcmTaskFactory, SyncService.class);
  }
}