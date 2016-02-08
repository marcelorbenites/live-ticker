package com.globo.brasileirao.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.BrasileiraoApplication;
import com.globo.brasileirao.R;
import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.data.MatchRepository;
import com.globo.brasileirao.entities.Match;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MatchListActivity extends RxActivity {

    @Bind(R.id.activity_match_list_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.activity_match_list_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @Inject MatchRepository repository;

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            showSwipeRefreshLayoutAndRefresh();
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        ButterKnife.bind(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        DaggerMatchListComponent.builder()
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule())
                .build()
                .injectMatchListActivity(this);
    }

    @Override protected void onResume() {
        super.onResume();
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
                        Toast.makeText(MatchListActivity.this, matches.toString(), Toast.LENGTH_LONG).show();
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
