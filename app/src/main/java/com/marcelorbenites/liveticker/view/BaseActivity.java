package com.marcelorbenites.liveticker.view;

import android.os.Bundle;
import com.marcelorbenites.liveticker.ApplicationComponent;
import com.marcelorbenites.liveticker.LiveTickerApplication;
import com.marcelorbenites.liveticker.view.modules.ActivityModule;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  protected ApplicationComponent getApplicationComponent() {
    return ((LiveTickerApplication) getApplication()).getApplicationComponent();
  }

  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }
}