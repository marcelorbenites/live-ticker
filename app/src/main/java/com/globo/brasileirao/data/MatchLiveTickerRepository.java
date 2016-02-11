package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.LiveTickerEntry;

import java.util.List;

import rx.Observable;

/**
 * Repository for Brasileirão {@link com.globo.brasileirao.entities.LiveTickerEntry}.
 */
public interface MatchLiveTickerRepository {

    /**
     * Refresh repository with latest live ticker entries for a specific match.
     * @param limit number of latest live ticker items to be refreshed.
     * @return observable that emits no items, only completes successfully if refresh succeeds.
     */
    Observable<Void> refreshLiveTicker(int limit);

    /**
     * @return observable for live ticker entries stored in repository.
     */
    Observable<List<LiveTickerEntry>> getLiveTickerEntries();

}