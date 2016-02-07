package com.globo.brasileirao.data.disk;

import com.globo.brasileirao.entities.Match;

import java.util.List;

import rx.Observable;

/**
 * Repository of matches stored on disk.
 */
public interface MatchDiskRepository {

    /**
     * @return observable for matches available on disk.
     */
    Observable<List<Match>> getMatches();

    /**
     * Save matches to repository.
     * @param matches to be saved in repository.
     */
    void saveMatches(List<Match> matches);

    /**
     * Clear all matches in repository.
     */
    void clearMatches();

}