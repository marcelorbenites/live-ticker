package com.marcelorbenites.liveticker.scheduler;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;
import com.marcelorbenites.liveticker.data.MatchLiveTickerRepository;
import com.marcelorbenites.liveticker.data.MatchRepository;
import com.google.android.gms.gcm.GcmNetworkManager;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class) public class GcmTaskTagExecutorTest {

  private MatchRepository matchRepositoryMock;
  private GcmTaskTagExecutor executor;
  private MatchLiveTickerRepository matchLiveTickerRepositoryMock;

  @Before public void setUp() throws Exception {
    matchRepositoryMock = mock(MatchRepository.class);
    matchLiveTickerRepositoryMock = mock(MatchLiveTickerRepository.class);
    executor = new GcmTaskTagExecutor(matchRepositoryMock, matchLiveTickerRepositoryMock);
  }

  @Test public void executeMatchesRefresh() throws Exception {
    when(matchRepositoryMock.refreshMatches()).thenReturn(Observable.<Void>empty());
    assertEquals(GcmNetworkManager.RESULT_SUCCESS,
        executor.execute("com.globo.sync.tag.SYNC_MATCHES", null));
    when(matchRepositoryMock.refreshMatches()).thenReturn(
        Observable.<Void>error(new IOException()));
    assertEquals(GcmNetworkManager.RESULT_FAILURE,
        executor.execute("com.globo.sync.tag.SYNC_MATCHES", null));
  }

  @Test public void executeMatchLiveTickerRefresh() throws Exception {
    final Bundle bundle = new Bundle();
    bundle.putInt("com.globo.sync.extra.EXTRA_MATCH_ID", 1);
    when(matchLiveTickerRepositoryMock.refreshMatchLiveTicker(1)).thenReturn(
        Observable.<Void>empty());
    assertEquals(GcmNetworkManager.RESULT_SUCCESS,
        executor.execute("com.globo.sync.tag.SYNC_MATCH_LIVE_TICKER", bundle));
    when(matchLiveTickerRepositoryMock.refreshMatchLiveTicker(1)).thenReturn(
        Observable.<Void>error(new IOException()));
    assertEquals(GcmNetworkManager.RESULT_FAILURE,
        executor.execute("com.globo.sync.tag.SYNC_MATCH_LIVE_TICKER", bundle));
  }
}