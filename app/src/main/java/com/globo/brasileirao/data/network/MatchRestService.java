package com.globo.brasileirao.data.network;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;

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
     * @param apiKey mongolab api key.
     * @return observable for matches stored in mongolab.
     */
    @GET("/api/1/databases/heroku_wm3w0h9v/collections/matches") Observable<List<Match>> getMatches(@Query("apiKey") String apiKey);

    /**
     * Get live ticker entries for specific match from mongolab using apiKey.
     * @param query used to filter the ticker entries. Refer to <a href="http://docs.mongolab.com/data-api/">http://docs.mongolab.com/data-api/</a>
     * @param skip number of entries to be skiped. Useful for pagination.
     * @param apiKey mongolab api key.
     * @return observable for live ticker entries stored in mongolab.
     */
    @GET("/api/1/databases/heroku_wm3w0h9v/collections/liveTickerEntries") Observable<List<LiveTickerEntry>> getLiveTickerEntries(@Query("q") String query, @Query("sk") int skip, @Query("apiKey") String apiKey);

}
