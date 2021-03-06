package com.marcelorbenites.liveticker.data.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class MatchRestRepositoryTest {

  private MatchRestRepository restRepository;
  private MatchRestService restServiceMock;

  @Before public void setUp() throws Exception {
    restServiceMock = mock(MatchRestService.class);
    restRepository = new MatchRestRepository(restServiceMock, "MyApiKey");
  }

  @Test public void getMatch() throws Exception {
    restRepository.getMatch(1);
    verify(restServiceMock).getMatch("{\"matchId\":1}", "MyApiKey");
  }

  @Test public void getMatches() throws Exception {
    restRepository.getMatches();
    verify(restServiceMock).getMatches("MyApiKey");
  }

  @Test public void getLiveTickerEntries() throws Exception {
    restRepository.getLiveTickerEntries(1);
    verify(restServiceMock).getLiveTickerEntries("{\"matchId\":1}", "MyApiKey");
  }
}