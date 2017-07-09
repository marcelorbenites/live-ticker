package com.marcelorbenites.liveticker.data.network;

import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import com.marcelorbenites.liveticker.entities.Match;
import java.util.List;
import rx.Observable;

/**
 * Match rest repository that interacts with a rest service in order to access Brasileirao matches.
 */
public class MatchRestRepository implements MatchNetworkRepository {

  private final MatchRestService restService;
  private final String apiKey;

  /**
   * Create new match rest repository that access matches using provided rest service.
   *
   * @param restService restful service.
   * @param serviceKey restful service key.
   */
  public MatchRestRepository(MatchRestService restService, String serviceKey) {
    this.restService = restService;
    this.apiKey = serviceKey;
  }

  @Override public Observable<List<Match>> getMatches() {
    return restService.getMatches(apiKey);
  }

  @Override public Observable<Match> getMatch(int match) {
    return restService.getMatch("{\"matchId\":" + match + "}", apiKey);
  }

  @Override public Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId) {
    return restService.getLiveTickerEntries("{\"matchId\":" + matchId + "}", apiKey);
  }
}
