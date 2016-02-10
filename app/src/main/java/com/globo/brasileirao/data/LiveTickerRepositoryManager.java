package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.Match;

import rx.Observable;

public class LiveTickerRepositoryManager implements MatchLiveTickerRepository {

    private final Match match;
    private final MatchRepository matchRepository;

    public LiveTickerRepositoryManager(Match match, MatchRepository matchRepository) {
        this.match = match;
        this.matchRepository = matchRepository;
    }

    @Override public Observable<Void> refreshLiveTicker(int limit) {
        return matchRepository.refreshLiveTicker(match.getMatchId(), limit);
    }
}
