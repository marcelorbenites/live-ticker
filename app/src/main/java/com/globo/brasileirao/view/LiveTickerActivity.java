package com.globo.brasileirao.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.globo.brasileirao.R;
import com.globo.brasileirao.data.MatchLiveTickerRepository;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.view.image.ImageLoader;
import com.globo.brasileirao.view.image.ImageModule;
import com.globo.brasileirao.view.modules.LiveTickerModule;
import com.globo.brasileirao.view.utils.UnitConverter;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.trello.rxlifecycle.ActivityEvent;

import java.text.DateFormat;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LiveTickerActivity extends BaseActivity {

    public static final String EXTRA_MATCH = "com.globo.brasileirao.intent.extra.MATCH";

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

    @Inject ImageLoader imageLoader;
    @Inject UnitConverter unitConverter;
    @Inject DateFormat dateFormat;
    @Inject MatchLiveTickerRepository repository;

    @SuppressWarnings("ConstantConditions") @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ticker);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(EXTRA_MATCH)) {
            final Match match = getIntent().getParcelableExtra(EXTRA_MATCH);
            inject(match);
            setMatch(match);
        } else {
            throw new IllegalStateException("No match provided.");
        }
    }

    @Override protected void onResume() {
        super.onResume();
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

    private void showSwipeRefreshLayoutAndRefresh() {
        swipeRefreshLayout.post(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        refresh();
    }

    private void refresh() {
        repository.refreshLiveTicker(10)
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
                .subscribe(new Observer<Void>() {
                    @Override public void onCompleted() {
                        Toast.makeText(LiveTickerActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onError(Throwable e) {
                        Toast.makeText(LiveTickerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onNext(Void aVoid) {

                    }
                });
    }

    private void inject(Match match) {
        DaggerLiveTickerComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .imageModule(new ImageModule())
                .liveTickerModule(new LiveTickerModule(match))
                .build()
                .injectLiveTickerActivity(this);
    }

    public void setMatch(Match match) {
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