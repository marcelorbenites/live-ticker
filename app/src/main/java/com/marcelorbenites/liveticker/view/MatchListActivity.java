package com.marcelorbenites.liveticker.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.marcelorbenites.liveticker.R;
import com.marcelorbenites.liveticker.data.DataModule;
import com.marcelorbenites.liveticker.data.MatchRepository;
import com.marcelorbenites.liveticker.entities.Match;
import com.marcelorbenites.liveticker.exceptions.ThrowableToStringResourceConverter;
import com.marcelorbenites.liveticker.scheduler.MatchSyncScheduler;
import com.marcelorbenites.liveticker.view.adapter.MatchListAdapter;
import com.marcelorbenites.liveticker.view.adapter.OnMatchClickListener;
import com.marcelorbenites.liveticker.view.image.ImageModule;
import com.marcelorbenites.liveticker.view.modules.MatchListModule;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.trello.rxlifecycle.ActivityEvent;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MatchListActivity extends BaseActivity {

  public static final int TEAM_ICON_WIDTH_DP = 24;
  public static final int TEAM_ICON_HEIGHT_DP = 24;
  public static final int SYNC_INTERVAL_SECONDS = 60;
  @Bind(R.id.activity_match_list_coordinator_layout) CoordinatorLayout coordinatorLayout;
  @Bind(R.id.activity_match_list_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.activity_match_list_recycler_view) RecyclerView list;
  @Bind(R.id.activity_match_list_toolbar) Toolbar toolbar;
  @Bind(R.id.activity_match_list_empty_text) TextView emptyText;

  @Inject MatchRepository repository;
  @Inject MatchListAdapter adapter;
  @Inject LinearLayoutManager layoutManager;
  @Inject MatchSyncScheduler syncScheduler;
  @Inject ThrowableToStringResourceConverter throwableToStringResourceConverter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match_list);
    ButterKnife.bind(this);
    inject();
    setSupportActionBar(toolbar);
    list.setLayoutManager(layoutManager);
    list.setAdapter(adapter);
    swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    syncScheduler.syncMatches(SYNC_INTERVAL_SECONDS);
  }

  private void inject() {
    DaggerMatchListComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .matchListModule(
            new MatchListModule(TEAM_ICON_WIDTH_DP, TEAM_ICON_HEIGHT_DP, R.mipmap.ic_launcher))
        .imageModule(new ImageModule())
        .dataModule(new DataModule())
        .build()
        .injectMatchListActivity(this);
  }

  @Override protected void onResume() {
    super.onResume();
    adapter.setOnMatchClickListener(new OnMatchClickListener() {
      @Override public void onMatchClick(Match match) {
        startActivity(new Intent(MatchListActivity.this, LiveTickerActivity.class).putExtra(
            LiveTickerActivity.EXTRA_MATCH, match));
      }
    });
    repository.getMatches()
        .compose(this.<List<Match>>bindUntilEvent(ActivityEvent.PAUSE))
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<Match>>() {
          @Override public void call(List<Match> matches) {
            adapter.refresh(matches);
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
    RxSwipeRefreshLayout.refreshes(swipeRefreshLayout)
        .compose(this.<Void>bindUntilEvent(ActivityEvent.PAUSE))
        .subscribe(new Action1<Void>() {
          @Override public void call(Void aVoid) {
            refresh();
          }
        });
    showSwipeRefreshLayoutAndRefresh();
  }

  @Override protected void onPause() {
    super.onPause();
    adapter.setOnMatchClickListener(null);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    syncScheduler.cancelMatchesSync();
  }

  private void refresh() {
    repository.refreshMatches()
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
            Crashlytics.logException(throwable);
            Snackbar.make(coordinatorLayout, throwableToStringResourceConverter.convert(throwable),
                Snackbar.LENGTH_SHORT)
                .show();
          }
        });
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
}
