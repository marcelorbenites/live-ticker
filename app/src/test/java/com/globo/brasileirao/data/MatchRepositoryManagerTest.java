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
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
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

    @Test public void getMatchesNetworkErrorEmptyDisk() throws Exception {

        Observable<List<Match>> networkErrorObservable = Observable.error(new IOException());
        when(networkRepositoryMock.getMatches()).thenReturn(networkErrorObservable);

        Observable<List<Match>> diskEmptyObservable = Observable.empty();
        when(diskRepositoryMock.getMatches()).thenReturn(diskEmptyObservable);

        TestSubscriber<List<Match>> testSubscriber = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }

    @Test public void getMatchesNetworkSuccess() throws Exception {

        List<Match> resultMatches = Arrays.asList(getSimpleMatch(1), getSimpleMatch(2));
        Observable<List<Match>> networkObservable = Observable.just(resultMatches);
        when(networkRepositoryMock.getMatches()).thenReturn(networkObservable);

        TestSubscriber<List<Match>> testSubscriber = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber);
        testSubscriber.assertValue(resultMatches);
        testSubscriber.assertCompleted();
        verify(diskRepositoryMock).saveOrOverwriteMatches(resultMatches);
    }

    @Test public void getMatchesNetworkErrorFullDisk() throws Exception {

        Observable<List<Match>> networkErrorObservable = Observable.error(new IOException());
        when(networkRepositoryMock.getMatches()).thenReturn(networkErrorObservable);

        List<Match> resultMatches = Arrays.asList(getSimpleMatch(1), getSimpleMatch(2));
        Observable<List<Match>> diskFullObservable = Observable.just(resultMatches);
        when(diskRepositoryMock.getMatches()).thenReturn(diskFullObservable);

        TestSubscriber<List<Match>> testSubscriber = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber);
        testSubscriber.assertValue(resultMatches);
        testSubscriber.assertError(IOException.class);
        testSubscriber.assertUnsubscribed();
    }

    @Test public void refreshLiveTickerNetworkError() throws Exception {
        Observable<List<LiveTickerEntry>> networkErrorObservable = Observable.error(new IOException());
        when(networkRepositoryMock.getLiveTickerEntries(1, 0, 10)).thenReturn(networkErrorObservable);
        when(diskRepositoryMock.getLiveTickerEntries(1)).thenReturn(Observable.just(Collections.<LiveTickerEntry>emptyList()));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshLiveTicker(1, 10).subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }

    @Test public void refreshLiveTickerSkip0() throws Exception {
        List<LiveTickerEntry> resultList = Arrays.asList(
                new LiveTickerEntry(1, 23, "test 1"),
                new LiveTickerEntry(1, 45, "test 2")
        );
        ;
        when(networkRepositoryMock.getLiveTickerEntries(1, 0, 10))
                .thenReturn(Observable.just(resultList));
        when(diskRepositoryMock.getLiveTickerEntries(1))
                .thenReturn(Observable.just(Collections.<LiveTickerEntry>emptyList()));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshLiveTicker(1, 10).subscribe(testSubscriber);
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
        when(networkRepositoryMock.getLiveTickerEntries(1, 1, 10))
                .thenReturn(Observable.just(resultList));
        when(diskRepositoryMock.getLiveTickerEntries(1))
                .thenReturn(Observable.just(Collections.singletonList(new LiveTickerEntry(1, 23, "test 0"))));

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        repository.refreshLiveTicker(1, 10).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
        verify(networkRepositoryMock).getLiveTickerEntries(1, 1, 10);
        verify(diskRepositoryMock).saveOrOverwriteLiveTickerEntries(1, resultList);
    }

    private Match getSimpleMatch(int id) {
        return new Match(id, null, null, 0, 0, null, null);
    }
}