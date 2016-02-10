/*
 * AUREA CONFIDENTIAL
 *
 * 2014 (c) Aurea Soluções Tecnológicas Ltda.
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Aurea Soluções Tecnológicas Ltda.
 * The intellectual and technical concepts contained
 * herein are proprietary to Aurea Soluções Tecnológicas
 * and may be covered by Brazil, U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Aurea Soluções Tecnológicas Ltda.
 */

package com.globo.brasileirao.data.disk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class that creates and handles updates of Aurea database later being accessed by AureaContentProvider.
 */
public class BrasileiraoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "brasileirao.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_MATCHES = String.format("create table %s (" +
                    "%s int primary key , " +
                    "%s text not null, " +
                    "%s text, " +
                    "%s text not null, " +
                    "%s text," +
                    "%s int not null," +
                    "%s int not null," +
                    "%s int not null," +
                    "%s text not null);",
            "matches",
            "matchId",
            "homeTeamName",
            "homeTeamIconUrl",
            "awayTeamName",
            "awayTeamIconUrl",
            "homeScore",
            "awayScore",
            "date",
            "location");

    private static final String CREATE_TABLE_LIVE_TICKER_ENTRIES = String.format("create table %s (" +
                    "%s int not null, " +
                    "%s int not null, " +
                    "%s text not null, " +
                    "primary key (%s, %s)," +
                    "foreign key (%s) references %s(%s));",
            "live_ticker_entries",
            "time",
            "matchId",
            "description",
            "time",
            "matchId",
            "matchId",
            "matches",
            "matchId");

    public BrasileiraoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_MATCHES);
            db.execSQL(CREATE_TABLE_LIVE_TICKER_ENTRIES);
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", "matches"));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", "live_ticker_entries"));
        onCreate(db);
    }
}