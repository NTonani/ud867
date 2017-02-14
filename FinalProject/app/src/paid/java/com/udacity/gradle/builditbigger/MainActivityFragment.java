package com.udacity.gradle.builditbigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nathantonani.androidjokes.AndroidJokeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Paid flavor fragment - basic
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.OnTaskComplete{

    private Intent mAndroidIntent;

    @BindView(R.id.jokesProgressBar)
    ProgressBar mSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // Bind Butterknife
        ButterKnife.bind(this,root);
        mSpinner.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Register new broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                new BroadcastReceiver() {

                    // Init CoundDownLatch object, load interstitial add, request joke
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // Request joke from CGE
                        requestJoke();

                    }
                }, new IntentFilter(getString(R.string.requesting_joke))
        );
    }

    @Override
    public void onTaskComplete(String joke) {
        mAndroidIntent = new Intent(getContext(), AndroidJokeActivity.class);
        mAndroidIntent.putExtra(getString(R.string.joke_extra),joke);
        startActivity(mAndroidIntent);

        mSpinner.setVisibility(View.GONE);
    }

    private void requestJoke(){
        mSpinner.setVisibility(View.VISIBLE);

        new EndpointsAsyncTask(this).execute();
    }
}
