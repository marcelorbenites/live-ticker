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

}