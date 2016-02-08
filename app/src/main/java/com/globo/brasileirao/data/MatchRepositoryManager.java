package com.globo.brasileirao.data;

import com.globo.brasileirao.data.disk.MatchDiskRepository;
import com.globo.brasileirao.data.network.MatchNetworkRepository;
import com.globo.brasileirao.entities.Match;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Repository manager for matches grabbing them from network or disk.
 */
public class MatchRepositoryManager implements MatchRepository {

    private final MatchNetworkRepository networkRepository;
    private final MatchDiskRepository diskRepository;

    /**
     * Create new repository manager that will manage disk and network sources of matches.
     * @param networkRepository network repository of matches.
     * @param diskRepository disk repository of matches.
     */
    public MatchRepositoryManager(MatchNetworkRepository networkRepository, MatchDiskRepository diskRepository) {
        this.networkRepository = networkRepository;
        this.diskRepository = diskRepository;
    }

    /**
     * First go to network if error fallback to disk and emmit network error.
     * @return observable for available matches.
     */
    @Override public Observable<List<Match>> getMatches() {
        return getMatchesFormNetworkAndSaveToDisk()
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Match>>>() {
                    @Override public Observable<? extends List<Match>> call(Throwable throwable) {
                        return Observable.concat(diskRepository.getMatches(), Observable.<List<Match>>error(throwable));
                    }
                });
    }

    private Observable<List<Match>> getMatchesFormNetworkAndSaveToDisk() {
        return networkRepository.getMatches().doOnNext(new Action1<List<Match>>() {
            @Override public void call(List<Match> matches) {
                diskRepository.saveOrOverwriteMatches(matches);
            }
        });
    }
}