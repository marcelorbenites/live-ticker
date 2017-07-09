package com.marcelorbenites.liveticker.data.disk;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import com.marcelorbenites.liveticker.entities.Match;
import com.marcelorbenites.liveticker.entities.Team;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

public class MatchSQLiteRepository implements MatchDiskRepository {

  private final BriteDatabase database;

  public MatchSQLiteRepository(BriteDatabase database) {
    this.database = database;
  }

  @Override public Observable<Match> getMatch(int matchId) {
    return database.createQuery("matches",
        "SELECT * FROM matches WHERE matchId = ? ORDER BY date ASC", String.valueOf(matchId))
        .mapToList(getCursorToMatch())
        .flatMap(new Func1<List<Match>, Observable<Match>>() {
          @Override public Observable<Match> call(List<Match> matches) {
            if (matches.isEmpty()) {
              return Observable.error(new SQLiteException("Match not found."));
            }
            return Observable.just(matches.get(0));
          }
        });
  }

  @Override public Observable<List<Match>> getMatches() {
    return database.createQuery("matches", "SELECT * FROM matches ORDER BY date ASC")
        .mapToList(getCursorToMatch());
  }

  @Override public void saveOrOverwriteMatch(Match match) {
    database.insert("matches", getMatchContentValues(match), SQLiteDatabase.CONFLICT_REPLACE);
  }

  @Override public void saveOrOverwriteMatches(List<Match> matches) {
    final BriteDatabase.Transaction transaction = database.newTransaction();
    try {
      for (Match match : matches) {
        saveOrOverwriteMatch(match);
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
  }

  @Override public void clearMatches() {
    database.delete("matches", "1");
  }

  @Override public void saveOrOverwriteLiveTickerEntries(int matchId,
      List<LiveTickerEntry> liveTickerEntries) {
    final BriteDatabase.Transaction transaction = database.newTransaction();
    try {
      for (LiveTickerEntry liveTickerEntry : liveTickerEntries) {
        database.insert("live_ticker_entries",
            getLiveTickerEntryContentValues(matchId, liveTickerEntry),
            SQLiteDatabase.CONFLICT_REPLACE);
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
  }

  @Override public Observable<List<LiveTickerEntry>> getLiveTickerEntries(int matchId) {
    return database.createQuery("live_ticker_entries",
        "SELECT * FROM live_ticker_entries WHERE matchId = ? ORDER BY time DESC",
        String.valueOf(matchId))
        .mapToList(getCursorToLiveTickerEntry());
  }

  @Override public void clearLiveTickerEntries(int matchId) {
    database.delete("live_ticker_entries", "matchId = ?", String.valueOf(matchId));
  }

  private ContentValues getLiveTickerEntryContentValues(int matchId,
      LiveTickerEntry liveTickerEntry) {
    final ContentValues contentValues = new ContentValues();
    contentValues.put("matchId", matchId);
    contentValues.put("time", liveTickerEntry.getMatchTime());
    contentValues.put("description", liveTickerEntry.getDescription());
    return contentValues;
  }

  @NonNull private ContentValues getMatchContentValues(Match match) {
    final ContentValues contentValues = new ContentValues();
    contentValues.put("matchId", match.getMatchId());
    contentValues.put("awayTeamName", match.getAwayTeam()
        .getName());
    contentValues.put("awayTeamIconUrl", match.getAwayTeam()
        .getIconUrl());
    contentValues.put("homeTeamName", match.getHomeTeam()
        .getName());
    contentValues.put("homeTeamIconUrl", match.getHomeTeam()
        .getIconUrl());
    contentValues.put("homeScore", match.getHomeScore());
    contentValues.put("awayScore", match.getAwayScore());
    contentValues.put("location", match.getLocation());
    contentValues.put("date", match.getDate()
        .getTime());
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

  private Func1<Cursor, LiveTickerEntry> getCursorToLiveTickerEntry() {
    return new Func1<Cursor, LiveTickerEntry>() {
      @Override public LiveTickerEntry call(Cursor cursor) {
        return new LiveTickerEntry(cursor.getInt(cursor.getColumnIndex("matchId")),
            cursor.getInt(cursor.getColumnIndex("time")),
            cursor.getString(cursor.getColumnIndex("description")));
      }
    };
  }
}
