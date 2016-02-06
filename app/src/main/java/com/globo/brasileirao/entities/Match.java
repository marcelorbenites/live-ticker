package com.globo.brasileirao.entities;

import java.util.Date;

/**
 * Describes a match between two teams.
 */
public class Match {

    private final int matchId;
    private final Team homeTeam;
    private final Team awayTeam;
    private final int homeScore;
    private final int awayScore;
    private final Date date;
    private final String location;

    public Match(int matchId, Team homeTeam, Team awayTeam, int homeScore, int awayScore, Date date, String location) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.date = date;
        this.location = location;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        return matchId == match.matchId;

    }

    @Override public int hashCode() {
        return matchId;
    }

    public int getMatchId() {
        return matchId;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
