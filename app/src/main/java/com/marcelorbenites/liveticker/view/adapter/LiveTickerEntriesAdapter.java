package com.marcelorbenites.liveticker.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.marcelorbenites.liveticker.R;
import com.marcelorbenites.liveticker.entities.LiveTickerEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

public class LiveTickerEntriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final LayoutInflater inflater;
  private final List<LiveTickerEntry> entries;

  @Inject public LiveTickerEntriesAdapter(LayoutInflater inflater) {
    this.inflater = inflater;
    this.entries = new ArrayList<>();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new LiveTickerEntryViewHolder(inflater.inflate(viewType, parent, false));
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    bindLiveTickerEntry((LiveTickerEntryViewHolder) holder, entries.get(position));
  }

  @Override public int getItemViewType(int position) {
    return R.layout.recycler_view_live_ticker_entry_item;
  }

  @Override public int getItemCount() {
    return entries.size();
  }

  private void bindLiveTickerEntry(LiveTickerEntryViewHolder holder, final LiveTickerEntry entry) {
    holder.matchTime.setText(String.format(Locale.US, "%d\'", entry.getMatchTime()));
    holder.description.setText(entry.getDescription());
  }

  public void refresh(List<LiveTickerEntry> entries) {
    this.entries.clear();
    this.entries.addAll(entries);
    notifyDataSetChanged();
  }

  public boolean isEmpty() {
    return entries.isEmpty();
  }
}
