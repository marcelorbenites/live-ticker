package com.globo.brasileirao.view.modules;

import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.data.MatchLiveTickerRepositoryManager;
import com.globo.brasileirao.data.MatchLiveTickerRepository;
import com.globo.brasileirao.data.MatchRepository;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.view.image.ImageModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityModule.class, ImageModule.class, DataModule.class})
public class LiveTickerModule {


}