package com.marcelorbenites.liveticker.entities;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A football team.
 */
public class Team implements Parcelable {

  public static final Creator<Team> CREATOR = new Creator<Team>() {
    @Override public Team createFromParcel(Parcel in) {
      return new Team(in);
    }

    @Override public Team[] newArray(int size) {
      return new Team[size];
    }
  };
  private final String name;
  private final String iconUrl;

  @JsonCreator
  public Team(@JsonProperty("name") String name, @JsonProperty("icon") String iconUrl) {
    this.name = name;
    this.iconUrl = iconUrl;
  }

  protected Team(Parcel in) {
    name = in.readString();
    iconUrl = in.readString();
  }

  public String getName() {
    return name;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(iconUrl);
  }
}