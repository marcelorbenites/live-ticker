package com.marcelorbenites.liveticker.data;

import com.marcelorbenites.liveticker.BuildConfig;
import com.marcelorbenites.liveticker.data.disk.MatchDiskRepository;
import com.marcelorbenites.liveticker.data.disk.MatchSQLiteRepository;
import com.marcelorbenites.liveticker.data.network.MatchNetworkRepository;
import com.marcelorbenites.liveticker.data.network.MatchRestRepository;
import com.marcelorbenites.liveticker.data.network.MatchRestService;
import com.squareup.sqlbrite.BriteDatabase;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module public class DataModule {

  @Provides MatchRepository provideMatchRepository(MatchNetworkRepository networkRepository,
      MatchDiskRepository diskRepository) {
    return new MatchRepositoryManager(networkRepository, diskRepository);
  }

  @Provides MatchNetworkRepository provideMatchNetworkRepository(Retrofit retrofit) {
    return new MatchRestRepository(retrofit.create(MatchRestService.class), BuildConfig.API_KEY);
  }

  @Provides MatchDiskRepository provideMatchDiskRepository(BriteDatabase database) {
    return new MatchSQLiteRepository(database);
  }

  @Provides MatchLiveTickerRepository provideMatchLiveTickerRepository(
      MatchRepository matchRepository) {
    return new MatchLiveTickerRepositoryManager(matchRepository);
  }
}