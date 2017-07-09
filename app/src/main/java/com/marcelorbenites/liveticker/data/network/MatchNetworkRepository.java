package com.marcelorbenites.liveticker.data.network;

import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import com.marcelorbenites.liveticker.entities.Match;
import java.util.List;
import rx.Observable;

/**
 * Repository of matches stored on network.
 */
public interface MatchNetworkRepository {

  /**
   * @return observable for matches available on network.
   */
  Observable<List<Match>> getMatches();

  /**
   * @return observable for match available on network.
   */
  Observable<Match> getMatch(int match);

  /**
   * Get live ticker entries for a specific match available on network.
   *
   * @param matchId ticker entries match id.
   *
   * @return observable for live ticker entries available on network.
   */
  Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId);
}
