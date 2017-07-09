package com.marcelorbenites.liveticker.data;

import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import com.marcelorbenites.liveticker.entities.Match;
import java.util.List;
import rx.Observable;

/**
 * Repository for Brasileirão {@link Match}es.
 */
public interface MatchRepository {

  /**
   * @return observable that return all matches stored in repository.
   * Note that this observable will not complete, any changes to underlying data will
   * cause the observable to emmit updated items.
   */
  Observable<List<Match>> getMatches();

  /**
   * Refresh repository with latest matches.
   *
   * @return observable that emits no items, only completes successfully if refresh succeeds.
   */
  Observable<Void> refreshMatches();

  /**
   * Refresh specific match in repository.
   *
   * @return observable that emits no items, only completes successfully if refresh succeeds.
   */
  Observable<Void> refreshMatch(int matchId);

  /**
   * Refresh repository with latest live ticker entries for a specific match.
   *
   * @param matchId ticker entries match id.
   *
   * @return observable that emits no items, only completes successfully if refresh succeeds.
   */
  Observable<Void> refreshLiveTickerEntries(int matchId);

  /**
   * @return observable for live ticker entries stored in repository.
   * Note that this observable will not complete, any changes to underlying data will
   * cause the observable to emmit updated items.
   */
  Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId);

  /**
   * @return observable for a match stored in repository.
   * Note that this observable will not complete, any changes to underlying data will
   * cause the observable to emmit updated items.
   */
  Observable<Match> getMatch(int matchId);
}