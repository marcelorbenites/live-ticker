package com.marcelorbenites.liveticker.data.network;

import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import com.marcelorbenites.liveticker.entities.Match;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Rest services for Brasileirão matches.
 */
public interface MatchRestService {

  /**
   * Get list of matches from mongolab using apiKey.
   *
   * @param apiKey mongolab api key.
   *
   * @return observable for matches stored in mongolab.
   */
  @GET("/api/1/databases/heroku_wm3w0h9v/collections/matches") Observable<List<Match>> getMatches(
      @Query("apiKey") String apiKey);

  /**
   * Get specific match from mongolab using apiKey.
   *
   * @param query used to filter the matches. Refer to <a href="http://docs.mongolab.com/data-api/">http://docs.mongolab.com/data-api/</a>
   * @param apiKey mongolab api key.
   *
   * @return observable for a match stored in mongolab.
   */
  @GET("/api/1/databases/heroku_wm3w0h9v/collections/matches?fo=true") Observable<Match> getMatch(
      @Query("q") String query, @Query("apiKey") String apiKey);

  /**
   * Get live ticker entries for specific match from mongolab using apiKey.
   *
   * @param query used to filter the ticker entries. Refer to <a href="http://docs.mongolab.com/data-api/">http://docs.mongolab.com/data-api/</a>
   * @param apiKey mongolab api key.
   *
   * @return observable for live ticker entries stored in mongolab.
   */
  @GET("/api/1/databases/heroku_wm3w0h9v/collections/liveTickerEntries")
  Observable<List<LiveTickerEntry>> getLiveTickerEntries(@Query("q") String query,
      @Query("apiKey") String apiKey);
}
