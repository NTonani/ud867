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

    private final static String EXPECTED_JOKE = "This is a Java Joke!";

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
        assertEquals(EXPECTED_JOKE,mJoke);
    }
}