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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nathantonani.androidjokes.AndroidJokeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Free flavor fragment - contains ad objects
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.OnTaskComplete{
    private final static String LOG_TAG = MainActivityFragment.class.getSimpleName();

    @BindView(R.id.jokesProgressBar)
    ProgressBar mSpinner;

    @BindView(R.id.adView)
    AdView mAdView;

    private InterstitialAd mInterstitialAd;
    private AdRequest mAdRequest;
    private Intent mAndroidIntent;
    private boolean mWaitOnAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mWaitOnAd = true;

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // Bind Butterknife
        ButterKnife.bind(this,root);
        mSpinner.setVisibility(View.GONE);

        // Init emulator / dev AdRequest
        mAdRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(mAdRequest);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create and load interstitial add
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        // Set listener which unblocks execution of loading activity
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mWaitOnAd = false;

                // Recreate adrequest
                mAdRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

                mInterstitialAd.loadAd(mAdRequest);

                // If intent object isn't null, start activity
                if(mAndroidIntent != null) startActivity(mAndroidIntent);
            }
        });

        mInterstitialAd.loadAd(mAdRequest);

        // Register new broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                new BroadcastReceiver() {

                    // Init CoundDownLatch object, load interstitial add, request joke
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // During loading, display interstitial ad
                        if(mInterstitialAd.isLoaded()){
                            mInterstitialAd.show();
                            mWaitOnAd = true;
                        }

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
        mSpinner.setVisibility(View.GONE);
        // If no ad displayed or already closed, start activity
        if(!mWaitOnAd) startActivity(mAndroidIntent);
    }

    private void requestJoke(){
        mSpinner.setVisibility(View.VISIBLE);
        new EndpointsAsyncTask(this).execute();
    }
}
