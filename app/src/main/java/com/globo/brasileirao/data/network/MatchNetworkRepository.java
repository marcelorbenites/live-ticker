package com.globo.brasileirao.data.network;

import com.globo.brasileirao.entities.Match;

import java.util.List;

import rx.Observable;

/**
 * Repository of matches stored on network.
 */
public interface MatchNetworkRepository {

    /**
     * @return observable for matches available on network.
     */
    Observable<List<Match>> getMatches();
}
