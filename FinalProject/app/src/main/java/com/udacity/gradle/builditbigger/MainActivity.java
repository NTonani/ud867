package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.nathantonani.androidjokes.AndroidJokeActivity;
import com.nathantonani.gcejokes.jokesApi.JokesApi;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements EndpointsAsyncTask.OnTaskComplete{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        // Inform receivers of joke request
        if(!BuildConfig.IS_PAID) {
            Intent requestingJokeIntent = new Intent(getString(R.string.requesting_joke));
            LocalBroadcastManager.getInstance(this).sendBroadcast(requestingJokeIntent);
        }else new EndpointsAsyncTask(this).execute();
    }

    @Override
    public void onTaskComplete(String joke){
        Intent intent = new Intent(this, AndroidJokeActivity.class);
        intent.putExtra("JOKE_EXTRA",joke);
        startActivity(intent);
    }
}

class EndpointsAsyncTask extends AsyncTask<Void,Void,String> {
    private static JokesApi myApiService = null;
    private Context context;
    private OnTaskComplete listener;

    public EndpointsAsyncTask(OnTaskComplete listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(myApiService == null) {  // Only do this once
            JokesApi.Builder builder = new JokesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("https://android-nano-builditbigger.appspot.com/_ah/api/");
                    /* // DEV
                    .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });*/

            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.tellJoke(BuildConfig.IS_PAID).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onTaskComplete(result);
    }

    public interface OnTaskComplete {
        void onTaskComplete(String joke);
    }
}
