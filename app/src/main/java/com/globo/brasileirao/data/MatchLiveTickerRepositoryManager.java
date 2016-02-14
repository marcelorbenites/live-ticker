package com.globo.brasileirao.data;

import android.support.annotation.NonNull;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.entities.MatchLiveTicker;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class MatchLiveTickerRepositoryManager implements MatchLiveTickerRepository {

    private final MatchRepository matchRepository;

    public MatchLiveTickerRepositoryManager(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override public Observable<Void> refreshMatchLiveTicker(int matchId) {
        return Observable.concat(matchRepository.refreshMatch(matchId),
                matchRepository.refreshLiveTickerEntries(matchId));
    }

    @Override public Observable<MatchLiveTicker> getMatchLiveTicker(int matchId) {
        return Observable.combineLatest(matchRepository.getMatch(matchId),
                matchRepository.getLiveTickerEntries(matchId), createMatchLiveTicker());
    }

    @NonNull private Func2<Match, List<LiveTickerEntry>, MatchLiveTicker> createMatchLiveTicker() {
        return new Func2<Match, List<LiveTickerEntry>, MatchLiveTicker>() {
            @Override public MatchLiveTicker call(Match match, List<LiveTickerEntry> liveTickerEntries) {
                return new MatchLiveTicker(match, liveTickerEntries);
            }
        };
    }
}