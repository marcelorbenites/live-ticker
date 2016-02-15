package com.globo.brasileirao.scheduler;

import android.os.Bundle;

import com.globo.brasileirao.data.MatchLiveTickerRepository;
import com.globo.brasileirao.data.MatchRepository;
import com.google.android.gms.gcm.GcmNetworkManager;

import javax.inject.Inject;

import rx.Observable;

public class GcmTaskTagExecutor {

    private final MatchRepository matchRepository;
    private final MatchLiveTickerRepository matchLiveTickerRepository;

    @Inject public GcmTaskTagExecutor(MatchRepository matchRepository, MatchLiveTickerRepository matchLiveTickerRepository) {
        this.matchRepository = matchRepository;
        this.matchLiveTickerRepository = matchLiveTickerRepository;
    }

    public int execute(String tag, Bundle bundle) {
        switch (tag) {
            case SyncSchedulerManager.TAG_SYNC_MATCHES:
                return matchRepository.refreshMatches()
                        .cast(Integer.class)
                        .onErrorResumeNext(Observable.just(GcmNetworkManager.RESULT_FAILURE))
                        .toBlocking()
                        .firstOrDefault(GcmNetworkManager.RESULT_SUCCESS);
            case SyncSchedulerManager.TAG_SYNC_MATCH_LIVE_TICKER:
                return matchLiveTickerRepository.refreshMatchLiveTicker(bundle.getInt(SyncSchedulerManager.EXTRA_MATCH_ID))
                        .cast(Integer.class)
                        .onErrorResumeNext(Observable.just(GcmNetworkManager.RESULT_FAILURE))
                        .toBlocking()
                        .firstOrDefault(GcmNetworkManager.RESULT_SUCCESS);
            default:
                return GcmNetworkManager.RESULT_FAILURE;
        }
    }
}
