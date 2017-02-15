package com.nathantonani.androidjokes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AndroidJokeActivity extends AppCompatActivity {

    @BindView(R2.id.joke_view)
    TextView jokeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_joke);

        ButterKnife.bind(this);

        // Set up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.android_jokes));

        String joke = getIntent().getStringExtra(getString(R.string.joke_extra));
        jokeView.setText(joke);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            // Mimic back behavior with Up navigation to return to caller
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
