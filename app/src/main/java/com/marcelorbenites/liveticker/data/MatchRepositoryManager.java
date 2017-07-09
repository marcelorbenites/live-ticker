package com.marcelorbenites.liveticker.data;

import com.marcelorbenites.liveticker.data.disk.MatchDiskRepository;
import com.marcelorbenites.liveticker.data.network.MatchNetworkRepository;
import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import com.marcelorbenites.liveticker.entities.Match;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;

/**
 * Repository manager for matches grabbing them from network or disk.
 */
public class MatchRepositoryManager implements MatchRepository {

  private final MatchNetworkRepository networkRepository;
  private final MatchDiskRepository diskRepository;

  /**
   * Create new repository manager that will manage disk and network sources of matches.
   *
   * @param networkRepository network repository of matches.
   * @param diskRepository disk repository of matches.
   */
  public MatchRepositoryManager(MatchNetworkRepository networkRepository,
      MatchDiskRepository diskRepository) {
    this.networkRepository = networkRepository;
    this.diskRepository = diskRepository;
  }

  @Override public Observable<List<Match>> getMatches() {
    return diskRepository.getMatches();
  }

  @Override public Observable<Void> refreshMatches() {
    return getMatchesFromNetworkAndSaveToDisk().ignoreElements()
        .cast(Void.class);
  }

  @Override public Observable<Void> refreshMatch(final int matchId) {
    return getMatchFromNetworkAndSaveToDisk(matchId).ignoreElements()
        .cast(Void.class);
  }

  @Override public Observable<Void> refreshLiveTickerEntries(final int matchId) {
    return getLiveTickerEntriesFromNetworkAndSaveToDisk(matchId).ignoreElements()
        .cast(Void.class);
  }

  @Override public Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId) {
    return diskRepository.getLiveTickerEntries(matchId);
  }

  @Override public Observable<Match> getMatch(int matchId) {
    return diskRepository.getMatch(matchId);
  }

  private Observable<List<Match>> getMatchesFromNetworkAndSaveToDisk() {
    return networkRepository.getMatches()
        .doOnNext(new Action1<List<Match>>() {
          @Override public void call(List<Match> matches) {
            diskRepository.saveOrOverwriteMatches(matches);
          }
        });
  }

  private Observable<List<LiveTickerEntry>> getLiveTickerEntriesFromNetworkAndSaveToDisk(
      final int matchId) {
    return networkRepository.getLiveTickerEntries(matchId)
        .doOnNext(new Action1<List<LiveTickerEntry>>() {
          @Override public void call(List<LiveTickerEntry> liveTickerEntries) {
            diskRepository.saveOrOverwriteLiveTickerEntries(matchId, liveTickerEntries);
          }
        });
  }

  public Observable<Match> getMatchFromNetworkAndSaveToDisk(int matchId) {
    return networkRepository.getMatch(matchId)
        .doOnNext(new Action1<Match>() {
          @Override public void call(Match match) {
            diskRepository.saveOrOverwriteMatch(match);
          }
        });
  }
}