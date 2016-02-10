package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.Match;

import java.util.List;

import rx.Observable;

/**
 * Repository for Brasileirão {@link Match}es.
 */
public interface MatchRepository {

    /**
     * @return observable for matches stored in repository.
     */
    Observable<List<Match>> getMatches();

    /**
     * Refresh repository with latest live ticker entries for a specific match.
     * @param matchId ticker entries match id.
     * @return observable that emits no items, only completes successfully if refresh succeeds.
     */
    Observable<Void> refreshLiveTicker(int matchId, int limit);
}