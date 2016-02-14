package com.globo.brasileirao.scheduler;

import com.globo.brasileirao.data.MatchRepository;
import com.google.android.gms.gcm.GcmNetworkManager;

import javax.inject.Inject;

import rx.Observable;

public class GcmTaskTagExecutor {

    private final MatchRepository matchRepository;

    @Inject public GcmTaskTagExecutor(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public int execute(String tag) {
        switch (tag) {
            case SyncSchedulerManager.TAG_SYNC_MATCHES:
                return matchRepository.refreshMatches()
                        .cast(Integer.class)
                        .onErrorResumeNext(Observable.just(GcmNetworkManager.RESULT_FAILURE))
                        .toBlocking()
                        .firstOrDefault(GcmNetworkManager.RESULT_SUCCESS);
            default:
                return GcmNetworkManager.RESULT_FAILURE;
        }
    }
}
