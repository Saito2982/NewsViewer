package com.example.hiroyki.newsviewer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class TweetAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater = null;
    private List<Tweet> tweetList;

    public TweetAdapter(Context context, List<Tweet> tweetList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tweetList = tweetList;
    }

    @Override
    public int getCount() {
        return tweetList.size();
    }

    @Override
    public Object getItem(int position) {
        return tweetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tweetList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.tweetrow, parent, false);

        TextView screenNameTextView = (TextView)convertView.findViewById(R.id.screen_name);
        TextView TweetTextTextView = (TextView)convertView.findViewById(R.id.tweet_text);
        ImageView UserIcon = (ImageView) convertView.findViewById(R.id.usericon);

        screenNameTextView.setText(tweetList.get(position).user.name);
        TweetTextTextView.setText(tweetList.get(position).text);
        //UserIcon.

        return convertView;
    }
}
