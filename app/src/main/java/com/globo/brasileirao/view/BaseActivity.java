package com.globo.brasileirao.view;

import android.os.Bundle;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.BrasileiraoApplication;
import com.globo.brasileirao.view.modules.ActivityModule;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((BrasileiraoApplication)getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}