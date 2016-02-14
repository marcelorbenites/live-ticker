package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.MatchLiveTicker;

import java.util.List;

import rx.Observable;

/**
 * Repository for Brasileirão {@link com.globo.brasileirao.entities.LiveTickerEntry}.
 */
public interface MatchLiveTickerRepository {

    /**
     * Refresh repository with latest live ticker for a specific match.
     * @param matchId match identification.
     * @return observable that emits no items, only completes successfully if refresh succeeds.
     */
    Observable<Void> refreshMatchLiveTicker(int matchId);

    /**
     * @param matchId match identification.
     * @return observable for a match live ticker stored in repository.
     * Note that this observable will not complete, any changes to underlying data will
     * cause the observable to emmit updated items.
     */
    Observable<MatchLiveTicker> getMatchLiveTicker(int matchId);

}