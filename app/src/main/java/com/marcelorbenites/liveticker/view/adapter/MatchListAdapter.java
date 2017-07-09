package com.marcelorbenites.liveticker.view.adapter;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.marcelorbenites.liveticker.R;
import com.marcelorbenites.liveticker.entities.Match;
import com.marcelorbenites.liveticker.view.image.ImageLoader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final LayoutInflater inflater;
  private final ImageLoader imageLoader;
  private final int teamIconWidth;
  private final int teamIconHeight;
  private final int teamIconPlaceholder;
  private final DateFormat matchDateFormat;
  private final List<Match> matches;
  private OnMatchClickListener clickListener;

  public MatchListAdapter(LayoutInflater inflater, ImageLoader imageLoader, int teamIconWidth,
      int teamIconHeight, @DrawableRes int teamIconPlaceholder, DateFormat matchDateFormat) {
    this.inflater = inflater;
    this.imageLoader = imageLoader;
    this.teamIconWidth = teamIconWidth;
    this.teamIconHeight = teamIconHeight;
    this.teamIconPlaceholder = teamIconPlaceholder;
    this.matchDateFormat = matchDateFormat;
    this.matches = new ArrayList<>();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MatchViewHolder(inflater.inflate(viewType, parent, false));
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    bindMatch((MatchViewHolder) holder, matches.get(position));
  }

  @Override public int getItemViewType(int position) {
    return R.layout.recycler_view_match_item;
  }

  @Override public int getItemCount() {
    return matches.size();
  }

  @Override public void onViewRecycled(RecyclerView.ViewHolder holder) {
    super.onViewRecycled(holder);
    ((MatchViewHolder) holder).setOnClickListener(null);
  }

  private void bindMatch(MatchViewHolder holder, final Match match) {
    holder.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (clickListener != null) {
          clickListener.onMatchClick(match);
        }
      }
    });
    holder.homeTeamName.setText(match.getHomeTeam()
        .getName());
    holder.homeTeamScore.setText(String.valueOf(match.getHomeScore()));
    imageLoader.loadFromNetwork(holder.homeTeamIcon, match.getHomeTeam()
        .getIconUrl(), teamIconWidth, teamIconHeight, teamIconPlaceholder);
    holder.awayTeamName.setText(match.getAwayTeam()
        .getName());
    holder.awayTeamScore.setText(String.valueOf(match.getAwayScore()));
    imageLoader.loadFromNetwork(holder.awayTeamIcon, match.getAwayTeam()
        .getIconUrl(), teamIconWidth, teamIconHeight, teamIconPlaceholder);
    holder.location.setText(match.getLocation());
    holder.date.setText(matchDateFormat.format(match.getDate()));
  }

  public void setOnMatchClickListener(OnMatchClickListener clickListener) {
    this.clickListener = clickListener;
  }

  public void refresh(List<Match> matches) {
    this.matches.clear();
    this.matches.addAll(matches);
    notifyDataSetChanged();
  }

  public boolean isEmpty() {
    return matches.isEmpty();
  }
}
