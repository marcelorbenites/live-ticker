package com.globo.brasileirao.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.globo.brasileirao.R;
import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.data.MatchLiveTickerRepository;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.entities.MatchLiveTicker;
import com.globo.brasileirao.exceptions.ThrowableToStringResourceConverter;
import com.globo.brasileirao.scheduler.MatchLiveTickerScheduler;
import com.globo.brasileirao.view.adapter.LiveTickerEntriesAdapter;
import com.globo.brasileirao.view.image.ImageLoader;
import com.globo.brasileirao.view.image.ImageModule;
import com.globo.brasileirao.view.modules.LiveTickerModule;
import com.globo.brasileirao.view.utils.UnitConverter;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.trello.rxlifecycle.ActivityEvent;

import java.text.DateFormat;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LiveTickerActivity extends BaseActivity {

    public static final String EXTRA_MATCH = "com.globo.brasileirao.intent.extra.MATCH";
    public static final int SYNC_INTERVAL_SECONDS = 60;

    @Bind(R.id.activity_live_ticker_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.activity_live_ticker_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.activity_live_ticker_toolbar) Toolbar toolbar;
    @Bind(R.id.activity_live_ticker_home_team_name) TextView homeTeamName;
    @Bind(R.id.activity_live_ticker_away_team_name) TextView awayTeamName;
    @Bind(R.id.activity_live_ticker_home_team_icon) ImageView homeTeamIcon;
    @Bind(R.id.activity_live_ticker_away_team_icon) ImageView awayTeamIcon;
    @Bind(R.id.activity_live_ticker_home_team_score) TextView homeTeamScore;
    @Bind(R.id.activity_live_ticker_away_team_score) TextView awayTeamScore;
    @Bind(R.id.activity_live_ticker_location) TextView location;
    @Bind(R.id.activity_live_ticker_date) TextView date;

    @Bind(R.id.activity_live_ticker_recycler_view) RecyclerView list;
    @Bind(R.id.activity_live_ticker_list_empty_text) TextView emptyText;

    @Inject ImageLoader imageLoader;
    @Inject UnitConverter unitConverter;
    @Inject DateFormat dateFormat;
    @Inject MatchLiveTickerRepository repository;
    @Inject LiveTickerEntriesAdapter adapter;
    @Inject LinearLayoutManager layoutManager;
    @Inject ThrowableToStringResourceConverter throwableToStringResourceConverter;
    @Inject MatchLiveTickerScheduler scheduler;

    private int matchId;

    @SuppressWarnings("ConstantConditions") @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ticker);
        ButterKnife.bind(this);
        inject();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        if (getIntent().hasExtra(EXTRA_MATCH)) {
            setMatch((Match) getIntent().getParcelableExtra(EXTRA_MATCH));
        } else {
            throw new IllegalStateException("No match provided.");
        }
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        scheduler.syncMatchLiveTicker(matchId, SYNC_INTERVAL_SECONDS);
    }

    @Override protected void onResume() {
        super.onResume();
        repository.getMatchLiveTicker(matchId)
                .compose(this.<MatchLiveTicker>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MatchLiveTicker>() {
                    @Override public void call(MatchLiveTicker matchLiveTicker) {
                        setMatch(matchLiveTicker.getMatch());
                        adapter.refresh(matchLiveTicker.getEntries());
                        updateListVisibility();
                    }
                });
        RxRecyclerView.scrollStateChanges(list)
                .compose(this.<Integer>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Action1<Integer>() {
                    @Override public void call(Integer state) {
                        // Enable swipe to refresh only when first item is visible
                        // to make smooth experience
                        swipeRefreshLayout.setEnabled(state == RecyclerView.SCROLL_STATE_IDLE
                                && layoutManager.findFirstVisibleItemPosition() == 0);
                    }
                });
        RxToolbar.navigationClicks(toolbar)
                .compose(this.<Void>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Action1<Void>() {
                    @Override public void call(Void aVoid) {
                        onBackPressed();
                    }
                });
        RxSwipeRefreshLayout.refreshes(swipeRefreshLayout)
                .compose(this.<Void>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Action1<Void>() {
                    @Override public void call(Void aVoid) {
                        refresh();
                    }
                });
        showSwipeRefreshLayoutAndRefresh();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        scheduler.cancelMatchesLiveTickerSync();
    }

    private void updateListVisibility() {
        if (adapter.isEmpty()) {
            list.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    private void showSwipeRefreshLayoutAndRefresh() {
        swipeRefreshLayout.post(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        refresh();
    }

    private void refresh() {
        repository.refreshMatchLiveTicker(matchId)
                .compose(this.<Void>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override public void call() {
                        swipeRefreshLayout.post(new Runnable() {
                            @Override public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                })
                .subscribe(new Action1<Void>() {
                    @Override public void call(Void aVoid) {

                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Snackbar.make(coordinatorLayout, throwableToStringResourceConverter.convert(throwable), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void inject() {
        DaggerLiveTickerComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .imageModule(new ImageModule())
                .liveTickerModule(new LiveTickerModule())
                .dataModule(new DataModule())
                .build()
                .injectLiveTickerActivity(this);
    }

    public void setMatch(Match match) {
        matchId = match.getMatchId();
        final int teamIconWidth = unitConverter.dpToPixels(40);
        final int teamIconHeight = unitConverter.dpToPixels(40);
        homeTeamName.setText(match.getHomeTeam().getName());
        awayTeamName.setText(match.getAwayTeam().getName());
        homeTeamScore.setText(String.valueOf(match.getHomeScore()));
        awayTeamScore.setText(String.valueOf(match.getAwayScore()));
        imageLoader.loadFromNetwork(homeTeamIcon, match.getHomeTeam().getIconUrl(), teamIconWidth, teamIconHeight, R.mipmap.ic_launcher);
        imageLoader.loadFromNetwork(awayTeamIcon, match.getAwayTeam().getIconUrl(), teamIconWidth, teamIconHeight, R.mipmap.ic_launcher);
        location.setText(match.getLocation());
        date.setText(dateFormat.format(match.getDate()));
    }
}