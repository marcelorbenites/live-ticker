package com.globo.brasileirao.scheduler;

import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.data.MatchRepository;

import dagger.Module;
import dagger.Provides;

@Module(includes = DataModule.class)
public class SyncServiceModule {

    @Provides GcmTaskTagExecutor provideGcmTaskTagExecutor(MatchRepository repository) {
        return new GcmTaskTagExecutor(repository);
    }

}