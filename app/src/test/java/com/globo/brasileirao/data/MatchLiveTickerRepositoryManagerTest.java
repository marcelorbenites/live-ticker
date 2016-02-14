package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.entities.MatchLiveTicker;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchLiveTickerRepositoryManagerTest {

    private MatchRepository matchRepositoryMock;
    private MatchLiveTickerRepositoryManager repository;

    @Before public void setUp() throws Exception {
        matchRepositoryMock = mock(MatchRepository.class);
        repository = new MatchLiveTickerRepositoryManager(matchRepositoryMock);
    }

    @Test public void refreshLiveTicker() throws Exception {
        when(matchRepositoryMock.refreshMatch(34)).thenReturn(Observable.<Void>empty());
        when(matchRepositoryMock.refreshLiveTickerEntries(34)).thenReturn(Observable.<Void>empty());

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshMatchLiveTicker(34).subscribe(testSubscriber);
        verify(matchRepositoryMock).refreshMatch(34);
        verify(matchRepositoryMock).refreshLiveTickerEntries(34);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
        testSubscriber.assertCompleted();
    }

    @Test public void getMatchLiveTicker() throws Exception {

        Match match = new Match(5, null, null, 5, 0, new Date(), "");
        when(matchRepositoryMock.getMatch(5)).thenReturn(Observable.just(match));
        List<LiveTickerEntry> liveTickerEntries = Arrays.asList(
                new LiveTickerEntry(5, 5, ""),
                new LiveTickerEntry(5, 15, ""),
                new LiveTickerEntry(5, 30, "")
        );
        when(matchRepositoryMock.getLiveTickerEntries(5)).thenReturn(Observable.just(liveTickerEntries));

        TestSubscriber<MatchLiveTicker> testSubscriber = new TestSubscriber<>();
        repository.getMatchLiveTicker(5).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(new MatchLiveTicker(match, liveTickerEntries));
    }
}