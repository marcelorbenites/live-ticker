package com.globo.brasileirao.scheduler;

import com.globo.brasileirao.data.MatchRepository;
import com.globo.brasileirao.entities.Match;
import com.google.android.gms.gcm.GcmNetworkManager;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import rx.Observable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GcmTaskTagExecutorTest {

    private MatchRepository matchRepositoryMock;
    private GcmTaskTagExecutor executor;

    @Before public void setUp() throws Exception {
        matchRepositoryMock = mock(MatchRepository.class);
        executor = new GcmTaskTagExecutor(matchRepositoryMock);
    }

    @Test public void execute() throws Exception {
        when(matchRepositoryMock.refreshMatches()).thenReturn(Observable.<Void>empty());
        assertEquals(GcmNetworkManager.RESULT_SUCCESS, executor.execute("com.globo.sync.tag.SYNC_MATCHES"));
        when(matchRepositoryMock.refreshMatches()).thenReturn(Observable.<Void>error(new IOException()));
        assertEquals(GcmNetworkManager.RESULT_FAILURE, executor.execute("com.globo.sync.tag.SYNC_MATCHES"));
    }
}