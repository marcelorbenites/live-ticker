package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.entities.MatchLiveTicker;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class MatchLiveTickerRepositoryManager implements MatchLiveTickerRepository {

    private final Match match;
    private final MatchRepository matchRepository;

    public MatchLiveTickerRepositoryManager(Match match, MatchRepository matchRepository) {
        this.match = match;
        this.matchRepository = matchRepository;
    }

    @Override public Observable<Void> refreshMatchLiveTicker() {
        return Observable.concat(matchRepository.refreshMatch(match.getMatchId()),
                matchRepository.refreshLiveTickerEntries(match.getMatchId()));
    }

    @Override public Observable<MatchLiveTicker> getMatchLiveTicker() {
        return Observable.combineLatest(matchRepository.getMatch(match.getMatchId()),
                matchRepository.getLiveTickerEntries(match.getMatchId()), new Func2<Match, List<LiveTickerEntry>, MatchLiveTicker>() {
                    @Override public MatchLiveTicker call(Match match, List<LiveTickerEntry> liveTickerEntries) {
                        return new MatchLiveTicker(match, liveTickerEntries);
                    }
                });
    }
}
