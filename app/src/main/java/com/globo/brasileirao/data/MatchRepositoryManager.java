package com.globo.brasileirao.data;

import com.globo.brasileirao.data.disk.MatchDiskRepository;
import com.globo.brasileirao.data.network.MatchNetworkRepository;
import com.globo.brasileirao.entities.LiveTickerEntry;
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

    @Override public Observable<List<Match>> getMatches() {
        return getMatchesFromNetworkAndSaveToDisk()
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Match>>>() {
                    @Override public Observable<? extends List<Match>> call(Throwable throwable) {
                        return Observable.concat(diskRepository.getMatches(), Observable.<List<Match>>error(throwable));
                    }
                });
    }

    @Override public Observable<Void> refreshLiveTicker(final int matchId, final int limit) {
        return diskRepository.getLiveTickerEntries(matchId)
                .first()
                .flatMap(new Func1<List<LiveTickerEntry>, Observable<List<LiveTickerEntry>>>() {
                    @Override public Observable<List<LiveTickerEntry>> call(List<LiveTickerEntry> liveTickerEntries) {
                        return getLiveTickerEntriesFromNetworkAndSaveToDisk(matchId, liveTickerEntries.size(), limit);

                    }
                })
                .ignoreElements()
                .cast(Void.class);

    }

    private Observable<List<Match>> getMatchesFromNetworkAndSaveToDisk() {
        return networkRepository.getMatches().doOnNext(new Action1<List<Match>>() {
            @Override public void call(List<Match> matches) {
                diskRepository.saveOrOverwriteMatches(matches);
            }
        });
    }

    private Observable<List<LiveTickerEntry>> getLiveTickerEntriesFromNetworkAndSaveToDisk(final int matchId, int skip, int limit) {
        return networkRepository.getLiveTickerEntries(matchId, skip, limit).doOnNext(new Action1<List<LiveTickerEntry>>() {
            @Override public void call(List<LiveTickerEntry> liveTickerEntries) {
                diskRepository.saveOrOverwriteLiveTickerEntries(matchId, liveTickerEntries);
            }
        });
    }
}