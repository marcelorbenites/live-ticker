package com.globo.brasileirao.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.globo.brasileirao.R;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.view.image.ImageLoader;
import com.globo.brasileirao.view.utils.UnitConverter;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.trello.rxlifecycle.ActivityEvent;

import java.text.DateFormat;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class LiveTickerActivity extends BaseActivity {

    public static final String EXTRA_MATCH = "com.globo.brasileirao.intent.extra.MATCH";

    @Bind(R.id.activity_live_ticker_coordinator_layout) CoordinatorLayout coordinatorLayout;
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

    @SuppressWarnings("ConstantConditions") @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ticker);
        ButterKnife.bind(this);
        inject();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(EXTRA_MATCH)) {
            setMatch((Match) getIntent().getParcelableExtra(EXTRA_MATCH));
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
    }

    private void inject() {
        DaggerLiveTickerComponent.builder()
                .activityComponent(getActivityComponent())
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