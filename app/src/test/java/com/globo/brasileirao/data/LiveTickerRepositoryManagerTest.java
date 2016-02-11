package com.globo.brasileirao.data;

import com.globo.brasileirao.entities.Match;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LiveTickerRepositoryManagerTest {

    private Match matchMock;
    private MatchRepository matchRepositoryMock;
    private LiveTickerRepositoryManager repository;

    @Before public void setUp() throws Exception {
        matchMock = mock(Match.class);
        matchRepositoryMock = mock(MatchRepository.class);
        repository = new LiveTickerRepositoryManager(matchMock, matchRepositoryMock);
    }

    @Test public void refreshLiveTicker() throws Exception {
        when(matchMock.getMatchId()).thenReturn(34);
        repository.refreshLiveTicker(10);
        verify(matchRepositoryMock).refreshLiveTicker(34, 10);
    }

    @Test public void getLiveTickerEntries() throws Exception {
        when(matchMock.getMatchId()).thenReturn(20);
        repository.getLiveTickerEntries();
        verify(matchRepositoryMock).getLiveTickerEntries(20);
    }
}