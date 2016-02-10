package com.globo.brasileirao.view.modules;

import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.data.LiveTickerRepositoryManager;
import com.globo.brasileirao.data.MatchLiveTickerRepository;
import com.globo.brasileirao.data.MatchRepository;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.view.image.ImageModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityModule.class, ImageModule.class, DataModule.class})
public class LiveTickerModule {

    private final Match match;

    public LiveTickerModule(Match match) {
        this.match = match;
    }

    @Provides MatchLiveTickerRepository provideMatchLiveTickerRepository(MatchRepository matchRepository) {
        return new LiveTickerRepositoryManager(match, matchRepository);
    }
}