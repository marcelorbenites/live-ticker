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
     * @return observable for match available on network.
     */
    Observable<Match> getMatch(int match);

    /**
     * Get live ticker entries for a specific match available on network.
     * @param matchId ticker entries match id.
     * @param skip number of live ticker entries to skip.
     * @return observable for live ticker entries available on network.
     */
    Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId, int skip);
}
