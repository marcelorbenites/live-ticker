package com.globo.brasileirao.data.disk;

import android.database.sqlite.SQLiteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.entities.Team;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MatchSQLiteRepositoryIntegrationTest {

    private MatchSQLiteRepository repository;

    @Before public void setUp() throws Exception {
        repository = new MatchSQLiteRepository(SqlBrite.create().wrapDatabaseHelper(new BrasileiraoDatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext())));
    }

    @Test public void getInvalidMatch() throws Exception {
        TestSubscriber<Match> testSubscriber = new TestSubscriber<>();
        repository.getMatch(5).subscribe(testSubscriber);
        testSubscriber.assertError(SQLiteException.class);
        testSubscriber.assertNoValues();
        testSubscriber.assertUnsubscribed();
    }

    @Test public void saveMatchAndQuery() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1909, 6, 21);
        final Match match = new Match(1, new Team("Grêmio", "www.host.com/gremioIcon"),
                        new Team("Internacional", "www.host.com/interIcon"),
                        2,
                        0,
                        calendar.getTime(),
                        "Sociedade Leopoldina Porto Alegrense");
        repository.saveOrOverwriteMatch(match);

        TestSubscriber<Match> testSubscriber = new TestSubscriber<>();
        repository.getMatch(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertEquals(match, testSubscriber.getOnNextEvents().get(0));
        testSubscriber.assertNotCompleted();

        final Match modifiedMatch = new Match(1, new Team("Grêmio", "www.host.com/gremioIcon"),
                new Team("Flamengo", "www.host.com/flamengoIcon"),
                2,
                0,
                calendar.getTime(),
                "Sociedade Leopoldina Porto Alegrense");
        repository.saveOrOverwriteMatch(modifiedMatch);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        assertEquals(match, testSubscriber.getOnNextEvents().get(1));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
        testSubscriber.assertUnsubscribed();

        repository.clearMatches();
    }

    @Test public void saveMatchesAndQuery() throws Exception {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(1909, 6, 21);
        final List<Match> matches = Arrays.asList(
                new Match(1, new Team("Grêmio", "www.host.com/gremioIcon"),
                        new Team("Internacional", "www.host.com/interIcon"),
                        2,
                        0,
                        calendar.getTime(),
                        "Sociedade Leopoldina Porto Alegrense"),
                new Match(2, new Team("Flamengo", "www.host.com/flamengoIcon"),
                        new Team("Vasco", "www.host.com/vascoIcon"),
                        5,
                        3,
                        calendar.getTime(),
                        "Maracanã"));
        repository.saveOrOverwriteMatches(matches);

        TestSubscriber<List<Match>> testSubscriber = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertEquals(matches, testSubscriber.getOnNextEvents().get(0));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
        testSubscriber.assertUnsubscribed();

        repository.clearMatches();

        TestSubscriber<List<Match>> testSubscriber4 = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber4);
        testSubscriber4.assertNoErrors();
        testSubscriber4.assertValueCount(1);
        assertTrue(testSubscriber4.getOnNextEvents().get(0).isEmpty());
        testSubscriber4.assertNotCompleted();
        testSubscriber4.unsubscribe();
        testSubscriber4.assertUnsubscribed();
    }

    @Test public void saveLiveTickerEntriesAndQuery() throws Exception {
        final List<Match> matches = Collections.singletonList(
                new Match(1, new Team("Grêmio", "www.host.com/gremioIcon"),
                        new Team("Internacional", "www.host.com/interIcon"),
                        2,
                        0,
                        new Date(),
                        "Sociedade Leopoldina Porto Alegrense"));
        repository.saveOrOverwriteMatches(matches);


        final List<LiveTickerEntry> liveTickerEntries = Arrays.asList(
                new LiveTickerEntry(1, 23, "Test 1"),
                new LiveTickerEntry(1, 45, "Test 2"));

        repository.saveOrOverwriteLiveTickerEntries(1, liveTickerEntries);

        TestSubscriber<List<LiveTickerEntry>> testSubscriber = new TestSubscriber<>();
        repository.getLiveTickerEntries(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertEquals(liveTickerEntries.get(1), testSubscriber.getOnNextEvents().get(0).get(0));
        testSubscriber.assertNotCompleted();

        final List<LiveTickerEntry> newLiveTickerEntries = Arrays.asList(
                new LiveTickerEntry(1, 60, "Test 3"),
                new LiveTickerEntry(1, 61, "Test 4"));

        repository.saveOrOverwriteLiveTickerEntries(1, newLiveTickerEntries);

        // Merge lists
        final List<LiveTickerEntry> mergedList = new ArrayList<>(liveTickerEntries);
        mergedList.addAll(newLiveTickerEntries);

        assertEquals(4, mergedList.size());

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
        testSubscriber.assertUnsubscribed();

        repository.clearLiveTickerEntries(1);

        TestSubscriber<List<LiveTickerEntry>> testSubscriber2 = new TestSubscriber<>();
        repository.getLiveTickerEntries(1).subscribe(testSubscriber2);
        testSubscriber2.assertNoErrors();
        testSubscriber2.assertValueCount(1);
        assertTrue(testSubscriber2.getOnNextEvents().get(0).isEmpty());
        testSubscriber2.assertNotCompleted();
        testSubscriber2.unsubscribe();
        testSubscriber2.assertUnsubscribed();

        repository.clearMatches();

        TestSubscriber<List<Match>> testSubscriber3 = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber3);
        testSubscriber3.assertNoErrors();
        testSubscriber3.assertValueCount(1);
        assertTrue(testSubscriber2.getOnNextEvents().get(0).isEmpty());
        testSubscriber3.assertNotCompleted();
        testSubscriber3.unsubscribe();
        testSubscriber3.assertUnsubscribed();

    }
}