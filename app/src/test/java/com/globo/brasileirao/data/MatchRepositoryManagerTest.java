package com.globo.brasileirao.data;

import com.globo.brasileirao.data.disk.MatchDiskRepository;
import com.globo.brasileirao.data.network.MatchNetworkRepository;
import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MatchRepositoryManagerTest {

    private MatchNetworkRepository networkRepositoryMock;
    private MatchDiskRepository diskRepositoryMock;
    private MatchRepositoryManager repository;

    @Before public void setUp() throws Exception {
        networkRepositoryMock = mock(MatchNetworkRepository.class);
        diskRepositoryMock = mock(MatchDiskRepository.class);
        repository = new MatchRepositoryManager(networkRepositoryMock, diskRepositoryMock);
    }

    @Test public void getMatches() throws Exception {
        repository.getMatches();
        verify(diskRepositoryMock).getMatches();
    }

    @Test public void getMatch() throws Exception {
        repository.getMatch(1);
        verify(diskRepositoryMock).getMatch(1);
    }

    @Test public void refreshMatchesNetworkError() throws Exception {
        Observable<List<Match>> networkErrorObservable = Observable.error(new IOException());
        when(networkRepositoryMock.getMatches()).thenReturn(networkErrorObservable);
        when(diskRepositoryMock.getMatches()).thenReturn(Observable.just(Collections.<Match>emptyList()));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshMatches().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }

    @Test public void refreshMatchNetworkError() throws Exception {
        Observable<Match> networkErrorObservable = Observable.error(new IOException());
        when(networkRepositoryMock.getMatch(5)).thenReturn(networkErrorObservable);
        Match resultMatch = new Match(5, null, null, 4, 5, new Date(), "");
        when(diskRepositoryMock.getMatch(5)).thenReturn(Observable.just(resultMatch));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshMatch(5).subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }

    @Test public void getMatchesNetworkSuccess() throws Exception {
        List<Match> resultList = Arrays.asList(getSimpleMatch(1), getSimpleMatch(2));
        when(networkRepositoryMock.getMatches())
                .thenReturn(Observable.just(resultList));
        when(diskRepositoryMock.getMatches())
                .thenReturn(Observable.just(Collections.<Match>emptyList()));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshMatches().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
        verify(diskRepositoryMock).saveOrOverwriteMatches(resultList);
    }

    @Test public void refreshLiveTickerNetworkError() throws Exception {
        Observable<List<LiveTickerEntry>> networkErrorObservable = Observable.error(new IOException());
        when(networkRepositoryMock.getLiveTickerEntries(1)).thenReturn(networkErrorObservable);
        when(diskRepositoryMock.getLiveTickerEntries(1)).thenReturn(Observable.just(Collections.<LiveTickerEntry>emptyList()));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshLiveTickerEntries(1).subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }

    @Test public void refreshLiveTickerSkip0() throws Exception {
        List<LiveTickerEntry> resultList = Arrays.asList(
                new LiveTickerEntry(1, 23, "test 1"),
                new LiveTickerEntry(1, 45, "test 2")
        );

        when(networkRepositoryMock.getLiveTickerEntries(1))
                .thenReturn(Observable.just(resultList));
        when(diskRepositoryMock.getLiveTickerEntries(1))
                .thenReturn(Observable.just(Collections.<LiveTickerEntry>emptyList()));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshLiveTickerEntries(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
        verify(diskRepositoryMock).saveOrOverwriteLiveTickerEntries(1, resultList);
    }

    @Test public void refreshLiveTickerSkip1() throws Exception {
        List<LiveTickerEntry> resultList = Arrays.asList(
                new LiveTickerEntry(1, 23, "test 1"),
                new LiveTickerEntry(1, 45, "test 2")
        );
        when(networkRepositoryMock.getLiveTickerEntries(1))
                .thenReturn(Observable.just(resultList));
        when(diskRepositoryMock.getLiveTickerEntries(1))
                .thenReturn(Observable.just(Collections.singletonList(new LiveTickerEntry(1, 23, "test 0"))));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshLiveTickerEntries(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
        verify(networkRepositoryMock).getLiveTickerEntries(1);
        verify(diskRepositoryMock).saveOrOverwriteLiveTickerEntries(1, resultList);
    }

    @Test public void getLiveTickerEntries() throws Exception {
        Observable<List<LiveTickerEntry>> emptyObservable = Observable.empty();
        when(diskRepositoryMock.getLiveTickerEntries(1)).thenReturn(emptyObservable);
        assertEquals(emptyObservable, repository.getLiveTickerEntries(1));
    }

    private Match getSimpleMatch(int id) {
        return new Match(id, null, null, 0, 0, null, null);
    }
}