package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;

import java.util.List;

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

    @Override public Observable<List<LiveTickerEntry>> getLiveTickerEntries() {
        return matchRepository.getLiveTickerEntries(match.getMatchId());
    }
}
