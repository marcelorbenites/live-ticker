package com.marcelorbenites.liveticker.entities;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Describes a match between two teams.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Match implements Parcelable {

  public static final Creator<Match> CREATOR = new Creator<Match>() {
    @Override public Match createFromParcel(Parcel in) {
      return new Match(in);
    }

    @Override public Match[] newArray(int size) {
      return new Match[size];
    }
  };
  private final int matchId;
  private final Team homeTeam;
  private final Team awayTeam;
  private final int homeScore;
  private final int awayScore;
  private final Date date;
  private final String location;

  @JsonCreator
  public Match(@JsonProperty("matchId") int matchId, @JsonProperty("homeTeam") Team homeTeam,
      @JsonProperty("awayTeam") Team awayTeam, @JsonProperty("homeScore") int homeScore,
      @JsonProperty("awayScore") int awayScore, @JsonProperty("date") Date date,
      @JsonProperty("location") String location) {
    this.matchId = matchId;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.homeScore = homeScore;
    this.awayScore = awayScore;
    this.date = date;
    this.location = location;
  }

  protected Match(Parcel in) {
    matchId = in.readInt();
    homeTeam = in.readParcelable(Team.class.getClassLoader());
    awayTeam = in.readParcelable(Team.class.getClassLoader());
    homeScore = in.readInt();
    awayScore = in.readInt();
    date = new Date(in.readLong());
    location = in.readString();
  }

  @Override public int hashCode() {
    return matchId;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Match match = (Match) o;

    return matchId == match.matchId;
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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(matchId);
    dest.writeParcelable(homeTeam, flags);
    dest.writeParcelable(awayTeam, flags);
    dest.writeInt(homeScore);
    dest.writeInt(awayScore);
    dest.writeLong(date.getTime());
    dest.writeString(location);
  }
}
