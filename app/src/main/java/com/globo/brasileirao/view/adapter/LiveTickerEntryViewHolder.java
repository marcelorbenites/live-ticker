package com.globo.brasileirao.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.globo.brasileirao.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LiveTickerEntryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.recycler_view_live_ticker_entry_item_match_time) TextView matchTime;
    @Bind(R.id.recycler_view_live_ticker_entry_item_description) TextView description;

    public LiveTickerEntryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
