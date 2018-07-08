package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    View view;
    String message; // tweet
    EditText editText;
//    String mCurrentTweetText = "";
//    public static String TWEET_INTENT_KEY = "tweet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        setTitle("MySimpleTweet - Compose Tweet");
    }

    public void onTweet(View view) {
        editText = (EditText) findViewById(R.id.et_simple);
        message = editText.getText().toString(); // as string

        if (message == null || message.isEmpty()) {
            Log.w("onTweet", "invalid tweet");
            return;
        }

        client.sendTweet(message, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);

                // prepare data intent
                Log.d("DEBUG", "SENT TWEET");

                try {
                    Tweet t = Tweet.fromJSON(response);
                    Log.d("TweetDebug", "t = " + t + "message = " + message);
                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(t));

                    setResult(RESULT_OK, intent);
//                    startActivityForResult(intent, RESULT_OK);

                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                finish();
            }

            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
//                Log.d("ComposeActivity", jsonObject.toString());
//                throwable.printStackTrace();
//            }
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

        });
    }
}
