package com.globo.brasileirao.view;

import android.os.Bundle;
import android.widget.Toast;

import com.globo.brasileirao.R;
import com.globo.brasileirao.entities.Match;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public class LiveTickerActivity extends RxAppCompatActivity {

    public static final String EXTRA_MATCH = "com.globo.brasileirao.intent.extra.MATCH";
    private Match match;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ticker);
        if (getIntent().hasExtra(EXTRA_MATCH)) {
            match = getIntent().getParcelableExtra(EXTRA_MATCH);
        } else {
            throw new IllegalStateException("No match provided.");
        }
        Toast.makeText(this, String.valueOf(match.getMatchId()), Toast.LENGTH_LONG).show();
    }
}
