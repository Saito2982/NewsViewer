package com.example.hiroyki.newsviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.hiroyki.newsviewer.Tab_setting.loadList;
import static com.example.hiroyki.newsviewer.Tab_setting.saveList;

// ItemDetailActivity.java
public class ItemDetailActivity extends Activity {
    private TextView mTitle;
    private TextView mDescr;
    public static ArrayList user_View = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        Intent intent = getIntent();

        String title = intent.getStringExtra("TITLE");
        mTitle = (TextView) findViewById(R.id.item_detail_title);
        mTitle.setText(title);
        mTitle.setTextColor(Color.WHITE);

        user_View = loadList(ItemDetailActivity.this,"view");
        user_View.add(title);
        Log.v("TAG", String.valueOf(user_View));
        saveList(ItemDetailActivity.this ,"view" , user_View);

        /*
        String descr = intent.getStringExtra("DESCRIPTION");
        mDescr = (TextView) findViewById(R.id.item_detail_descr);
        mDescr.setText(descr);
        mDescr.setTextColor(Color.BLACK);
        */

        String link = intent.getStringExtra("Link");
        WebView myWebView = (WebView)findViewById(R.id.webView);
        //標準ブラウザをキャンセル
        myWebView.setWebViewClient(new WebViewClient());
        //アプリ起動時に読み込むURL
        myWebView.loadUrl(link);
    }
}