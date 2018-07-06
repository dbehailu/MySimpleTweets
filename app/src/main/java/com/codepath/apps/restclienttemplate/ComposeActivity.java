package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    String message; // tweet
    Button button;
    EditText editText;
    public static String TWEET_INTENT_KEY = "tweet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        editText = (EditText) findViewById(R.id.et_simple);
    }



    // onSubmit in guide
    public void onTweet(View view) {
        message= editText.getText().toString();
        if (message == null || message.isEmpty()) {
            Log.w("onTweet", "invalid tweet");
            return;
        }
        client.sendTweet(message, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                // prepare data intent
                Log.d("DEBUG", "SENT TWEET");

                try {
                    Tweet t = Tweet.fromJSON(response);
                    Intent data = new Intent();
                    data.putExtra("tweet", Parcels.wrap(t));
                    setResult(RESULT_OK, data);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                Log.d("ComposeActivity", jsonObject.toString());
                throwable.printStackTrace();
            }

        });
    }
}
