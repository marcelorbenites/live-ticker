package com.globo.brasileirao.entities;

import java.util.List;

public class MatchLiveTicker {

    private final Match match;
    private final List<LiveTickerEntry> entries;

    public MatchLiveTicker(Match match, List<LiveTickerEntry> entries) {
        this.match = match;
        this.entries = entries;
    }

    public Match getMatch() {
        return match;
    }

    public List<LiveTickerEntry> getEntries() {
        return entries;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchLiveTicker that = (MatchLiveTicker) o;

        if (!match.equals(that.match)) return false;
        return entries.equals(that.entries);

    }

    @Override public int hashCode() {
        int result = match.hashCode();
        result = 31 * result + entries.hashCode();
        return result;
    }
}
