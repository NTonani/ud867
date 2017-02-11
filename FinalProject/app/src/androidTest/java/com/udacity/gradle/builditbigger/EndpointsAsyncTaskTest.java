package com.udacity.gradle.builditbigger;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
/**
 * Created by ntonani on 2/6/17.
 */
@RunWith(AndroidJUnit4.class)
public class EndpointsAsyncTaskTest {

    private static final String EXPECTED_FREE_JOKE = "This is a funny joke";
    private static final String EXPECTED_PAID_JOKE = "This is a hilarious joke!";

    private CountDownLatch mSignal;
    private String mJoke;

    @Before
    public void init(){
        // To handle the async thread
        mSignal = new CountDownLatch(1);
    }

    @Test
    public void testJokesEndpoint() throws Exception {
        EndpointsAsyncTask jokesTask = new EndpointsAsyncTask(new EndpointsAsyncTask.OnTaskComplete() {
            @Override
            public void onTaskComplete(String joke) {
                mJoke = joke;
                mSignal.countDown();
            }
        });

        jokesTask.execute();
        mSignal.await();

        assertNotNull(mJoke);
        if(BuildConfig.IS_PAID) assertEquals(EXPECTED_PAID_JOKE,mJoke);
        else assertEquals(EXPECTED_FREE_JOKE,mJoke);
    }
}