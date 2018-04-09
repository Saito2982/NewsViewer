package com.example.hiroyki.newsviewer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.hiroyki.newsviewer.Tab_setting.loadList;

public class ItemListAdapter2 extends ArrayAdapter<Item> {
    private LayoutInflater mInflater;
    private TextView mTitle;
    private TextView mDescr;
    private TextView mDate;
    private double Tfidf;
    public static ArrayList user_View = new ArrayList<String>();

    public ItemListAdapter2(Context context, List<Item> objects) {
        super(context, 0, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 1行ごとのビューを生成する
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_row, null);
        }

        // 現在参照しているリストの位置からItemを取得する
        Item item = this.getItem(position);
        if (item != null) {
            // Itemから必要なデータを取り出し、それぞれTextViewにセットする
            String title = item.getTitle().toString();
            mTitle = view.findViewById(R.id.item_title);
            //mTitle.setText(title);
            mTitle.setTextColor(Color.BLACK);

            /*
            String descr = item.getDescription().toString();
            mDescr = view.findViewById(R.id.item_descr);
            mDescr.setText(descr);
            */

            String date = item.getDate().toString();
            mDate = view.findViewById(R.id.item_descr);
            //mDate.setText(date);

            // サムネイル画像を設定
            String image = item.getThumbnail().toString();
            //画像取得スレッド起動
            ImageView Image = view.findViewById(R.id.thumbnail);
            Image.setTag(image);
            ImageGetTask task = new ImageGetTask(Image);
            //task.execute(image);

            //user_View = loadList(this.getContext() ,"view");
            //TF_IDF tfidf = new TF_IDF(user_View, title);
            //Log.v("nyu-ryoku", String.valueOf(user_View));
            //Log.v("nyu-ryoku", String.valueOf(title));
            //Tfidf = tfidf.getTfidf();
            //Log.v("TFIDF_AVE", String.valueOf(Tfidf));

            mTitle.setText(title);
            mDate.setText(date);
            task.execute(image);
        }
        return view;
    }
}