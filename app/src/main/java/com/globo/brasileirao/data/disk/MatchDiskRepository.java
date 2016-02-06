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

}
