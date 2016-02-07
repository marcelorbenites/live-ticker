package com.globo.brasileirao.data.disk;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.entities.Team;
import com.squareup.sqlbrite.SqlBrite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MatchSQLiteRepositoryTest {

    private MatchSQLiteRepository repository;

    @Before public void setUp() throws Exception {
        repository = new MatchSQLiteRepository(SqlBrite.create().wrapDatabaseHelper(new BrasileiraoDatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext())));
    }

    @Test public void testSaveMatchesAndQuery() throws Exception {

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
        repository.saveMatches(matches);

        TestSubscriber<List<Match>> testSubscriber = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertEquals(matches, testSubscriber.getOnNextEvents().get(0));
        testSubscriber.assertCompleted();

        repository.clearMatches();

        TestSubscriber<List<Match>> testSubscriber2 = new TestSubscriber<>();
        repository.getMatches().subscribe(testSubscriber2);
        testSubscriber2.assertNoErrors();
        testSubscriber2.assertNoValues();
        testSubscriber2.assertCompleted();
    }
}