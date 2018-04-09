package com.example.hiroyki.newsviewer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// RssParserTask.java
public class RssParserTask extends AsyncTask<String, Integer, ItemListAdapter> {
    public Tab1Fragment mActivity;
    private ItemListAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    // コンストラクタtab1
    public RssParserTask(Tab1Fragment activity, ItemListAdapter adapter) {
        mActivity = activity;
        mAdapter = adapter;
    }


    // タスクを実行した直後にコールされる
    @Override
    protected void onPreExecute() {
        // プログレスバーを表示する
        mProgressDialog = new ProgressDialog(mActivity.getActivity());
        mProgressDialog.setMessage("Now Loading...");
        mProgressDialog.show();
    }


    // バックグラウンドにおける処理を担う。タスク実行時に渡された値を引数とする
    @Override
    protected ItemListAdapter doInBackground(String... params) {
        ItemListAdapter result = null;
        try {
            // HTTP経由でアクセスし、InputStreamを取得する
            URL url = new URL(params[0]);
            InputStream is = url.openConnection().getInputStream();
            result = parseXml(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ここで返した値は、onPostExecuteメソッドの引数として渡される
        return result;
    }

    // メインスレッド上で実行される
    @Override
    protected void onPostExecute(ItemListAdapter result) {
        mProgressDialog.dismiss();
        mActivity.setListAdapter(result);
    }

    // XMLをパースする
    public ItemListAdapter parseXml(InputStream is) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            Item currentItem = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        Log.d("TAG",tag);
                        if (tag.equals("item")) {
                            currentItem = new Item();
                        } else if (currentItem != null) {
                            if (tag.equals("title")) {
                                currentItem.setTitle(parser.nextText());
                            } else if (tag.equals("description")) {
                                currentItem.setDescription(parser.nextText());
                            } else if (tag.equals("link")) {
                                currentItem.setLink(parser.nextText());
                            } else if (tag.equals("encoded")) {
                                currentItem.setThumbnail(parser.nextText());
                            }else if (tag.equals("date")) {
                                //更新日
                                String tmpDate = parser.nextText();
                                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                //フォーマットを変換
                                tmpDate = tmpDate.replace("T", " ").substring(0,19);
                                tmpDate = tmpDate.replace("-", "/").substring(0,19);
                                //Log.v("TAG",sdf.format(tmpDate));
                                currentItem.setDate(tmpDate);
                            }else if (tag.equals("pubDate")){
                                String tmpDate = parser.nextText();
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
                                Log.v("TAG",tmpDate);
                                currentItem.setDate(tmpDate);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")) {
                            mAdapter.add(currentItem);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAdapter;
    }
}