package com.example.hiroyki.newsviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.hiroyki.newsviewer.Tab_setting.alertList;
import static com.example.hiroyki.newsviewer.Tab_setting.loadList;
import static com.example.hiroyki.newsviewer.Tab_setting.saveList;

@SuppressLint("ValidFragment")
public class RecomFragment extends ListFragment {
    public static final int MENU_ITEM_RELOAD = Menu.FIRST;
    private  String RSS_FEED_URL;
    private ItemListAdapter2 mAdapter;
    private ArrayList<Item> mItems;
    public ArrayList alertList;
    public static ArrayList user_View = new ArrayList<String>();


    public RecomFragment(ArrayList list) {alertList = list;}

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        // Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
        mItems = new ArrayList<Item>();
        mAdapter = new ItemListAdapter2(this.getActivity(), mItems);

        //user_View = loadList(this.getActivity() ,"view");
        //TF_IDF tfidf = new TF_IDF(user_View, String.valueOf(user_View.get(0)));

        for (int i = 1; i < alertList.size() ; i=i+2) {
            RSS_FEED_URL = (String) alertList.get(i);
            // タスクを起動する
            RecomRssParserTask task = new RecomRssParserTask(this, mAdapter);
            task.execute(RSS_FEED_URL);
        }
        Log.d("compare2", String.valueOf(mItems));
        //Collections.sort(mItems, new DateComparator());
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment, container, false);
        return v;
    }

    // リストの項目を選択した時の処理
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = mItems.get(position);
        Intent intent = new Intent(this.getActivity(), ItemDetailActivity.class);
        intent.putExtra("TITLE", item.getTitle());
        intent.putExtra("DESCRIPTION", item.getDescription());
        intent.putExtra("Link", item.getLink());
        intent.putExtra("DATE", item.getDate());
        startActivity(intent);
    }


    /*
    // MENUボタンを押したときの処理
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        // デフォルトではアイテムを追加した順番通りに表示する
        menu.add(0, MENU_ITEM_RELOAD, 0, "更新");
        return result;
    }
    */


    // MENUの項目を押したときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 更新
            case MENU_ITEM_RELOAD:
                // アダプタを初期化し、タスクを起動する
                mItems = new ArrayList();
                mAdapter = new ItemListAdapter2(this.getActivity(), mItems);
                // タスクはその都度生成する
                RecomRssParserTask task = new RecomRssParserTask(this, mAdapter);
                task.execute(RSS_FEED_URL);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}