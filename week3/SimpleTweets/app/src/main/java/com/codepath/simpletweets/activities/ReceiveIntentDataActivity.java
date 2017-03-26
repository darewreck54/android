package com.codepath.simpletweets.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.simpletweets.R;
import com.codepath.simpletweets.fragments.ComposeTweetDialogFragment;

public class ReceiveIntentDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                // Make sure to check whether returned data will be null.
                String urlOfPage = intent.getStringExtra(Intent.EXTRA_TEXT);
                boolean isFromShareIntent = getIntent().getBooleanExtra("fromReceiveIntent", false);

                Intent newIntent = new Intent(this, TwitterActivity.class);
                newIntent.putExtra("fromReceiveIntent", true);

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("composeTweet",urlOfPage);
                edit.commit();
                startActivity(newIntent);
            }
        }
    }
}
