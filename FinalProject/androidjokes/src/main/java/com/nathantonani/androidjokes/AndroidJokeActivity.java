package com.nathantonani.androidjokes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        String joke = getIntent().getStringExtra(getString(R.string.joke_extra));
        jokeView.setText(joke);
    }
}
