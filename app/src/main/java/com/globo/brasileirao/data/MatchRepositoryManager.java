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
        return diskRepository.getMatches();
    }

    @Override public Observable<Match> getMatch(int matchId) {
        return diskRepository.getMatch(matchId);
    }

    @Override public Observable<Void> refreshMatch(final int matchId) {
        return diskRepository.getMatch(matchId)
                .first()
                .flatMap(new Func1<Match, Observable<Match>>() {
                    @Override public Observable<Match> call(Match match) {
                        return getMatchFromNetworkAndSaveToDisk(match.getMatchId());
                    }
                })
                .ignoreElements()
                .cast(Void.class);
    }

    @Override public Observable<Void> refreshMatches() {
        return diskRepository.getMatches()
                .first()
                .flatMap(new Func1<List<Match>, Observable<List<Match>>>() {
                    @Override public Observable<List<Match>> call(List<Match> matches) {
                        return getMatchesFromNetworkAndSaveToDisk();

                    }
                })
                .ignoreElements()
                .cast(Void.class);
    }

    @Override public Observable<Void> refreshLiveTickerEntries(final int matchId) {
        return diskRepository.getLiveTickerEntries(matchId)
                .first()
                .flatMap(new Func1<List<LiveTickerEntry>, Observable<List<LiveTickerEntry>>>() {
                    @Override public Observable<List<LiveTickerEntry>> call(List<LiveTickerEntry> liveTickerEntries) {
                        // Skip live ticker entries that are already stored in the database
                        return getLiveTickerEntriesFromNetworkAndSaveToDisk(matchId, liveTickerEntries.size());

                    }
                })
                .ignoreElements()
                .cast(Void.class);

    }

    @Override public Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId) {
        return diskRepository.getLiveTickerEntries(matchId);
    }

    private Observable<List<Match>> getMatchesFromNetworkAndSaveToDisk() {
        return networkRepository.getMatches().doOnNext(new Action1<List<Match>>() {
            @Override public void call(List<Match> matches) {
                diskRepository.saveOrOverwriteMatches(matches);
            }
        });
    }

    private Observable<List<LiveTickerEntry>> getLiveTickerEntriesFromNetworkAndSaveToDisk(final int matchId, int skip) {
        return networkRepository.getLiveTickerEntries(matchId, skip).doOnNext(new Action1<List<LiveTickerEntry>>() {
            @Override public void call(List<LiveTickerEntry> liveTickerEntries) {
                diskRepository.saveOrOverwriteLiveTickerEntries(matchId, liveTickerEntries);
            }
        });
    }

    public Observable<Match> getMatchFromNetworkAndSaveToDisk(int matchId) {
        return networkRepository.getMatch(matchId).doOnNext(new Action1<Match>() {
            @Override public void call(Match match) {
                diskRepository.saveOrOverwriteMatch(match);
            }
        });
    }
}