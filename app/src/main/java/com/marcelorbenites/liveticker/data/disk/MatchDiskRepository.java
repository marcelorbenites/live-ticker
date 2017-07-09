package com.marcelorbenites.liveticker.data.disk;

import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import com.marcelorbenites.liveticker.entities.Match;
import java.util.List;
import rx.Observable;

/**
 * Repository of matches stored on disk.
 */
public interface MatchDiskRepository {

  /**
   * @param matchId match identification
   * Note that this observable will not complete, any changes to underlying data will
   * cause the observable to emmit updated items.
   *
   * @return observable that return a match stored in repository.
   */
  Observable<Match> getMatch(int matchId);

  /**
   * @return observable that return all matches stored in repository.
   * Note that this observable will not complete, any changes to underlying data will
   * cause the observable to emmit updated items.
   */
  Observable<List<Match>> getMatches();

  /**
   * Save match to repository overwriting it if it already exist.
   *
   * @param match the match to save.
   */
  void saveOrOverwriteMatch(Match match);

  /**
   * Save matches to repository overwriting them if they already exist.
   *
   * @param matches to be saved in repository.
   */
  void saveOrOverwriteMatches(List<Match> matches);

  /**
   * Clear all matches in repository.
   */
  void clearMatches();

  /**
   * Save live ticker entries for match to repository overwriting them if they already exist.
   *
   * @param matchId the match to save the live ticker entries for.
   * @param liveTickerEntries to be saved in repository.
   */
  void saveOrOverwriteLiveTickerEntries(int matchId, List<LiveTickerEntry> liveTickerEntries);

  /**
   * @param matchId the match to return the live ticker entries for.
   *
   * @return observable that return all live ticker entries stored in repository for a particular
   * match.
   * Note that this observable will not complete, any changes to underlying data will
   * cause the observable to emmit updated items.
   */
  Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId);

  /**
   * Clear all live ticker entries for a match in repository.
   *
   * @param matchId the match to clear the live ticker entries for.
   */
  void clearLiveTickerEntries(int matchId);
}