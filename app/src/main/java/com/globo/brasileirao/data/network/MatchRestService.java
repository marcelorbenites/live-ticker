package com.globo.brasileirao.data.network;

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

}
