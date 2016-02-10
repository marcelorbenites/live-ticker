package com.globo.brasileirao.data.network;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Repository of matches stored on network.
 */
public interface MatchNetworkRepository {

    /**
     * @return observable for matches available on network.
     */
    Observable<List<Match>> getMatches();

    /**
     * Get live ticker entries for a specific match.
     * @param matchId ticker entries match id.
     * @param skip number of live ticker entries to skip.
     * @param limit max number of live ticker entries to return.
     * @return observable for live ticker entries available on network.
     */
    Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId, int skip, int limit);
}
