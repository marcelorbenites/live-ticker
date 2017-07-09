package com.marcelorbenites.liveticker.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.marcelorbenites.liveticker.R;

public class MatchViewHolder extends RecyclerView.ViewHolder {

  private final View view;
  @Bind(R.id.recycler_view_match_item_home_team_name) TextView homeTeamName;
  @Bind(R.id.recycler_view_match_item_home_team_score) TextView homeTeamScore;
  @Bind(R.id.recycler_view_match_item_home_team_icon) ImageView homeTeamIcon;
  @Bind(R.id.recycler_view_match_item_away_team_name) TextView awayTeamName;
  @Bind(R.id.recycler_view_match_item_away_team_score) TextView awayTeamScore;
  @Bind(R.id.recycler_view_match_item_away_team_icon) ImageView awayTeamIcon;
  @Bind(R.id.recycler_view_match_item_location) TextView location;
  @Bind(R.id.recycler_view_match_item_date) TextView date;

  public MatchViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    this.view = itemView;
  }

  public void setOnClickListener(View.OnClickListener clickListener) {
    view.setOnClickListener(clickListener);
  }
}
