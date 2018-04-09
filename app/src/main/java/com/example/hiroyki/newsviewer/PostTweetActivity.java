package com.example.hiroyki.newsviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import retrofit2.Call;

public class PostTweetActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                post(text);
            }
        });

        editText = (EditText) findViewById(R.id.edit_post_text);
    }

    private void post(String postText) {

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<Tweet> call = statusesService.update(postText, null, null, null, null, null, null, null, null);

        call.enqueue(new Callback<Tweet>() {

            @Override
            public void success(Result<Tweet> result) {
                Log.d("ツイート成功", result.data.text);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("ツイート失敗", exception.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(PostTweetActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(PostTweetActivity.this, "ログイン中", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(this, TimelineActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(PostTweetActivity.this, Tab_setting.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}