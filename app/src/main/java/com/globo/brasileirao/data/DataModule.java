package com.globo.brasileirao.data;

import com.globo.brasileirao.BuildConfig;
import com.globo.brasileirao.data.disk.MatchDiskRepository;
import com.globo.brasileirao.data.disk.MatchSQLiteRepository;
import com.globo.brasileirao.data.network.MatchNetworkRepository;
import com.globo.brasileirao.data.network.MatchRestRepository;
import com.globo.brasileirao.data.network.MatchRestService;
import com.squareup.sqlbrite.BriteDatabase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DataModule {

    @Provides MatchRepository provideMatchRepository(MatchNetworkRepository networkRepository, MatchDiskRepository diskRepository) {
        return new MatchRepositoryManager(networkRepository, diskRepository);
    }

    @Provides MatchNetworkRepository provideMatchNetworkRepository(Retrofit retrofit) {
        return new MatchRestRepository(retrofit.create(MatchRestService.class), BuildConfig.API_KEY);
    }

    @Provides MatchDiskRepository provideMatchDiskRepository(BriteDatabase database) {
        return new MatchSQLiteRepository(database);
    }
}