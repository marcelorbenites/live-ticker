package com.globo.brasileirao.data.disk;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.entities.Team;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class MatchSQLiteRepository implements MatchDiskRepository {

    private final BriteDatabase database;

    public MatchSQLiteRepository(BriteDatabase database) {
        this.database = database;
    }

    /**
     * @return observable that return all matches stored in repository and completes. If no match is found
     * observable completes without emitting items.
     */
    @Override public Observable<List<Match>> getMatches() {
        return database.createQuery("matches", "SELECT * FROM matches")
                .mapToList(getCursorToMatch())
                // use first in order to force completion and default
                // to avoid an exception when there are no matches stored.
                .firstOrDefault(Collections.<Match>emptyList())
                .filter(new Func1<List<Match>, Boolean>() {
                    @Override public Boolean call(List<Match> matches) {
                        return !matches.isEmpty();
                    }
                });
    }

    @Override public void saveMatches(List<Match> matches) {
        final BriteDatabase.Transaction transaction = database.newTransaction();
        try {
            for (Match match : matches) {
                database.insert("matches", getMatchContentValues(match));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override public void clearMatches() {
        database.delete("matches", "1");
    }

    @NonNull private ContentValues getMatchContentValues(Match match) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("matchId", match.getMatchId());
        contentValues.put("awayTeamName", match.getAwayTeam().getName());
        contentValues.put("awayTeamIconUrl", match.getAwayTeam().getIconUrl());
        contentValues.put("homeTeamName", match.getHomeTeam().getName());
        contentValues.put("homeTeamIconUrl", match.getHomeTeam().getIconUrl());
        contentValues.put("homeScore", match.getHomeScore());
        contentValues.put("awayScore", match.getAwayScore());
        contentValues.put("location", match.getLocation());
        contentValues.put("date", match.getDate().getTime());
        return contentValues;
    }

    @NonNull private Func1<Cursor, Match> getCursorToMatch() {
        return new Func1<Cursor, Match>() {
            @Override public Match call(Cursor cursor) {
                return new Match(cursor.getInt(cursor.getColumnIndex("matchId")),
                        new Team(cursor.getString(cursor.getColumnIndex("homeTeamName")),
                                cursor.getString(cursor.getColumnIndex("homeTeamIconUrl"))),
                        new Team(cursor.getString(cursor.getColumnIndex("awayTeamName")),
                                cursor.getString(cursor.getColumnIndex("awayTeamIconUrl"))),
                        cursor.getInt(cursor.getColumnIndex("homeScore")),
                        cursor.getInt(cursor.getColumnIndex("awayScore")),
                        new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("location")));
            }
        };
    }
}
