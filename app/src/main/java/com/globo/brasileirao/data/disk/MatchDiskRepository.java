package com.globo.brasileirao.data.disk;

import com.globo.brasileirao.entities.Match;

import java.util.List;

import rx.Observable;

/**
 * Repository of matches stored on disk.
 */
public interface MatchDiskRepository {

    /**
     * @return observable that return all matches stored in repository and completes. If no match is found
     * observable completes without emitting items.
     */
    Observable<List<Match>> getMatches();

    /**
     * Save matches to repository overwriting them if they already exist.
     * @param matches to be saved in repository.
     */
    void saveOrOverwriteMatches(List<Match> matches);

    /**
     * Clear all matches in repository.
     */
    void clearMatches();

}