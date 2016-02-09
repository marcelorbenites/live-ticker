package com.globo.brasileirao.view;

import android.os.Bundle;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.BrasileiraoApplication;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity {

    private ActivityComponent activityComponent;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    private ApplicationComponent getApplicationComponent() {
        return ((BrasileiraoApplication)getApplication()).getApplicationComponent();
    }

    protected ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}