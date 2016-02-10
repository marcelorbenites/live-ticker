package com.globo.brasileirao.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveTickerEntry {

    private final int matchId;
    private final int matchTime;
    private final String description;

    @JsonCreator public LiveTickerEntry(@JsonProperty("matchId") int matchId, @JsonProperty("time") int matchTime, @JsonProperty("description") String description) {
        this.matchId = matchId;
        this.matchTime = matchTime;
        this.description = description;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LiveTickerEntry that = (LiveTickerEntry) o;

        if (matchId != that.matchId) return false;
        return matchTime == that.matchTime;

    }

    @Override public int hashCode() {
        int result = matchId;
        result = 31 * result + matchTime;
        return result;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getMatchTime() {
        return matchTime;
    }

    public String getDescription() {
        return description;
    }
}
