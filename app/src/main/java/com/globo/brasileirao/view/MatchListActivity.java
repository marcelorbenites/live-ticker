package com.globo.brasileirao.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.BrasileiraoApplication;
import com.globo.brasileirao.R;
import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.data.MatchRepository;
import com.globo.brasileirao.entities.Match;
import com.globo.brasileirao.view.adapter.MatchListAdapter;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MatchListActivity extends RxAppCompatActivity {

    public static final int TEAM_ICON_WIDTH_DP = 24;
    public static final int TEAM_ICON_HEIGHT_DP = 24;
    @Bind(R.id.activity_match_list_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.activity_match_list_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.activity_match_list_recycler_view) RecyclerView list;
    @Bind(R.id.activity_match_list_toolbar) Toolbar toolbar;

    @Inject MatchRepository repository;
    @Inject MatchListAdapter adapter;
    @Inject LinearLayoutManager layoutManager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        ButterKnife.bind(this);
        inject();
        setSupportActionBar(toolbar);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    private void inject() {
        DaggerMatchListComponent.builder()
                .activityComponent(getActivityComponent())
                .dataModule(new DataModule())
                .matchListModule(new MatchListModule(TEAM_ICON_WIDTH_DP, TEAM_ICON_HEIGHT_DP, R.mipmap.ic_launcher))
                .build()
                .injectMatchListActivity(this);
    }

    private ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override protected void onResume() {
        super.onResume();
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

    private ApplicationComponent getApplicationComponent() {
        return ((BrasileiraoApplication) getApplication()).getApplicationComponent();
    }

    private void refresh() {
        repository.getMatches()
                .compose(this.<List<Match>>bindUntilEvent(ActivityEvent.PAUSE))
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
                .subscribe(new Action1<List<Match>>() {
                    @Override public void call(List<Match> matches) {
                        adapter.refresh(matches);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Toast.makeText(MatchListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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
