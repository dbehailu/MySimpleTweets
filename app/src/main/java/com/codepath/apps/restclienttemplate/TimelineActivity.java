package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity  {

    final int REQUEST_CODE = 20;
    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    Button button;
    private SmartFragmentStatePagerAdapter adapterViewPager;
    private ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);


        // find the recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // init the arraylist (datasource)
        tweets = new ArrayList<>();
        // construct adapter from datasource
        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.item1:
                composeMessage();
                return true;
//            case R.id.miProfile:
//                showProfileView();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void composeMessage(){
        Toast.makeText(this, "COMPOSING MESSAGE", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        i.putExtra("tweetText", 2);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check request code and result code first

        // Use data parameter
        Tweet tweet = (Tweet) data.getSerializableExtra("tweet");

        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient",response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient",response.toString());

                // iterate JSON array
                // deserialize each json entry
                for (int i = 0; i < response.length(); i++) {
                    // convert each object to a Tweet model
                    // add that Tweet model to our data source


                    // notify adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        // tweet.getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient",responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    public void onComposeAction(MenuItem item) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, ComposeActivity.class);
        startActivity(i); // brings up the second activity
        // put "extras" into the bundle for access in the second activity
        i.putExtra("username", "foobar");
        i.putExtra("in_reply_to", "george");
        i.putExtra("code", 400);
        startActivityForResult(i, REQUEST_CODE);
    }

//    public void showProfileView(){
//
//        Intent i = new Intent(this, ProfileActivity.class);
//        startActivity(i);
//
//    }
//    public void onSubmit(View v) {
//        // closes the activity and returns to first screen
//        this.finish();
//    }

//    public void onClick(View view) {
//        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
//        i.putExtra("mode", 2); // pass arbitrary data to launched activity
//        startActivityForResult(i, REQUEST_CODE);
//    }



}
